package com.phantom.banguminote.detail.subject.data

import com.phantom.banguminote.data.ImageData

data class SubjectCharacterData(
    val images: ImageData?,
    val name: String?,
    val relation: String?,
    val actors: List<SubjectPersonData>?,
    val id: Int?,
    val type: Int?,
)