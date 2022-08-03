package api.firebase

/** методы по работе с базой данных Firebase Firestore */
interface FirebaseAuthApi {
    /** авторизация через google провайдер. Только если установлены гугло сервисы !!!
     * @return true если пользовать успешно авторизован, иначе false */
    suspend fun signGoogle(idToken : String) : Boolean

    /** авторизован ли пользователь */
    suspend fun authType() : UserAuthType

    /** мой ли это идентификатор (испльзуется для проверки принадлежности текущему пользователю) */
    suspend fun isMe(id : String) : Boolean

    /** анонимная авторизация пользователя */
    suspend fun signInAnonymously() : Boolean
}

enum class UserAuthType {
    EmailVerified,
    Anonymous,
    NonAuth
}