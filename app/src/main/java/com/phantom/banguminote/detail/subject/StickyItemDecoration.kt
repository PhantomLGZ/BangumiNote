package com.phantom.banguminote.detail.subject


import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.phantom.banguminote.R
import com.phantom.banguminote.base.dpToPx
import com.phantom.banguminote.base.spToPx
import kotlin.math.max
import kotlin.math.roundToInt

class StickyItemDecoration(
    val context: Context,
    val getTitle: (pos: Int) -> String
) :
    RecyclerView.ItemDecoration() {

    private val rectHeight = 32f.dpToPx(context)
    private val textPaintSize = 17f.spToPx(context)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.color = context.getColor(R.color.text_light)
        it.textSize = textPaintSize
        it.isFakeBoldText = true
    }
    private val rectPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.style = Paint.Style.FILL
        it.color = context.getColor(R.color.theme_background)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val childCount = parent.childCount
        val itemCount = state.itemCount
        val left = parent.paddingLeft.toFloat()
        val right = (parent.width - parent.paddingRight).toFloat()
        for (i in 0..<childCount) {
            val child = parent.getChildAt(i)
            val pos = parent.getChildLayoutPosition(child)
            val title = getTitle(pos)
            var textBaseLine = max(rectHeight, child.top.toFloat())
            val viewBottom = child.bottom.toFloat()
            if (pos < itemCount - 1 && viewBottom < textBaseLine) {
                textBaseLine = viewBottom
            }
            c.drawRect(left, textBaseLine - rectHeight, right, textBaseLine, rectPaint)
            val value = textPaint.fontMetrics.descent + textPaint.fontMetrics.ascent
            c.drawText(
                title,
                left + 8f.dpToPx(context),
                textBaseLine - (rectHeight + value) / 2,
                textPaint
            )
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = rectHeight.roundToInt()
    }
}