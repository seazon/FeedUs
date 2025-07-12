package com.seazon.feedme.lib.rss.service.localrss.bo

import com.seazon.feedme.lib.rss.bo.Entity
import com.seazon.feedme.lib.rss.bo.RssFeed
import com.seazon.feedme.lib.rss.bo.RssTag
import com.seazon.feedme.lib.utils.toJson
import kotlinx.serialization.Serializable

@Serializable
class LocalRssSubscription : Entity() {
    var id: String? = null
    var title: String? = null
    var feedUrl: String? = null
    var url: String? = null
    var favicon: String? = null
    var categories: MutableList<LocalRssCategory> = mutableListOf()
    var lastClawTime: Long = 0
    var spiderPackage: String? = null

    fun convertToRssFeed(): RssFeed {
        return RssFeed().also {
            it.id = id
            it.title = title
            it.url = url
            it.feedUrl = feedUrl
            it.categories = this.categories.map { lc ->
                RssTag(lc.id, lc.label)
            }
            it.favicon = favicon
        }
    }

    companion object {
        fun parseList(json: String?): List<LocalRssSubscription> {
            return toJson<List<LocalRssSubscription>>(json.orEmpty())
        }
    }
}