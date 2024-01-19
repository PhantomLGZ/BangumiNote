package com.phantom.banguminote.base

import android.content.Context
import android.util.TypedValue

fun Float.spToPx(context: Context?): Float =
    context?.let {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this,
            it.resources.displayMetrics
        )
    } ?: 0f

fun String.checkHttps(): String = this.replace("http:", "https:")

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