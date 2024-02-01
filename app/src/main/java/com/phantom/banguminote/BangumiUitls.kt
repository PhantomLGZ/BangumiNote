package com.phantom.banguminote

import android.content.Context


fun Context.getCollectionTypeName(type: Int?, subjectType: Int?): String =
    when (type) {
        1 -> getString(R.string.collection_wish, getSubjectActionName(subjectType))
        2 -> getString(R.string.collection_collect, getSubjectActionName(subjectType))
        3 -> getString(R.string.collection_doing, getSubjectActionName(subjectType))
        4 -> getString(R.string.collection_on_hold)
        5 -> getString(R.string.collection_dropped)
        else -> ""
    }

fun Context.getSubjectActionName(subjectType: Int?) =
    when (subjectType) {
        1 -> getString(R.string.collection_read)
        2 -> getString(R.string.collection_watch)
        3 -> getString(R.string.collection_listen)
        4 -> getString(R.string.collection_play)
        6 -> getString(R.string.collection_watch)
        else -> ""
    }