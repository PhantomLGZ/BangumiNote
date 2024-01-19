package com.phantom.banguminote

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.phantom.banguminote.base.checkHttps

class ImageDialogFragment : DialogFragment(R.layout.dialog_image) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.setOnClickListener(onClickListener)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        arguments?.getString(KEY_IMAGE_URL)?.also {
            Glide.with(view)
                .load(it.checkHttps())
                .into(view.findViewById(R.id.ivImage))
        }
    }

    private val onClickListener = OnClickListener {
        dismiss()
    }

    companion object {
        const val KEY_IMAGE_URL = "KEY_IMAGE_URL"
    }
}