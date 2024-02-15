package com.phantom.banguminote.detail.subject.episode

data class EpisodeReq(
    val subject_id: Int,
    /** type
     * 0 = 本篇
     * 1 = 特别篇
     * 2 = OP
     * 3 = ED
     * 4 = 预告/宣传/广告
     * 5 = MAD
     * 6 = 其他
     */
    val type: Int,
)