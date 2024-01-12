package com.phantom.banguminote.authorization.data


data class AuthorizationReq(
    val client_id: String = "bgm2897659da10d90152",
    val response_type: String = "code",
    val redirect_uri: String = "",
    val scope: String? = null,
    val state: String = ""
)

data class AuthorizationRes(
    val test: String = ""
)
