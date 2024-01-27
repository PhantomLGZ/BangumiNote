package com.phantom.banguminote.detail.subject.summary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import com.chad.library.adapter4.BaseQuickAdapter
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.base.http.setDataOrObserve
import com.phantom.banguminote.data.TagsData
import com.phantom.banguminote.databinding.FragmentSubjectSummaryBinding
import com.phantom.banguminote.databinding.LayoutDetailSummaryBinding
import com.phantom.banguminote.detail.subject.data.SubjectData
import com.phantom.banguminote.detail.subject.SubjectViewModel

class SubjectSummaryFragment : BaseFragment<FragmentSubjectSummaryBinding>() {

    private var viewModel: SubjectViewModel? = null
    private var mergeBinding: LayoutDetailSummaryBinding? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubjectSummaryBinding =
        FragmentSubjectSummaryBinding.inflate(inflater, container, false)

    override fun init() {
        viewModel = parentFragment?.let { ViewModelProvider(it)[SubjectViewModel::class.java] }
        viewModel?.subjectRes?.setDataOrObserve(viewLifecycleOwner) {
            setData(it)
        }
        mergeBinding = binding?.root?.let { LayoutDetailSummaryBinding.bind(it) }
    }

    private fun setData(data: SubjectData) {
        mergeBinding?.also { b ->
            b.tvSummary.text = data.summary
        }
        binding?.also { b ->
            b.recyclerView.also { rv ->
                rv.layoutManager = FlexboxLayoutManager(context).also {
                    it.justifyContent = JustifyContent.SPACE_AROUND
                }
                rv.addItemDecoration(FlexboxItemDecoration(context).also {
                    it.setDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.flexbox_divider
                        )
                    )
                })
                rv.adapter = SubjectTagAdapter().also {
                    it.setOnItemClickListener(onItemClickListener)
                    it.submitList(data.tags)
                }
            }
        }
    }

    private val onItemClickListener =
        BaseQuickAdapter.OnItemClickListener<TagsData> { adapter, view, position ->
            // TODO
            println("TEST ${adapter.items[position].name}")
        }
}