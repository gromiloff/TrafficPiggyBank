package design.list

import androidx.annotation.UiThread
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView

/**
 * Базовый класс ячейки списка с хранением текущей позиции
 *
 * @param binding - текущая реализация биндинга ячейки
 *
 * @author gromiloff
 * */
class BaseViewHolder (
    private val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root) {

    @UiThread
    fun bind(item : Any) {
        binding.setVariable(BR.model, item)
        binding.executePendingBindings()
    }
}

