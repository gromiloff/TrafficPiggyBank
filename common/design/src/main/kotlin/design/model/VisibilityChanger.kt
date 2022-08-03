package design.model

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * Объект описывающий изменений видимости [View] элемента на экране
 *
 * @author gromiloff
 * */
sealed class VisibilityChanger {
    /** перевести в состояние невидимости [View.GONE]*/
    object Gone : VisibilityChanger()
    /** перевести в состояние невидимости [View.VISIBLE]*/
    object Visible : VisibilityChanger()
    /** перевести в состояние невидимости [View.INVISIBLE]*/
    object Invisible : VisibilityChanger()
}

@BindingAdapter("visibilityChanger")
fun View.visibilityChanger(show : VisibilityChanger?) {
    visibility = when(show) {
        VisibilityChanger.Visible -> View.VISIBLE
        VisibilityChanger.Invisible -> View.INVISIBLE
        VisibilityChanger.Gone, null -> View.GONE
    }
}