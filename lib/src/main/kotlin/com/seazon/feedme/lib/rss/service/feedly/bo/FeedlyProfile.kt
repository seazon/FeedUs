package com.seazon.feedme.lib.rss.service.feedly.bo

import kotlinx.serialization.Serializable

@Serializable
data class FeedlyProfile(
    val id: String?,
    val email: String?,
    val picture: String?,
)