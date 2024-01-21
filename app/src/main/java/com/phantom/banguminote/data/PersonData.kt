package com.phantom.banguminote.data

data class PersonData(
    val id: Int?,
    val type: Int?,
    val images: ImageData?,
    val name: String?,
    val relation: String?,
    val career: List<String>?,
    val short_summary: String?,
    val locked: Boolean?,
)
