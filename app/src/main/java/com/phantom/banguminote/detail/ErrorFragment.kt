package com.phantom.banguminote.detail

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.data.HttpErrorData
import com.phantom.banguminote.databinding.FragmentErrorBinding

class ErrorFragment : BaseFragment<FragmentErrorBinding>() {

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentErrorBinding =
        FragmentErrorBinding.inflate(inflater, container, false)

    override fun init() {
        val httpErrorData = arguments?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(KEY_HTTP_ERROR_DATA, HttpErrorData::class.java)
            } else {
                it.getParcelable(KEY_HTTP_ERROR_DATA)
            }
        }
        if (httpErrorData != null) {
            binding?.also {
                it.ivImage.setImageResource(
                    when {
                        else -> R.drawable.error_404
                    }
                )
                it.tvDescription.text = httpErrorData.description
            }
        }
    }

    companion object {
        const val KEY_HTTP_ERROR_DATA = "KEY_HTTP_ERROR_DATA"
    }
}