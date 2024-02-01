package com.phantom.banguminote.base

import android.content.Context
import android.content.SharedPreferences
import com.phantom.banguminote.base.http.RetrofitHelper


const val KEY_PHANTOM_SP = "KEY_PHANTOM_SP"

fun Context.getSp(): SharedPreferences =
    getSharedPreferences(KEY_PHANTOM_SP, Context.MODE_PRIVATE)

const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"

fun Context.getUserToken(): String =
    getSp().getString(KEY_ACCESS_TOKEN, "") ?: ""

fun Context.setUserToken(accessToken: String) {
    val sp = getSp()
    with(sp.edit()) {
        putString(KEY_ACCESS_TOKEN, accessToken)
        RetrofitHelper.setAccessToken(accessToken)
        apply()
    }
}


const val KEY_USER_NAME = "KEY_USER_NAME"

fun Context.getUserName(): String =
    getSp().getString(KEY_USER_NAME, "") ?: ""

fun Context.setUserName(name: String) {
    val sp = getSp()
    with(sp.edit()) {
        putString(KEY_USER_NAME, name)
        apply()
    }
}