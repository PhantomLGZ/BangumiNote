package com.phantom.banguminote.detail.person.character

import android.content.Context
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.phantom.banguminote.base.CutOffLogo
import com.phantom.banguminote.R
import com.phantom.banguminote.detail.person.data.PersonCharacterData

class PersonCharacterAdapter : BaseQuickAdapter<PersonCharacterData, QuickViewHolder>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder =
        QuickViewHolder(R.layout.item_person_character, parent)

    override fun onBindViewHolder(
        holder: QuickViewHolder,
        position: Int,
        item: PersonCharacterData?
    ) {
        holder.also { h ->
            h.setText(R.id.tvRelation, item?.staff)
            Glide.with(context)
                .load(item?.images?.medium
                    ?.takeIf { it.isNotBlank() } ?: "https://bangumi.tv/img/info_only.png")
                .transform(CutOffLogo())
                .into(h.getView(R.id.ivImage))
            h.setText(R.id.tvName, item?.name)
            h.setText(
                R.id.tvSubjectName,
                item?.subject_name_cn?.takeIf { it.isNotBlank() } ?: item?.subject_name)
        }
    }
}