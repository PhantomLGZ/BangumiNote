package com.phantom.banguminote.detail.subject.data

import com.phantom.banguminote.data.ImageData

data class SubjectPersonData(
    val id: Int?,
    val type: Int?,
    val images: ImageData?,
    val name: String?,
    val relation: String?,
    val career: List<String>?,
    val short_summary: String?,
    val locked: Boolean?,
)
