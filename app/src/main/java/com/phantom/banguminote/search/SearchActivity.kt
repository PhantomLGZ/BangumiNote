package com.phantom.banguminote.search

import android.os.Bundle
import androidx.activity.viewModels
import com.phantom.banguminote.base.BaseActivity
import com.phantom.banguminote.databinding.ActivitySearchBinding

class SearchActivity : BaseActivity<ActivitySearchBinding>() {

    private val viewModel: SearchViewModel by viewModels()

    override fun inflateViewBinding(): ActivitySearchBinding =
        ActivitySearchBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.getString(KEY_TAG)?.also {
            viewModel.searchReq.value = SearchReq(filter = SearchFilter(tag = mutableListOf(it)))
            println("TEST ${viewModel.searchReq.value}")
        }
    }

    companion object {
        const val KEY_TAG = "KEY_TAG"

        fun createBundleWithTag(tag: String): Bundle =
            Bundle().also {
                it.putString(KEY_TAG, tag)
            }

    }
}