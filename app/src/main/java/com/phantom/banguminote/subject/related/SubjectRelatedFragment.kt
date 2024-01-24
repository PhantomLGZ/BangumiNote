package com.phantom.banguminote.subject.related

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter4.BaseQuickAdapter
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.base.http.setDataOrObserve
import com.phantom.banguminote.data.RelatedSubjectData
import com.phantom.banguminote.databinding.FragmentSubjectRelatedBinding
import com.phantom.banguminote.subject.StickyItemDecoration
import com.phantom.banguminote.subject.SubjectViewModel

class SubjectRelatedFragment : BaseFragment<FragmentSubjectRelatedBinding>() {

    private var viewModel: SubjectViewModel? = null

    private val adapter = SubjectRelatedGroupedAdapter()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubjectRelatedBinding =
        FragmentSubjectRelatedBinding.inflate(inflater, container, false)

    override fun init() {
        viewModel = activity?.let { ViewModelProvider(it)[SubjectViewModel::class.java] }
        adapter.mOnItemClickListener = onItemClickListener
        binding?.recyclerView?.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(context)
            it.addItemDecoration(StickyItemDecoration(requireContext()) { pos ->
                adapter.items[pos].first
            })
        }
        viewModel?.subjectRelatedSubjectRes?.setDataOrObserve(viewLifecycleOwner) {
            setData(it)
        }
    }

    private fun setData(data: List<RelatedSubjectData>) {
        data.sortedBy { it.relation }.groupBy { it.relation }.map {
            Pair(it.key ?: "", it.value)
        }.also {
            adapter.submitList(it)
        }
    }

    private val onItemClickListener =
        BaseQuickAdapter.OnItemClickListener<RelatedSubjectData> { adapter, view, position ->
            // TODO
            println("TEST ${adapter.items[position].id}")
        }

}