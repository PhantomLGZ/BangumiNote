package com.phantom.banguminote.detail.subject.info

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter4.BaseQuickAdapter
import com.phantom.banguminote.R
import com.phantom.banguminote.TransparentDividerItemDecoration
import com.phantom.banguminote.base.BaseDialogFragment
import com.phantom.banguminote.databinding.DialogPersonListBinding
import com.phantom.banguminote.detail.person.PersonFragment
import kotlinx.parcelize.Parcelize

class PersonDialogFragment : BaseDialogFragment<DialogPersonListBinding>() {

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogPersonListBinding =
        DialogPersonListBinding.inflate(inflater, container, false)

    @Parcelize
    data class DialogPersonData(
        val id: Int,
        val name: String,
        val image: String
    ) : Parcelable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val data = arguments?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelableArray(KEY_PERSONS, DialogPersonData::class.java)?.toList()
            } else {
                it.getParcelableArray(KEY_PERSONS)?.map { p ->
                    p as DialogPersonData
                }
            }
        }
        binding?.recyclerView?.also { r ->
            r.layoutManager = GridLayoutManager(context, 3)
            r.adapter = PersonDialogAdapter().also {
                it.setOnItemClickListener(onItemClickListener)
                it.submitList(data)
            }
            r.addItemDecoration(TransparentDividerItemDecoration.vertical(requireContext()))
            r.addItemDecoration(TransparentDividerItemDecoration.horizontal(requireContext()))
        }
    }

    private val onItemClickListener =
        BaseQuickAdapter.OnItemClickListener<DialogPersonData> { adapter, view, position ->
            findNavController().navigate(
                R.id.action_nav_to_activity_person,
                PersonFragment.createBundle(adapter.items[position].id)
            )
            dismiss()
        }

    companion object {
        const val KEY_PERSONS = "KEY_PERSONS"
    }

}