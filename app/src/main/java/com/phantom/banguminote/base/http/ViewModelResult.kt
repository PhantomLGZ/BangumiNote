package com.phantom.banguminote.base.http


sealed class ViewModelResult<out T : Any> {
    data class Success<T : Any>(val data: T?) : ViewModelResult<T>()
    data class Error(val exception: Exception) :
        ViewModelResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}