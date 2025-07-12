package com.seazon.feedme.lib.rss.service.feedly.bo

import com.seazon.feedme.lib.rss.bo.Entity
import kotlinx.serialization.Serializable

@Serializable
data class FeedlyOrigin(
    var streamId: String? = null,
    var htmlUrl: String? = null,
    var title: String? = null,
) : Entity()