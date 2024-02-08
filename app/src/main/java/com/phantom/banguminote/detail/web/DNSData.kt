package com.phantom.banguminote.detail.web

data class DNSData(
    val Status: Int?,
    val TC: Boolean?,
    val RD: Boolean?,
    val AD: Boolean?,
    val CD: Boolean?,
    val Question: List<QuestionData>?,
    val Answer: List<AnswerData>?,
)

data class QuestionData(
    val name: String?,
    val type: Int?,
)

data class AnswerData(
    val name: String?,
    val type: Int?,
    val TTL: Int?,
    val data: String?,
)
