package com.phantom.banguminote.detail


import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phantom.banguminote.R
import com.phantom.banguminote.base.dpToPx
import com.phantom.banguminote.base.spToPx
import kotlin.math.max
import kotlin.math.roundToInt

class StickyItemDecoration(
    val context: Context,
    val getTitle: (pos: Int) -> String,
    val shouldShow: (pos: Int) -> Boolean = { true },
    val isNotEmpty: () -> Boolean = { true },
) : RecyclerView.ItemDecoration() {

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
        val itemCount = state.itemCount
        val left = parent.paddingLeft.toFloat()
        val right = (parent.width - parent.paddingRight).toFloat()
        val firstVisiblePos =
            (parent.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()
                ?: RecyclerView.NO_POSITION
        val lastVisiblePos =
            (parent.layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition()
                ?: RecyclerView.NO_POSITION
        if (isNotEmpty()) {
            for (i in 0..<itemCount) {
                val child = parent.getChildAt(i)
                val pos = parent.getChildLayoutPosition(child)
                if (pos in firstVisiblePos..lastVisiblePos
                    && (shouldShow(pos) || pos == firstVisiblePos)
                    && pos != RecyclerView.NO_POSITION
                ) {
                    val title = getTitle(pos)
                    var textBaseLine = max(rectHeight, child.top.toFloat())
                    if (pos == firstVisiblePos
                        && (pos + 1) < (parent.adapter?.itemCount ?: 0)
                        && shouldShow(pos + 1)
                    ) {
                        val preStickyPos = parent.findViewHolderForLayoutPosition(pos + 1)
                            ?.itemView?.top?.toFloat()
                        if (preStickyPos != null && preStickyPos - rectHeight < textBaseLine) {
                            textBaseLine = preStickyPos - rectHeight
                        }
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
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val pos = parent.getChildLayoutPosition(view)
        if (shouldShow(pos)) {
            outRect.top = rectHeight.roundToInt()
        }
    }
}