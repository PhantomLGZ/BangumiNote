package com.phantom.banguminote.search

import android.content.Context
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.phantom.banguminote.R
import com.phantom.banguminote.base.checkHttps
import com.phantom.banguminote.detail.person.data.PersonData

class SearchPersonAdapter : BaseQuickAdapter<PersonData, QuickViewHolder>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder =
        QuickViewHolder(R.layout.item_person, parent)

    override fun onBindViewHolder(
        holder: QuickViewHolder,
        position: Int,
        item: PersonData?
    ) {
        holder.also { h ->
            item?.images?.medium?.checkHttps()?.also { url ->
                Glide.with(context).load(url).into(h.getView(R.id.ivImage))
            }
            h.setText(
                R.id.tvCareer,
                item?.career?.joinToString(" / ") { careerToString(it) })
            h.setText(
                R.id.tvName,
                item?.infobox?.getOrNull(0)?.actualValue?.value
                    ?.takeIf { it.isNotBlank() } ?: item?.name)
        }
    }

    private fun careerToString(career: String): String =
        when (career) {
            "artist" -> context.getString(R.string.type_person_artist)
            "director" -> context.getString(R.string.type_person_director)
            "seiyu" -> context.getString(R.string.type_person_seiyu)
            "actor" -> context.getString(R.string.type_person_actor)
            "producer" -> context.getString(R.string.type_person_producer)
            "illustrator" -> context.getString(R.string.type_person_illustrator)
            "mangaka" -> context.getString(R.string.type_person_mangaka)
            "writer" -> context.getString(R.string.type_person_writer)
            else -> career
        }

}