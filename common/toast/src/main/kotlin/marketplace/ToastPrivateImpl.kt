package marketplace

import android.content.Context
import android.widget.Toast
import api.function.ShowToastApi
import api.model.text.TextHolder
import gromiloff.toast.R

/**
 * Реализация рубличных функций текущего модуля
 * @author gromiloff
 * */
internal object ToastPrivateImpl : ShowToastApi {
    override fun show(context: Context, text: TextHolder) {
        Toast.makeText(
            context,
            text.text ?: context.getString(text.textId!!),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun showNotImplement(context: Context) {
        show(context, TextHolder.Normal(textId = R.string.not_implemented))
    }
}