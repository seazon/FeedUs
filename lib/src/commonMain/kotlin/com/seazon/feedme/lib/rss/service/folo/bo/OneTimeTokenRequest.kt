package com.seazon.feedme.lib.rss.service.folo.bo

import kotlinx.serialization.Serializable

@Serializable
data class OneTimeTokenRequest(
    val token: String? = null,
)