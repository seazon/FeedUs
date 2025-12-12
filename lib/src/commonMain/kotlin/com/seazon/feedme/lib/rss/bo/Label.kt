package com.seazon.feedme.lib.rss.bo

import kotlinx.serialization.Serializable

@Serializable
data class Label(
    var id: String,
    var label: String? = null
)