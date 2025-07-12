package com.seazon.feedme.lib.rss.service.gr.bo

import kotlinx.serialization.Serializable

@Serializable
data class GrUserInfo(
    val userId: String? = null,
    val userEmail: String? = null,
)