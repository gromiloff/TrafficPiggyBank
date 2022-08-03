package design

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter
import api.model.data.Rarity
import pro.gromiloff.design.R
import timber.log.Timber

/** установка специального окраса для объекта согласно его раритетности */
@BindingAdapter("rarity")
fun View.rarity(data: Rarity?) {
    if(data == null) return

    setBackgroundResource(when(data) {
        Rarity.Simple -> R.color.colorRaritySimple
        Rarity.Complex -> R.color.colorRarityComplex
        Rarity.Rare -> R.color.colorRarityRare
        Rarity.Unreal -> R.color.colorRarityUnreal
    })
}

/** покрасить картинку в заданный цвет. применяется к динамической покраске иконок */
@BindingAdapter("tint")
fun AppCompatImageView.tint(value : Int?) {
    if(value != null && value > 0) {
        Timber.d("tint $value")
        DrawableCompat.setTint(drawable, ContextCompat.getColor(context, value))
    }
}
