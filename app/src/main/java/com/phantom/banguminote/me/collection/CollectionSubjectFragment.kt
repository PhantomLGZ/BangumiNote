package com.phantom.banguminote.me.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.databinding.FragmentCollectionSubjectBinding
import com.phantom.banguminote.getCollectionTypeName

class CollectionSubjectFragment : BaseFragment<FragmentCollectionSubjectBinding>() {

    private var adapter: CollectionListViewPagerAdapter? = null
    private var subjectType = 0

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCollectionSubjectBinding =
        FragmentCollectionSubjectBinding.inflate(inflater, container, false)

    override fun init() {
        subjectType = arguments?.getInt(KEY_SUBJECT_TYPE) ?: 0
        adapter = CollectionListViewPagerAdapter(subjectType, childFragmentManager, lifecycle)
        binding?.also { b ->
            b.viewPage.adapter = adapter
            TabLayoutMediator(b.tabLayout, b.viewPage) { tab, position ->
                tab.text = context?.getCollectionTypeName(
                    adapter?.getCollectionType(position),
                    subjectType
                )
            }.attach()
        }
    }

    companion object {
        const val KEY_SUBJECT_TYPE = "KEY_SUBJECT_TYPE"

        fun createFragment(type: Int) =
            CollectionSubjectFragment().also { f ->
                f.arguments = Bundle().also { it.putInt(KEY_SUBJECT_TYPE, type) }
            }
    }
}