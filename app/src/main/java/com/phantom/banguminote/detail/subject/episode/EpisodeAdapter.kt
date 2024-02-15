package com.phantom.banguminote.detail.subject.episode

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.phantom.banguminote.R

class EpisodeAdapter : BaseQuickAdapter<EpisodeData, QuickViewHolder>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder =
        QuickViewHolder(R.layout.item_episode, parent)

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: EpisodeData?) {
        holder.also { h ->
            h.setGone(R.id.tvEpNum, item?.ep?.toString().isNullOrBlank())
            h.setText(R.id.tvEpNum, item?.ep?.toString() ?: "")
            h.setText(R.id.tvName, item?.name_cn?.takeIf { it.isNotBlank() } ?: item?.name)
            h.setText(
                R.id.tvInfo,
                context.getString(R.string.episode_info, item?.airdate ?: "", item?.duration ?: "")
            )
            h.setGone(R.id.tvSummary, item?.desc.isNullOrBlank())
            h.setText(R.id.tvSummary, item?.desc)
        }
    }
}