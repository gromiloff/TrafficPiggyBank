package firebase

import api.firebase.FirebaseAuthApi
import api.firebase.UserAuthType
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import timber.log.Timber

/**
 * Работа с функционалом авторизации пользвоателя
 *
 * @author gromiloff
 * */
internal object FirebaseAuthImpl : FirebaseAuthApi {
    override suspend fun signGoogle(idToken : String) = try {
        Firebase.auth
            .signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
            .await()
        true
    } catch (e:Exception) {
        Timber.d(e)
        false
    }

    override suspend fun authType() : UserAuthType {
        val current = Firebase.auth.currentUser ?: return UserAuthType.NonAuth

        return try {
            current.reload().await()
            Timber.d("Current user [${Firebase.auth.currentUser}] reloaded")
            when {
                current.isEmailVerified -> UserAuthType.EmailVerified
                current.isAnonymous -> UserAuthType.Anonymous
                else -> UserAuthType.NonAuth
            }
        } catch (e:Exception) {
            Timber.d(e)
            UserAuthType.NonAuth
        }
    }

    override suspend fun isMe(id: String): Boolean = Firebase.auth.currentUser?.uid == id

    override suspend fun signInAnonymously(): Boolean {
        return try {
            Firebase.auth.signInAnonymously().await()
            true
        } catch (e:Exception) {
            Timber.e(e)
            false
        }
    }
}

