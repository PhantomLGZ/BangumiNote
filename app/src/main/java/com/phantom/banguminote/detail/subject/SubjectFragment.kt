package com.phantom.banguminote.detail.subject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.phantom.banguminote.R
import com.phantom.banguminote.databinding.FragmentSubjectBinding
import com.phantom.banguminote.detail.BaseDetailFragment
import com.phantom.banguminote.base.BaseViewPagerAdapter.FragmentData
import com.phantom.banguminote.base.getUserName
import com.phantom.banguminote.detail.subject.character.SubjectCharacterFragment
import com.phantom.banguminote.detail.subject.collection.CollectionDialogFragment
import com.phantom.banguminote.detail.subject.collection.SubjectCollectionFragment
import com.phantom.banguminote.detail.subject.data.RelatedSubjectData
import com.phantom.banguminote.detail.subject.data.SubjectCharacterData
import com.phantom.banguminote.detail.subject.data.SubjectData
import com.phantom.banguminote.detail.subject.episode.EpisodeListFragment
import com.phantom.banguminote.detail.subject.info.SubjectInfoFragment
import com.phantom.banguminote.detail.subject.related.SubjectRelatedFragment
import com.phantom.banguminote.detail.subject.summary.SubjectSummaryFragment
import com.phantom.banguminote.detail.subject.web.SubjectRelatedWebFragment
import com.phantom.banguminote.me.collection.CollectionItemReq
import com.phantom.banguminote.me.collection.CollectionItemRes

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
            FragmentData(getString(R.string.subject_episode), EpisodeListFragment()),
            FragmentData(getString(R.string.subject_character), SubjectCharacterFragment()),
            FragmentData(getString(R.string.subject_related), SubjectRelatedFragment()),
            FragmentData(getString(R.string.subject_web), SubjectRelatedWebFragment()),
        )

    override fun init() {
        val id = activity?.intent?.extras?.getInt(KEY_SUBJECT_ID) ?: -1
        super.init()
        resetTabSelect()
        viewModel?.also { v ->
            v.id.value = id
            v.error.observe(viewLifecycleOwner) { showToast(it.message) }
            v.subjectRes.observe(viewLifecycleOwner) { setSubjectBaseData(it) }
            v.subjectCharacterRes.observe(viewLifecycleOwner) { setCharacterData(it) }
            v.subjectRelatedSubjectRes.observe(viewLifecycleOwner) { setRelatedSubjectData(it) }
            v.collectionInfoRes.observe(viewLifecycleOwner) { handleCollectionInfo(it) }
            v.collectionHttpError.observe(viewLifecycleOwner) { handleCollectionInfo(null) }

            v.subject(id)
            v.subjectCharacter(id)
            v.subjectRelatedSubjects(id)
            v.subjectPersons(id)
            context?.getUserName()?.takeIf { it.isNotBlank() }?.also {
                v.getCollectionInfo(CollectionItemReq(it, id))
            }
        }
        binding?.fbCollection?.setOnClickListener {
            CollectionDialogFragment().show(childFragmentManager, "")
        }
    }

    private fun resetTabSelect() {
        mergeBinding?.also {
            it.tabLayout.selectTab(it.tabLayout.getTabAt(2))
        }
    }

    private fun setSubjectBaseData(data: SubjectData) {
        mergeBinding?.also { b ->
            if ((data.eps ?: 0) == 0 && (data.volumes ?: 0) == 0) {
                viewPageAdapter?.remove(getString(R.string.subject_episode))
                resetTabSelect()
            }
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
            resetTabSelect()
        }
    }

    private fun setRelatedSubjectData(data: List<RelatedSubjectData>) {
        if (data.isEmpty()) {
            viewPageAdapter?.remove(getString(R.string.subject_related))
            resetTabSelect()
        }
    }

    private fun handleCollectionInfo(data: CollectionItemRes?) {
        println("TEST ${data}")
        binding?.also { b ->
            b.fbCollection.setImageResource(
                when (data?.type) {
                    1 -> R.drawable.round_favorite_24
                    2 -> R.drawable.round_catching_pokemon_24
                    3 -> R.drawable.round_play_arrow_24
                    4 -> R.drawable.round_pause_24
                    5 -> R.drawable.round_inventory_2_24
                    else -> R.drawable.round_add_24
                }
            )
            b.fbCollection.visibility = View.VISIBLE
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