package login.ui

import android.content.Intent
import android.view.View
import api.function.SyncApi
import design.BaseViewModel
import files.shareDB
import org.koin.core.component.get

/**
 * Модель бизнесс логики экрана профиля пользователя
 *
 * @author gromiloff
 * */
internal class LoginViewModel : BaseViewModel<LoginAction>() {

    /** авторизация через google */
    val signAuth = View.OnClickListener {

    }

    /** анонимная авторизация */
    val startSync = View.OnClickListener {
        wrapperWaiting {
            val api = get<SyncApi>()
            val permission = api.requestPermissions()
            if(permission.isEmpty()) {
                api.start()
            }
            else postCommand(LoginAction.RequestPermission(permission))
        }
    }

    /** анонимная авторизация */
    val print = View.OnClickListener {
        wrapperWaiting {
            shareDB(it.context)
        }
    }

    fun start() {
        wrapperWaiting {
            // проверяем что пользователь действительно не авторизован,
            // иначе просто уходим на следующий экран
            /*if(UserAuthType.NonAuth != api.authType()) {
                postCommand(LoginAction.NextScreen)
            }*/
        }
    }

    fun onActivityResult(data : Intent?) {
        wrapperWaiting {

        }
    }
}