package com.phantom.banguminote.front.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.databinding.FragmentCalendarAnimeListBinding
import com.phantom.banguminote.front.calendar.CalendarViewPagerAdapter.Companion.KEY_DAY

class CalendarAnimeListFragment :
    BaseFragment<FragmentCalendarAnimeListBinding>() {

    private var viewModel: CalendarViewModel? = null
    private var day = 0

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCalendarAnimeListBinding =
        FragmentCalendarAnimeListBinding.inflate(inflater, container, false)

    override fun init() {
        day = arguments?.getInt(KEY_DAY) ?: 0
        viewModel = parentFragment?.let { ViewModelProvider(it)[CalendarViewModel::class.java] }
        viewModel?.apply {
            calendarRes.observe(viewLifecycleOwner) { setCalendarData(it) }
        }
        binding?.apply {
            recyclerView.also {
                it.layoutManager = LinearLayoutManager(context)
                it.addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    ).also { drivider ->
                        context?.let { c ->
                            ContextCompat.getDrawable(c, R.drawable.default_drivider)
                        }?.also { d ->
                            drivider.setDrawable(d)
                        }
                    })
                it.adapter = CalendarAnimeAdapter()
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
        (binding?.recyclerView?.adapter as? CalendarAnimeAdapter)?.submitList(
            list.find { it.weekday.id == day }?.items
        )
    }
}