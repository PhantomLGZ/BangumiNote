package com.phantom.banguminote.me.login.data


data class AccessTokenReq(
    val grant_type: String = GRANT_TYPE_AUTH,
    val client_id: String = CLIENT_ID,
    val client_secret: String = CLIENT_SECRET,
    val code: String? = null,
    val refresh_token: String? = null,
    val redirect_uri: String = REDIRECT_URI,
    val state: Int? = null
)

const val GRANT_TYPE_AUTH = "authorization_code"
const val GRANT_TYPE_REFRESH = "refresh_token"
const val CLIENT_ID = "bgm2897659da10d90152"
const val CLIENT_SECRET = "ab9a58c5bd35f141450952f2b2b343a2"
const val REDIRECT_URI = "https://phantom"

data class AccessTokenRes(
    val access_token: String,
    val expires_in: Long,
    val token_type: String,
    val scope: Any?,
    val refresh_token: String,
    val user_id: String?
)
