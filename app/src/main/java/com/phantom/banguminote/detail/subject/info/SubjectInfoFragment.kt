package com.phantom.banguminote.detail.subject.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter4.BaseQuickAdapter
import com.phantom.banguminote.detail.InfoboxAdapter
import com.phantom.banguminote.R
import com.phantom.banguminote.base.TransparentDividerItemDecoration
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.base.http.setDataOrObserve
import com.phantom.banguminote.data.InfoData
import com.phantom.banguminote.data.InfoDataType
import com.phantom.banguminote.data.InfoValueData
import com.phantom.banguminote.databinding.FragmentSubjectInfoBinding
import com.phantom.banguminote.detail.subject.SubjectViewModel
import com.phantom.banguminote.detail.subject.data.SubjectPersonData

class SubjectInfoFragment : BaseFragment<FragmentSubjectInfoBinding>() {

    private var viewModel: SubjectViewModel? = null
    private val adapter = InfoboxAdapter<SubjectPersonData>()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubjectInfoBinding =
        FragmentSubjectInfoBinding.inflate(inflater, container, false)

    override fun init() {
        viewModel = parentFragment?.let { ViewModelProvider(it)[SubjectViewModel::class.java] }
        viewModel?.subjectRes?.setDataOrObserve(viewLifecycleOwner) {
            it.infobox?.also { it1 -> setData(it1) }
        }
        binding?.recyclerView?.also { rv ->
            rv.addItemDecoration(TransparentDividerItemDecoration(requireContext()))
            rv.layoutManager = LinearLayoutManager(context)
            rv.adapter = adapter
        }
    }

    private fun setData(data: List<InfoData>) {
        data.forEach { item ->
            item.actualValue = InfoData.valueGson.fromJson(
                item.value.toString(),
                InfoValueData::class.java
            )
        }
        val list = mutableListOf(
            InfoData(
                key = getString(R.string.info_ori_name),
                value = null,
                actualValue = InfoValueData(
                    value = viewModel?.subjectRes?.value?.name,
                    values = null,
                    type = InfoDataType.SINGLE
                )
            )
        ).also { it.addAll(data) }
            .map {
                InfoboxAdapter.InfoboxData<SubjectPersonData>(it, null)
            }
        viewModel?.subjectPersonRes?.setDataOrObserve(viewLifecycleOwner) { persons ->
            persons.sortedBy { it.relation }
                .groupBy { it.relation }
                .forEach { map ->
                    list.findLast { it.infoData.key == map.key }
                        ?.persons = map.value
                }
        }
        adapter.addOnItemChildClickListener(R.id.btMore, onItemChildClickListener)
        adapter.submitList(list)
    }

    private val onItemChildClickListener =
        BaseQuickAdapter.OnItemChildClickListener<InfoboxAdapter.InfoboxData<SubjectPersonData>> { adapter, view, position ->
            PersonDialogFragment().also { dialog ->
                dialog.arguments = Bundle().also { args ->
                    args.putParcelableArray(
                        PersonDialogFragment.KEY_PERSONS,
                        adapter.getItem(position)?.persons?.map {
                            PersonDialogFragment.DialogPersonData(
                                it.id ?: 0,
                                it.name ?: "",
                                it.images?.medium ?: ""
                            )
                        }?.toTypedArray()
                    )
                }
            }.show(childFragmentManager, "PersonDialogFragment")
        }

}