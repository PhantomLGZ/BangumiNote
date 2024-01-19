package com.phantom.banguminote.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.databinding.FragmentHomeBinding

class HomeFragment: BaseFragment<FragmentHomeBinding>() {
    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding =
        FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            cardView.setOnClickListener {
            }
        }
    }
}