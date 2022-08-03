package api.model.text

import android.content.DialogInterface
import android.view.View
import androidx.annotation.StringRes

/**
 * Модель для использования с заполненным один параметром текста
 * @author gromiloff
 * */
sealed class TextHolder(
    val text : String? = null,
    @StringRes val textId : Int? = null
) {
    /** нормальное определение для текст + ссылка на ресурс */
    class Normal (
        text : String? = null,
        @StringRes textId : Int? = null
    ) : TextHolder(text, textId)

    class WithListeners(
        text : String? = null,
        @StringRes textId : Int? = null,
        val listener : DialogInterface.OnClickListener
    ) : TextHolder(text, textId)

    class StringItems(
        val items : Array<String>,
        val listener : DialogInterface.OnClickListener
    ) : TextHolder(null, null)

    class CustomView(
        val view : View,
    ) : TextHolder(null, null)
}

/** заглушка для слушателя клика в диалогах */
object DialogInterfaceOnClickListenerEmpty : DialogInterface.OnClickListener {
    override fun onClick(p0: DialogInterface?, p1: Int) {
        p0?.dismiss()
    }
}