package com.phantom.banguminote.search.data

data class SearchPersonReq(
    var keyword: String? = "",
    var filter: SearchPersonFilter = SearchPersonFilter(),
)

data class SearchPersonFilter(
    var career: MutableList<String> = mutableListOf(),
)
