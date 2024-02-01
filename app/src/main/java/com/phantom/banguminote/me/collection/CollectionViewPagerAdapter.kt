package com.phantom.banguminote.me.collection

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class CollectionViewPagerAdapter(manager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(manager, lifecycle) {

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment =
        CollectionSubjectFragment.createFragment(getSubjectType(position))

    fun getSubjectType(position: Int): Int =
        when (position) {
            0 -> 2
            1 -> 1
            2 -> 4
            3 -> 3
            4 -> 6
            else -> 0
        }

}