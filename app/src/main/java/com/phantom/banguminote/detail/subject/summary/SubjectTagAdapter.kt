package com.phantom.banguminote.detail.subject.summary

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.phantom.banguminote.R
import com.phantom.banguminote.data.TagsData

class SubjectTagAdapter : BaseQuickAdapter<TagsData, QuickViewHolder>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder =
        QuickViewHolder(R.layout.item_subject_tag, parent)

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: TagsData?) {
        holder.also {
            it.setText(R.id.tvTag, item?.name)
            it.setText(R.id.tvCount, (item?.count ?: 0).toString())
        }
    }
}