package com.phantom.banguminote.data

data class CharacterData(
    val images: ImageData?,
    val name: String?,
    val relation: String?,
    val actors: List<PersonData>?,
    val id: Int?,
    val type: Int?,
)