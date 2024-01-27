package com.phantom.banguminote.detail.person.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.phantom.banguminote.R
import com.phantom.banguminote.TransparentDividerItemDecoration
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.base.http.setDataOrObserve
import com.phantom.banguminote.data.InfoData
import com.phantom.banguminote.data.InfoDataType
import com.phantom.banguminote.data.InfoValueData
import com.phantom.banguminote.databinding.FragmentPersonInfoBinding
import com.phantom.banguminote.detail.InfoboxAdapter
import com.phantom.banguminote.detail.person.PersonViewModel

class PersonInfoFragment : BaseFragment<FragmentPersonInfoBinding>() {

    private var viewModel: PersonViewModel? = null
    private val adapter = InfoboxAdapter<Nothing>()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonInfoBinding =
        FragmentPersonInfoBinding.inflate(inflater, container, false)

    override fun init() {
        viewModel = parentFragment?.let {
            ViewModelProvider(it)[PersonViewModel::class.java]
        }
        viewModel?.personRes?.setDataOrObserve(viewLifecycleOwner) {
            it.infobox?.let { it1 -> setData(it1) }
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
                    value = viewModel?.personRes?.value?.name,
                    values = null,
                    type = InfoDataType.SINGLE
                )
            )
        )
        list.addAll(data)
        adapter.submitList(list.map { InfoboxAdapter.InfoboxData(it) })
    }

}