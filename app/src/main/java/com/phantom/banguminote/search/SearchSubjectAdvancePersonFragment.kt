package com.phantom.banguminote.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.widget.AppCompatToggleButton
import androidx.fragment.app.activityViewModels
import com.phantom.banguminote.base.BaseFragment
import com.phantom.banguminote.databinding.FragmentSearchAdvancePersonBinding
import kotlin.getValue

class SearchSubjectAdvancePersonFragment :
    BaseFragment<FragmentSearchAdvancePersonBinding>() {

    private val viewModel: SearchViewModel by activityViewModels()
    private var careerViewList: List<AppCompatToggleButton?>? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchAdvancePersonBinding =
        FragmentSearchAdvancePersonBinding.inflate(inflater, container, false)

    override fun init() {
        careerViewList = listOf(
            binding?.tbArtist,
            binding?.tbDirector,
            binding?.tbSeiyu,
            binding?.tbActor,
            binding?.tbProducer,
            binding?.tbIllustrator,
            binding?.tbMangaka,
            binding?.tbWriter,
        )
        binding?.also { b ->
            careerViewList?.forEach {
                it?.setOnCheckedChangeListener(onCareerCheckedChangeListener)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.tbArtist?.visibility = View.GONE
        binding?.tbArtist?.visibility = View.VISIBLE
        syncFilterView()
    }

    private fun syncFilterView() {
        careerViewList?.forEach { v ->
            v?.isSelected = viewModel.searchPersonReq.filter.career.contains(buttonToString(v))
        }
    }

    private val onCareerCheckedChangeListener =
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            println(buttonToString(buttonView))
            if (isChecked) {
                viewModel.searchPersonReq.filter.career.add(buttonToString(buttonView))
            } else {
                viewModel.searchPersonReq.filter.career.remove(buttonToString(buttonView))
            }
        }

    private fun buttonToString(view: View?): String = when (view) {
        binding?.tbArtist -> "artist"
        binding?.tbDirector -> "director"
        binding?.tbSeiyu -> "seiyu"
        binding?.tbActor -> "actor"
        binding?.tbProducer -> "producer"
        binding?.tbIllustrator -> "illustrator"
        binding?.tbMangaka -> "mangaka"
        binding?.tbWriter -> "writer"
        else -> ""
    }
}