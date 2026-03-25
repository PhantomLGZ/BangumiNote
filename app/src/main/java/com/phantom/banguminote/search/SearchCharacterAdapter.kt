package com.phantom.banguminote.search

import android.content.Context
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.phantom.banguminote.R
import com.phantom.banguminote.base.checkHttps
import com.phantom.banguminote.detail.character.data.CharacterData

class SearchCharacterAdapter : BaseQuickAdapter<CharacterData, QuickViewHolder>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder =
        QuickViewHolder(R.layout.item_character, parent)

    override fun onBindViewHolder(
        holder: QuickViewHolder,
        position: Int,
        item: CharacterData?
    ) {
        holder.also { h ->
            item?.images?.medium?.checkHttps()?.also { url ->
                Glide.with(context).load(url).into(h.getView(R.id.ivImage))
            }
            h.setText(
                R.id.tvName,
                item?.infobox?.getOrNull(0)?.actualValue?.value
                    ?.takeIf { it.isNotBlank() } ?: item?.name)
        }
    }

}