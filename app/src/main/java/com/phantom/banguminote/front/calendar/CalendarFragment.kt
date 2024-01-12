package com.phantom.banguminote.front.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.databinding.FragmentCalendarBinding

class CalendarFragment : BaseFragment<FragmentCalendarBinding>() {

    private var viewModel: CalendarViewModel? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCalendarBinding =
        FragmentCalendarBinding.inflate(inflater, container, false)

    override fun init() {
        viewModel = ViewModelProvider(this)[CalendarViewModel::class.java]
        viewModel?.apply {
            error.observe(viewLifecycleOwner) { showToast(it.message) }
            if (calendarRes.value.isNullOrEmpty()) {
                updateCalendarData()
            }
        }
        binding?.apply {
            viewPage.also {
                it.adapter = CalendarViewPagerAdapter(childFragmentManager, lifecycle)
            }
            TabLayoutMediator(tabLayout, viewPage) { tab, pos ->
                tab.customView = getTabView(pos)
            }.attach()
        }
    }

    fun updateCalendarData() {
        viewModel?.calendar()
    }

    private fun getTabView(pos: Int): View {
        return LayoutInflater.from(context).inflate(R.layout.view_tab_week, binding?.root, false)
            .also {
                it.findViewById<ConstraintLayout>(R.id.layout).setBackgroundResource(
                    when (pos) {
                        0 -> R.drawable.color_week_sunday
                        1 -> R.drawable.color_week_monday
                        2 -> R.drawable.color_week_tuesday
                        3 -> R.drawable.color_week_wednesday
                        4 -> R.drawable.color_week_thursday
                        5 -> R.drawable.color_week_friday
                        6 -> R.drawable.color_week_saturday
                        else -> R.drawable.color_nav_item
                    }
                )
                it.findViewById<TextView>(R.id.tvCh).text = when (pos) {
                    0 -> "星期日"
                    1 -> "星期一"
                    2 -> "星期二"
                    3 -> "星期三"
                    4 -> "星期四"
                    5 -> "星期五"
                    6 -> "星期六"
                    else -> "未知"
                }
                it.findViewById<TextView>(R.id.tvEn).text = when (pos) {
                    0 -> "Sun"
                    1 -> "Mon"
                    2 -> "Tue"
                    3 -> "Wed"
                    4 -> "Thu"
                    5 -> "Fri"
                    6 -> "Sat"
                    else -> "Unknown"
                }
                it.findViewById<TextView>(R.id.tvJa).text = when (pos) {
                    0 -> "日"
                    1 -> "月"
                    2 -> "火"
                    3 -> "水"
                    4 -> "木"
                    5 -> "金"
                    6 -> "土"
                    else -> "未知"
                }
            }
    }
}

