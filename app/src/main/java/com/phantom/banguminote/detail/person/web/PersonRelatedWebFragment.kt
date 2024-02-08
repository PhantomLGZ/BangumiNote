package com.phantom.banguminote.detail.person.web

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.databinding.FragmentPersonRelatedWebBinding
import com.phantom.banguminote.detail.person.PersonViewModel
import com.phantom.banguminote.detail.web.RelatedWebViewActivity
import com.phantom.banguminote.detail.web.getBangumiPersonUrl

class PersonRelatedWebFragment : BaseFragment<FragmentPersonRelatedWebBinding>() {

    private val viewModel: PersonViewModel by viewModels({ requireParentFragment() })

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonRelatedWebBinding =
        FragmentPersonRelatedWebBinding.inflate(inflater, container, false)

    override fun init() {
        binding?.btBangumi?.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener {
        when (it) {
            binding?.btBangumi -> {
                findNavController().navigate(
                    R.id.action_nav_to_activity_related_web,
                    RelatedWebViewActivity.createBundle(viewModel.id.value?.getBangumiPersonUrl())
                )
            }
        }
    }
}