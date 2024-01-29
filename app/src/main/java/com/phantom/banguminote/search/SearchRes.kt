package com.phantom.banguminote.search

import com.phantom.banguminote.data.TagsData

data class SearchRes(
    val id: Int?,
    val type: Int?,
    val date: String?,
    val image: String?,
    val summary: String?,
    val name: String?,
    val name_cn: String?,
    val tags: List<TagsData>?,
    val score: Double?,
    val rank: Int?,
)