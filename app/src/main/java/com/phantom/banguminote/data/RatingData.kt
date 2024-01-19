package com.phantom.banguminote.data

data class RatingData(
    val total: Int,
    val count: Map<String, Int>,
    val score: Double?
)
