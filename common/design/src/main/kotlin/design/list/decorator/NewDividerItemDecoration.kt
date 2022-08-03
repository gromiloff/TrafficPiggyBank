package design.list.decorator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import pro.gromiloff.design.R

open class NewDividerItemDecoration : RecyclerView.ItemDecoration() {
    private var mPaint : Paint? = null

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val params = view.layoutParams as RecyclerView.LayoutParams

        // we retrieve the position in the list
        val position = params.viewAdapterPosition

        // add space for the separator to the bottom of every view but the last one
        if (position < state.itemCount) {
            outRect.set(0, 0, 0, getPaint(parent.context).strokeWidth.toInt()) // left, top, right, bottom
        } else {
            outRect.setEmpty() // 0, 0, 0, 0
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        // a line will draw half its size to top and bottom,
        // hence the offset to place it correctly
        val offset = getPaint(parent.context).strokeWidth / 2

        // this will iterate over every visible view
        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            // and finally draw the separator
            if ((view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition < state.itemCount) {
                // apply alpha to support animations
                // this.mPaint.alpha = view.alpha.toInt()
                // val positionY = view.translationY + view.height - offset
                val positionY = view.bottom.toFloat() + offset + view.translationY
                // do the drawing
                c.drawLine(view.left.toFloat(), positionY, view.right.toFloat(), positionY, getPaint(parent.context))
            } else {
                break
            }
        }
    }

    private fun getPaint(context: Context) : Paint {
        if(mPaint == null) {
            mPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                this.color = ContextCompat.getColor(context, R.color.divider)
                this.style = Paint.Style.FILL_AND_STROKE
                this.strokeWidth = context.resources.getDimensionPixelSize(R.dimen.divider_size).toFloat()
            }
        }
        return mPaint!!
    }
}
