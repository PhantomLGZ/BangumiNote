package com.phantom.banguminote.search.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseDialogFragment
import com.phantom.banguminote.databinding.DialogSearchFilterRankBinding

class SearchRankDialogFragment(submit: (above: Int?, below: Int?) -> Unit) :
    BaseDialogFragment<DialogSearchFilterRankBinding>() {

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogSearchFilterRankBinding =
        DialogSearchFilterRankBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val above = arguments?.getInt(KEY_ABOVE)?.takeIf { it != -1 }
        val below = arguments?.getInt(KEY_BELOW)?.takeIf { it != -1 }

        binding?.also { b ->
            b.etAbove.addTextChangedListener(aboveWatcher)
            b.etBelow.addTextChangedListener(belowWatcher)
            b.btCancel.setOnClickListener(onClickListener)
            b.btSubmit.setOnClickListener(onClickListener)

            b.etAbove.setText(above?.toString() ?: "", TextView.BufferType.EDITABLE)
            b.tvAbove.text = getString(R.string.search_rank_above, above?.toString() ?: "-")
            b.etBelow.setText(below?.toString() ?: "", TextView.BufferType.EDITABLE)
            b.tvBelow.text = getString(R.string.search_rank_below, below?.toString() ?: "-")
        }
    }

    private val aboveWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding?.tvAbove?.text = getString(R.string.search_rank_above, s?.toString() ?: "-")
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private val belowWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding?.tvBelow?.text = getString(R.string.search_rank_below, s?.toString() ?: "-")
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private val onClickListener = View.OnClickListener { v ->
        when (v) {
            binding?.btSubmit -> {
                val above = binding?.etAbove?.text?.toString()?.toIntOrNull()
                val below = binding?.etBelow?.text?.toString()?.toIntOrNull()
                if (above != null && below != null && above <= below) {
                    showToast(getString(R.string.search_error))
                } else {
                    submit(above, below)
                    dismiss()
                }
            }

            binding?.btCancel -> {
                dismiss()
            }
        }
    }

    companion object {

        const val KEY_ABOVE = "KEY_ABOVE"
        const val KEY_BELOW = "KEY_BELOW"

        fun createBundle(above: Int?, below: Int?): Bundle =
            Bundle().also {
                it.putInt(KEY_ABOVE, above ?: -1)
                it.putInt(KEY_BELOW, below ?: -1)
            }
    }

}