package com.seazon.feedme.lib.rss.service.gr.bo

import com.seazon.feedme.lib.rss.bo.Entity
import kotlinx.serialization.Serializable

@Serializable
class GrOrigin(
    var streamId: String? = null,
    var htmlUrl: String? = null,
    var title: String? = null,
) : Entity()
