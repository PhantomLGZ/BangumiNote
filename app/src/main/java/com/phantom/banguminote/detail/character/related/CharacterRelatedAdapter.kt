package com.phantom.banguminote.detail.character.related

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.phantom.banguminote.R
import com.phantom.banguminote.TransparentDividerItemDecoration
import com.phantom.banguminote.detail.character.data.CharacterPersonData
import com.phantom.banguminote.detail.character.data.CharacterRelatedData

class CharacterRelatedAdapter : BaseQuickAdapter<CharacterRelatedData, QuickViewHolder>() {

    var mOnItemClickListener: OnItemClickListener<CharacterPersonData>? = null

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder =
        QuickViewHolder(R.layout.item_character_related, parent)

    override fun onBindViewHolder(
        holder: QuickViewHolder,
        position: Int,
        item: CharacterRelatedData?
    ) {
        holder.also { h ->
            h.setText(R.id.tvRelation, item?.staff)
            Glide.with(context)
                .load(item?.image
                    ?.takeIf { it.isNotBlank() } ?: "https://bangumi.tv/img/info_only.png")
                .into(h.getView(R.id.ivImage))
            h.setText(R.id.tvName, item?.name_cn)
            h.setGone(R.id.tvName, item?.name_cn?.isBlank() ?: true)
            h.setText(R.id.tvOriName, item?.name)
            h.setGone(R.id.tvOriName, item?.name?.isBlank() ?: true)
            h.setGone(R.id.recyclerView, item?.persons?.isEmpty() ?: true)
            item?.persons?.takeIf { it.isNotEmpty() }?.also { list ->
                h.getView<RecyclerView>(R.id.recyclerView).also {
                    it.layoutManager = GridLayoutManager(context, 2)
                    it.adapter = ActorAdapter().also { a ->
                        a.setOnItemClickListener(mOnItemClickListener)
                        a.submitList(list)
                    }
                    it.addItemDecoration(TransparentDividerItemDecoration.vertical(context))
                    it.addItemDecoration(TransparentDividerItemDecoration.horizontal(context))
                }
            }
        }
    }

    class ActorAdapter : BaseQuickAdapter<CharacterPersonData, QuickViewHolder>() {

        override fun onCreateViewHolder(
            context: Context,
            parent: ViewGroup,
            viewType: Int
        ): QuickViewHolder =
            QuickViewHolder(R.layout.item_character_actor, parent)

        override fun onBindViewHolder(
            holder: QuickViewHolder,
            position: Int,
            item: CharacterPersonData?
        ) {
            holder.also { h ->
                Glide.with(context)
                    .load(item?.images?.small
                        ?.takeIf { it.isNotBlank() } ?: "https://bangumi.tv/img/info_only.png")
                    .into(h.getView(R.id.ivImage))
                h.setText(R.id.tvName, item?.name)
            }
        }

    }

}