package com.phantom.banguminote.subject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.data.InfoData
import com.phantom.banguminote.data.InfoDataType
import com.phantom.banguminote.data.InfoValueData
import com.phantom.banguminote.databinding.FragmentInfoListBinding

class SubjectInfoFragment : BaseFragment<FragmentInfoListBinding>() {

    private var viewModel: SubjectViewModel? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentInfoListBinding =
        FragmentInfoListBinding.inflate(inflater, container, false)

    override fun init() {
        viewModel = activity?.let { ViewModelProvider(it)[SubjectViewModel::class.java] }
        viewModel?.subjectRes?.value?.infobox?.also {
            binding?.recyclerView?.also { rv ->
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