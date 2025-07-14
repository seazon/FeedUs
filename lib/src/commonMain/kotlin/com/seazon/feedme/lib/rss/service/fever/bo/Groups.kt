package com.seazon.feedme.lib.rss.service.fever.bo

import com.seazon.feedme.lib.rss.bo.Entity
import com.seazon.feedme.lib.utils.toJson
import kotlinx.serialization.Serializable

@Serializable
data class Groups(
    val groups: List<Group>? = null,
    val feeds_groups: List<FeedGroup>? = null,
) {
    companion object {
        fun parse(json: String?): List<Group> {
            if (json.isNullOrBlank()) {
                return emptyList()
            }
            return toJson<Groups>(json).groups.orEmpty()
        }
    }
}

@Serializable
data class Group(
    val id: Int? = null,
    val title: String? = null,
) : Entity() {
    fun getId(): String {
        return id.toString()
    }

    fun getLabel(): String {
        return title.orEmpty()
    }

    fun getLabelFromId(): String {
        return title.orEmpty()
    }
}

@Serializable
data class FeedGroup(
    val group_id: Int? = null,
    val feed_ids: String? = null,
)