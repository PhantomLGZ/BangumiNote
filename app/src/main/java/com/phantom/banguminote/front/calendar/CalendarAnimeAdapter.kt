package com.phantom.banguminote.front.calendar

import android.content.Context
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.phantom.banguminote.R
import com.phantom.banguminote.base.spToPx

class CalendarAnimeAdapter : BaseQuickAdapter<AnimeItemInfo, QuickViewHolder>() {

    override fun onCreateViewHolder(
        context: Context, parent: ViewGroup, viewType: Int
    ): QuickViewHolder = QuickViewHolder(R.layout.item_calendar_anime, parent)

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: AnimeItemInfo?) {
        holder.also {
            item?.also { i ->
                i.images?.common?.replace("http:", "https:")?.also { url ->
                    Glide.with(context).load(url).into(it.getView(R.id.ivCover))
                }
                it.setText(R.id.tvTitle, i.name_cn.takeIf { it.isNotBlank() } ?: i.name)
                it.setText(R.id.tvOriTitle, i.name)
                it.setText(R.id.tvAirDate, i.air_date)
                it.setText(
                    R.id.tvScore,
                    "${"%.1f".format(i.rating?.score ?: 0.0)} | ${i.rating?.total ?: 0}"
                )
                it.getView<BarChart>(R.id.bcScore).also { chart ->
                    chart.setPinchZoom(false)
                    chart.isDoubleTapToZoomEnabled = false
                    chart.isClickable = false
                    chart.xAxis.isEnabled = false
                    chart.axisLeft.isEnabled = false
                    chart.axisRight.isEnabled = false
                    chart.legend.isEnabled = false
                    chart.description.isEnabled = false
                    val dataSet = BarDataSet(
                        (i.rating?.count ?: mapOf(
                            Pair("10", 0),
                            Pair("9", 0),
                            Pair("8", 0),
                            Pair("7", 0),
                            Pair("6", 0),
                            Pair("5", 0),
                            Pair("4", 0),
                            Pair("3", 0),
                            Pair("2", 0),
                            Pair("1", 0),
                        )).map { entry ->
                            BarEntry((11 - entry.key.toInt()) * 1f, entry.value.toFloat())
                        }, ""
                    ).also { data ->
                        data.valueTextSize = 4f.spToPx(context)
                        data.color = context.getColor(R.color.rating_bar_color)
                        data.valueFormatter = DefaultValueFormatter(0)
                    }
                    chart.data = BarData(dataSet)
                }
            }
        }
    }


}