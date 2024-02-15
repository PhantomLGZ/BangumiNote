package com.phantom.banguminote.detail.subject.episode

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.base.TransparentDividerItemDecoration
import com.phantom.banguminote.data.PageReqData
import com.phantom.banguminote.data.PageResData
import com.phantom.banguminote.databinding.FragmentEpisodeListBinding
import com.phantom.banguminote.detail.StickyItemDecoration
import com.phantom.banguminote.detail.subject.SubjectViewModel
import com.phantom.banguminote.getEpisodeTypeName

class EpisodeListFragment : BaseFragment<FragmentEpisodeListBinding>() {

    private val viewModel: SubjectViewModel by viewModels({ requireParentFragment() })
    private val mAdapter = EpisodeAdapter()
    private var helper: QuickAdapterHelper? = null
    private val limit = 10
    private var offset = 0
    private val typeList = mutableListOf(0, 1, 2, 3, 4, 6)
    private var nowType = typeList.first()
    private var isQuerying = false

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEpisodeListBinding =
        FragmentEpisodeListBinding.inflate(inflater, container, false)

    override fun init() {
        offset = 0
        helper = QuickAdapterHelper.Builder(mAdapter)
            .setTrailingLoadStateAdapter(onTrailingListener)
            .build()
        binding?.also { b ->
            b.tbEpisodeMain.setOnCheckedChangeListener(onTypeCheckedChangeListener)
            b.tbEpisodeSp.setOnCheckedChangeListener(onTypeCheckedChangeListener)
            b.tbEpisodeOp.setOnCheckedChangeListener(onTypeCheckedChangeListener)
            b.tbEpisodeEd.setOnCheckedChangeListener(onTypeCheckedChangeListener)
            b.tbEpisodePv.setOnCheckedChangeListener(onTypeCheckedChangeListener)
            b.tbEpisodeMad.setOnCheckedChangeListener(onTypeCheckedChangeListener)
            b.tbEpisodeOther.setOnCheckedChangeListener(onTypeCheckedChangeListener)

            b.recyclerView.also { rv ->
                rv.layoutManager = LinearLayoutManager(requireContext())
                rv.adapter = helper?.adapter
                rv.addItemDecoration(TransparentDividerItemDecoration.vertical(requireContext()))
                rv.addItemDecoration(StickyItemDecoration(
                    requireContext(),
                    {
                        requireContext().getEpisodeTypeName(mAdapter.getItem(it)?.type)
                    },
                    {
                        val nowType = mAdapter.getItem(it)?.type
                        val preType = mAdapter.getItem(it - 1)?.type
                        it == 0 || (nowType != null && preType != null && nowType != preType)
                    },
                    { mAdapter.itemCount > 0 }
                ))
            }
        }
        viewModel.episodeRes.observe(viewLifecycleOwner) { setData(it) }
        doNewQuery()
    }

    private fun doNewQuery() {
        if (typeList.isNotEmpty()) {
            nowType = typeList.first()
            offset = 0
            doQuery()
        } else {
            mAdapter.submitList(listOf())
        }
    }

    private fun doQuery() {
        viewModel.id.value?.let {
            isQuerying = true
            viewModel.getEpisode(PageReqData(EpisodeReq(it, nowType), limit, offset))
        }
    }

    private fun setData(data: PageResData<EpisodeData>) {
        if (typeList.isNotEmpty()) {
            if (offset == 0 && nowType == typeList.first()) {
                mAdapter.submitList(data.data)
            } else {
                mAdapter.addAll(data.data)
            }
            helper?.trailingLoadState = LoadState.NotLoading(
                (data.data.size + offset) >= data.total
                        && nowType == typeList.last()
            )
            if ((data.data.size + offset) >= data.total && nowType < typeList.last()) {
                nowType = typeList.find { it > nowType } ?: 0
                offset = 0
            } else {
                offset += limit
            }
        } else {
            helper?.trailingLoadState = LoadState.NotLoading(true)
        }
        isQuerying = false
    }

    private val onTrailingListener = object : TrailingLoadStateAdapter.OnTrailingListener {
        override fun onFailRetry() {}

        override fun onLoad() {
            doQuery()
        }

        override fun isAllowLoading(): Boolean = !isQuerying
    }

    private val onTypeCheckedChangeListener =
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            when (buttonView) {
                binding?.tbEpisodeMain -> {
                    handleTypeList(isChecked, 0)
                }

                binding?.tbEpisodeSp -> {
                    handleTypeList(isChecked, 1)
                }

                binding?.tbEpisodeOp -> {
                    handleTypeList(isChecked, 2)
                }

                binding?.tbEpisodeEd -> {
                    handleTypeList(isChecked, 3)
                }

                binding?.tbEpisodePv -> {
                    handleTypeList(isChecked, 4)
                }

                binding?.tbEpisodeMad -> {
                    handleTypeList(isChecked, 5)
                }

                binding?.tbEpisodeOther -> {
                    handleTypeList(isChecked, 6)
                }
            }
        }

    private fun handleTypeList(isChecked: Boolean, type: Int) {
        if (isChecked) {
            if (!typeList.contains(type)) {
                typeList.add(type)
                typeList.sortBy { it }
            }
        } else {
            typeList.remove(type)
        }
        doNewQuery()
    }
}