package com.phantom.banguminote.calendar

import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatRatingBar
import com.bumptech.glide.Glide
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.phantom.banguminote.R
import com.phantom.banguminote.base.checkHttps

class CalendarAnimeAdapter : BaseQuickAdapter<AnimeItemInfo, QuickViewHolder>() {

    override fun onCreateViewHolder(
        context: Context, parent: ViewGroup, viewType: Int
    ): QuickViewHolder = QuickViewHolder(R.layout.item_calendar_anime, parent)

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: AnimeItemInfo?) {
        holder.also {
            item?.also { i ->
                i.images?.common?.checkHttps()?.also { url ->
                    Glide.with(context).load(url).into(it.getView(R.id.ivCover))
                }
                it.setText(R.id.tvTitle, i.name_cn.takeIf { it.isNotBlank() } ?: i.name)
                it.setText(R.id.tvOriTitle, i.name)
                it.setText(R.id.tvAirDate, i.air_date)
                it.getView<AppCompatRatingBar>(R.id.ratingBar).rating =
                    ((i.rating?.score ?: 0.0) / 2.0).toFloat()
                it.setText(R.id.tvScore, "%.1f".format(i.rating?.score ?: 0.0))
                it.setText(
                    R.id.tvRatingTotal,
                    "(${context.getString(R.string.subject_rating_total, i.rating?.total ?: 0)})"
                )
            }
        }
    }


}