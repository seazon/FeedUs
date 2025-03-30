package com.seazon.feedme.lib.rss.service.gr.bo

import com.seazon.feedme.lib.rss.bo.Entity
import kotlinx.serialization.Serializable

@Serializable
class GrAlternate(
    var href: String? = null,
    var type: String? = null,
) : Entity()
