package com.seazon.feedme.lib.rss.service.feedly.bo

import com.seazon.feedme.lib.rss.bo.Entity
import com.seazon.feedme.lib.rss.bo.RssStream
import kotlinx.serialization.Serializable

@Serializable
class FeedlyStream : Entity() {
    /**
     * Sample : user/37bdfef5-6c3e-4c33-a405-e40a7be3d9cf/category/global.all
     */
    val id: String? = null
    val updated: Long = 0
    var continuation: String? = null
    var items: List<FeedlyItem>? = null
    var ids: List<String>? = null

    fun convert(): RssStream {
        return RssStream(continuation, items?.convert().orEmpty(), ids.orEmpty())
    }

}