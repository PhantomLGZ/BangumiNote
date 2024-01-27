package com.phantom.banguminote.detail.subject.character

import android.content.Context
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.phantom.banguminote.CutOffLogo
import com.phantom.banguminote.R
import com.phantom.banguminote.detail.subject.data.SubjectCharacterData

class SubjectCharacterAdapter : BaseQuickAdapter<SubjectCharacterData, QuickViewHolder>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder =
        QuickViewHolder(R.layout.item_subject_character, parent)

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: SubjectCharacterData?) {
        holder.also { h ->
            h.setText(R.id.tvRelation, item?.relation)
            Glide.with(context)
                .load(item?.images?.medium
                    ?.takeIf { it.isNotBlank() } ?: "https://bangumi.tv/img/info_only.png")
                .transform(CutOffLogo())
                .into(h.getView(R.id.ivImage))
            h.setText(R.id.tvName, item?.name)
            h.setText(R.id.tvActor, item?.actors?.map { it.name }?.joinToString(" / "))
        }
    }

}