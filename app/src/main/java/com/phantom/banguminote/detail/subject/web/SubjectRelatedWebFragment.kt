package com.phantom.banguminote.detail.subject.web

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.databinding.FragmentSubjectRelatedWebBinding
import com.phantom.banguminote.detail.subject.SubjectViewModel
import com.phantom.banguminote.detail.web.RelatedWebViewActivity
import com.phantom.banguminote.detail.web.getBangumiSubjectUrl
import com.phantom.banguminote.detail.web.getMikanUrl
import com.phantom.banguminote.detail.web.getPixivUrl

class SubjectRelatedWebFragment : BaseFragment<FragmentSubjectRelatedWebBinding>() {

    private val viewModel: SubjectViewModel by viewModels({ requireParentFragment() })

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubjectRelatedWebBinding =
        FragmentSubjectRelatedWebBinding.inflate(inflater, container, false)

    override fun init() {
        binding?.btBangumi?.setOnClickListener(onClickListener)
        binding?.btPixiv?.setOnClickListener(onClickListener)
        binding?.btMikan?.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view) {
            binding?.btBangumi -> {
                findNavController().navigate(
                    R.id.action_nav_to_activity_related_web,
                    RelatedWebViewActivity.createBundle(viewModel.id.value?.getBangumiSubjectUrl())
                )
            }

            binding?.btPixiv -> {
                findNavController().navigate(
                    R.id.action_nav_to_activity_related_web,
                    RelatedWebViewActivity.createBundle(viewModel.subjectRes.value?.name?.getPixivUrl())
                )
            }

            binding?.btMikan -> {
                findNavController().navigate(
                    R.id.action_nav_to_activity_related_web,
                    RelatedWebViewActivity.createBundle(viewModel.subjectRes.value?.let { data ->
                        data.name_cn?.takeIf { it.isNotBlank() } ?: data.name
                    }?.getMikanUrl())
                )
            }
        }
    }

}