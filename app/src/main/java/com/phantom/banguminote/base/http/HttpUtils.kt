package com.phantom.banguminote.base.http

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.setDataOrObserve(
    lifecycleOwner: LifecycleOwner,
    func: (data: T) -> Unit
) {
    value?.also {
        func(it)
    } ?: observe(lifecycleOwner) {
        func(it)
    }
}