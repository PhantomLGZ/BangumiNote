package com.phantom.banguminote.me.collection

import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.phantom.banguminote.R
import com.phantom.banguminote.base.checkHttps
import com.phantom.banguminote.base.TagAdapter
import com.phantom.banguminote.base.reformatDate

class CollectionAdapter : BaseQuickAdapter<CollectionItemRes, QuickViewHolder>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder =
        QuickViewHolder(R.layout.item_collection, parent)

    override fun onBindViewHolder(
        holder: QuickViewHolder,
        position: Int,
        item: CollectionItemRes?
    ) {
        holder.also { h ->
            val rateVisible = (item?.rate ?: 0) != 0
            val epsVisible = (item?.ep_status ?: 0) != 0 || (item?.subject?.eps ?: 0) != 0
            h.setVisible(R.id.rbMyRate, rateVisible)
            h.getView<AppCompatRatingBar>(R.id.rbMyRate).rating = item?.rate?.toFloat() ?: 0f
            h.setVisible(R.id.tvEps, epsVisible)
            h.setText(
                R.id.tvEps,
                context.getString(
                    R.string.collection_eps,
                    item?.ep_status ?: 0,
                    item?.subject?.eps ?: 0
                )
            )
            h.setGone(R.id.layoutCollection, !rateVisible && !epsVisible)
            h.setText(R.id.tvCollectionDate, item?.updated_at?.reformatDate())
            item?.subject?.images?.common?.checkHttps()?.also { url ->
                Glide.with(context).load(url).into(h.getView(R.id.ivCover))
            }
            h.setText(
                R.id.tvTitle,
                item?.subject?.name_cn?.takeIf { it.isNotBlank() } ?: item?.subject?.name)
            h.setText(R.id.tvAirDate, item?.subject?.date)
            h.getView<AppCompatRatingBar>(R.id.ratingBar).rating =
                ((item?.subject?.score ?: 0.0) / 2.0).toFloat()
            h.setText(R.id.tvScore, "%.1f".format(item?.subject?.score ?: 0.0))
            h.setImageDrawable(
                R.id.ivType,
                AppCompatResources.getDrawable(
                    context,
                    when (item?.subject_type) {
                        1 -> R.drawable.round_book_24
                        2 -> R.drawable.round_local_movies_24
                        3 -> R.drawable.round_music_note_24
                        4 -> R.drawable.round_videogame_asset_24
                        6 -> R.drawable.round_live_tv_24
                        else -> R.drawable.shape_gradient_background1
                    }
                )
            )
            h.setGone(R.id.rvTag, item?.tags.isNullOrEmpty())
            if (item?.tags?.isNotEmpty() == true) {
                h.getView<RecyclerView>(R.id.rvTag).also { rv ->
                    rv.layoutManager = FlexboxLayoutManager(context)
                    rv.addItemDecoration(FlexboxItemDecoration(context).also {
                        it.setDrawable(
                            AppCompatResources.getDrawable(context, R.drawable.flexbox_divider)
                        )
                    })
                    rv.adapter = TagAdapter().also {
                        it.setOnItemClickListener(mOnItemClickListener)
                        it.submitList(item.tags)
                    }
                }
            }
        }
    }

    var mOnItemClickListener: OnItemClickListener<String>? = null

}