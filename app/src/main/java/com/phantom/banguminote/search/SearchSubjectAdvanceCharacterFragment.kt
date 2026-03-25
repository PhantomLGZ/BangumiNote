package com.phantom.banguminote.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.base.getUserToken
import com.phantom.banguminote.databinding.FragmentSearchAdvanceCharacterBinding
import kotlin.getValue

class SearchSubjectAdvanceCharacterFragment :
    BaseFragment<FragmentSearchAdvanceCharacterBinding>() {

    private val viewModel: SearchViewModel by activityViewModels()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchAdvanceCharacterBinding =
        FragmentSearchAdvanceCharacterBinding.inflate(inflater, container, false)

    override fun init() {
        binding?.also { b ->
            b.tbNSFW.setOnCheckedChangeListener { buttonView, isChecked ->
                viewModel.searchCharacterReq.filter.nsfw = isChecked
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.tbNSFW?.isEnabled = !context?.getUserToken().isNullOrBlank()
        syncFilterView()
    }

    private fun syncFilterView() {
        viewModel.searchCharacterReq.also { searchReq ->
            binding?.tbNSFW?.isChecked = searchReq.filter.nsfw
        }
    }
}