package com.phantom.banguminote.detail.character.summary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.base.http.setDataOrObserve
import com.phantom.banguminote.detail.character.CharacterViewModel
import com.phantom.banguminote.detail.character.data.CharacterData
import com.phantom.banguminote.databinding.FragmentCharacterSummaryBinding
import com.phantom.banguminote.databinding.LayoutDetailSummaryBinding

class CharacterSummaryFragment : BaseFragment<FragmentCharacterSummaryBinding>() {

    private val viewModel: CharacterViewModel by viewModels({ requireParentFragment() })
    private var mergeBinding: LayoutDetailSummaryBinding? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCharacterSummaryBinding =
        FragmentCharacterSummaryBinding.inflate(inflater, container, false)

    override fun init() {
        viewModel.characterRes.setDataOrObserve(viewLifecycleOwner) {
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