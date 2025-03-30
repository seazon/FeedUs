package com.seazon.feedme.lib.rss.service.gr.bo

import com.seazon.feedme.lib.rss.bo.Entity
import kotlinx.serialization.Serializable

@Serializable
data class GrTag(
    /**
     * Sample : user/37bdfef5-6c3e-4c33-a405-e40a7be3d9cf/tag/feedme
     */
    var id: String? = null,
    var type: String? = null, // folder / tag
    var sortid: String? = null,
) : Entity()
