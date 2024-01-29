package com.phantom.banguminote.data

data class PageReqData<T>(
    val req: T,
    val limit: Int,
    val offset: Int,
)
