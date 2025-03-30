package com.seazon.feedme.lib.rss.bo

import kotlinx.serialization.Serializable

@Serializable
open class RssStream(
    var continuation: String? = null,
    var items: List<RssItem> = emptyList(),
    var ids: List<String> = emptyList()
) : Entity()