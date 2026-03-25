package com.phantom.banguminote.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import com.chad.library.adapter4.BaseQuickAdapter
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.base.TagAdapter
import com.phantom.banguminote.base.getUserToken
import com.phantom.banguminote.databinding.FragmentSearchAdvanceSubjectBinding
import com.phantom.banguminote.search.dialog.SearchDateDialogFragment
import com.phantom.banguminote.search.dialog.SearchRankDialogFragment
import com.phantom.banguminote.search.dialog.SearchScoreDialogFragment
import kotlin.getValue

class SearchSubjectAdvanceSubjectFragment : BaseFragment<FragmentSearchAdvanceSubjectBinding>() {

    private val viewModel: SearchViewModel by activityViewModels()
    private val tagAdapter = TagAdapter()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchAdvanceSubjectBinding =
        FragmentSearchAdvanceSubjectBinding.inflate(inflater, container, false)

    override fun init() {
        binding?.also { b ->
            b.rvTag.also { rv ->
                rv.layoutManager = FlexboxLayoutManager(context).also {
                    it.justifyContent = JustifyContent.FLEX_START
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

            b.tbNSFW.setOnCheckedChangeListener { buttonView, isChecked ->
                viewModel.searchSubjectReq.filter.nsfw = isChecked
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.tbNSFW?.visibility = if (context?.getUserToken().isNullOrBlank()) {
            View.GONE
        } else {
            View.VISIBLE
        }
        syncFilterView()
    }

    private fun syncFilterView() {
        viewModel.searchSubjectReq.also { searchReq ->
            when (searchReq.sort) {
                "", "match" -> binding?.rbMatch?.isChecked = true
                "score" -> binding?.rbScore?.isChecked = true
                "heat" -> binding?.rbHeat?.isChecked = true
                "rank" -> binding?.rbRank?.isChecked = true
            }

            val list = mutableListOf(1, 2, 3, 4, 6)
            val types = searchReq.filter.type
            if (types.isNullOrEmpty()) {
                binding?.tbBook?.isChecked = true
                binding?.tbAnime?.isChecked = true
                binding?.tbMusic?.isChecked = true
                binding?.tbGame?.isChecked = true
                binding?.tbReal?.isChecked = true
            } else {
                list.removeAll(types)
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

            tagAdapter.submitList(searchReq.filter.tag?.toMutableList())

            binding?.tbNSFW?.isChecked = searchReq.filter.nsfw

            val after = searchReq.filter.air_date
                ?.find { it.contains(">=") }
                ?.replace(">=", "") ?: ""
            val before = searchReq.filter.air_date
                ?.find { it.contains("<=") }
                ?.replace("<=", "") ?: ""
            binding?.btDate?.text = getString(R.string.search_range, after, before)

            val greater = searchReq.filter.rating
                ?.find { it.contains(">=") }
                ?.replace(">=", "") ?: ""
            val less = searchReq.filter.rating
                ?.find { it.contains("<=") }
                ?.replace("<=", "") ?: ""
            binding?.btScore?.text = getString(R.string.search_range, greater, less)

            val above = searchReq.filter.rank
                ?.find { it.contains("<=") }
                ?.replace("<=", "") ?: ""
            val below = searchReq.filter.rank
                ?.find { it.contains(">=") }
                ?.replace(">=", "") ?: ""
            binding?.btRank?.text = getString(R.string.search_range, above, below)
        }
    }

    private val onClickListener = View.OnClickListener { v ->
        val req = viewModel.searchSubjectReq
        when (v) {
            binding?.btDate -> {
                SearchDateDialogFragment { after, before ->
                    binding?.btDate?.text = if (after == null && before == null) {
                        viewModel.searchSubjectReq.filter.air_date = null
                        ""
                    } else {
                        val list = mutableListOf<String>()
                        after?.also { list.add(">=${it}") }
                        before?.also { list.add("<=${it}") }
                        viewModel.searchSubjectReq.filter.air_date = list
                        getString(R.string.search_range, after ?: "", before ?: "")
                    }
                }.also { dialog ->
                    val after = req.filter.air_date
                        ?.find { it.contains(">=") }
                        ?.replace(">=", "")
                    val before = req.filter.air_date
                        ?.find { it.contains("<=") }
                        ?.replace("<=", "")
                    dialog.arguments = SearchDateDialogFragment.createBundle(after, before)
                }.show(childFragmentManager, "")
            }

            binding?.btScore -> {
                SearchScoreDialogFragment { greater, less ->
                    binding?.btScore?.text = if (greater == 0f && less == 10f) {
                        viewModel.searchSubjectReq.filter.rating = null
                        ""
                    } else {
                        val list = mutableListOf<String>()
                        greater.takeIf { it != 0f }?.also { list.add(">=${it}") }
                        less.takeIf { it != 10f }?.also { list.add("<=${it}") }
                        viewModel.searchSubjectReq.filter.rating = list
                        getString(R.string.search_range, greater.toString(), less.toString())
                    }
                }.also { dialog ->
                    val greater = req.filter.rating
                        ?.find { it.contains(">=") }
                        ?.replace(">=", "")
                        ?.toFloat()
                    val less = req.filter.rating
                        ?.find { it.contains("<=") }
                        ?.replace("<=", "")
                        ?.toFloat()
                    dialog.arguments = SearchScoreDialogFragment.createBundle(greater, less)
                }.show(childFragmentManager, "")
            }

            binding?.btRank -> {
                SearchRankDialogFragment { above, below ->
                    binding?.btRank?.text = if (above == null && below == null) {
                        viewModel.searchSubjectReq.filter.rank = null
                        ""
                    } else {
                        val list = mutableListOf<String>()
                        above?.also { list.add("<=${it}") }
                        below?.also { list.add(">=${it}") }
                        viewModel.searchSubjectReq.filter.rank = list
                        getString(
                            R.string.search_range,
                            above?.toString() ?: "",
                            below?.toString() ?: ""
                        )
                    }
                }.also { dialog ->
                    val above = req.filter.rank
                        ?.find { it.contains("<=") }
                        ?.replace("<=", "")
                        ?.toInt()
                    val below = req.filter.rank
                        ?.find { it.contains(">=") }
                        ?.replace(">=", "")
                        ?.toInt()
                    dialog.arguments = SearchRankDialogFragment.createBundle(above, below)
                }.show(childFragmentManager, "")
            }

            binding?.btTagAdd -> {
                val tag = binding?.etTag?.text?.toString()
                if (req.filter.tag?.find { it == tag } != null) {
                    showToast(getString(R.string.search_tag_error))
                } else if (tag?.isNotBlank() == true) {
                    if (req.filter.tag == null) {
                        viewModel.searchSubjectReq.filter.tag = mutableListOf()
                    }
                    viewModel.searchSubjectReq.filter.tag?.add(tag)
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

    private val onTagItemClickListener =
        BaseQuickAdapter.OnItemClickListener<String> { adapter, view, position ->
            viewModel.searchSubjectReq.filter.tag?.removeIf { it == adapter.items[position] }
            adapter.removeAt(position)
        }

    private val onSortCheckedChangeListener =
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewModel.searchSubjectReq.sort = when (buttonView) {
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
            val req = viewModel.searchSubjectReq
            if (isChecked) {
                if (req.filter.type == null) {
                    viewModel.searchSubjectReq.filter.type = mutableListOf()
                }
                req.filter.type?.add(getTypeNum(buttonView))
                if (req.filter.type?.containsAll(listOf(1, 2, 3, 4, 6)) == true) {
                    viewModel.searchSubjectReq.filter.type = null
                }
            } else {
                if (req.filter.type == null) {
                    viewModel.searchSubjectReq.filter.type = mutableListOf(1, 2, 3, 4, 6).also {
                        it.remove(getTypeNum(buttonView))
                    }
                } else {
                    viewModel.searchSubjectReq.filter.type?.remove(getTypeNum(buttonView))
                    if (req.filter.type?.isEmpty() == true) {
                        viewModel.searchSubjectReq.filter.type = null
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
}