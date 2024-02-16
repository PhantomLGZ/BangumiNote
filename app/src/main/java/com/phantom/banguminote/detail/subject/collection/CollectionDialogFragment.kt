package com.phantom.banguminote.detail.subject.collection

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import com.chad.library.adapter4.BaseQuickAdapter
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.phantom.banguminote.R
import com.phantom.banguminote.base.BaseDialogFragment
import com.phantom.banguminote.base.LoadingDialogFragment
import com.phantom.banguminote.base.TagAdapter
import com.phantom.banguminote.base.getUserName
import com.phantom.banguminote.databinding.DialogCollectionBinding
import com.phantom.banguminote.detail.subject.SubjectViewModel
import com.phantom.banguminote.getCollectionTypeName
import com.phantom.banguminote.me.collection.CollectionItemReq
import kotlin.math.roundToInt

class CollectionDialogFragment : BaseDialogFragment<DialogCollectionBinding>() {

    private val viewModel: SubjectViewModel by viewModels({ requireParentFragment() })
    private val tagAdapter = TagAdapter()
    private var loadingDialog: LoadingDialogFragment? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogCollectionBinding =
        DialogCollectionBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val subjectType = viewModel.subjectRes.value?.type

        viewModel.also { v ->
            v.modifyCollectionRes.observeForever {
                viewModel.id.value?.also { id ->
                    viewModel.getCollectionInfo(
                        CollectionItemReq(
                            requireContext().getUserName(),
                            id
                        )
                    )
                }
                endLoading()
                dismiss()
            }
            v.error.observeForever {
                endLoading()
                showToast(it.message)
            }
            v.httpError.observeForever {
                endLoading()
                showToast(it.description)
            }
        }

        binding?.also { b ->
            b.tvTitle.text = context?.getCollectionTypeName(
                viewModel.collectionInfoRes.value?.type,
                subjectType
            )
            b.rbWish.text = context?.getCollectionTypeName(1, subjectType)
            b.rbCollect.text = context?.getCollectionTypeName(2, subjectType)
            b.rbDoing.text = context?.getCollectionTypeName(3, subjectType)
            b.rbWish.setOnCheckedChangeListener { buttonView, isChecked ->
                b.rbMyRate.visibility = if (isChecked) View.GONE else View.VISIBLE
            }

            b.etComment.addTextChangedListener(textWatcher)

            b.btTagAdd.setOnClickListener(onClickListener)
            b.btCancel.setOnClickListener(onClickListener)
            b.btSubmit.setOnClickListener(onClickListener)

            b.rvTag.also { rv ->
                rv.layoutManager = FlexboxLayoutManager(context)
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

            viewModel.collectionInfoRes.value?.also { data ->
                when (data.type) {
                    1 -> b.rbWish.isChecked = true
                    2 -> b.rbCollect.isChecked = true
                    3 -> b.rbDoing.isChecked = true
                    4 -> b.rbOnHold.isChecked = true
                    5 -> b.rbDropped.isChecked = true
                }
                b.rbMyRate.rating = (data.rate ?: 0).toFloat()
                b.etComment.setText(data.comment ?: "")
                tagAdapter.submitList(data.tags?.toMutableList())
            }
        }
    }

    private fun endLoading() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    private val onClickListener = View.OnClickListener { v ->
        when (v) {
            binding?.btCancel -> {
                dismiss()
            }

            binding?.btSubmit -> {
                if ((binding?.etComment?.text?.length ?: 0) <= 380) {
                    val req = ModifyCollectionReq(
                        type = when (binding?.rgCollectType?.checkedRadioButtonId) {
                            binding?.rbWish?.id -> 1
                            binding?.rbCollect?.id -> 2
                            binding?.rbDoing?.id -> 3
                            binding?.rbOnHold?.id -> 4
                            binding?.rbDropped?.id -> 5
                            else -> 1
                        },
                        rate = binding?.rbMyRate?.rating?.roundToInt() ?: 0,
                        comment = binding?.etComment?.text?.toString() ?: "",
                        tags = tagAdapter.items
                    )
                    viewModel.modifyCollection(viewModel.id.value!!, req)
                    loadingDialog = LoadingDialogFragment()
                    loadingDialog?.show(childFragmentManager, "")
                } else {
                    showToast(getString(R.string.collection_error_comment_exceeded))
                }
            }

            binding?.btTagAdd -> {
                val tag = binding?.etTag?.text?.toString()
                if (tagAdapter.items.find { it == tag } != null) {
                    showToast(getString(R.string.search_tag_error))
                } else if (tag?.isNotBlank() == true) {
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

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding?.also { b ->
                b.tvCommentCount.text =
                    context?.getString(
                        R.string.collection_comment_limit,
                        b.etComment.text?.length ?: 0,
                        380
                    )
                b.tvCommentCount.setTextColor(
                    requireContext().getColor(
                        if ((b.etComment.text?.length ?: 0) > 380) R.color.text_error
                        else R.color.text_gray_dark
                    )
                )
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private val onTagItemClickListener =
        BaseQuickAdapter.OnItemClickListener<String> { adapter, view, position ->
            adapter.removeAt(position)
        }

}