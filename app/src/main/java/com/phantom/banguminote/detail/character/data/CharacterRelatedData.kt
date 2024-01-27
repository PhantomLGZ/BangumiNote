package com.phantom.banguminote.detail.character.data

data class CharacterRelatedData(
    val id: Int?,
    val staff: String?,
    val name: String?,
    val name_cn: String?,
    val image: String?,
    var persons: List<CharacterPersonData>?,
)
