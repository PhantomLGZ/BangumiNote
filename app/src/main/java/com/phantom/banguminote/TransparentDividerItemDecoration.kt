package com.phantom.banguminote

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DividerItemDecoration

class TransparentDividerItemDecoration(context: Context, orientation: Int = VERTICAL) :
    DividerItemDecoration(context, orientation) {

    init {
        AppCompatResources.getDrawable(context, R.drawable.transparent_divider)?.let {
            setDrawable(it)
        }
    }

    companion object {
        fun vertical(context: Context): TransparentDividerItemDecoration =
            TransparentDividerItemDecoration(context)

        fun horizontal(context: Context): TransparentDividerItemDecoration =
            TransparentDividerItemDecoration(context, HORIZONTAL)
    }

}