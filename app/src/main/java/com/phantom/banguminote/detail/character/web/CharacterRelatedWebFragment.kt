package com.phantom.banguminote.detail.character.web

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.databinding.FragmentCharacterRelatedWebBinding
import com.phantom.banguminote.detail.character.CharacterViewModel
import com.phantom.banguminote.detail.web.RelatedWebViewActivity
import com.phantom.banguminote.detail.web.getBangumiCharacterUrl
import com.phantom.banguminote.detail.web.getPixivUrl

class CharacterRelatedWebFragment : BaseFragment<FragmentCharacterRelatedWebBinding>() {

    private val viewModel: CharacterViewModel by viewModels({ requireParentFragment() })

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCharacterRelatedWebBinding =
        FragmentCharacterRelatedWebBinding.inflate(inflater, container, false)

    override fun init() {
        binding?.btBangumi?.setOnClickListener(onClickListener)
        binding?.btPixiv?.setOnClickListener(onClickListener)
    }


    private val onClickListener = View.OnClickListener {
        when (it) {
            binding?.btBangumi -> {
                findNavController().navigate(
                    R.id.action_nav_to_activity_related_web,
                    RelatedWebViewActivity.createBundle(viewModel.id.value?.getBangumiCharacterUrl())
                )
            }

            binding?.btPixiv -> {
                findNavController().navigate(
                    R.id.action_nav_to_activity_related_web,
                    RelatedWebViewActivity.createBundle(viewModel.characterRes.value?.name?.getPixivUrl())
                )
            }
        }
    }
}