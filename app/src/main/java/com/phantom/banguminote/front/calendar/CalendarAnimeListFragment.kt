package com.phantom.banguminote.front.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState
import com.phantom.banguminote.TransparentDividerItemDecoration
import com.phantom.banguminote.ImageDialogFragment
import com.phantom.banguminote.ImageDialogFragment.Companion.KEY_IMAGE_URL
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.databinding.FragmentCalendarAnimeListBinding
import com.phantom.banguminote.front.calendar.CalendarFragment.Companion.KEY_DAY
import com.phantom.banguminote.detail.subject.SubjectFragment

class CalendarAnimeListFragment :
    BaseFragment<FragmentCalendarAnimeListBinding>() {

    private var viewModel: CalendarViewModel? = null
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
        viewModel = parentFragment?.let { ViewModelProvider(it)[CalendarViewModel::class.java] }
        viewModel?.apply {
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
        viewModel?.calendarRes?.value?.takeIf { it.isNotEmpty() }?.also {
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
                ImageDialogFragment().also {
                    it.arguments = Bundle().also { args ->
                        args.putString(KEY_IMAGE_URL, url)
                    }
                }.show(childFragmentManager, "ImageDialog")
            }
        }
}