package com.phantom.banguminote.me.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.base.ImageDialogFragment
import com.phantom.banguminote.base.TransparentDividerItemDecoration
import com.phantom.banguminote.base.getUserName
import com.phantom.banguminote.data.PageReqData
import com.phantom.banguminote.data.PageResData
import com.phantom.banguminote.databinding.FragmentCollectionSubjectListBinding
import com.phantom.banguminote.detail.subject.SubjectFragment
import com.phantom.banguminote.search.SearchActivity

class CollectionSubjectListFragment : BaseFragment<FragmentCollectionSubjectListBinding>() {

    private val viewModel: CollectionViewModel by viewModels()
    private val mAdapter = CollectionAdapter()
    private var helper: QuickAdapterHelper? = null
    private var subjectType = 0
    private var collectionType = 0
    private val limit = 10
    private var offset = 0

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCollectionSubjectListBinding =
        FragmentCollectionSubjectListBinding.inflate(inflater, container, false)

    override fun init() {
        subjectType = arguments?.getInt(KEY_SUBJECT_TYPE) ?: 0
        collectionType = arguments?.getInt(KEY_TYPE) ?: 0

        helper = QuickAdapterHelper.Builder(mAdapter)
            .setTrailingLoadStateAdapter(onTrailingListener)
            .build()

        mAdapter.setOnItemClickListener(onItemClickListener)
        mAdapter.mOnItemClickListener = onTagItemClickListener
        mAdapter.addOnItemChildClickListener(R.id.ivCover, onItemChildClickListener)
        binding?.also { b ->
            b.refreshLayout.setOnRefreshListener { doNewInquire() }
            b.recyclerView.also { rv ->
                rv.layoutManager = LinearLayoutManager(context)
                rv.adapter = helper?.adapter
                rv.addItemDecoration(TransparentDividerItemDecoration.vertical(requireContext()))
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { showToast(it.message) }
        viewModel.collectionRes.observe(viewLifecycleOwner) { setData(it) }
        doNewInquire()
    }

    private fun setData(data: PageResData<CollectionItemRes>) {
        binding?.refreshLayout?.isRefreshing = false
        if (offset == 0) {
            mAdapter.submitList(data.data)
        } else {
            mAdapter.addAll(data.data)
        }
        helper?.trailingLoadState = LoadState.NotLoading(
            (data.data.size + offset) >= data.total
        )
    }

    private val onTrailingListener = object : TrailingLoadStateAdapter.OnTrailingListener {
        override fun onFailRetry() {}

        override fun onLoad() {
            offset += limit
            inquire()
        }

        override fun isAllowLoading(): Boolean {
            return binding?.refreshLayout?.isRefreshing?.not() ?: true
        }
    }

    private fun doNewInquire() {
        offset = 0
        binding?.refreshLayout?.isRefreshing = inquire()
    }

    private fun inquire(): Boolean =
        context?.getUserName()?.takeIf { it.isNotBlank() }?.let {
            viewModel.getCollections(
                PageReqData(
                    CollectionsReq(it, subjectType, collectionType),
                    limit, offset
                )
            )
            true
        } ?: false

    private val onItemClickListener =
        BaseQuickAdapter.OnItemClickListener<CollectionItemRes> { adapter, view, position ->
            findNavController().navigate(
                R.id.action_nav_to_activity_subject,
                SubjectFragment.createBundle(adapter.items[position].subject_id)
            )
        }

    private val onTagItemClickListener =
        BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            findNavController().navigate(
                R.id.action_nav_to_activity_search,
                SearchActivity.createBundleWithTag(adapter.items[position])
            )
        }

    private val onItemChildClickListener =
        BaseQuickAdapter.OnItemChildClickListener<CollectionItemRes> { adapter, view, position ->
            val url = adapter.items[position].subject?.images?.large
            if (url?.isNotBlank() == true) {
                ImageDialogFragment.createFragment(url)
                    .show(childFragmentManager, "ImageDialog")
            }
        }

    companion object {
        const val KEY_SUBJECT_TYPE = "KEY_SUBJECT_TYPE"
        const val KEY_TYPE = "KEY_TYPE"

        fun createFragment(subjectType: Int, type: Int): CollectionSubjectListFragment =
            CollectionSubjectListFragment().also { f ->
                f.arguments = Bundle().also {
                    it.putInt(KEY_SUBJECT_TYPE, subjectType)
                    it.putInt(KEY_TYPE, type)
                }
            }
    }
}