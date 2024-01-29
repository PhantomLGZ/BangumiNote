package com.phantom.banguminote.search.dialog

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseDialogFragment
import com.phantom.banguminote.databinding.DialogSearchFilterScoreBinding

class SearchScoreDialogFragment(submit: (greater: Float, less: Float) -> Unit) :
    BaseDialogFragment<DialogSearchFilterScoreBinding>() {

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogSearchFilterScoreBinding =
        DialogSearchFilterScoreBinding.inflate(inflater, container, false)

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val greater = arguments?.getFloat(KEY_GREATER) ?: 0f
        val less = arguments?.getFloat(KEY_LESS) ?: 10f

        binding?.also { b ->
            b.rbGreater.setOnTouchListener { v, event ->
                binding?.tvGreater?.text =
                    getString(R.string.search_score_greater, b.rbGreater.rating)
                b.rbGreater.onTouchEvent(event)
            }
            b.rbLess.setOnTouchListener { v, event ->
                binding?.tvLess?.text = getString(R.string.search_score_less, b.rbLess.rating)
                b.rbLess.onTouchEvent(event)
            }
            b.rbGreater.rating = greater
            binding?.tvGreater?.text = getString(R.string.search_score_greater, greater)
            b.rbLess.rating = less
            binding?.tvLess?.text = getString(R.string.search_score_less, less)
            b.btCancel.setOnClickListener(onClickListener)
            b.btSubmit.setOnClickListener(onClickListener)
        }
    }

    private val onClickListener = View.OnClickListener { v ->
        when (v) {
            binding?.btSubmit -> {
                val greater = binding?.rbGreater?.rating ?: 0f
                val less = binding?.rbLess?.rating ?: 10f
                if (greater < less) {
                    submit(greater, less)
                    dismiss()
                } else {
                    showToast(getString(R.string.search_error))
                }
            }

            binding?.btCancel -> {
                dismiss()
            }
        }
    }

    companion object {
        const val KEY_GREATER = "KEY_GREATER"
        const val KEY_LESS = "KEY_LESS"

        fun createBundle(greater: Float?, less: Float?): Bundle =
            Bundle().also {
                it.putFloat(KEY_GREATER, greater ?: 0f)
                it.putFloat(KEY_LESS, less ?: 10f)
            }
    }

}