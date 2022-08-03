package api.model.text

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter

/**
 * базовый класс тестовых данных
 * */
sealed class TextData {
    companion object {
        fun from(value : String?) = if(value.isNullOrBlank()) NoText else NormalText(value)
    }

    object NoText : TextData()
    object NoTextAndInvisible : TextData()
    object NoTextButVisible : TextData()

    data class ResIdText(@StringRes val id : Int) : TextData()
    data class NormalText(val value : String) : TextData()
    data class TextCounter(val number : Int) : TextData()
    data class TextBoolean(val value : Boolean) : TextData()
}


@BindingAdapter("textData")
fun textData(view: TextView, data: TextData?) {
    view.visibility = when (data) {
        is TextData.TextCounter -> {
            view.text = data.number.toString()
            View.VISIBLE
        }
        is TextData.TextBoolean -> {
            view.text = data.value.toString()
            View.VISIBLE
        }
        is TextData.ResIdText -> {
            view.setText(data.id)
            View.VISIBLE
        }
        is TextData.NormalText -> {
            view.text = data.value
            View.VISIBLE
        }
        is TextData.NoTextAndInvisible -> {
            view.text = ""
            View.INVISIBLE
        }
        is TextData.NoTextButVisible -> {
            view.text = ""
            View.VISIBLE
        }
        else -> View.GONE
    }
}