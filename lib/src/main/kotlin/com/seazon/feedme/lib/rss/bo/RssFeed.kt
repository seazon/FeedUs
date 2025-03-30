package com.seazon.feedme.lib.rss.bo

import kotlinx.serialization.Serializable

@Serializable
data class RssFeed(
    var id: String? = null, // feed/[feedurl]
    var title: String? = null,
    var url: String? = null, // website url
    var feedUrl: String? = null, // rss url
    var categories: List<RssTag>? = null,
    var favicon: String? = null
) : Entity()