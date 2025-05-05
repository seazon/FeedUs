package com.seazon.feedme.lib.rss.service.fever.bo

import kotlinx.serialization.Serializable

@Serializable
data class CommonResponse(
    val api_version: Int? = null,
    val auth: Int? = null,
    val last_refreshed_on_time: Long? = null,
)