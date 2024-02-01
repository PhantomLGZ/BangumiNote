package com.phantom.banguminote.me.collection

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class CollectionListViewPagerAdapter(
    private val subjectType: Int,
    manager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(manager, lifecycle) {

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment =
        CollectionSubjectListFragment.createFragment(subjectType, getCollectionType(position))

    fun getCollectionType(position: Int): Int =
        when (position) {
            0 -> 1
            1 -> 2
            2 -> 3
            3 -> 4
            4 -> 5
            else -> 0
        }

}