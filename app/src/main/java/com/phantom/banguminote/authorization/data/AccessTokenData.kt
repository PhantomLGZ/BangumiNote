package com.phantom.banguminote.authorization.data


data class AccessTokenReq(
    val grant_type: String = "authorization_code",
    val client_id: String = "bgm2897659da10d90152",
    val client_secret: String = "ab9a58c5bd35f141450952f2b2b343a2",
    val code: String = "",
    val redirect_uri: String = "",
    val state: String = ""
)

data class AccessTokenRes(
    val access_token: String,
    val expires_in: Int,
    val token_type: String,
    val scope: Any?,
    val refresh_token: String,
    val user_id: String
)
