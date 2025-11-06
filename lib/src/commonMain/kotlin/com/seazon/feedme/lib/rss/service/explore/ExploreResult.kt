package com.seazon.feedme.lib.rss.service.explore

import com.seazon.feedme.lib.rss.service.feedly.bo.FeedlySearchResponse
import com.seazon.feedme.lib.rss.service.folo.bo.DiscoverFeed
import com.seazon.feedme.lib.rss.service.folo.bo.FoloListData
import com.seazon.feedme.lib.utils.DateUtil
import kotlinx.serialization.Serializable

@Serializable
data class ExploreResult(
    val feedId: String? = null,
    val updated: Long? = null,
    val title: String? = null,
    val subscribers: Int? = null,
    val feedUrl: String? = null,
    val webUrl: String? = null,
    val description: String? = null,
    val iconUrl: String? = null,
) {
    companion object {

        fun parseFeedlySearch(r: FeedlySearchResponse): List<ExploreResult> {
            return r.results?.map {
                ExploreResult(
                    it.feedId,
                    it.updated,
                    it.title,
                    it.subscribers,
                    it.feedUrl,
                    it.website,
                    it.description,
                    it.iconUrl,
                )
            }.orEmpty()
        }

        fun parseFoloSearch(r: FoloListData<DiscoverFeed>?): List<ExploreResult> {
            return r?.data?.map {
                ExploreResult(
                    it.feed?.id,
                    DateUtil.isoStringToTimestamp(it.entries?.firstOrNull()?.publishedAt),
                    it.feed?.title,
                    it.subscriptionCount,
                    it.feed?.url,
                    it.feed?.siteUrl,
                    it.feed?.description,
                    it.feed?.image,
                )
            }.orEmpty()
        }
    }
}
