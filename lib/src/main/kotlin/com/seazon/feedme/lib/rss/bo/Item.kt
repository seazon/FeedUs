package com.seazon.feedme.lib.rss.bo

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: String,
    val fid: String? = null, // feed/[feedurl]
    var flag: Int = 0,
    val status: Int = 0,
    val process: Int = 0,
    val star: Int = 0, // 0:unstar,1:starred,2:starred-synced
    val tag: Int = 0, // 0:sync,1:unsync
    val title: String? = null,
    val titleTranslated: String? = null,
    val link: String? = null,
    val author: String? = null,
    val publishedDate: Long? = null,
    val updatedDate: Long? = null,
    val description: String? = null,
    val visual: String? = null,
    // sample 【,tag 1,tag 2,;tag 3,tag 4,】
    // The tags in the front are new tags, and the tags in the server are behind them.
    val tags: String? = null,
) {
    companion object {
        const val FLAG_UNREAD = 2
        const val FLAG_READ = 0
    }

    fun isUnread() = flag == FLAG_UNREAD

//    fun time(): String {
//        return DateUtil.format(publishedDate.orZero())
//    }
}