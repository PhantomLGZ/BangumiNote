package com.phantom.banguminote.search.data

data class SearchSubjectReq(
    var keyword: String? = null,
    var sort: String = "", // """match" / "score"↓ / "heat"↓ / "rank"↑
    var filter: SearchSubjectFilter = SearchSubjectFilter(),
)

data class SearchSubjectFilter(
    var type: MutableList<Int>? = null,
    var tag: MutableList<String>? = null,
    var air_date: List<String>? = null,
    var rating: List<String>? = null,
    var rank: List<String>? = null,
    var nsfw: Boolean = true,
)
