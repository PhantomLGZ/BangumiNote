package com.phantom.banguminote.detail.subject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.phantom.banguminote.R
import com.phantom.banguminote.databinding.FragmentSubjectBinding
import com.phantom.banguminote.detail.BaseDetailFragment
import com.phantom.banguminote.base.BaseViewPagerAdapter.FragmentData
import com.phantom.banguminote.detail.subject.character.SubjectCharacterFragment
import com.phantom.banguminote.detail.subject.collection.SubjectCollectionFragment
import com.phantom.banguminote.detail.subject.data.RelatedSubjectData
import com.phantom.banguminote.detail.subject.data.SubjectCharacterData
import com.phantom.banguminote.detail.subject.data.SubjectData
import com.phantom.banguminote.detail.subject.info.SubjectInfoFragment
import com.phantom.banguminote.detail.subject.related.SubjectRelatedFragment
import com.phantom.banguminote.detail.subject.summary.SubjectSummaryFragment

class SubjectFragment : BaseDetailFragment<SubjectViewModel, FragmentSubjectBinding>() {

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubjectBinding =
        FragmentSubjectBinding.inflate(inflater, container, false)

    override fun assignViewModel(): SubjectViewModel =
        ViewModelProvider(this)[SubjectViewModel::class.java]

    override fun assignFragments(): List<FragmentData> =
        listOf(
            FragmentData(getString(R.string.subject_info), SubjectInfoFragment()),
            FragmentData(getString(R.string.subject_summary), SubjectSummaryFragment()),
            FragmentData(getString(R.string.subject_collection), SubjectCollectionFragment()),
            FragmentData(getString(R.string.subject_character), SubjectCharacterFragment()),
            FragmentData(getString(R.string.subject_related), SubjectRelatedFragment()),
        )

    override fun init() {
        val id = activity?.intent?.extras?.getInt(KEY_SUBJECT_ID) ?: -1
        super.init()
        mergeBinding?.also {
            it.tabLayout.selectTab(it.tabLayout.getTabAt(2))
        }
        viewModel?.also { v ->
            v.subjectRes.observe(viewLifecycleOwner) { setSubjectBaseData(it) }
            v.subjectCharacterRes.observe(viewLifecycleOwner) { setCharacterData(it) }
            v.subjectRelatedSubjectRes.observe(viewLifecycleOwner) { setRelatedSubjectData(it) }

            v.subject(id)
            v.subjectCharacter(id)
            v.subjectRelatedSubjects(id)
            v.subjectPersons(id)
        }
    }

    private fun setSubjectBaseData(data: SubjectData) {
        mergeBinding?.also { b ->
            b.toolbar.title =
                "${(data.platform)?.let { p -> "$p " }} ${data.name_cn?.takeIf { it.isNotBlank() } ?: data.name}"
            Glide.with(this)
                .load(data.images?.large)
                .into(b.ivCover)
        }
    }

    private fun setCharacterData(data: List<SubjectCharacterData>) {
        if (data.isEmpty()) {
            viewPageAdapter?.remove(getString(R.string.subject_character))
        }
    }

    private fun setRelatedSubjectData(data: List<RelatedSubjectData>) {
        if (data.isEmpty()) {
            viewPageAdapter?.remove(getString(R.string.subject_related))
        }
    }

    companion object {
        const val KEY_SUBJECT_ID = "KEY_SUBJECT_ID"

        fun createBundle(id: Int?): Bundle =
            Bundle().also {
                it.putInt(KEY_SUBJECT_ID, id ?: 0)
            }

    }
}