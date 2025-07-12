package com.seazon.feedme.lib.rss.service.gr.bo

import kotlinx.serialization.Serializable

@Serializable
data class GrItemRefs(
    var id: String? = null,
    var directStreamIds: MutableList<String>? = null,
    var timestampUsec: Long = 0,
)