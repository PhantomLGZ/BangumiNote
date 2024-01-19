package com.phantom.banguminote.base.http

import com.phantom.banguminote.data.HttpErrorData


sealed class ViewModelResult<out T : Any> {
    data class Success<T : Any>(val data: T?) : ViewModelResult<T>()

    data class HttpError(val data: HttpErrorData) : ViewModelResult<Nothing>()

    data class Error(val exception: Exception) :
        ViewModelResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is HttpError -> "HttpError[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}