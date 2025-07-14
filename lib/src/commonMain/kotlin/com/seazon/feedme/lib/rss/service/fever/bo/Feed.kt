package com.seazon.feedme.lib.rss.service.fever.bo

import kotlinx.serialization.Serializable

@Serializable
data class Feed(
    val id: Int? = null,
    val favicon_id: Int? = null,
    val title: String? = null,
    val url: String? = null,
    val site_url: String? = null,
    val is_spark: Int? = null,
    val last_updated_on_time: Long? = null,
)