package com.phantom.banguminote.front.calendar

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
    val images: ImageInfo?,
    val eps: Int?,
    val eps_count: Int?,
    val rating: RatingInfo?,
    val rank: Int,
    val collection: CollectionInfo
)

data class ImageInfo(
    val large: String?,
    val common: String?,
    val medium: String?,
    val small: String?,
    val grid: String?
)

data class RatingInfo(
    val total: Int,
    val count: Map<String, Int>,
    val score: Double?
)

data class CollectionInfo(
    val wish: Int?,
    val collect: Int?,
    val doing: Int?,
    val on_hold: Int?,
    val dropped: Int?
)