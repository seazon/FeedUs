package com.seazon.feedme.lib.rss.service.ttrss.bo

import com.seazon.feedme.lib.rss.bo.Entity
import kotlinx.serialization.Serializable

@Serializable
data class TtrssAttachments(
    var content_url: String? = null,
    var content_type: String? = null,
) : Entity()
