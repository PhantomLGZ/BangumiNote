package com.phantom.banguminote.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.phantom.banguminote.R
import com.phantom.banguminote.TransparentDividerItemDecoration
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.data.PageReqData
import com.phantom.banguminote.data.PageResData
import com.phantom.banguminote.databinding.FragmentSearchBinding
import com.phantom.banguminote.detail.subject.SubjectFragment
import com.phantom.banguminote.search.dialog.SearchDateDialogFragment
import com.phantom.banguminote.search.dialog.SearchRankDialogFragment
import com.phantom.banguminote.search.dialog.SearchScoreDialogFragment

class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private val viewModel: SearchViewModel by activityViewModels()
    private val mAdapter = SearchAdapter()
    private val tagAdapter = SearchTagAdapter()
    private var helper: QuickAdapterHelper? = null
    private val limit = 10
    private var offset = 0
    private val searchReq = SearchReq()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding =
        FragmentSearchBinding.inflate(inflater, container, false)

    override fun init() {
        mAdapter.setOnItemClickListener(onItemClickListener)
        helper = QuickAdapterHelper.Builder(mAdapter)
            .setTrailingLoadStateAdapter(onTrailingListener)
            .build()
        binding?.also { b ->
            b.searchView.setOnQueryTextListener(onQueryTextListener)
            b.refreshLayout.also {
                it.setOnRefreshListener {
                    searchReq.keyword = binding?.searchView?.query?.toString() ?: ""
                    doNewSearch()
                }
            }
            b.recyclerView.also { rv ->
                rv.layoutManager = LinearLayoutManager(context)
                rv.adapter = helper?.adapter
                rv.addItemDecoration(TransparentDividerItemDecoration.vertical(requireContext()))
            }
            b.rvTag.also { rv ->
                rv.layoutManager = FlexboxLayoutManager(context).also {
                    it.justifyContent = JustifyContent.SPACE_AROUND
                }
                rv.addItemDecoration(FlexboxItemDecoration(context).also {
                    it.setDrawable(
                        AppCompatResources.getDrawable(
                            requireContext(),
                            R.drawable.flexbox_divider
                        )
                    )
                })
                rv.adapter = tagAdapter.also {
                    it.setOnItemClickListener(onTagItemClickListener)
                }
            }
            b.tbAdvance.setOnCheckedChangeListener { buttonView, isChecked ->
                binding?.layoutAdvance?.visibility = if (isChecked) View.VISIBLE else View.GONE
            }

            b.btDate.setOnClickListener(onClickListener)
            b.btScore.setOnClickListener(onClickListener)
            b.btRank.setOnClickListener(onClickListener)
            b.btTagAdd.setOnClickListener(onClickListener)

            b.rbMatch.setOnCheckedChangeListener(onSortCheckedChangeListener)
            b.rbScore.setOnCheckedChangeListener(onSortCheckedChangeListener)
            b.rbHeat.setOnCheckedChangeListener(onSortCheckedChangeListener)
            b.rbRank.setOnCheckedChangeListener(onSortCheckedChangeListener)

            b.tbAnime.setOnCheckedChangeListener(onTypeCheckedChangeListener)
            b.tbBook.setOnCheckedChangeListener(onTypeCheckedChangeListener)
            b.tbGame.setOnCheckedChangeListener(onTypeCheckedChangeListener)
            b.tbMusic.setOnCheckedChangeListener(onTypeCheckedChangeListener)
            b.tbReal.setOnCheckedChangeListener(onTypeCheckedChangeListener)
        }
        viewModel.also { v ->
            v.error.observe(viewLifecycleOwner) { showToast(it.message) }
            v.searchRes.observe(viewLifecycleOwner) { setData(it) }
        }
        syncReq()
    }

    private fun syncReq() {
        viewModel.searchReq.value?.also { req ->
            if (req.keyword.isNotBlank()) {
                searchReq.keyword = req.keyword
                binding?.searchView?.setQuery(req.keyword, false)
            }
            searchReq.sort = req.sort
            when (req.sort) {
                "", "match" -> binding?.rbMatch?.isChecked = true
                "score" -> binding?.rbScore?.isChecked = true
                "heat" -> binding?.rbHeat?.isChecked = true
                "rank" -> binding?.rbRank?.isChecked = true
            }
            req.filter.type?.also {
                req.filter.type = it
                val list = mutableListOf(1, 2, 3, 4, 6)
                list.removeAll(it)
                list.forEach {
                    when (it) {
                        getTypeNum(binding?.tbBook) -> binding?.tbBook?.isChecked = false
                        getTypeNum(binding?.tbAnime) -> binding?.tbAnime?.isChecked = false
                        getTypeNum(binding?.tbMusic) -> binding?.tbMusic?.isChecked = false
                        getTypeNum(binding?.tbGame) -> binding?.tbGame?.isChecked = false
                        getTypeNum(binding?.tbReal) -> binding?.tbReal?.isChecked = false
                    }
                }
            }
            req.filter.tag?.also {
                val list = mutableListOf<String>()
                list.addAll(it)
                searchReq.filter.tag = it
                tagAdapter.submitList(list)
            }
            req.filter.air_date?.also {
                searchReq.filter.air_date = it
                val after = it.find { it.contains(">=") }
                    ?.replace(">=", "") ?: ""
                val before = it.find { it.contains("<=") }
                    ?.replace("<=", "") ?: ""
                binding?.btDate?.text = getString(R.string.search_range, after, before)
            }
            req.filter.rating?.also {
                searchReq.filter.rating = it
                val greater = it.find { it.contains(">=") }
                    ?.replace(">=", "") ?: ""
                val less = it.find { it.contains("<=") }
                    ?.replace("<=", "") ?: ""
                binding?.btScore?.text = getString(R.string.search_range, greater, less)
            }
            req.filter.rank?.also {
                searchReq.filter.rank = it
                val above = it.find { it.contains("<=") }
                    ?.replace("<=", "") ?: ""
                val below = it.find { it.contains(">=") }
                    ?.replace(">=", "") ?: ""
                binding?.btRank?.text = getString(R.string.search_range, above, below)
            }

            doNewSearch()
        }
    }

    private fun setData(data: PageResData<SearchRes>) {
        binding?.refreshLayout?.isRefreshing = false
        if (offset == 0) {
            mAdapter.submitList(data.data)
        } else {
            mAdapter.addAll(data.data)
        }
        helper?.trailingLoadState = LoadState.NotLoading(
            (data.data.size + offset) >= data.total
        )
    }

    private val onClickListener = View.OnClickListener { v ->
        when (v) {
            binding?.btDate -> {
                SearchDateDialogFragment { after, before ->
                    binding?.btDate?.text = if (after == null && before == null) {
                        searchReq.filter.air_date = null
                        ""
                    } else {
                        val list = mutableListOf<String>()
                        after?.also { list.add(">=${it}") }
                        before?.also { list.add("<=${it}") }
                        searchReq.filter.air_date = list
                        getString(R.string.search_range, after ?: "", before ?: "")
                    }
                }.also { dialog ->
                    val after = searchReq.filter.air_date
                        ?.find { it.contains(">=") }
                        ?.replace(">=", "")
                    val before = searchReq.filter.air_date
                        ?.find { it.contains("<=") }
                        ?.replace("<=", "")
                    dialog.arguments = SearchDateDialogFragment.createBundle(after, before)
                }.show(childFragmentManager, "")
            }

            binding?.btScore -> {
                SearchScoreDialogFragment { greater, less ->
                    binding?.btScore?.text = if (greater == 0f && less == 10f) {
                        searchReq.filter.rating = null
                        ""
                    } else {
                        val list = mutableListOf<String>()
                        greater.takeIf { it != 0f }?.also { list.add(">=${it}") }
                        less.takeIf { it != 10f }?.also { list.add("<=${it}") }
                        searchReq.filter.rating = list
                        getString(R.string.search_range, greater.toString(), less.toString())
                    }
                }.also { dialog ->
                    val greater = searchReq.filter.rating
                        ?.find { it.contains(">=") }
                        ?.replace(">=", "")
                        ?.toFloat()
                    val less = searchReq.filter.rating
                        ?.find { it.contains("<=") }
                        ?.replace("<=", "")
                        ?.toFloat()
                    dialog.arguments = SearchScoreDialogFragment.createBundle(greater, less)
                }.show(childFragmentManager, "")
            }

            binding?.btRank -> {
                SearchRankDialogFragment { above, below ->
                    binding?.btRank?.text = if (above == null && below == null) {
                        searchReq.filter.rank = null
                        ""
                    } else {
                        val list = mutableListOf<String>()
                        above?.also { list.add("<=${it}") }
                        below?.also { list.add(">=${it}") }
                        searchReq.filter.rank = list
                        getString(
                            R.string.search_range,
                            above?.toString() ?: "",
                            below?.toString() ?: ""
                        )
                    }
                }.also { dialog ->
                    val above = searchReq.filter.rank
                        ?.find { it.contains("<=") }
                        ?.replace("<=", "")
                        ?.toInt()
                    val below = searchReq.filter.rank
                        ?.find { it.contains(">=") }
                        ?.replace(">=", "")
                        ?.toInt()
                    dialog.arguments = SearchRankDialogFragment.createBundle(above, below)
                }.show(childFragmentManager, "")
            }

            binding?.btTagAdd -> {
                val tag = binding?.etTag?.text?.toString()
                if (searchReq.filter.tag?.find { it == tag } != null) {
                    showToast(getString(R.string.search_tag_error))
                } else if (tag?.isNotBlank() == true) {
                    if (searchReq.filter.tag == null) {
                        searchReq.filter.tag = mutableListOf()
                    }
                    searchReq.filter.tag?.add(tag)
                    if (tagAdapter.itemCount == 0) {
                        tagAdapter.submitList(mutableListOf(tag))
                    } else {
                        tagAdapter.add(tag)
                    }
                    binding?.etTag?.text = null
                }
            }
        }
    }

    private val onSortCheckedChangeListener =
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                searchReq.sort = when (buttonView) {
                    binding?.rbMatch -> ""
                    binding?.rbScore -> "score"
                    binding?.rbHeat -> "heat"
                    binding?.rbRank -> "rank"
                    else -> ""
                }
            }
        }

    private val onTypeCheckedChangeListener =
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (searchReq.filter.type == null) {
                    searchReq.filter.type = mutableListOf()
                }
                searchReq.filter.type?.add(getTypeNum(buttonView))
                if (searchReq.filter.type?.containsAll(listOf(1, 2, 3, 4, 6)) == true) {
                    searchReq.filter.type = null
                }
            } else {
                if (searchReq.filter.type == null) {
                    searchReq.filter.type = mutableListOf(1, 2, 3, 4, 6).also {
                        it.remove(getTypeNum(buttonView))
                    }
                } else {
                    searchReq.filter.type?.remove(getTypeNum(buttonView))
                    if (searchReq.filter.type?.isEmpty() == true) {
                        searchReq.filter.type = null
                    }
                }
            }
        }

    private fun getTypeNum(buttonView: View?): Int =
        when (buttonView) {
            binding?.tbBook -> 1
            binding?.tbAnime -> 2
            binding?.tbMusic -> 3
            binding?.tbGame -> 4
            binding?.tbReal -> 6
            else -> 0
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
            searchReq.keyword = query ?: ""
            doNewSearch()
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean = false
    }

    private val onItemClickListener =
        BaseQuickAdapter.OnItemClickListener<SearchRes> { adapter, view, position ->
            findNavController().navigate(
                R.id.action_nav_to_activity_subject,
                SubjectFragment.createBundle(adapter.items[position].id)
            )
        }

    private val onTagItemClickListener =
        BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            searchReq.filter.tag?.remove(adapter.items[position])
            adapter.removeAt(position)
        }

    private fun doNewSearch() {
        binding?.tbAdvance?.isChecked = false
        binding?.refreshLayout?.isRefreshing = true
        offset = 0
        doSearch()
    }

    private fun doSearch() {
        viewModel.search(PageReqData(searchReq, limit, offset))
    }

}