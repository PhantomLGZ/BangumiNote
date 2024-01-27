package com.phantom.banguminote.detail.character.data

import com.phantom.banguminote.data.ImageData
import com.phantom.banguminote.data.InfoData

data class CharacterData(
    val id: Int?,
    val type: Int?,
    val name: String?,
    val summary: String?,
    val images: ImageData?,
    val infobox: List<InfoData>?,
    val locked: Boolean?,
    val gender: String?,
    val blood_type: Int?,
    val birth_year: Int?,
    val birth_mon: Int?,
    val birth_day: Int?,
    val stat: CharacterStatData?,
)