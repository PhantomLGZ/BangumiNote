package com.phantom.banguminote.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState
import com.phantom.banguminote.base.TransparentDividerItemDecoration
import com.phantom.banguminote.base.ImageDialogFragment
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.databinding.FragmentCalendarAnimeListBinding
import com.phantom.banguminote.detail.subject.SubjectFragment

class CalendarAnimeListFragment :
    BaseFragment<FragmentCalendarAnimeListBinding>() {

    private val viewModel: CalendarViewModel by viewModels({ requireParentFragment() })
    private var day = 0
    private val mAdapter = CalendarAnimeAdapter()
    private lateinit var helper: QuickAdapterHelper

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCalendarAnimeListBinding =
        FragmentCalendarAnimeListBinding.inflate(inflater, container, false)

    override fun init() {
        day = arguments?.getInt(KEY_DAY) ?: 0
        mAdapter.also {
            it.setOnItemClickListener(onItemClickListener)
            it.addOnItemChildClickListener(R.id.ivCover, onItemChildClickListener)
        }
        helper = QuickAdapterHelper.Builder(mAdapter)
            .build()
        viewModel.apply {
            error.observe(viewLifecycleOwner) { binding?.refreshLayout?.isRefreshing = false }
            calendarRes.observe(viewLifecycleOwner) { setCalendarData(it) }
        }
        binding?.apply {
            refreshLayout.also {
                it.isRefreshing = true
                it.setOnRefreshListener {
                    (parentFragment as? CalendarFragment)?.updateCalendarData()
                }
            }
            recyclerView.also {
                it.layoutManager = LinearLayoutManager(context)
                it.addItemDecoration(TransparentDividerItemDecoration(requireContext()))
                it.adapter = helper.adapter
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.calendarRes.value?.takeIf { it.isNotEmpty() }?.also {
            setCalendarData(it)
        }
    }

    private fun setCalendarData(list: List<CalendarRes>) {
        binding?.refreshLayout?.isRefreshing = false
        mAdapter.submitList(
            list.find { it.weekday.id == day }?.items
        )
        helper.trailingLoadState = LoadState.NotLoading(true)
    }

    private val onItemClickListener =
        BaseQuickAdapter.OnItemClickListener<AnimeItemInfo> { adapter, view, position ->
            findNavController().navigate(
                R.id.action_nav_to_activity_subject,
                SubjectFragment.createBundle(adapter.items[position].id)
            )
        }

    private val onItemChildClickListener =
        BaseQuickAdapter.OnItemChildClickListener<AnimeItemInfo> { adapter, view, position ->
            val url = adapter.items[position].images?.large
            if (url?.isNotBlank() == true) {
                ImageDialogFragment.createFragment(url)
                    .show(childFragmentManager, "ImageDialog")
            }
        }

    companion object {

        const val KEY_DAY = "Day"

        fun createFragment(day: Int) =
            CalendarAnimeListFragment().also { f ->
                f.arguments = Bundle().also { it.putInt(KEY_DAY, day) }
            }
    }
}