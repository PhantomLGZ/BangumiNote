package com.phantom.banguminote.me.collection

import com.phantom.banguminote.data.ImageData
import com.phantom.banguminote.data.TagsData

data class CollectionsReq(
    val username: String,
    val subjectType: Int,
    val collectionType: Int,
)

data class CollectionItemReq(
    val username: String,
    val subject_id: Int,
)

data class CollectionItemRes(
    val updated_at: String?,
    val comment: String?,
    val tags: List<String>?,
    val subject: CollectionSubjectData?,
    val subject_id: Int?,
    val vol_status: Int?,
    val ep_status: Int?,
    val subject_type: Int?,
    val type: Int?,
    val rate: Int?,
    val private: Boolean?,
)

data class CollectionSubjectData(
    val date: String?,
    val images: ImageData?,
    val name: String?,
    val name_cn: String?,
    val short_summary: String?,
    val tags: List<TagsData>,
    val score: Double?,
    val type: Int?,
    val id: Int?,
    val eps: Int?,
    val volumes: Int?,
    val collection_total: Int?,
    val rank: Int?,
)
