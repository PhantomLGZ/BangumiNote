package com.phantom.banguminote.front.calendar

import com.phantom.banguminote.data.CollectionData
import com.phantom.banguminote.data.ImageData
import com.phantom.banguminote.data.RatingData

data class CalendarRes(
    val weekday: WeekDayInfo,
    val items: List<AnimeItemInfo>
)

data class WeekDayInfo(
    val en: String,
    val cn: String,
    val ja: String,
    val id: Int
)

data class AnimeItemInfo(
    val id: Int,
    val url: String,
    val type: Int,
    val name: String,
    val name_cn: String,
    val summary: String,
    val air_date: String,
    val air_weekday: Int,
    val images: ImageData?,
    val eps: Int?,
    val eps_count: Int?,
    val rating: RatingData?,
    val rank: Int,
    val collection: CollectionData
)