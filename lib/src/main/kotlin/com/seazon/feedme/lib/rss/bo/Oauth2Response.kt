package com.seazon.feedme.lib.rss.bo

import kotlinx.serialization.Serializable

@Serializable
data class Oauth2Response(
    val id: String?,
    val refresh_token: String?,
    val access_token: String?,
    val expires_in: Long?,
)