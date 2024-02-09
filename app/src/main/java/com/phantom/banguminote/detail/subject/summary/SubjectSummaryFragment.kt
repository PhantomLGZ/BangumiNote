package com.phantom.banguminote.detail.subject.summary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chad.library.adapter4.BaseQuickAdapter
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.base.http.setDataOrObserve
import com.phantom.banguminote.data.TagsData
import com.phantom.banguminote.databinding.FragmentSubjectSummaryBinding
import com.phantom.banguminote.databinding.LayoutDetailSummaryBinding
import com.phantom.banguminote.detail.subject.data.SubjectData
import com.phantom.banguminote.detail.subject.SubjectViewModel
import com.phantom.banguminote.search.SearchActivity

class SubjectSummaryFragment : BaseFragment<FragmentSubjectSummaryBinding>() {

    private val viewModel: SubjectViewModel by viewModels({ requireParentFragment() })
    private var includeBinding: LayoutDetailSummaryBinding? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubjectSummaryBinding =
        FragmentSubjectSummaryBinding.inflate(inflater, container, false)

    override fun init() {
        viewModel.subjectRes.setDataOrObserve(viewLifecycleOwner) {
            setData(it)
        }
        includeBinding = binding?.root?.let { LayoutDetailSummaryBinding.bind(it) }
    }

    private fun setData(data: SubjectData) {
        includeBinding?.also { b ->
            b.tvSummary.text = data.summary
        }
        binding?.also { b ->
            b.recyclerView.also { rv ->
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
                rv.adapter = SubjectTagAdapter().also {
                    it.setOnItemClickListener(onItemClickListener)
                    it.submitList(data.tags)
                }
            }
        }
    }

    private val onItemClickListener =
        BaseQuickAdapter.OnItemClickListener<TagsData> { adapter, view, position ->
            findNavController().navigate(
                R.id.action_nav_to_activity_search,
                SearchActivity.createBundleWithTag(adapter.items[position].name)
            )
        }
}