package com.phantom.banguminote.search.data


data class SearchCharacterReq(
    var keyword: String? = "",
    var filter: SearchCharacterFilter = SearchCharacterFilter()
)

data class SearchCharacterFilter(
    var nsfw: Boolean = true
)

