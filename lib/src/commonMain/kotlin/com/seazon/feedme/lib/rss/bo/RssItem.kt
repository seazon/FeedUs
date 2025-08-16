package com.seazon.feedme.lib.rss.bo

import kotlinx.serialization.Serializable

@Serializable
open class RssItem(
    var id: String? = null,
    var fid: String? = null, // feed/[feedurl]
    var title: String? = null,
    var link: String? = null,
    var author: String? = null,
    var publisheddate: Long? = null,
    var updateddate: Long? = null,
    var description: String? = null,
    var tags: String? = null, // sample 【,tag 1,tag 2,;tag 3,tag 4,】 // The tags in the front are new tags, and the tags in the server are behind them.
    var visual: String? = null,
    var podcastUrl: String? = null,
    var podcastSize: Int = 0, // byte, supports up to 2G files, enough
    var feed: RssFeed? = RssFeed(),
    var enclosure: List<RssEnclosure>? = null,
    var isUnread: Boolean = false,
    var isStar: Boolean? = null,
    var since: String? = null
) : Entity() {

    open fun addTag(tag: String?) {
        if (tag.isNullOrBlank()) return
        if (tags.isNullOrBlank()) {
            tags = ",$tag,"
        } else {
            if (!tags!!.contains(",$tag,")) tags += "$tag,"
        }
    }

}