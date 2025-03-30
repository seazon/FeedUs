package com.seazon.feedme.lib.rss.service.feedly.bo

import com.seazon.feedme.lib.rss.bo.Entity
import com.seazon.feedme.lib.rss.bo.RssFeed
import com.seazon.feedme.lib.rss.bo.RssTag
import com.seazon.feedme.lib.rss.service.feedly.FeedlyConstants
import kotlinx.serialization.Serializable

@Serializable
class FeedlySubscription : Entity() {
    /**
     * Sample : feed/http://daichuanqing.com/index.php/feed
     */
    var id: String = ""
    var title: String? = null
    var updated: Long = 0
    var categories: List<FeedlyCategory>? = null
    var website: String? = null
    var sortid: String? = null
    var visualUrl: String? = null

    fun convert(): RssFeed {
        val feed = RssFeed()
        feed.id = id
        feed.title = title
        feed.url = website
        feed.feedUrl = id.substring(5)
        feed.favicon = visualUrl
        feed.categories = categories?.map {
            RssTag(it.id, it.label)
        }?.filter {
            !FeedlyConstants.isIgnoredTag(it.label) && !FeedlyConstants.isIgnoredForTag(it.label)
        } ?: ArrayList()
        return feed
    }

//    companion object {
//        @JvmStatic
//        @Throws(JSONException::class)
//        fun parseList(json: String): List<RssFeed> {
//            return try {
//                val subscriptions: List<FeedlySubscription> = Gson().fromJson(json, object : TypeToken<List<FeedlySubscription>>() {}.type)
//                subscriptions.convert()
//            } catch (e: Exception) {
//                throw wrapException(json, e)
//            }
//        }
//    }
}

fun Collection<FeedlySubscription>.convert(): List<RssFeed> = map {
    it.convert()
}

fun Collection<FeedlyTag>.convert2(): List<RssTag> = map {
    RssTag(
        it.id,
        it.label ?: (it.id?.substring(it.id?.lastIndexOf("/") ?: 0 + 1))
    )
}.filter {
    !FeedlyConstants.isIgnoredTag(it.label) && !FeedlyConstants.isIgnoredForTag(it.label)
}

//data class SubscriptionUpdateRequest(
//    val id: String,
//    val title: String,
//    val categories: List<Category>?,
//)

//data class Category(
//    val id: String,
//    val label: String,
//)
