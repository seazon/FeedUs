package com.seazon.feedme.lib.rss.service.feedly.bo

import kotlinx.serialization.Serializable

@Serializable
data class FeedlyProfile(
    val id: String? = null,
    val email: String? = null,
    val picture: String? = null,
)