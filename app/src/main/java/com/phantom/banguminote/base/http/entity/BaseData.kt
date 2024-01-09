package com.phantom.banguminote.base.http.entity

open class BaseData<T : Any?>(
    private val code: String?,
    private val msg: String?,
    val data: T?
) {
    fun success(): Boolean = code == "200"

    fun getCode(): String? = code

    fun getMsg(): String? = msg
}