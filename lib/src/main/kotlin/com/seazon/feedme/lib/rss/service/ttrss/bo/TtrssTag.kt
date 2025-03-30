package com.seazon.feedme.lib.rss.service.ttrss.bo

import com.seazon.feedme.lib.rss.bo.Entity
import com.seazon.feedme.lib.utils.toJson

data class TtrssTagList(
    var content: List<TtrssTag>? = null,
)

data class TtrssTag(
    var id: String? = null,
    var caption: String? = null,
) : Entity() {

    companion object {
        fun parseList(json: String?): List<TtrssTag>? {
            return toJson<TtrssTagList>(json.orEmpty()).content
        }
    }
}
