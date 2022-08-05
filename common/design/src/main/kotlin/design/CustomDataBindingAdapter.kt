package design

import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter
import timber.log.Timber


/** покрасить картинку в заданный цвет. применяется к динамической покраске иконок */
@BindingAdapter("tint")
fun AppCompatImageView.tint(value : Int?) {
    if(value != null && value > 0) {
        Timber.d("tint $value")
        DrawableCompat.setTint(drawable, ContextCompat.getColor(context, value))
    }
}
