package com.seazon.feedme.lib.rss.service.gr.bo

import kotlinx.serialization.Serializable

@Serializable
data class GrQuickAdd(
    /**
     * Successful response:
     *
     *
     * {"query":"blog.theoldreader.com","numResults":1,"streamId":
     * "feed/00157a17b192950b65be3791"}
     *
     *
     * Failed response:
     *
     *
     * {"query":"blog.theoldreader.com","numResults":0,"error":
     * "Feed was not added. You already have 129 subscriptions, your limit is 100. You should upgrade to premium."
     * } Updating
     */
    var query: String? = null,
    var numResults: Int = 0,
    var streamId: String? = null,
    var error: String? = null,
)
