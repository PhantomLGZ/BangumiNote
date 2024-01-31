package com.phantom.banguminote.base

import android.content.Context
import android.content.SharedPreferences
import android.util.TypedValue
import com.phantom.banguminote.base.http.RetrofitHelper
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun Float.spToPx(context: Context?): Float =
    context?.let {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this,
            it.resources.displayMetrics
        )
    } ?: 0f

fun Float.dpToPx(context: Context?): Float =
    context?.let {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            it.resources.displayMetrics
        )
    } ?: 0f

fun String.checkHttps(): String = this.replace("http:", "https:")

val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

fun String.toLocalDate(): LocalDate =
    LocalDate.parse(this, dateFormatter)

fun LocalDate.toFormattedString(): String =
    dateFormatter.format(this)

/**
 * copy from hutool
 */
fun String.unicodeToString(): String {
    if (this.isBlank()) {
        return this
    }
    val len = this.length
    val sb = StringBuilder(len)
    var i: Int
    var pos = 0
    while (this.indexOf("\\u", pos, true).also { i = it } != -1) {
        sb.append(this, pos, i) //写入Unicode符之前的部分
        pos = i
        if (i + 5 < len) {
            var c: Char
            try {
                c = this.substring(i + 2, i + 6).toInt(16).toChar()
                sb.append(c)
                pos = i + 6 //跳过整个Unicode符
            } catch (e: NumberFormatException) {
                //非法Unicode符，跳过
                sb.append(this, pos, i + 2) //写入"\\u"
                pos = i + 2
            }
        } else {
            //非Unicode符，结束
            break
        }
    }
    if (pos < len) {
        sb.append(this, pos, len)
    }
    return sb.toString()
}

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