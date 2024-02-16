package com.phantom.banguminote.detail.subject.collection

data class ModifyCollectionReq(
    val type: Int,
    val rate: Int,
    val comment: String,
    val tags: List<String>,
    val private: Boolean = false,
    val ep_status: Int? = null,
    val vol_status: Int? = null,
)
