package com.phantom.banguminote.detail.subject.collection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.base.http.setDataOrObserve
import com.phantom.banguminote.base.spToPx
import com.phantom.banguminote.databinding.FragmentSubjectCollectionBinding
import com.phantom.banguminote.detail.subject.data.SubjectData
import com.phantom.banguminote.detail.subject.SubjectViewModel
import kotlin.math.roundToInt

class SubjectCollectionFragment : BaseFragment<FragmentSubjectCollectionBinding>() {

    private var viewModel: SubjectViewModel? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubjectCollectionBinding =
        FragmentSubjectCollectionBinding.inflate(inflater, container, false)

    override fun init() {
        viewModel = parentFragment?.let { ViewModelProvider(it)[SubjectViewModel::class.java] }
        viewModel?.subjectRes?.setDataOrObserve(viewLifecycleOwner) {
            setData(it)
        }
    }

    private fun setData(data: SubjectData) {
        binding?.also { b ->
            val type = when (data.type) {
                1 -> getString(R.string.collection_read)
                2 -> getString(R.string.collection_watch)
                3 -> getString(R.string.collection_listen)
                4 -> getString(R.string.collection_play)
                6 -> getString(R.string.collection_watch)
                else -> ""
            }
            b.tvWish.text = getString(R.string.collection_wish, type)
            b.tvWishCount.text = data.collection?.wish.toString()
            b.tvDoing.text = getString(R.string.collection_doing, type)
            b.tvDoingCount.text = data.collection?.doing.toString()
            b.tvCollect.text = getString(R.string.collection_collect, type)
            b.tvCollectCount.text = data.collection?.collect.toString()
            b.tvOnHoldCount.text = data.collection?.on_hold.toString()
            b.tvDroppedCount.text = data.collection?.dropped.toString()

            b.ratingBar.rating = data.rating?.score?.toFloat() ?: 0f
            b.tvScore.text = getString(R.string.subject_rating_score, data.rating?.score ?: 0.0)
            b.tvRatingTotal.text =
                getString(R.string.subject_rating_total, data.rating?.total ?: 0)


            b.barChart.also { chart ->
                chart.setScaleEnabled(false)
                chart.isClickable = false
                chart.xAxis.also {
                    it.setDrawGridLines(false)
                    it.position = XAxis.XAxisPosition.BOTTOM_INSIDE
                    it.textSize = 6f.spToPx(context)
                    it.textColor = requireContext().getColor(R.color.rating_bar_color)
                    it.valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return (11 - value.roundToInt()).toString()
                        }
                    }
                    it.granularity = 1f
                    it.setLabelCount(11, true)
                    it.setCenterAxisLabels(true)
                }
                chart.axisLeft.isEnabled = false
                chart.axisRight.isEnabled = false
                chart.legend.isEnabled = false
                chart.description.isEnabled = false
                val dataSet = BarDataSet(
                    (data.rating?.count ?: mapOf(
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
                    )).map {
                        BarEntry((11 - it.key.toInt()).toFloat(), it.value.toFloat())
                    },
                    ""
                ).also {
                    it.valueTextSize = 6f.spToPx(context)
                    it.color = requireContext().getColor(R.color.rating_bar_color)
                    it.valueFormatter = DefaultValueFormatter(0)
                }
                chart.data = BarData(dataSet)
            }
        }
    }

}