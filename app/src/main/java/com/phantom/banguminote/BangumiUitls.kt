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

fun Context.getSubjectType(type: Int): String =
    when (type) {
        1 -> getString(R.string.type_book)
        2 -> getString(R.string.type_anime)
        3 -> getString(R.string.type_music)
        4 -> getString(R.string.type_game)
        6 -> getString(R.string.type_real)
        else -> ""
    }

fun Context.getEpisodeTypeName(type: Int?) =
    when (type) {
        0 -> getString(R.string.episode_type_main)
        1 -> getString(R.string.episode_type_sp)
        2 -> getString(R.string.episode_type_op)
        3 -> getString(R.string.episode_type_ed)
        4 -> getString(R.string.episode_type_pv)
        5 -> getString(R.string.episode_type_mad)
        6 -> getString(R.string.episode_type_other)
        else -> ""
    }