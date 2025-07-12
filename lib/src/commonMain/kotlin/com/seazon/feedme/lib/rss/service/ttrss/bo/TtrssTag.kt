package com.seazon.feedme.lib.rss.service.ttrss.bo

import com.seazon.feedme.lib.rss.bo.Entity
import com.seazon.feedme.lib.utils.IntAsStringSerializer
import com.seazon.feedme.lib.utils.toJson
import kotlinx.serialization.Serializable

@Serializable
data class TtrssTagList(
    var content: List<TtrssTag>? = null,
)

@Serializable
data class TtrssTag(
    @Serializable(with = IntAsStringSerializer::class)
    var id: String? = null,
    var caption: String? = null,
) : Entity() {

    companion object {
        fun parseList(json: String?): List<TtrssTag>? {
            return toJson<TtrssTagList>(json.orEmpty()).content
        }
    }
}
