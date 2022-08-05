package login.ui

import android.content.Intent
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import api.function.ShowToastApi
import api.model.text.TextHolder
import design.BaseActivity
import gromiloff.login.databinding.ActivityLoginBinding
import org.koin.android.ext.android.get

/**
 * @author agromilo@gmail.com
 *
 * Экран авторизации.
 * Точка входа в приложение.
 * */
internal class LoginScreen : BaseActivity<LoginAction>() {
    object Const {
        const val REQ_ONE_TAP = 25062022  // для авторизации через google
    }

    override val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java].apply {
            start()
        }
    }

    override fun createView() = ActivityLoginBinding.inflate(LayoutInflater.from(this)).apply {
        model = viewModel
        waitingModel = viewModel
        lifecycleOwner = this@LoginScreen
    }.root

    override fun handleAction(action: LoginAction) {
        val api = get<ShowToastApi>()
        when(action) {
            is LoginAction.Launch -> startIntentSenderForResult(
                action.sender,
                Const.REQ_ONE_TAP,
                null,
                0,
                0,
                0,
                null
            )
            is LoginAction.Error -> api.show(
                this,
                TextHolder.Normal(text = action.e.localizedMessage)
            )
            LoginAction.Cancel -> api.show(
                this,
                TextHolder.Normal(text = "Ошибка авторизации")
            )
            LoginAction.NextScreen -> {
                TODO("no impl")
                finish()
            }
            is LoginAction.RequestPermission -> handlePermission(action.set)
        }
    }

    // todo переделать на контакт
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_ONE_TAP -> {
                viewModel.onActivityResult(data)
            }
        }
    }
}
