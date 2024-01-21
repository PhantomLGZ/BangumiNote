package com.phantom.banguminote.subject.summary

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
import com.phantom.banguminote.data.TagsData
import com.phantom.banguminote.databinding.FragmentSubjectSummaryBinding
import com.phantom.banguminote.subject.SubjectViewModel

class SubjectSummaryFragment : BaseFragment<FragmentSubjectSummaryBinding>() {

    private var viewModel: SubjectViewModel? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubjectSummaryBinding =
        FragmentSubjectSummaryBinding.inflate(inflater, container, false)

    override fun init() {
        viewModel = activity?.let { ViewModelProvider(it)[SubjectViewModel::class.java] }
        viewModel?.subjectRes?.value?.also { data ->
            binding?.also { b ->
                b.tvSummary.text = data.summary
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
    }

    private val onItemClickListener =
        BaseQuickAdapter.OnItemClickListener<TagsData> { adapter, view, position ->
            println("TEST ${adapter.items[position].name}")
        }
}