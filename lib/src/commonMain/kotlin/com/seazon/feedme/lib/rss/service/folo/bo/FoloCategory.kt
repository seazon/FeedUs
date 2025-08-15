package com.seazon.feedme.lib.rss.service.folo.bo

import kotlinx.serialization.Serializable

@Serializable
data class FoloCategory(
    val category: String? = null,
    val title: String? = null,
)