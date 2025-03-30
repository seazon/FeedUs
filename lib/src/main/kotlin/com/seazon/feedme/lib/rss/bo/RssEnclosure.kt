package com.seazon.feedme.lib.rss.bo

import kotlinx.serialization.Serializable

@Serializable
open class RssEnclosure(
    var href: String? = null,
    val type: String? = null,
    val length: Long = 0,
) : Entity()