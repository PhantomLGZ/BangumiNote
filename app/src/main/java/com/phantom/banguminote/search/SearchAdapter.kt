package com.phantom.banguminote.search

import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatRatingBar
import com.bumptech.glide.Glide
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.phantom.banguminote.R
import com.phantom.banguminote.base.checkHttps

class SearchAdapter : BaseQuickAdapter<SearchRes, QuickViewHolder>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder =
        QuickViewHolder(R.layout.item_subject, parent)

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: SearchRes?) {
        holder.also { h ->
            item?.image?.checkHttps()?.also { url ->
                Glide.with(context).load(url).into(h.getView(R.id.ivCover))
            }
            h.setText(R.id.tvTitle, item?.name_cn?.takeIf { it.isNotBlank() } ?: item?.name)
            h.setText(R.id.tvAirDate, item?.date)
            h.getView<AppCompatRatingBar>(R.id.ratingBar).rating =
                ((item?.score ?: 0.0) / 2.0).toFloat()
            h.setText(R.id.tvScore, "%.1f".format(item?.score ?: 0.0))
            h.setImageDrawable(
                R.id.ivType,
                AppCompatResources.getDrawable(
                    context,
                    when (item?.type) {
                        1 -> R.drawable.round_book_24
                        2 -> R.drawable.round_local_movies_24
                        3 -> R.drawable.round_music_note_24
                        4 -> R.drawable.round_videogame_asset_24
                        6 -> R.drawable.round_live_tv_24
                        else -> R.drawable.shape_gradient_background1
                    }
                )
            )
        }
    }

}