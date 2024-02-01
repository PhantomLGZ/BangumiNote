package com.phantom.banguminote.search

data class SearchReq(
    var keyword: String? = null,
    var sort: String = "", // """match" / "score"↓ / "heat"↓ / "rank"↑
    var filter: SearchFilter = SearchFilter(),
)

data class SearchFilter(
    var type: MutableList<Int>? = null,
    var tag: MutableList<String>? = null,
    var air_date: List<String>? = null,
    var rating: List<String>? = null,
    var rank: List<String>? = null,
    var nsfw: Boolean = true,
)
