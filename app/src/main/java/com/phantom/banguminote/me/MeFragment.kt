package com.phantom.banguminote.me

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.databinding.FragmentMeBinding

class MeFragment : BaseFragment<FragmentMeBinding>() {

    private var viewModel: MeViewModel? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMeBinding =
        FragmentMeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MeViewModel::class.java]
        viewModel?.run {
            error.observe(viewLifecycleOwner) { showToast(it.toString()) }
            authorizeRes.observe(viewLifecycleOwner) {}
        }
        binding?.run {
            btTest.setOnClickListener(onClickListener)
        }
    }

    private val onClickListener = OnClickListener {
        when (it.id) {
            R.id.btTest -> {
//                viewModel?.authorize(AuthorizationReq())
            }
        }
    }
}