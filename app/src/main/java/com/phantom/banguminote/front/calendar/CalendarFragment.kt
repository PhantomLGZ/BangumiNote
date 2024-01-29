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
import java.time.LocalDate

class CalendarFragment : BaseFragment<FragmentCalendarBinding>() {

    private var viewModel: CalendarViewModel? = null
    private lateinit var viewPageAdapter: CalendarViewPagerAdapter

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCalendarBinding =
        FragmentCalendarBinding.inflate(inflater, container, false)

    override fun init() {
        viewPageAdapter = CalendarViewPagerAdapter(childFragmentManager, lifecycle)
        viewModel = ViewModelProvider(this)[CalendarViewModel::class.java]
        viewModel?.apply {
            error.observe(viewLifecycleOwner) { showToast(it.message) }
            if (calendarRes.value.isNullOrEmpty()) {
                updateCalendarData()
            }
        }
        binding?.apply {
            viewPage.also {
                it.adapter = viewPageAdapter
            }
            TabLayoutMediator(tabLayout, viewPage) { tab, pos ->
                tab.customView = getTabView(pos)
            }.attach()
            tabLayout.getTabAt(
                when (LocalDate.now().dayOfWeek.value) {
                    in 1..6 -> LocalDate.now().dayOfWeek.value
                    7 -> 0
                    else -> 0
                }
            )?.select()
        }
    }

    fun updateCalendarData() {
        viewModel?.calendar()
    }

    private fun getTabView(pos: Int): View {
        return LayoutInflater.from(context).inflate(R.layout.view_tab_week, binding?.root, false)
            .also {
                it.findViewById<ConstraintLayout>(R.id.layout).setBackgroundColor(
                    requireContext().getColor(
                        when (pos) {
                            0 -> R.color.color_week_sunday
                            1 -> R.color.color_week_monday
                            2 -> R.color.color_week_tuesday
                            3 -> R.color.color_week_wednesday
                            4 -> R.color.color_week_thursday
                            5 -> R.color.color_week_friday
                            6 -> R.color.color_week_saturday
                            else -> R.color.color_nav_item
                        }
                    )
                )
                it.findViewById<TextView>(R.id.tvCh).text = weekDay.getOrNull(pos)?.cn ?: "未知"
                it.findViewById<TextView>(R.id.tvEn).text = weekDay.getOrNull(pos)?.en ?: "Unknown"
                it.findViewById<TextView>(R.id.tvJa).text = weekDay.getOrNull(pos)?.ja ?: "未"
            }
    }

    companion object {
        const val KEY_DAY = "Day"

        val weekDay = listOf(
            WeekDayInfo("Sun", "星期日", "日", 7),
            WeekDayInfo("Mon", "星期一", "月", 1),
            WeekDayInfo("Tue", "星期二", "火", 2),
            WeekDayInfo("Wed", "星期三", "水", 3),
            WeekDayInfo("Thu", "星期四", "木", 4),
            WeekDayInfo("Fri", "星期五", "金", 5),
            WeekDayInfo("Sat", "星期六", "土", 6),
        )
    }

}

