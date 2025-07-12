package com.seazon.feedme.lib.rss.service.feedbin.bo

import com.seazon.feedme.lib.rss.bo.Entity
import kotlinx.serialization.Serializable

@Serializable
data class FeedbinCategory(
    var id: Int = 0,
    var feed_id: Int = 0,
    var name: String? = null,
) : Entity() {

    fun getLabel(): String? {
        return name
    }

    fun getLabelFromId(): String? {
        return name
    }
}
