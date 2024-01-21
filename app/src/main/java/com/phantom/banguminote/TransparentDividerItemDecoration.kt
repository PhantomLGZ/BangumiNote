package com.phantom.banguminote

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DividerItemDecoration

class TransparentDividerItemDecoration(context: Context) : DividerItemDecoration(context, VERTICAL) {

    init {
        AppCompatResources.getDrawable(context, R.drawable.transparent_divider)?.let {
            setDrawable(it)
        }
    }

}