package com.seazon.feedme.lib.rss.service.fever.bo

import kotlinx.serialization.Serializable

@Serializable
data class Feed(
    val id: Int,
    val favicon_id: Int,
    val title: String,
    val url: String,
    val site_url: String,
    val is_spark: Int,
    val last_updated_on_time: Long
)