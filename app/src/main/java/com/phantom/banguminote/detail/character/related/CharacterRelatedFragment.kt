package com.phantom.banguminote.detail.character.related

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
import com.phantom.banguminote.detail.character.CharacterViewModel
import com.phantom.banguminote.detail.character.data.CharacterPersonData
import com.phantom.banguminote.detail.character.data.CharacterRelatedData
import com.phantom.banguminote.databinding.FragmentCharacterRelatedBinding
import com.phantom.banguminote.detail.person.PersonFragment
import com.phantom.banguminote.detail.subject.SubjectFragment

class CharacterRelatedFragment : BaseFragment<FragmentCharacterRelatedBinding>() {

    private var viewModel: CharacterViewModel? = null
    private val adapter = CharacterRelatedAdapter()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCharacterRelatedBinding =
        FragmentCharacterRelatedBinding.inflate(inflater, container, false)

    override fun init() {
        viewModel = parentFragment?.let { ViewModelProvider(it)[CharacterViewModel::class.java] }
        adapter.mOnItemClickListener = onActorItemClickListener
        adapter.setOnItemClickListener(onItemClickListener)
        binding?.recyclerView?.also {
            it.adapter = adapter
            it.layoutManager = GridLayoutManager(context, 2)
            it.addItemDecoration(TransparentDividerItemDecoration.vertical(requireContext()))
            it.addItemDecoration(TransparentDividerItemDecoration.horizontal(requireContext()))
        }
        viewModel?.characterRelatedRes?.setDataOrObserve(viewLifecycleOwner) {
            setData(it)
        }
    }

    private fun setData(data: List<CharacterRelatedData>) {
        viewModel?.characterPersonRes?.setDataOrObserve(viewLifecycleOwner) { list ->
            list.groupBy { it.subject_id }
                .forEach { map ->
                    data.findLast { it.id == map.key }
                        ?.persons = map.value
                }
        }
        adapter.submitList(data)
    }

    private val onItemClickListener =
        BaseQuickAdapter.OnItemClickListener<CharacterRelatedData> { adapter, view, position ->
            findNavController().navigate(
                R.id.action_nav_to_activity_subject,
                SubjectFragment.createBundle(adapter.items[position].id)
            )
        }

    private val onActorItemClickListener =
        BaseQuickAdapter.OnItemClickListener<CharacterPersonData> { adapter, view, position ->
            findNavController().navigate(
                R.id.action_nav_to_activity_person,
                PersonFragment.createBundle(adapter.items[position].id)
            )
        }

}