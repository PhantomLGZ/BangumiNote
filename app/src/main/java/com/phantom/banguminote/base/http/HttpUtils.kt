package com.phantom.banguminote.base.http

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import kotlin.reflect.full.memberProperties

inline fun <reified T : Any> T.toMap(): Map<String, String> {
    return T::class.memberProperties.associate {
        it.name to (it.get(this)?.toString() ?: "")
    }
}

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