package com.phantom.banguminote.detail.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.phantom.banguminote.R
import com.phantom.banguminote.databinding.FragmentPersonBinding
import com.phantom.banguminote.detail.BaseDetailFragment
import com.phantom.banguminote.detail.person.character.PersonCharacterFragment
import com.phantom.banguminote.detail.person.data.PersonCharacterData
import com.phantom.banguminote.detail.person.data.PersonData
import com.phantom.banguminote.detail.person.data.PersonRelatedData
import com.phantom.banguminote.detail.person.info.PersonInfoFragment
import com.phantom.banguminote.detail.person.related.PersonRelatedFragment
import com.phantom.banguminote.detail.person.summary.PersonSummaryFragment
import com.phantom.banguminote.base.BaseViewPagerAdapter.FragmentData

class PersonFragment : BaseDetailFragment<PersonViewModel, FragmentPersonBinding>() {

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonBinding =
        FragmentPersonBinding.inflate(inflater, container, false)

    override fun assignViewModel(): PersonViewModel =
        ViewModelProvider(this)[PersonViewModel::class.java]

    override fun assignFragments(): List<FragmentData> =
        listOf(
            FragmentData(getString(R.string.person_info), PersonInfoFragment()),
            FragmentData(getString(R.string.person_summary), PersonSummaryFragment()),
            FragmentData(getString(R.string.person_related), PersonRelatedFragment()),
            FragmentData(getString(R.string.person_character), PersonCharacterFragment()),
        )

    override fun init() {
        val id = activity?.intent?.extras?.getInt(KEY_PERSON_ID) ?: 0
        super.init()
        mergeBinding?.also {
            it.tabLayout.selectTab(it.tabLayout.getTabAt(1))
        }
        viewModel?.also { v ->
            v.personRes.observe(viewLifecycleOwner) { setData(it) }
            v.personRelatedRes.observe(viewLifecycleOwner) { setRelatedData(it) }
            v.personCharacterRes.observe(viewLifecycleOwner) { setCharacterData(it) }

            v.person(id)
            v.personRelated(id)
            v.personCharacter(id)
        }
    }

    private fun setData(data: PersonData) {
        mergeBinding?.also {
            it.toolbar.title = data.name
            Glide.with(this)
                .load(data.images?.large)
                .into(it.ivCover)
        }
    }

    private fun setRelatedData(data: List<PersonRelatedData>) {
        if (data.isEmpty()) {
            viewPageAdapter?.remove(getString(R.string.person_related))
        }
    }

    private fun setCharacterData(data: List<PersonCharacterData>) {
        if (data.isEmpty()) {
            viewPageAdapter?.remove(getString(R.string.person_character))
        }
    }

    companion object {
        const val KEY_PERSON_ID = "KEY_PERSON_ID"

        fun createBundle(id: Int?): Bundle =
            Bundle().also {
                it.putInt(KEY_PERSON_ID, id ?: 0)
            }
    }
}