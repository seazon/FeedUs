package com.seazon.feedme.lib.rss.bo

import kotlinx.serialization.Serializable

@Serializable
data class Oauth2Response(
    val id: String? = null,
    val refresh_token: String? = null,
    val access_token: String? = null,
    val expires_in: Long? = null,
)