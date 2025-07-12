package com.seazon.feedme.lib.rss.service.localrss.bo

import com.seazon.feedme.lib.rss.bo.Entity
import kotlinx.serialization.Serializable

@Serializable
data class LocalRssCategory(
    var id: String? = null,
    var label: String? = null,

    ) : Entity() {

    fun getLabelFromId(): String? {
        return id
    }
}
