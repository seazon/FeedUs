package com.seazon.feedme.lib.rss.service.feedly.bo

import com.seazon.feedme.lib.rss.bo.Entity
import kotlinx.serialization.Serializable

@Serializable
data class FeedlyEnclosure(
    var href: String? = null,
    var type: String? = null,
    var length: Long = 0,
) : Entity()
