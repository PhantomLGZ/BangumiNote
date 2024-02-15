package com.phantom.banguminote.detail.subject.related

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter4.BaseQuickAdapter
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.base.http.setDataOrObserve
import com.phantom.banguminote.detail.subject.data.RelatedSubjectData
import com.phantom.banguminote.databinding.FragmentSubjectRelatedBinding
import com.phantom.banguminote.detail.StickyItemDecoration
import com.phantom.banguminote.detail.subject.SubjectFragment
import com.phantom.banguminote.detail.subject.SubjectViewModel

class SubjectRelatedFragment : BaseFragment<FragmentSubjectRelatedBinding>() {

    private val viewModel: SubjectViewModel by viewModels({ requireParentFragment() })
    private val adapter = SubjectRelatedGroupedAdapter()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubjectRelatedBinding =
        FragmentSubjectRelatedBinding.inflate(inflater, container, false)

    override fun init() {
        adapter.mOnItemClickListener = onItemClickListener
        binding?.recyclerView?.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(context)
            it.addItemDecoration(StickyItemDecoration(
                requireContext(),
                { pos ->
                    adapter.items[pos].first
                }
            ))
        }
        viewModel.subjectRelatedSubjectRes.setDataOrObserve(viewLifecycleOwner) {
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
            findNavController().navigate(
                R.id.action_nav_to_activity_subject,
                SubjectFragment.createBundle(adapter.items[position].id ?: 0)
            )
        }

}