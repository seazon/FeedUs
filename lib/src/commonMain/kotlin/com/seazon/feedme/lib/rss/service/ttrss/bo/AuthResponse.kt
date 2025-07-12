package com.seazon.feedme.lib.rss.service.ttrss.bo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//{"seq":0,"status":0,"content":{"session_id":"rjpcv2qbk68l1d2s8chie5g3u4","api_level":14}}
@Serializable
data class AuthResponse(
    val seq: Int = 0,
    val status: Int = 0,
    val content: AuthContent? = null,
)

@Serializable
data class AuthContent(
    @SerialName("session_id")
    val sessionId: String? = null,
    @SerialName("api_level")
    val apiLevel: Int = 0,
    val error: String? = null,
)