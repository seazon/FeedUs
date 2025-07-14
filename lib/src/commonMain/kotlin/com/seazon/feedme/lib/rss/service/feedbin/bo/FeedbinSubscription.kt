package com.seazon.feedme.lib.rss.service.feedbin.bo

import com.seazon.feedme.lib.rss.bo.Entity
import com.seazon.feedme.lib.rss.bo.RssFeed
import com.seazon.feedme.lib.rss.bo.RssTag
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//    {
//        "id": 525,
//        "created_at": "2013-03-12T11:30:25.209432Z",
//        "feed_id": 47,
//        "title": "Daring Fireball",
//        "feed_url": "http://daringfireball.net/index.xml",
//        "site_url": "http://daringfireball.net/"
//    }
@Serializable
data class FeedbinSubscription(
    @SerialName("id")
    var realId: Int = 0, // 用户订阅的feed的记录的id
    var feed_id: Int = 0, // feed源表的id
    var title: String? = null,
    var created_at: String? = null,
    var feed_url: String? = null, // feed地址
    var site_url: String? = null, // 网站地址
    var categories: MutableList<FeedbinCategory>? = null,
) : Entity() {

    fun getId(): String {
        // feed删除需要订阅id，所以要用id，而不是feed_id，但是获取item时里面只有feed_id，所以删除feed不能使用
        return feed_id.toString()
        //        return String.valueOf(id);
    }

    fun getUrl(): String? {
        return feed_url
    }

    fun getFavicon(): String? {
        return null
    }

    fun addCategory(category: FeedbinCategory) {
        if (categories == null) {
            categories = mutableListOf()
        }
        categories!!.add(category)
    }
}

fun Collection<FeedbinSubscription>.convert(): List<RssFeed> {
    return map {
        RssFeed(
            it.getId(),
            it.title,
            it.site_url ?: it.feed_url,
            it.feed_url,
            it.categories?.map { category ->
                RssTag(category.id.toString(), category.name)
            },
            null
        )
    }
}
