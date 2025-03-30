package com.seazon.feedme.lib.rss.service.feedbin.bo

import com.seazon.feedme.lib.rss.bo.Entity
import com.seazon.feedme.lib.rss.bo.RssStream
import com.seazon.feedme.lib.rss.service.Static
import kotlinx.serialization.Serializable

@Serializable
data class FeedbinStream(
    var items: List<FeedbinItem>? = null,
    var ids: List<String>? = null,
) : Entity() {

    fun getContinuation(): String? {
        return null
    }

    fun getItemIds(): List<String>? {
        return ids
    }

    companion object {
        fun parse(json: String?): RssStream? {
            return Static.defaultJson.decodeFromString<FeedbinStream?>(json.orEmpty())?.convert()
        }
    }
}

fun FeedbinStream.convert(): RssStream {
    return RssStream(
        null,
        items?.map { it.convert() } ?: ArrayList(),
        ids.orEmpty()
    )
}
