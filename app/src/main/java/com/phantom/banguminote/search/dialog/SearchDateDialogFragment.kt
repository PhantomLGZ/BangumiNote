package com.phantom.banguminote.search.dialog

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseDialogFragment
import com.phantom.banguminote.base.toFormattedString
import com.phantom.banguminote.base.toLocalDate
import com.phantom.banguminote.databinding.DialogSearchFilterDateBinding
import java.time.LocalDate

class SearchDateDialogFragment(submit: (after: String?, before: String?) -> Unit) :
    BaseDialogFragment<DialogSearchFilterDateBinding>() {

    private var after: LocalDate? = null
    private var before: LocalDate? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogSearchFilterDateBinding =
        DialogSearchFilterDateBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        after = arguments?.getString(KEY_AFTER)?.toLocalDate()
        before = arguments?.getString(KEY_BEFORE)?.toLocalDate()

        setAfter()
        setBefore()

        binding?.btAfter?.setOnClickListener(onClickListener)
        binding?.btBefore?.setOnClickListener(onClickListener)
        binding?.btCancel?.setOnClickListener(onClickListener)
        binding?.btSubmit?.setOnClickListener(onClickListener)
    }

    private fun setAfter() {
        binding?.tvAfter?.text =
            getString(R.string.search_date_after, after?.toFormattedString() ?: "-")
        binding?.btAfter?.text = getString(
            if (after == null) {
                R.string.search_date_set
            } else {
                R.string.search_date_remove
            }
        )
    }

    private fun setBefore() {
        binding?.tvBefore?.text =
            getString(R.string.search_date_before, before?.toFormattedString() ?: "-")
        binding?.btBefore?.text = getString(
            if (before == null) {
                R.string.search_date_set
            } else {
                R.string.search_date_remove
            }
        )
    }

    private val onClickListener = View.OnClickListener { v ->
        when (v) {
            binding?.btAfter -> {
                if (after != null) {
                    after = null
                    setAfter()
                } else {
                    DatePickerDialog(requireContext()).also {
                        it.setOnDateSetListener { view, year, month, dayOfMonth ->
                            after = LocalDate.of(year, month + 1, dayOfMonth)
                            setAfter()
                        }
                    }.show()
                }
            }

            binding?.btBefore -> {
                if (before != null) {
                    before = null
                    setBefore()
                } else {
                    DatePickerDialog(requireContext()).also {
                        it.setOnDateSetListener { view, year, month, dayOfMonth ->
                            before = LocalDate.of(year, month + 1, dayOfMonth)
                            setBefore()
                        }
                    }.show()
                }
            }

            binding?.btCancel -> {
                dismiss()
            }

            binding?.btSubmit -> {
                val b = before
                val a = after
                if (b != null && a!= null && !b.isAfter(a)) {
                    showToast(getString(R.string.search_error))
                } else {
                    submit(after?.toFormattedString(), before?.toFormattedString())
                    dismiss()
                }
            }
        }
    }

    companion object {
        const val KEY_AFTER = "KEY_AFTER"
        const val KEY_BEFORE = "KEY_BEFORE"

        fun createBundle(after: String?, before: String?): Bundle =
            Bundle().also {
                it.putString(KEY_AFTER, after)
                it.putString(KEY_BEFORE, before)
            }
    }
}