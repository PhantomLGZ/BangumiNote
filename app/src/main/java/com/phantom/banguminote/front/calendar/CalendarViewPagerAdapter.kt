package com.phantom.banguminote.front.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class CalendarViewPagerAdapter(manager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(manager, lifecycle) {

    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment =
        CalendarAnimeListFragment().also { f ->
            f.arguments = Bundle().also {
                it.putInt(
                    CalendarFragment.KEY_DAY,
                    when (position) {
                        0 -> 7
                        else -> position
                    }
                )
            }
        }

}