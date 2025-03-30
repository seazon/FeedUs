package com.seazon.feedme.lib.rss.service.fever.bo

import com.seazon.feedme.lib.rss.bo.Entity
import com.seazon.feedme.lib.rss.service.Static
import kotlinx.serialization.Serializable

@Serializable
data class Groups(
    val groups: List<Group>,
    val feeds_groups: List<FeedGroup>
) {
    companion object {
        fun parse(json: String): List<Group> {
            return Static.defaultJson.decodeFromString<Groups>(json).groups
        }
    }
}

@Serializable
data class Group(
    val id: Int,
    val title: String
) : Entity() {
    fun getId(): String {
        return id.toString()
    }

    fun getLabel(): String {
        return title
    }

    fun getLabelFromId(): String {
        return title
    }
}

@Serializable
data class FeedGroup(
    val group_id: Int,
    val feed_ids: String
)