package com.phantom.banguminote.subject.character

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.phantom.banguminote.R
import com.phantom.banguminote.TransparentDividerItemDecoration
import com.phantom.banguminote.data.CharacterData

class SubjectCharacterGroupedAdapter :
    BaseQuickAdapter<Pair<String, List<CharacterData>>, QuickViewHolder>() {

    var mOnItemClickListener: OnItemClickListener<CharacterData>? = null

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder =
        QuickViewHolder(R.layout.item_grouped_list, parent)

    override fun onBindViewHolder(
        holder: QuickViewHolder,
        position: Int,
        item: Pair<String, List<CharacterData>>?
    ) {
        holder.also { h ->
            h.getView<RecyclerView>(R.id.recyclerView).also {
                it.layoutManager = GridLayoutManager(context, 2)
                it.adapter = SubjectCharacterAdapter().also { a ->
                    a.setOnItemClickListener(mOnItemClickListener)
                    a.submitList(item?.second)
                }
                it.addItemDecoration(TransparentDividerItemDecoration.vertical(context))
                it.addItemDecoration(TransparentDividerItemDecoration.horizontal(context))
            }
        }
    }

}