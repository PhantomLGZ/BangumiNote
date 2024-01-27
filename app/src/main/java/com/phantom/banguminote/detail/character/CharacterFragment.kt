package com.phantom.banguminote.detail.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.phantom.banguminote.R
import com.phantom.banguminote.detail.character.data.CharacterData
import com.phantom.banguminote.detail.character.info.CharacterInfoFragment
import com.phantom.banguminote.detail.character.related.CharacterRelatedFragment
import com.phantom.banguminote.detail.character.summary.CharacterSummaryFragment
import com.phantom.banguminote.databinding.FragmentCharacterBinding
import com.phantom.banguminote.detail.BaseDetailFragment
import com.phantom.banguminote.front.calendar.BaseViewPagerAdapter.FragmentData

class CharacterFragment : BaseDetailFragment<CharacterViewModel, FragmentCharacterBinding>() {

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCharacterBinding =
        FragmentCharacterBinding.inflate(inflater, container, false)

    override fun assignViewModel(): CharacterViewModel =
        ViewModelProvider(this)[CharacterViewModel::class.java]

    override fun assignFragments(): List<FragmentData> =
        listOf(
            FragmentData(getString(R.string.character_info), CharacterInfoFragment()),
            FragmentData(getString(R.string.character_summary), CharacterSummaryFragment()),
            FragmentData(getString(R.string.character_related), CharacterRelatedFragment()),
        )

    override fun init() {
        val id = activity?.intent?.extras?.getInt(KEY_CHARACTER_ID) ?: 0
        super.init()
        mergeBinding?.also {
            it.tabLayout.selectTab(it.tabLayout.getTabAt(1))
        }
        viewModel?.also { v ->
            v.characterRes.observe(viewLifecycleOwner) { setData(it) }

            v.character(id)
            v.characterRelated(id)
            v.characterPerson(id)
        }
    }

    private fun setData(data: CharacterData) {
        mergeBinding?.also {
            it.toolbar.title = data.name
            Glide.with(this)
                .load(data.images?.large)
                .into(it.ivCover)
        }
    }

    companion object {
        const val KEY_CHARACTER_ID = "KEY_CHARACTER_ID"

        fun createBundle(id: Int?): Bundle =
            Bundle().also {
                it.putInt(KEY_CHARACTER_ID, id ?: 0)
            }

    }
}