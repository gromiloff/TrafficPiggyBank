package firebase

import android.content.Context
import api.firebase.RemoteConfigApi
import api.model.data.BoxRestrictions
import api.model.data.Rarity
import api.model.data.Restriction
import api.model.data.Template
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import gromiloff.prefs.PrefEnum
import gromiloff.prefs.PublicEmptyImpl
import gromiloff.prefs.PublicPref
import pro.gromiloff.firebase.R
import timber.log.Timber
import kotlin.random.Random

/**
 * Управление данными полученными при изменении Firebase Remote Config
 * @author gromiloff
 * */
internal object ConfigPref : RemoteConfigApi {
    private var instance : PublicPref? = null
    private var cache : LocalCache? = null

    fun create(context: Context) {
        instance = PublicEmptyImpl(context, "firebase_rc_prefs")
        Firebase.remoteConfig.apply {
            setDefaultsAsync(R.xml.remote_config_defaults)
        }
    }

    @Throws(InstantiationException::class)
    override fun activateConfig() {
        Firebase.remoteConfig.let { config ->
            config.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // сохраняем новые значения
                val configString = config. getString("constant")

                if(configString.length == 2) {
                    // получаем дефолтный конфиг, а не данные с сервера. Проверяем состояние кеша данных
                    val tmp = instancePref().getString(ConfigPrefImpl.Value, "") ?: ""
                    if(tmp.length < 3) {
                        // старых данных нет - надо дропать пользователя
                        throw InstantiationException("Unable update data from server")
                    } else {
                        createCache(tmp)
                        Timber.e("Get default config value")
                    }
                } else {
                    // сохраняем текущее состояние в конфиг
                    instancePref().setString(ConfigPrefImpl.Value, configString)
                    createCache(configString)
                    Timber.d("Firebase config update success")
                }
            } else {
                // удалем данные и считаем состояние не валидным
                Timber.e("Firebase config update failed ${task.exception}")
            }
        }
        }
    }

    override fun getRadius(): Int {
        checkCacheEmpty()
        return cache!!.radius
    }

    override fun getPriceBy(weight : Int, restriction : BoxRestrictions?) : Int {
        checkCacheEmpty()
        return (weight * cache!!.pricePerWeight * when(restriction?.owner) {
            is Restriction.Type0 -> 1f + 0.01f * (restriction.owner as Restriction.Type0).value
            is Restriction.Type1 -> {
                val t = (restriction.owner as Restriction.Type1).value
                1f + 0.01f * if(restriction.customValue > 0) {
                    // смещение вправо, берем индексы 2 и 3
                    restriction.customValue.toFloat() * t[2] / t[3]
                } else {
                    // смещение влево, берем индексы 0 и 1
                    restriction.customValue.toFloat() * t[0] / t[1]
                }
            }
            null -> 1f
        }).toInt()
    }

    override fun getRarityByWeight(weight: Int): Rarity? {
        checkCacheEmpty()
        cache!!.templates.forEach {
            if(it.weightMax >= weight && it.weightMin <= weight) {
                return it.rarity
            }
        }
        return null
    }

    override fun getTemplates(): List<Template> {
        checkCacheEmpty()
        return cache!!.templates
    }

    override fun getRestrictions(): List<Restriction<*>> {
        checkCacheEmpty()
        return cache!!.restrictions
    }

    override fun createRestrictionWithRandomType0() : BoxRestrictions {
        checkCacheEmpty()
        return BoxRestrictions(cache!!.restrictions[0])
    }

    override fun createRestrictionWithRandomType1() : BoxRestrictions {
        checkCacheEmpty()
        val owner = cache!!.restrictions[1] as Restriction.Type1
        // математика такая: (-75, 75) значит
        // Random.nextInt(75 - (-75)) + (-75) = Random.nextInt(150) -75 = 0-150 - 75 = (-75 +75)
        val random = Random.nextInt(owner.value[2] - owner.value[0]) + owner.value[0]
        return BoxRestrictions(owner, random)
    }

    /** конвертация данных в FireStore для хранения отсылки на тип и значение  */
    fun mapRestrictionToHash(restrictions : BoxRestrictions?) : HashMap<String, Any>? {
        return when(restrictions?.owner) {
            is Restriction.Type0 -> hashMapOf(
                "type" to 0L
            )
            is Restriction.Type1 -> hashMapOf(
                "type" to 1L,
                "value" to restrictions.customValue
            )
            null -> null
        }
    }

    /** конвертация данных в FireStore для хранения отсылки на тип и значение  */
    fun mapRestrictionFromHash(map : java.util.HashMap<String, Any>?) : BoxRestrictions? {
        checkCacheEmpty()
        return when(map?.get("type")) {
            0L -> BoxRestrictions(cache!!.restrictions[0])
            1L -> BoxRestrictions(cache!!.restrictions[1], (map["value"] as Long).toInt())
            else -> null
        }
    }

    private fun instancePref() = instance!!

    /** проверка на дурака что текущий кеш вообще существует */
    private fun checkCacheEmpty() {
        if(cache == null) {
            createCache(instancePref().getString(ConfigPrefImpl.Value)!!)
        }
    }

    /** наполняем текущий кеш данными согласно модели описанной в документации */
    private fun createCache(from : String) {
        val jsonConfig = JsonParser.parseString(from).asJsonObject
        val rarities = Rarity.values()
        var rarityIndex = 0

        cache = LocalCache(
            radius = jsonConfig.get("radius").asInt,
            pricePerWeight = jsonConfig.get("price_per_weight").asFloat,
            templates = jsonConfig.get("templates").asJsonArray.mapTo(ArrayList()) {
                val obj = it.asString.split(":")
                Template(
                    rarity = rarities[rarityIndex++],
                    weightMin = obj[0].toInt(),
                    weightMax = obj[1].toInt(),
                    distanceMin = obj[2].toInt(),
                    distanceMax = obj[3].toInt(),
                    speed = obj[4].toFloat()
                )
             },
            restrictions = jsonConfig.get("restrictions").asJsonArray.mapTo(ArrayList()) {
                val obj = it.asJsonObject
                val value : JsonElement = obj.get("rules")
                when(obj.get("type").asInt) {
                    0 -> Restriction.Type0(value.asInt)
                    1 -> Restriction.Type1(value.asJsonArray.mapTo(ArrayList()) { p -> p.asInt })
                    else -> throw InstantiationException("Unable parse json for restrictions [$it]")
                }
            },
        )

        Timber.d("new cache loaded $cache")
    }
}

/** Структура сохраняемых данных для работы с удаленной конфигурацией через Firebase Remote Config */
private enum class ConfigPrefImpl(override val defaultValue: String) : PrefEnum<String> {
    Value(""),
}

/** описание локального кеша с данными которые требуются для работы приложения.
 * Грузим их в оперативную память для максимально быстрого доступа */
private data class LocalCache(
    val radius : Int,
    val pricePerWeight : Float,
    val templates : List<Template>,
    val restrictions : List<Restriction<*>>
)
