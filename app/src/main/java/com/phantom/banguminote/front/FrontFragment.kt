package com.phantom.banguminote.front

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.databinding.FragmentFrontBinding

class FrontFragment : BaseFragment<FragmentFrontBinding>() {

    private var viewModel: FrontViewModel? = null
    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFrontBinding =
        FragmentFrontBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[FrontViewModel::class.java]
        viewModel?.apply {
            error.observe(viewLifecycleOwner) { showToast(it.toString()) }
        }
        binding?.apply {

//            btTest.setOnClickListener(onClickListener)
        }
    }

//    private val onClickListener = OnClickListener {
//        when (it.id) {
//            R.id.btTest -> {
//                viewModel?.calendar()
//            }
//        }
//    }
}