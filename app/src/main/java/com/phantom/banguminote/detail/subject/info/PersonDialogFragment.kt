package com.phantom.banguminote.detail.subject.info

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.phantom.banguminote.R
import com.phantom.banguminote.TransparentDividerItemDecoration
import com.phantom.banguminote.detail.person.PersonFragment
import kotlinx.parcelize.Parcelize

class PersonDialogFragment : DialogFragment(R.layout.dialog_person_list) {

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
        view.findViewById<RecyclerView>(R.id.recyclerView).also { r ->
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