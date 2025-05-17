package com.seazon.feedme.lib.rss.service.ttrss.bo

import com.seazon.feedme.lib.rss.bo.Entity
import com.seazon.feedme.lib.rss.bo.RssFeed
import com.seazon.feedme.lib.rss.bo.RssTag
import com.seazon.feedme.lib.rss.service.ttrss.TtrssApi
import com.seazon.feedme.lib.utils.toJson
import kotlinx.serialization.Serializable

@Serializable
data class TtrssSubscription(
    private var id: String? = null, // ttrss的类别和订阅源id都是int数值，无法区分，为了区分，订阅源id加上feed/前缀
    var title: String? = null,
    var updated: Long = 0,
    var added: Long = 0,
    var categories: List<TtrssCategory>? = null,
    var website: String? = null,
    var sortid: String? = null,
) : Entity() {

    //    @Override
    //    public Feed convert(Object... objects) {
    //        Feed feed = new Feed();
    //        feed.setId(id);
    //        feed.setTitle(title);
    //        feed.setUrl(website);
    //        feed.setFeedUrl(website);
    //        feed.setFavicon(null);
    //        for (RssCategory category : categories) {
    //            feed.addCategory(category.getId());
    //        }
    //        return feed;
    //    }
    fun getFavicon(): String? {
        return null
    }

    companion object {
        const val ID_PREFIX: String = "feed/"

        fun parse(json: String?, map: Map<String, RssTag>): List<RssFeed> {
            val list = toJson<TtrssFeedList>(json.orEmpty())
            return list.content?.map {
                val c = RssFeed()
                c.id = TtrssApi.wrapFeedId(it.id.orEmpty())
                c.title = it.title
                c.categories = listOfNotNull(map[TtrssApi.wrapCategoryId(it.cat_id.orEmpty())])
                c.url = it.feed_url
                c.feedUrl = c.url
                c.favicon = null
                c
            }.orEmpty()
        }
    }
}
