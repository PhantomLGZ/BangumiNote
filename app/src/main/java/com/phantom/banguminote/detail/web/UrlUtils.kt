package com.phantom.banguminote.detail.web

fun Int.getBangumiSubjectUrl(): String =
    "https://bgm.tv/subject/${this}"

fun Int.getBangumiCharacterUrl(): String =
    "https://bgm.tv/character/${this}"

fun Int.getBangumiPersonUrl(): String =
    "https://bgm.tv/person/${this}"

fun String.getPixivUrl(): String =
    "https://www.pixiv.net/tags/${this}/artworks?s_mode=s_tag"

fun String.getMikanUrl(): String =
    "https://mikanime.tv/Home/Search?searchstr=${this}"

fun String.getDmhyUrl(): String =
    "https://share.dmhy.org/topics/list?keyword=${this}"
