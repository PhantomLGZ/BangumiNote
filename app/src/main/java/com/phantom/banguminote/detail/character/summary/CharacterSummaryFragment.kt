package com.phantom.banguminote.detail.character.summary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.base.http.setDataOrObserve
import com.phantom.banguminote.detail.character.CharacterViewModel
import com.phantom.banguminote.detail.character.data.CharacterData
import com.phantom.banguminote.databinding.FragmentCharacterSummaryBinding
import com.phantom.banguminote.databinding.LayoutDetailSummaryBinding

class CharacterSummaryFragment : BaseFragment<FragmentCharacterSummaryBinding>() {

    private var viewModel: CharacterViewModel? = null
    private var mergeBinding: LayoutDetailSummaryBinding? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCharacterSummaryBinding =
        FragmentCharacterSummaryBinding.inflate(inflater, container, false)

    override fun init() {
        viewModel = parentFragment?.let { ViewModelProvider(it)[CharacterViewModel::class.java] }
        viewModel?.characterRes?.setDataOrObserve(viewLifecycleOwner) {
            setData(it)
        }
        mergeBinding = binding?.root?.let { LayoutDetailSummaryBinding.bind(it) }
    }

    private fun setData(data: CharacterData) {
        mergeBinding?.also { b ->
            b.tvSummary.text = data.summary
        }
    }

}