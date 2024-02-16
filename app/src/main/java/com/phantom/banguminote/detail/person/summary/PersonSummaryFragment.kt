package com.phantom.banguminote.detail.person.summary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.databinding.FragmentPersonSummaryBinding
import com.phantom.banguminote.databinding.LayoutDetailSummaryBinding
import com.phantom.banguminote.detail.person.PersonViewModel
import com.phantom.banguminote.detail.person.data.PersonData

class PersonSummaryFragment : BaseFragment<FragmentPersonSummaryBinding>() {

    private val viewModel: PersonViewModel by viewModels({ requireParentFragment() })
    private var mergeBinding: LayoutDetailSummaryBinding? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonSummaryBinding =
        FragmentPersonSummaryBinding.inflate(inflater, container, false)

    override fun init() {
        viewModel.personRes.observe(viewLifecycleOwner) { setData(it) }
        mergeBinding = binding?.root?.let { LayoutDetailSummaryBinding.bind(it) }
    }

    private fun setData(data: PersonData) {
        mergeBinding?.also { b ->
            b.tvSummary.text = data.summary
        }
    }
}