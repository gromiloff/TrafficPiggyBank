package firebase

import api.firebase.FireStoreApi
import api.model.BoxBuyStatus
import api.model.BoxMintStatus
import api.model.data.Box
import api.model.data.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import timber.log.Timber

/**
 * Работа с базой данных
 *
 * @author gromiloff
 * */
internal object FireStoreImpl : FireStoreApi {
    init {
        Firebase.firestore.firestoreSettings = firestoreSettings {
            //host = "your host"
            //isSslEnabled = false
            cacheSizeBytes = FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED
            isPersistenceEnabled = true
        }
    }

    private object UserConstant {
        const val collection = "users"

        const val balance = "balance"
        const val task = "current_task_map"
        // характеристики задания: вес (для награды) и стартовая точка + радиус (с учетом ограничений)
        const val task_weight = "weight"
        const val task_center = "point" // центр области перемещения
        const val task_radius = "radius"
        const val task_cost = "cost" // потраченные деньги на задание
        const val task_deadline = "deadline" // время когда задание протухнет

    }
    private object BoxConstant {
        const val collection = "boxes"

        const val time = "created"
        const val weight = "weight"
        const val owner = "owner"
        const val lock_deadline = "lock_deadline" // время снятия лока с коробки
        const val restrictions = "restrictions"

        const val seconds_for_lock = 60 // минута для лока коробки
    }

    /** при создании пользователя даем ему денег. Если авторизован через google одна сумма, иначе другая */
    suspend fun newUser() {
        Firebase.firestore.collection(UserConstant.collection)
            .document(Firebase.auth.currentUser!!.uid)
            .set(
                hashMapOf(
                    UserConstant.balance to if(Firebase.auth.currentUser!!.isEmailVerified) 100 else 50,
                    UserConstant.task to null,
                ).apply {
                    Timber.d("Added [$this]")
                }
            ).await()
    }

    // todd нет проверки на деньги которые надо списать при минте
    override suspend fun mintBox(box: Box): BoxMintStatus {
        return try {
            Firebase.firestore.collection(BoxConstant.collection)
                .document()
                .set(
                    hashMapOf(
                        BoxConstant.time to FieldValue.serverTimestamp(),
                        BoxConstant.owner to Firebase.auth.currentUser!!.uid, // не берем из box потому что там нет ссылки на пользователя
                        BoxConstant.weight to box.weight,
                        BoxConstant.lock_deadline to Timestamp(0,0), // время залока коробки
                        BoxConstant.restrictions to ConfigPref.mapRestrictionToHash(box.restrictions),
                    )
                ).await()
            BoxMintStatus.Success
        } catch (e:Exception) {
            Timber.d(e)
            BoxMintStatus.DefaultFail
        }
    }

    override suspend fun currentTask() : Task? {
        return try {
            val map = Firebase.firestore.collection(UserConstant.collection)
                .document(Firebase.auth.currentUser!!.uid)
                .get()
                .await()
                .get(UserConstant.task) as HashMap<*,*>
            Task(
                weight = (map[UserConstant.task_weight] as Long).toInt(),
                cost = (map[UserConstant.task_cost] as Long).toInt(),
                timeEnd = (map[UserConstant.task_deadline] as Timestamp).seconds * 1000,
                centerPoint = (map[UserConstant.task_center] as GeoPoint).run { arrayListOf(this.latitude, this.longitude) },
                radius = (map[UserConstant.task_radius] as Long).toInt()
            )
        } catch (e:Exception) {
            null
        }
    }

    override suspend fun lockBoxIf(box: Box): BoxBuyStatus {
        // проверяем что у пользователя нет текущего незаконченного задания
        val user = Firebase.firestore.collection(UserConstant.collection).document(Firebase.auth.currentUser!!.uid).get().await()
        // todo добавить на протухаемость проверку
        if(user.get(UserConstant.task) != null) return BoxBuyStatus.HasTask

        // проверяем хватает ли пользователю денег
        val currentMoney = userBalance()
        val boxMoney = ConfigPref.getPriceBy(box.weight, box.restrictions)
        if(boxMoney > currentMoney) return BoxBuyStatus.NoMoney

        return try {
            val map = java.util.HashMap<String, Any>().apply {
                put(
                    BoxConstant.lock_deadline,
                    Timestamp((System.currentTimeMillis() + BoxConstant.seconds_for_lock * 1000) / 1000, 0)
                )
            }
            Firebase.firestore.collection(BoxConstant.collection).document(box.id!!).update(map).await()
            BoxBuyStatus.Success
        } catch (e:Exception) {
            Timber.e(e)
            BoxBuyStatus.DefaultFail
        }
    }
/*
    override suspend fun buyBox(box: Box): BuyOrMintStatus {


        return try {
            // определяем характеристики области перемещения
            val radiusChange = if(box.restrictions?.owner is Restriction.Type1) 0.01f * (100 + box.restrictions!!.customValue) else 0f
            val task = hashMapOf(
                UserConstant.task_weight to box.weight,
                UserConstant.task_cost to box.weight,
                UserConstant.task_radius to ConfigPref.getRadius() * (1f + radiusChange),
                UserConstant.task_center to null, // todo как передать сюда область для генерации
            )
            // обновляем информацию о пользователе
            Firebase.firestore.collection(UserConstant.collection)
                .document(Firebase.auth.currentUser!!.uid)
                .set(
                    hashMapOf(
                        UserConstant.balance to currentMoney - boxMoney,
                        UserConstant.task to task,
                    )
                ).await()
            // удаляем коробку из доступных
            Firebase.firestore.collection(BoxConstant.collection).document(box.id!!).delete().await()
            BuyOrMintStatus.Success
        } catch (e:Exception) {
            Timber.e(e)
            BuyOrMintStatus.DefaultFail
        }
    }*/

    override suspend fun getAllBoxes(my : Boolean): List<Box> {
        val myId = Firebase.auth.currentUser?.uid
        val items = Firebase.firestore
            .collection(BoxConstant.collection)
            .run {
                if(my) {
                    whereEqualTo(BoxConstant.owner, myId)
                } else {
                    whereNotEqualTo(BoxConstant.owner, myId)
                }
            }
            .get().await().documents

        val boxes = ArrayList<Box>()
        // проходим по каждому элементу и просматриваем время протухаемости, исключая залоченные коробки
        items.forEach { documentSnapshot ->
            val lock = documentSnapshot[BoxConstant.lock_deadline] as Timestamp
            when {
                // коробка не залочена
                lock.seconds == 0L -> { boxes.add(map(documentSnapshot)) }
                // коробка залочена и время не прошло
                lock > Timestamp.now() -> { /* коробка еще залочена и мы ее пропускаем */ }
                // коробка залочена, но время закончилось
                else -> {
                    boxes.add(map(documentSnapshot))
                    // время лока коробки прошло, разлочиваем ее и добавляем
                    val map = java.util.HashMap<String, Any>().apply {
                        put(
                            BoxConstant.lock_deadline,
                            Timestamp(0, 0)
                        )
                    }
                    Firebase.firestore.collection(BoxConstant.collection)
                        .document(documentSnapshot.id).update(map).await()
                }
            }
        }

        return boxes
    }

    override suspend fun findBox(id: String): Box? {
        return try {
            map(Firebase.firestore.collection(BoxConstant.collection).document(id).get().await())
        } catch (e:Exception) {
            Timber.d(e)
            null
        }
    }

    override suspend fun userBalance(): Int {
        return (Firebase.firestore.collection(UserConstant.collection)
            .document(Firebase.auth.currentUser!!.uid)
            .get()
            .await()[UserConstant.balance] as Long).toInt()
    }

    @Suppress("UNCHECKED_CAST")
    private fun map(source : DocumentSnapshot) = Box(
        owner = source[BoxConstant.owner] as String,
        created = (source[BoxConstant.time] as Timestamp).seconds,
        id = source.id,
        weight = (source[BoxConstant.weight] as Long).toInt(),
        restrictions = ConfigPref.mapRestrictionFromHash(source[BoxConstant.restrictions] as? HashMap<String, Any>)
    )
}
