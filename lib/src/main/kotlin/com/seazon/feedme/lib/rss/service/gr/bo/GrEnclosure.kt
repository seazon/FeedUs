package com.seazon.feedme.lib.rss.service.gr.bo

import com.seazon.feedme.lib.rss.bo.Entity
import kotlinx.serialization.Serializable

@Serializable
data class GrEnclosure(
    var href: String? = null,
    var type: String? = null,
    var length: Long = 0,
) : Entity()