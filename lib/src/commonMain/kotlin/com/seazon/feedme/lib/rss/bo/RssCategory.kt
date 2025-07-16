package com.seazon.feedme.lib.rss.bo

import kotlinx.serialization.Serializable

@Serializable
open class RssCategory(
    var id: String? = null,
    var label: String? = null
) : Entity()