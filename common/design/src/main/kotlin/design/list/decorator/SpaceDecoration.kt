package design.list.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Декоратор для отступов со всех сторон для ячейки списка.
 *
 * применимо только для 2х столбиков данных в ленте
 *
 * @author gromiloff
 * */
open class SpaceDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val space = (view.resources.displayMetrics.density * 8).toInt()
        outRect.bottom = space

        // Add top margin only for the first item to avoid double space between items
        val position = parent.getChildLayoutPosition(view)
        if (position % 2 == 0) {
            // правый столбик
            outRect.left = space
            outRect.right = space / 2
        } else {
            // левый столбик
            outRect.left = space / 2
            outRect.right = space
        }

        if(position < 2) {
            outRect.top = space
        }
    }
}
