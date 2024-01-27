package com.phantom.banguminote.detail

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

class InfoboxAdapter<T> : BaseQuickAdapter<InfoboxAdapter.InfoboxData<T>, QuickViewHolder>() {

    data class InfoboxData<T>(
        val infoData: InfoData,
        var persons: List<T>? = null
    )

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder =
        QuickViewHolder(R.layout.item_info, parent)

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: InfoboxData<T>?) {
        holder.also {
            it.setText(R.id.tvTitle, item?.infoData?.key)
            when (item?.infoData?.actualValue?.type) {
                InfoDataType.SINGLE -> {
                    it.setGone(R.id.singleLayout, false)
                    it.setGone(R.id.recyclerView, true)
                    it.setText(R.id.tvValue, item.infoData.actualValue?.value)
                    it.setGone(R.id.btMore, item.persons.isNullOrEmpty())
                }

                InfoDataType.LIST -> {
                    it.setGone(R.id.singleLayout, true)
                    it.setGone(R.id.recyclerView, false)
                    it.getView<RecyclerView>(R.id.recyclerView).also { rv ->
                        rv.layoutManager = LinearLayoutManager(context)
                        rv.adapter = InfoValueAdapter().also { a ->
                            a.submitList(item.infoData.actualValue?.values)
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

    private class InfoValueAdapter : BaseQuickAdapter<ValuesData, QuickViewHolder>() {

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