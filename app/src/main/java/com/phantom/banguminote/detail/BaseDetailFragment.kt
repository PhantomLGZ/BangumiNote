package com.phantom.banguminote.detail

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.base.BaseViewPagerAdapter
import com.phantom.banguminote.base.http.BaseViewModel
import com.phantom.banguminote.data.HttpErrorData
import com.phantom.banguminote.databinding.LayoutDetailBinding

abstract class BaseDetailFragment<VM : BaseViewModel, T : ViewBinding> : BaseFragment<T>() {

    var viewModel: VM? = null
    var viewPageAdapter: BaseViewPagerAdapter? = null
    var mergeBinding: LayoutDetailBinding? = null

    abstract fun assignViewModel(): VM

    abstract fun assignFragments(): List<BaseViewPagerAdapter.FragmentData>

    override fun init() {
        viewModel = assignViewModel()
        viewPageAdapter = BaseViewPagerAdapter(childFragmentManager, lifecycle).also {
            it.setFragments(assignFragments())
        }
        mergeBinding = binding?.root?.let { LayoutDetailBinding.bind(it) }
        mergeBinding?.also {
            it.viewPage.adapter = viewPageAdapter
            TabLayoutMediator(it.tabLayout, it.viewPage) { tab, position ->
                tab.text = viewPageAdapter?.getTitle(position)
            }.attach()
        }
        viewModel?.also { v ->
            v.error.observe(viewLifecycleOwner) { showToast(it.message) }
            v.httpError.observe(viewLifecycleOwner) { httpError(it) }
        }
    }

    private fun httpError(data: HttpErrorData) {
        mergeBinding?.also {
            it.toolbar.title = "Error"
            it.appBarLayout.setExpanded(false)
            it.tabLayout.visibility = View.GONE
        }
        viewPageAdapter?.setFragments(
            mutableListOf(BaseViewPagerAdapter.FragmentData(fragment = ErrorFragment().also {
                it.arguments = Bundle().apply {
                    putParcelable(ErrorFragment.KEY_HTTP_ERROR_DATA, data)
                }
            }))
        )
    }

}