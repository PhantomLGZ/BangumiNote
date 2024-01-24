package com.phantom.banguminote.subject.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter4.BaseQuickAdapter
import com.phantom.banguminote.R
import com.phantom.banguminote.TransparentDividerItemDecoration
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.base.http.setDataOrObserve
import com.phantom.banguminote.data.InfoData
import com.phantom.banguminote.data.InfoDataType
import com.phantom.banguminote.data.InfoValueData
import com.phantom.banguminote.databinding.FragmentSubjectInfoBinding
import com.phantom.banguminote.subject.SubjectViewModel

class SubjectInfoFragment : BaseFragment<FragmentSubjectInfoBinding>() {

    private var viewModel: SubjectViewModel? = null
    private val adapter = SubjectInfoAdapter()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubjectInfoBinding =
        FragmentSubjectInfoBinding.inflate(inflater, container, false)

    override fun init() {
        viewModel = activity?.let { ViewModelProvider(it)[SubjectViewModel::class.java] }
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
        viewModel?.subjectPersonRes?.setDataOrObserve(viewLifecycleOwner) { list ->
            list.sortedBy { it.relation }
                .groupBy { it.relation }
                .forEach { map ->
                    data.findLast { it.key == map.key }
                        ?.persons = map.value
                }
        }
        data.forEach { item ->
            item.actualValue = InfoData.valueGson.fromJson(
                item.value.toString(),
                InfoValueData::class.java
            )
        }
        val list = mutableListOf(
            InfoData(
                key = "原名",
                value = null,
                actualValue = InfoValueData(
                    value = viewModel?.subjectRes?.value?.name,
                    values = null,
                    type = InfoDataType.SINGLE
                ),
                persons = null
            )
        )
        list.addAll(data)
        adapter.addOnItemChildClickListener(R.id.btMore, onItemChildClickListener)
        adapter.submitList(list)
    }

    private val onItemChildClickListener =
        BaseQuickAdapter.OnItemChildClickListener<InfoData> { adapter, view, position ->
            // TODO
            println("TEST ${adapter.getItem(position)?.persons}")
        }

}