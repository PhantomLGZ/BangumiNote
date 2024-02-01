package com.phantom.banguminote.me.login.data

data class TokenStatusRes(
    val access_token: String,
    val client_id: String,
    val expires: Long,
    val scope: Any?,
    val user_id: String,
)
