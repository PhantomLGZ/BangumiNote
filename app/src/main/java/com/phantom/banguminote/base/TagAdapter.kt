package com.phantom.banguminote.base

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.phantom.banguminote.R

class TagAdapter : BaseQuickAdapter<String, QuickViewHolder>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder =
        QuickViewHolder(R.layout.item_tag, parent)

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: String?) {
        holder.setText(R.id.tvTag, item)
    }
}