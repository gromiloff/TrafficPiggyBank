package design.list

import androidx.annotation.AnyThread
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.model.text.TextData
import api.model.text.TextHolder
import timber.log.Timber

/*interface RefreshListModel : SwipeRefreshLayout.OnRefreshListener {
    val refresh : MutableLiveData<Boolean>
    val enableRefresh : MutableLiveData<Boolean>
}*/

/** описание модели по работе с заглушкой списка.
 * Для расширения функционала необхома дополнительная реализация
 *
 * @property emptyModel изменяемая ссылка на текст чтобы адаптер мог устанавливать корректное
 *                      значение и видимость заглушки
 * @property message постоянное значение для текстовки самой заглушки. Может иметь null значение, в
 *                   этом случае заглушка не будет отображаться даже при пустом списке
 *
 * @author gromiloff
 * */
interface EmptyModel {
    val emptyModel : MutableLiveData<TextData?>
    val message : TextHolder?

    /** изменить видимость (состояние) заглушки. Основная логика по работе с заглушкой
     *  реализовывается только тут
     *  @param isListEmpty маркер пустого списка
     **/
    @AnyThread
    fun changeMessage(isListEmpty : Boolean) {
        // проверка на установки корректных данных изначально
        val textModel = message as? TextHolder.Normal ?: return
        emptyModel.postValue(
            if(isListEmpty) {
                when {
                    textModel.text != null -> TextData.NormalText(textModel.text!!)
                    textModel.textId != null -> TextData.ResIdText(textModel.textId!!)
                    else -> {
                        Timber.e("incorrect setting data for empty")
                        TextData.NoText
                    }
                }
            } else TextData.NoText
        )
    }
}

/** основное описание модели по работе со списком
 * @param Adapter реализация адаптера списка для получения прямой ссылки на него
 * @property decorators список возможных декораторов для списка
 * @property customAdapter реализация базового адаптера
 *
 * @author gromiloff
 * */
interface ListModel<Adapter : BaseAdapter> {
    val decorators : MutableLiveData<List<RecyclerView.ItemDecoration>>?
    val customAdapter : MutableLiveData<Adapter?>
}

//interface ListRefreshModel<Adapter : BaseAdapter> : RefreshListModel, ListModel<Adapter>

interface GridModel {
    val counts : MutableLiveData<Int?>
}

/** реалзиация биндинга данных для списка */
@BindingAdapter("customListAdapter", "decorations", "gridColumnCount")
fun RecyclerView.setup(
    a: BaseAdapter?,
    d: List<RecyclerView.ItemDecoration>?,
    columnCount : Int? = null
) {
    layoutManager =
        if(columnCount == null) LinearLayoutManager(context)
        else GridLayoutManager(context, columnCount)
    adapter = a
    d?.forEach { addItemDecoration(it) }
}
