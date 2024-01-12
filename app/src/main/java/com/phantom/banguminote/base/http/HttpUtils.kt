package com.phantom.banguminote.base.http

import kotlin.reflect.full.memberProperties

inline fun <reified T : Any> T.toMap(): Map<String, String> {
    return T::class.memberProperties.associate {
        it.name to (it.get(this)?.toString() ?: "")
    }
}