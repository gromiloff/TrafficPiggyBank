package api.screen

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import api.model.data.Box
import kotlinx.parcelize.Parcelize

/**
 * Публичный интерфейс для работы с экранами списков коробок
 *
 * @author gromiloff
 * */
interface OpenReuseBoxesScreenApi {
    /** @return [Fragment] списка моих коробок */
    fun myBoxesFragment() : Fragment
    /** @return [Fragment] списка коробок для меня */
    fun forMeBoxesFragment() : Fragment
    /** открыть экран коробки по ее идентификатору */
    fun showDetailsForBox(fm : FragmentManager, owner : LifecycleOwner, id : String, result : (BoxCardActionResult?) -> Unit)
    /** открыть экран временной коробки */
    fun showDetailsForBox(fm : FragmentManager, owner : LifecycleOwner, box: Box, result : (BoxCardActionResult?) -> Unit)
}

/** варианты обратной связи для получения результата работы с карточкой коробки */
sealed class BoxCardActionResult : Parcelable {
    /** открыть экран карты чтобы открыть коробку и создать задание для пользователя */
    @Parcelize
    data class OpenMap(val box: Box) : BoxCardActionResult()
    /** при возврате на предыдущий экран - его надо обновить (например успешный минтинг) */
    @Parcelize
    object RefreshList : BoxCardActionResult()
}