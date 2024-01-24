package com.phantom.banguminote.subject

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.phantom.banguminote.ErrorFragment
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseActivity
import com.phantom.banguminote.data.CharacterData
import com.phantom.banguminote.data.HttpErrorData
import com.phantom.banguminote.data.RelatedSubjectData
import com.phantom.banguminote.databinding.ActivitySubjectBinding
import com.phantom.banguminote.front.calendar.BaseViewPagerAdapter
import com.phantom.banguminote.front.calendar.BaseViewPagerAdapter.FragmentData
import com.phantom.banguminote.subject.character.SubjectCharacterFragment
import com.phantom.banguminote.subject.collection.SubjectCollectionFragment
import com.phantom.banguminote.subject.info.SubjectInfoFragment
import com.phantom.banguminote.subject.related.SubjectRelatedFragment
import com.phantom.banguminote.subject.summary.SubjectSummaryFragment

class SubjectActivity : BaseActivity<ActivitySubjectBinding>() {

    private val viewModel: SubjectViewModel by viewModels()
    private var id = -1
    private lateinit var viewPageAdapter: BaseViewPagerAdapter

    override fun inflateViewBinding(): ActivitySubjectBinding =
        ActivitySubjectBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.extras?.getInt(KEY_SUBJECT_ID) ?: -1
        viewPageAdapter = BaseViewPagerAdapter(supportFragmentManager, lifecycle).also {
            it.addFragments(
                FragmentData(getString(R.string.subject_basic_information), SubjectInfoFragment()),
                FragmentData(getString(R.string.subject_summary), SubjectSummaryFragment()),
                FragmentData(getString(R.string.subject_collection), SubjectCollectionFragment()),
                FragmentData(getString(R.string.subject_character), SubjectCharacterFragment()),
                FragmentData(getString(R.string.subject_related), SubjectRelatedFragment()),
            )
        }
        binding.also {
            it.viewPage.adapter = viewPageAdapter
            TabLayoutMediator(it.tabLayout, it.viewPage) { tab, position ->
                tab.text = viewPageAdapter.getTitle(position)
            }.attach()
            it.tabLayout.selectTab(it.tabLayout.getTabAt(2))
        }
        viewModel.also { v ->
            v.error.observe(this) { showToast(it.message) }
            v.httpError.observe(this) { httpError(it) }
            v.subjectRes.observe(this) { setSubjectBaseData(it) }
            v.subjectCharacterRes.observe(this) { setCharacterData(it) }
            v.subjectRelatedSubjectRes.observe(this) { setRelatedSubjectData(it) }

            v.subject(id)
            v.subjectCharacter(id)
            v.subjectRelatedSubjects(id)
            v.subjectPersons(id)
        }
    }

    private fun httpError(data: HttpErrorData) {
        binding.also {
            it.toolbar.title = "Error"
            it.appBarLayout.setExpanded(false)
            it.tabLayout.visibility = View.GONE
        }
        viewPageAdapter.fragmentData = mutableListOf(FragmentData(fragment = ErrorFragment().also {
            it.arguments = Bundle().apply {
                putParcelable(ErrorFragment.KEY_HTTP_ERROR_DATA, data)
            }
        }))
    }

    private fun setSubjectBaseData(data: SubjectData) {
        binding.also {
            it.toolbar.title =
                "${(data.platform)?.let { p -> "$p " }} ${data.name_cn?.takeIf { it.isNotBlank() } ?: data.name}"
            Glide.with(this)
                .load(data.images?.large)
                .into(it.ivCover)
        }
    }

    private fun setCharacterData(data: List<CharacterData>) {
        if (data.isEmpty()) {
            viewPageAdapter.remove(getString(R.string.subject_character))
        }
    }

    private fun setRelatedSubjectData(data: List<RelatedSubjectData>) {
        if (data.isEmpty()) {
            viewPageAdapter.remove(getString(R.string.subject_related))
        }
    }

    companion object {
        const val KEY_SUBJECT_ID = "KEY_SUBJECT_ID"
    }
}