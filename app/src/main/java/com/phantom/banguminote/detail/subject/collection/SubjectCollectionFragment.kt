package com.phantom.banguminote.detail.subject.collection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chad.library.adapter4.BaseQuickAdapter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.base.TagAdapter
import com.phantom.banguminote.base.http.setDataOrObserve
import com.phantom.banguminote.base.spToPx
import com.phantom.banguminote.databinding.FragmentSubjectCollectionBinding
import com.phantom.banguminote.detail.subject.data.SubjectData
import com.phantom.banguminote.detail.subject.SubjectViewModel
import com.phantom.banguminote.getCollectionTypeName
import com.phantom.banguminote.getSubjectActionName
import com.phantom.banguminote.me.collection.CollectionItemRes
import com.phantom.banguminote.search.SearchActivity
import kotlin.math.roundToInt

class SubjectCollectionFragment : BaseFragment<FragmentSubjectCollectionBinding>() {

    private val viewModel: SubjectViewModel by viewModels({ requireParentFragment() })

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubjectCollectionBinding =
        FragmentSubjectCollectionBinding.inflate(inflater, container, false)

    override fun init() {
        viewModel.subjectRes.setDataOrObserve(viewLifecycleOwner) {
            setData(it)
        }
        viewModel.collectionInfoRes.setDataOrObserve(viewLifecycleOwner) {
            setMyCollectionData(it)
        }
    }

    private fun setData(data: SubjectData) {
        binding?.also { b ->
            val type = context?.getSubjectActionName(data.type)
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

    private fun setMyCollectionData(data: CollectionItemRes) {
        binding?.also { b ->
            b.layoutMyCollection.visibility = View.VISIBLE
            b.tvMyCollection.text = context?.getCollectionTypeName(data.type, data.subject_type)
            b.tvEps.visibility = if ((data.subject?.eps ?: 0) == 0) {
                View.GONE
            } else {
                b.tvEps.text = getString(
                    R.string.collection_eps, data.ep_status, data.subject?.eps
                )
                View.VISIBLE
            }
            val rate = data.rate?.toFloat() ?: 0f
            b.rbMyRate.visibility = if (rate == 0f) View.GONE else View.VISIBLE
            b.rbMyRate.rating = rate
            b.tvComment.visibility = if (data.comment.isNullOrBlank()) View.GONE else View.VISIBLE
            b.tvComment.text = data.comment
            b.rvTag.visibility = if (data.tags.isNullOrEmpty()) View.GONE else View.VISIBLE
            if (data.tags?.isNotEmpty() == true) {
                b.rvTag.also { rv ->
                    rv.layoutManager = FlexboxLayoutManager(context)
                    rv.addItemDecoration(FlexboxItemDecoration(context).also {
                        it.setDrawable(
                            AppCompatResources.getDrawable(
                                requireContext(),
                                R.drawable.flexbox_divider
                            )
                        )
                    })
                    rv.adapter = TagAdapter().also {
                        it.setOnItemClickListener(onTagItemClickListener)
                        it.submitList(data.tags)
                    }
                }
            }
        }
    }

    private val onTagItemClickListener =
        BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            findNavController().navigate(
                R.id.action_nav_to_activity_search,
                SearchActivity.createBundleWithTag(adapter.items[position])
            )
        }

}