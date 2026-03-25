package com.phantom.banguminote.search

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.base.BaseViewPagerAdapter
import com.phantom.banguminote.base.BaseViewPagerAdapter.FragmentData
import com.phantom.banguminote.base.ImageDialogFragment
import com.phantom.banguminote.base.TransparentDividerItemDecoration
import com.phantom.banguminote.data.PageReqData
import com.phantom.banguminote.data.PageResData
import com.phantom.banguminote.databinding.FragmentSearchBinding
import com.phantom.banguminote.detail.character.CharacterFragment
import com.phantom.banguminote.detail.character.data.CharacterData
import com.phantom.banguminote.detail.person.PersonFragment
import com.phantom.banguminote.detail.person.data.PersonData
import com.phantom.banguminote.detail.subject.SubjectFragment
import com.phantom.banguminote.search.data.SearchSubjectRes

class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private val viewModel: SearchViewModel by activityViewModels()
    var viewPageAdapter: BaseViewPagerAdapter? = null
    private val subjectAdapter = SearchSubjectAdapter()
    private val characterAdapter = SearchCharacterAdapter()
    private val personAdapter = SearchPersonAdapter()
    private var subjectHelper: QuickAdapterHelper? = null
    private var characterHelper: QuickAdapterHelper? = null
    private var personHelper: QuickAdapterHelper? = null
    private val limit = 10
    private var offset = 0
    private var translateHeight = 0

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding =
        FragmentSearchBinding.inflate(inflater, container, false)

    override fun init() {
        viewPageAdapter = BaseViewPagerAdapter(childFragmentManager, lifecycle).also {
            it.setFragments(
                listOf(
                    FragmentData(
                        getString(R.string.search_advance_subject),
                        SearchSubjectAdvanceSubjectFragment()
                    ),
                    FragmentData(
                        getString(R.string.search_advance_character),
                        SearchSubjectAdvanceCharacterFragment()
                    ),
                    FragmentData(
                        getString(R.string.search_advance_person),
                        SearchSubjectAdvancePersonFragment()
                    ),
                )
            )
        }
        subjectAdapter.setOnItemClickListener { adapter, view, position ->
            findNavController().navigate(
                R.id.action_nav_to_activity_subject,
                SubjectFragment.createBundle(adapter.items[position].id)
            )
        }
        subjectAdapter.addOnItemChildClickListener(R.id.ivCover, onItemChildClickListener)
        subjectHelper = QuickAdapterHelper.Builder(subjectAdapter)
            .setTrailingLoadStateAdapter(onTrailingListener)
            .build()
        characterAdapter.setOnItemClickListener { adapter, view, position ->
            findNavController().navigate(
                R.id.action_nav_to_activity_character,
                CharacterFragment.createBundle(adapter.items[position].id)
            )
        }
        characterHelper = QuickAdapterHelper.Builder(characterAdapter)
            .setTrailingLoadStateAdapter(onTrailingListener)
            .build()
        personAdapter.setOnItemClickListener { adapter, view, position ->
            findNavController().navigate(
                R.id.action_nav_to_activity_person,
                PersonFragment.createBundle(adapter.items[position].id)
            )
        }
        personHelper = QuickAdapterHelper.Builder(personAdapter)
            .setTrailingLoadStateAdapter(onTrailingListener)
            .build()
        binding?.also { b ->
            b.viewPage.adapter = viewPageAdapter
            TabLayoutMediator(b.tabLayout, b.viewPage) { tab, position ->
                tab.text = viewPageAdapter?.getTitle(position)
            }.attach()
            b.searchView.setOnQueryTextListener(onQueryTextListener)
            b.refreshLayout.also {
                it.setOnRefreshListener {
                    viewModel.searchSubjectReq.keyword =
                        binding?.searchView?.query?.toString()?.takeIf { it.isNotBlank() }
                    doNewSearch()
                }
            }
            b.rvSubject.also { rv ->
                rv.layoutManager = LinearLayoutManager(context)
                rv.adapter = subjectHelper?.adapter
                rv.addItemDecoration(TransparentDividerItemDecoration.vertical(requireContext()))
            }
            b.rvCharacter.also { rv ->
                rv.layoutManager = GridLayoutManager(context, 2)
                rv.adapter = characterHelper?.adapter
                rv.addItemDecoration(TransparentDividerItemDecoration.vertical(requireContext()))
                rv.addItemDecoration(TransparentDividerItemDecoration.horizontal(requireContext()))
            }
            b.rvPerson.also { rv ->
                rv.layoutManager = GridLayoutManager(context, 2)
                rv.adapter = personHelper?.adapter
                rv.addItemDecoration(TransparentDividerItemDecoration.vertical(requireContext()))
                rv.addItemDecoration(TransparentDividerItemDecoration.horizontal(requireContext()))
            }
            b.layoutAdvance.viewTreeObserver?.also {
                it.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        binding?.layoutAdvance?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                        translateHeight = binding?.layoutAdvance?.measuredHeight ?: 0
                        binding?.layoutAdvance?.visibility = View.GONE
                        binding?.layoutAdvance?.translationY =
                            -translateHeight.toFloat()
                    }
                })
            }
            b.btSearch.setOnClickListener(onClickListener)
            b.btAdvance.setOnClickListener(onClickListener)
        }
        viewModel.also { v ->
            v.error.observe(viewLifecycleOwner) { showToast(it.message) }
            v.searchSubjectRes.observe(viewLifecycleOwner) { setSubjectData(it) }
            v.searchCharacterRes.observe(viewLifecycleOwner) { setCharacterData(it) }
            v.searchPersonRes.observe(viewLifecycleOwner) { setPersonData(it) }
        }
        doNewSearch()
    }

    override fun onResume() {
        super.onResume()
        syncFilterView()
    }

    private fun syncFilterView() {
        binding?.searchView?.setQuery(when (binding?.tabLayout?.selectedTabPosition) {
            0 -> viewModel.searchSubjectReq.keyword
            1 -> viewModel.searchCharacterReq.keyword
            2 -> viewModel.searchPersonReq.keyword
            else -> null
        } ?: "", false)
        binding?.btAdvance?.isChecked = false
    }

    private fun setSubjectData(data: PageResData<SearchSubjectRes>) {
        binding?.refreshLayout?.isRefreshing = false
        if (offset == 0) {
            subjectAdapter.submitList(data.data)
        } else {
            subjectAdapter.addAll(data.data)
        }
        subjectHelper?.trailingLoadState = LoadState.NotLoading(
            (data.data.size + offset) >= data.total
        )
    }

    private fun setCharacterData(data: PageResData<CharacterData>) {
        binding?.refreshLayout?.isRefreshing = false
        if (offset == 0) {
            characterAdapter.submitList(data.data)
        } else {
            characterAdapter.addAll(data.data)
        }
        characterHelper?.trailingLoadState = LoadState.NotLoading(
            (data.data.size + offset) >= data.total
        )
    }

    private fun setPersonData(data: PageResData<PersonData>) {
        binding?.refreshLayout?.isRefreshing = false
        if (offset == 0) {
            personAdapter.submitList(data.data)
        } else {
            personAdapter.addAll(data.data)
        }
        personHelper?.trailingLoadState = LoadState.NotLoading(
            (data.data.size + offset) >= data.total
        )
    }

    private val onClickListener = View.OnClickListener { v ->
        when (v) {
            binding?.btSearch -> {
                when (binding?.tabLayout?.selectedTabPosition) {
                    0 -> {
                        viewModel.searchSubjectReq.keyword = binding?.searchView?.query?.toString()
                    }
                    1 -> {
                        viewModel.searchCharacterReq.keyword = binding?.searchView?.query?.toString()
                    }
                    2 -> {
                        viewModel.searchPersonReq.keyword = binding?.searchView?.query?.toString()
                    }
                }
                doNewSearch()
            }

            binding?.btAdvance -> {
                animateAdvancePanel()
            }
        }
    }

    private val onTrailingListener = object : TrailingLoadStateAdapter.OnTrailingListener {
        override fun onFailRetry() {}

        override fun onLoad() {
            offset += limit
            doSearch()
        }

        override fun isAllowLoading(): Boolean {
            return binding?.refreshLayout?.isRefreshing?.not() ?: true
        }
    }

    private val onQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            when (binding?.tabLayout?.selectedTabPosition) {
                0 -> {
                    viewModel.searchSubjectReq.keyword = query?.takeIf { it.isNotBlank() }
                }
                1 -> {
                    viewModel.searchCharacterReq.keyword = query?.takeIf { it.isNotBlank() }
                }
                2 -> {
                    viewModel.searchPersonReq.keyword = query?.takeIf { it.isNotBlank() }
                }
            }
            doNewSearch()
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean = false
    }

    private val onItemChildClickListener =
        BaseQuickAdapter.OnItemChildClickListener<SearchSubjectRes> { adapter, view, position ->
            val url = adapter.items[position].image
            if (url?.isNotBlank() == true) {
                ImageDialogFragment.createFragment(url)
                    .show(childFragmentManager, "ImageDialog")
            }
        }

    private fun doNewSearch() {
        if (binding?.btAdvance?.isChecked == true) {
            binding?.btAdvance?.isChecked = false
            animateAdvancePanel()
        }
        binding?.refreshLayout?.isRefreshing = true
        offset = 0
        when (binding?.tabLayout?.selectedTabPosition) {
            0 -> {
                binding?.rvSubject?.visibility = View.VISIBLE
                binding?.rvCharacter?.visibility = View.GONE
                binding?.rvPerson?.visibility = View.GONE
            }
            1 -> {
                binding?.rvSubject?.visibility = View.GONE
                binding?.rvCharacter?.visibility = View.VISIBLE
                binding?.rvPerson?.visibility = View.GONE
            }
            2 -> {
                binding?.rvSubject?.visibility = View.GONE
                binding?.rvCharacter?.visibility = View.GONE
                binding?.rvPerson?.visibility = View.VISIBLE
            }
        }
        doSearch()
    }

    private fun animateAdvancePanel() {
        binding?.let { b ->
            val isChecked = b.btAdvance.isChecked
            println()
            b.layoutAdvance.animate()
                ?.translationY(if (isChecked) 0f else -translateHeight.toFloat())
                ?.setDuration(100)
                ?.setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        if (isChecked) {
                            binding?.layoutAdvance?.visibility = View.VISIBLE
                        }
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        if (isChecked) {
                            translateHeight = binding?.layoutAdvance?.measuredHeight ?: 0
                        } else {
                            binding?.layoutAdvance?.visibility = View.GONE
                        }
                    }
                })
        }
    }

    private fun doSearch() {
        when (binding?.tabLayout?.selectedTabPosition) {
            0 -> {
                viewModel.searchSubject(PageReqData(viewModel.searchSubjectReq, limit, offset))
            }
            1 -> {
                viewModel.searchCharacter(PageReqData(viewModel.searchCharacterReq, limit, offset))
            }
            2 -> {
                viewModel.searchPerson(PageReqData(viewModel.searchPersonReq, limit, offset))
            }
        }

    }

}