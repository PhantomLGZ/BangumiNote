package com.phantom.banguminote.subject

import com.phantom.banguminote.data.CollectionData
import com.phantom.banguminote.data.ImageData
import com.phantom.banguminote.data.InfoData
import com.phantom.banguminote.data.RatingData
import com.phantom.banguminote.data.TagsData

data class SubjectData(
    val id: Int?,
    val type: Int?,
    val name: String?,
    val name_cn: String?,
    val summary: String?,
    val nsfw: Boolean?,
    val locked: Boolean?,
    val date: String?,
    val platform: String?,
    val volumes: Int?,
    val eps: Int?,
    val total_episodes: Int?,
    val images: ImageData?,
    val infobox: List<InfoData>?,
    val rating: RatingData?,
    val collection: CollectionData?,
    val tags: List<TagsData>?
)
