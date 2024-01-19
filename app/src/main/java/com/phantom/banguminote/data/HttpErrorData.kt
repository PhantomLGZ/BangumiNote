package com.phantom.banguminote.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HttpErrorData(
    val title: String?,
    val description: String?,
    val details: String?
) : Parcelable
