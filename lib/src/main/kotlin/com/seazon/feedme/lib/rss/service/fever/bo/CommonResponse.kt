package com.seazon.feedme.lib.rss.service.fever.bo

import kotlinx.serialization.Serializable

@Serializable
data class CommonResponse(
    val api_version: Int,
    val auth: Int,
    val last_refreshed_on_time: Long
)