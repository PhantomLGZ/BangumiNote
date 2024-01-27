package com.phantom.banguminote.detail.person.summary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.base.http.setDataOrObserve
import com.phantom.banguminote.databinding.FragmentPersonSummaryBinding
import com.phantom.banguminote.databinding.LayoutDetailSummaryBinding
import com.phantom.banguminote.detail.person.PersonViewModel
import com.phantom.banguminote.detail.person.data.PersonData

class PersonSummaryFragment : BaseFragment<FragmentPersonSummaryBinding>() {

    private var viewModel: PersonViewModel? = null
    private var mergeBinding: LayoutDetailSummaryBinding? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonSummaryBinding =
        FragmentPersonSummaryBinding.inflate(inflater, container, false)

    override fun init() {
        viewModel = parentFragment?.let { ViewModelProvider(it)[PersonViewModel::class.java] }
        viewModel?.personRes?.setDataOrObserve(viewLifecycleOwner) {
            setData(it)
        }
        mergeBinding = binding?.root?.let { LayoutDetailSummaryBinding.bind(it) }
    }

    private fun setData(data: PersonData) {
        mergeBinding?.also { b ->
            b.tvSummary.text = data.summary
        }
    }
}