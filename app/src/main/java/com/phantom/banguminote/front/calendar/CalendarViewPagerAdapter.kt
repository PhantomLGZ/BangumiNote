package com.phantom.banguminote.front.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class CalendarViewPagerAdapter(manager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(manager, lifecycle) {

    private val fragmentData = listOf(
        CalendarAnimeListFragment().also {
            it.arguments = Bundle().also { args -> args.putInt(KEY_DAY, 7) }
        },
        CalendarAnimeListFragment().also {
            it.arguments = Bundle().also { args -> args.putInt(KEY_DAY, 1) }
        },
        CalendarAnimeListFragment().also {
            it.arguments = Bundle().also { args -> args.putInt(KEY_DAY, 2) }
        },
        CalendarAnimeListFragment().also {
            it.arguments = Bundle().also { args -> args.putInt(KEY_DAY, 3) }
        },
        CalendarAnimeListFragment().also {
            it.arguments = Bundle().also { args -> args.putInt(KEY_DAY, 4) }
        },
        CalendarAnimeListFragment().also {
            it.arguments = Bundle().also { args -> args.putInt(KEY_DAY, 5) }
        },
        CalendarAnimeListFragment().also {
            it.arguments = Bundle().also { args -> args.putInt(KEY_DAY, 6) }
        },
    )

    override fun getItemCount(): Int = fragmentData.size


    override fun createFragment(position: Int): Fragment =
        fragmentData[position]

    companion object {
        const val KEY_DAY = "Day"
    }
}