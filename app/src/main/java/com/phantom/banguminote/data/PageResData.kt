package com.phantom.banguminote.data

data class PageResData<T>(
    val data: List<T>,
    val total: Int,
    val limit: Int,
    val offset: Int,
)
