package com.phantom.banguminote.detail.subject.data

import com.phantom.banguminote.data.ImageData

data class RelatedSubjectData(
    val images: ImageData?,
    val name: String?,
    val name_cn: String?,
    val relation: String?,
    val type: Int?,
    val id: Int?,
)