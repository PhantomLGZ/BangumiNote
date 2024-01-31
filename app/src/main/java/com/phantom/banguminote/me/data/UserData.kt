package com.phantom.banguminote.me.data

import com.phantom.banguminote.data.ImageData

data class UserData(
    val avatar: ImageData?,
    val sign: String?,
    val username: String?,
    val nickname: String?,
    val id: Int?,
    val user_group: Int?,
)
