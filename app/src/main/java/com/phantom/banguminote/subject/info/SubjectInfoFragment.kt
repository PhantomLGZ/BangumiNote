package com.phantom.banguminote.subject.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.phantom.banguminote.TransparentDividerItemDecoration
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.data.InfoData
import com.phantom.banguminote.data.InfoDataType
import com.phantom.banguminote.data.InfoValueData
import com.phantom.banguminote.databinding.FragmentSubjectInfoBinding
import com.phantom.banguminote.subject.SubjectViewModel

class SubjectInfoFragment : BaseFragment<FragmentSubjectInfoBinding>() {

    private var viewModel: SubjectViewModel? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubjectInfoBinding =
        FragmentSubjectInfoBinding.inflate(inflater, container, false)

    override fun init() {
        viewModel = activity?.let { ViewModelProvider(it)[SubjectViewModel::class.java] }
        viewModel?.subjectRes?.value?.infobox?.also {
            binding?.recyclerView?.also { rv ->
                rv.addItemDecoration(TransparentDividerItemDecoration(requireContext()))
                rv.layoutManager = LinearLayoutManager(context)
                rv.adapter = SubjectInfoAdapter().also { a ->
                    it.forEach { item ->
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
                            )
                        )
                    )
                    list.addAll(it)
                    a.submitList(list)
                }
            }
        }
    }
}