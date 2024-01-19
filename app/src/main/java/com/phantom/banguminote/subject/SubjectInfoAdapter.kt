package com.phantom.banguminote.subject

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.phantom.banguminote.R
import com.phantom.banguminote.data.InfoData
import com.phantom.banguminote.data.InfoDataType
import com.phantom.banguminote.data.ValuesData

class SubjectInfoAdapter : BaseQuickAdapter<InfoData, QuickViewHolder>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder =
        QuickViewHolder(R.layout.item_info, parent)

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: InfoData?) {
        holder.also {
            it.setText(R.id.tvTitle, item?.key)
            when (item?.actualValue?.type) {
                InfoDataType.SINGLE -> {
                    it.setGone(R.id.singleLayout, false)
                    it.setGone(R.id.recyclerView, true)
                    it.setText(R.id.tvValue, item.actualValue?.value)
                }

                InfoDataType.LIST -> {
                    it.setGone(R.id.singleLayout, true)
                    it.setGone(R.id.recyclerView, false)
                    it.getView<RecyclerView>(R.id.recyclerView).also { rv ->
                        rv.layoutManager = LinearLayoutManager(context)
                        rv.adapter = SubjectInfoValueAdapter().also { a ->
                            a.submitList(item.actualValue?.values)
                        }
                    }
                }

                else -> {
                    it.setGone(R.id.singleLayout, true)
                    it.setGone(R.id.recyclerView, true)
                }
            }
        }
    }

    private class SubjectInfoValueAdapter : BaseQuickAdapter<ValuesData, QuickViewHolder>() {

        override fun onCreateViewHolder(
            context: Context,
            parent: ViewGroup,
            viewType: Int
        ): QuickViewHolder =
            QuickViewHolder(R.layout.item_info_value, parent)

        override fun onBindViewHolder(
            holder: QuickViewHolder,
            position: Int,
            item: ValuesData?
        ) {
            holder.also {
                it.setText(R.id.tvValue, item?.v)
            }
        }

    }

}