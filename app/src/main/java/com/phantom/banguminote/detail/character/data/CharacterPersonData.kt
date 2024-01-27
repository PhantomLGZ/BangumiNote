package com.phantom.banguminote.detail.character.data

import com.phantom.banguminote.data.ImageData

data class CharacterPersonData(
    val id: Int?,
    val name: String?,
    val type: Int?,
    val images: ImageData?,
    val subject_id: Int?,
    val subject_name: String?,
    val subject_name_cn: String?,
    val staff: String?,
)
