package com.phantom.banguminote.detail.person.character

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter4.BaseQuickAdapter
import com.phantom.banguminote.R
import com.phantom.banguminote.TransparentDividerItemDecoration
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.base.http.setDataOrObserve
import com.phantom.banguminote.databinding.FragmentPersonCharacterBinding
import com.phantom.banguminote.detail.character.CharacterFragment
import com.phantom.banguminote.detail.person.PersonViewModel
import com.phantom.banguminote.detail.person.data.PersonCharacterData

class PersonCharacterFragment : BaseFragment<FragmentPersonCharacterBinding>() {

    private var viewModel: PersonViewModel? = null
    private val adapter = PersonCharacterAdapter()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonCharacterBinding =
        FragmentPersonCharacterBinding.inflate(inflater, container, false)

    override fun init() {
        viewModel = parentFragment?.let { ViewModelProvider(it)[PersonViewModel::class.java] }
        adapter.setOnItemClickListener(onItemClickListener)
        binding?.recyclerView?.also {
            it.adapter = adapter
            it.layoutManager = GridLayoutManager(context, 2)
            it.addItemDecoration(TransparentDividerItemDecoration.vertical(requireContext()))
            it.addItemDecoration(TransparentDividerItemDecoration.horizontal(requireContext()))
        }
        viewModel?.personCharacterRes?.setDataOrObserve(viewLifecycleOwner) {
            setData(it)
        }
    }

    private fun setData(data: List<PersonCharacterData>) {
        adapter.submitList(data)
    }

    private val onItemClickListener =
        BaseQuickAdapter.OnItemClickListener<PersonCharacterData> { adapter, view, position ->
            findNavController().navigate(
                R.id.action_nav_to_activity_character,
                CharacterFragment.createBundle(adapter.items[position].id)
            )
        }
}