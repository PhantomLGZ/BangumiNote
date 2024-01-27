package com.phantom.banguminote.detail.subject.info

import android.content.Context
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.phantom.banguminote.R

class PersonDialogAdapter : BaseQuickAdapter<PersonDialogFragment.DialogPersonData, QuickViewHolder>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder =
        QuickViewHolder(R.layout.item_info_dialog_person, parent)

    override fun onBindViewHolder(
        holder: QuickViewHolder,
        position: Int,
        item: PersonDialogFragment.DialogPersonData?
    ) {
        holder.also {
            Glide.with(context)
                .load(item?.image
                    ?.takeIf { it.isNotBlank() } ?: "https://bangumi.tv/img/info_only.png")
                .into(it.getView(R.id.ivImage))
            it.setText(R.id.tvName, item?.name)
        }
    }
}