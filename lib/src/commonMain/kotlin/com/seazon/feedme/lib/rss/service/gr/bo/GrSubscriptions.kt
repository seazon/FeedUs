package com.seazon.feedme.lib.rss.service.gr.bo

import com.seazon.feedme.lib.rss.bo.Entity
import com.seazon.feedme.lib.rss.bo.RssCategory
import com.seazon.feedme.lib.rss.bo.RssFeed
import com.seazon.feedme.lib.rss.service.gr.GrConstants
import kotlinx.serialization.Serializable

@Serializable
data class GrSubscriptions(
    var subscriptions: MutableList<GrSubscription>? = null
) : Entity()

fun Collection<GrSubscription>?.convert2(): List<RssFeed>? {
    return this?.map {
        RssFeed(
            it.id,
            it.title,
            it.htmlUrl ?: it.url,
            it.url,
            it.categories?.map { category ->
                RssCategory(category.id, category.label)
            }?.filter {
                !GrConstants.isIgnoredTag(it.label.orEmpty()) && !GrConstants.isIgnoredForTag(it.label.orEmpty())
            },
            it.favicon
        )
    }
}
