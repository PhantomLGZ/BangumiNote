package com.phantom.banguminote.front.calendar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class BaseViewPagerAdapter(manager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(manager, lifecycle) {

    var fragmentData = mutableListOf<Pair<String, Fragment>>()

    override fun getItemCount(): Int = fragmentData.size

    override fun createFragment(position: Int): Fragment =
        fragmentData[position].second

    fun getTitle(pos: Int): String = fragmentData[pos].first
}