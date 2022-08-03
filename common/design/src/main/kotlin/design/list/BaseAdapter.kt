package design.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import design.list.model.ItemAdapterModel

/**
 * Базовая реализация адаптера списка.
 *
 * @param emptyModel реализация модели управления заглушкой
 * @param items внутренний список элементов для работы
 *
 * @author gromiloff
 * */
abstract class BaseAdapter (
    private val emptyModel: EmptyModel,
    private val items: ArrayList<ItemAdapterModel> = ArrayList()
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun getItemCount() = items.size

    /** определяем viewType как ссылка на верстку */
    override fun getItemViewType(position: Int): Int = items[position].layoutId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            parent.inflater(),
            viewType,
            parent,
            false
        ).apply {
            lifecycleOwner = parent.findViewTreeLifecycleOwner()
        }

        return BaseViewHolder(binding).apply {
            // todo перенести в наследника эту реализацию
            binding.root.setOnClickListener {
                click(items[this.adapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(items[position])
    }

    /** обработчик нажатия на ячейку */
    protected open fun click(item : ItemAdapterModel) = Unit

    /** установить значение списка моделей во внутренний список для отображения */
    @SuppressLint("NotifyDataSetChanged")
    @UiThread
    fun add(l: List<ItemAdapterModel>) {
        items.clear()
        items.addAll(l)
        // список пустой и имеется ненулевое значение текста заглушки - необходимо показать ее
        emptyModel.changeMessage(items.isEmpty())
        // todo перейти на notifyItemInserted(items.size - 1)
        notifyDataSetChanged()
    }

    private fun ViewGroup.inflater(): LayoutInflater = LayoutInflater.from(context)
}
