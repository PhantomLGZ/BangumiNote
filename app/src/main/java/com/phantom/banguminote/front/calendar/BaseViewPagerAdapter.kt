package com.phantom.banguminote.front.calendar

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class BaseViewPagerAdapter(manager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(manager, lifecycle) {

    data class FragmentData(
        val title: String = "",
        val fragment: Fragment,
    )

    var fragmentData = mutableListOf<FragmentData>()

    override fun getItemCount(): Int = fragmentData.size

    override fun createFragment(position: Int): Fragment =
        fragmentData[position].fragment

    @SuppressLint("NotifyDataSetChanged")
    fun addFragments(vararg fragments: FragmentData) {
        fragmentData.addAll(fragments.toList())
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun remove(title: String) {
        val pos = fragmentData.indexOfFirst { it.title == title }
        fragmentData.removeAt(pos)
        notifyItemRangeChanged(pos, fragmentData.size)
        notifyDataSetChanged()
    }

    fun getTitle(pos: Int): String = fragmentData[pos].title
}