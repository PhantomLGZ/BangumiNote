package com.phantom.banguminote

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.phantom.banguminote.base.BaseDialogFragment
import com.phantom.banguminote.base.checkHttps
import com.phantom.banguminote.databinding.DialogImageBinding

class ImageDialogFragment : BaseDialogFragment<DialogImageBinding>() {

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogImageBinding =
        DialogImageBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        arguments?.getString(KEY_IMAGE_URL)?.also {
            binding?.ivImage?.also { iv ->
                Glide.with(view)
                    .load(it.checkHttps())
                    .into(iv)
            }
        }
        binding?.ivImage?.setOnClickListener(onClickListener)
    }

    private val onClickListener = OnClickListener {
        dismiss()
    }

    companion object {
        const val KEY_IMAGE_URL = "KEY_IMAGE_URL"
    }
}