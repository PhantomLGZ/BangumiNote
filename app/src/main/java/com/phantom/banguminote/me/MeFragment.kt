package com.phantom.banguminote.me

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.base.LoadingDialogFragment
import com.phantom.banguminote.base.dpToPx
import com.phantom.banguminote.base.getUserName
import com.phantom.banguminote.base.getUserToken
import com.phantom.banguminote.base.setUserName
import com.phantom.banguminote.databinding.FragmentMeBinding
import com.phantom.banguminote.getSubjectType
import com.phantom.banguminote.me.collection.CollectionViewPagerAdapter
import com.phantom.banguminote.me.data.UserData
import kotlin.math.roundToInt

class MeFragment : BaseFragment<FragmentMeBinding>() {

    private val viewModel: MeViewModel by viewModels()
    private var adapter: CollectionViewPagerAdapter? = null
    private var dialog: LoadingDialogFragment? = null
    private var nowToken = ""

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMeBinding =
        FragmentMeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = CollectionViewPagerAdapter(childFragmentManager, lifecycle)
        viewModel.run {
            error.observe(viewLifecycleOwner) {
                dialog?.dismiss()
                dialog = null
                showToast(it.toString())
            }
            userRes.observe(viewLifecycleOwner) { setUserData(it) }
        }
        binding?.also { b ->
            Glide.with(this@MeFragment)
                .load(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_acatar_null))
                .apply(
                    RequestOptions.bitmapTransform(RoundedCorners(8f.dpToPx(context).roundToInt()))
                )
                .into(b.ivAvatar)
            b.layoutMe.setOnClickListener(onClickListener)
            b.viewPage.adapter = adapter
            b.viewPage.isUserInputEnabled = false
            TabLayoutMediator(b.tabLayout, b.viewPage) { tab, position ->
                tab.text = adapter?.getSubjectType(position)?.let {
                    requireContext().getSubjectType(it)
                }
            }.attach()
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.also {
            if ((!context?.getUserToken().isNullOrBlank()
                        && binding?.tvName?.text.isNullOrBlank())
                || context?.getUserToken() != nowToken
            ) {
                viewModel.me()
                dialog = LoadingDialogFragment()
                dialog?.show(childFragmentManager, "")
            } else if (context?.getUserName().isNullOrBlank()) {
                binding?.ivAvatar?.let { iv ->
                    Glide.with(this)
                        .load(
                            AppCompatResources.getDrawable(
                                requireContext(),
                                R.drawable.ic_acatar_null
                            )
                        )
                        .into(iv)
                }
                binding?.tvName?.text = ""
                binding?.tvId?.text = ""
            }
        }
    }

    private fun setUserData(data: UserData) {
        nowToken = context?.getUserToken() ?: ""
        binding?.ivAvatar?.let { iv ->
            Glide.with(this)
                .load(data.avatar?.medium)
                .apply(
                    RequestOptions.bitmapTransform(
                        RoundedCorners(8f.dpToPx(context).roundToInt())
                    )
                )
                .into(iv)
        }
        binding?.tvName?.text = data.nickname
        binding?.tvId?.text = data.username?.takeIf { it.isNotBlank() } ?: data.id?.toString()
        context?.setUserName(binding?.tvId?.text?.toString() ?: "")
        dialog?.dismiss()
        dialog = null
    }

    private val onClickListener = OnClickListener {
        when (it.id) {
            R.id.layoutMe -> {
//                if (binding?.tvName?.text.isNullOrBlank()) {
                findNavController().navigate(R.id.action_nav_to_activity_login)
//                }
            }
        }
    }
}