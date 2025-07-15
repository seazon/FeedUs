package com.seazon.feedme.lib.rss.service.gr.api

import com.seazon.feedme.lib.network.HttpException
import com.seazon.feedme.lib.network.HttpMethod
import com.seazon.feedme.lib.network.HttpUtils
import com.seazon.feedme.lib.network.NameValuePair
import com.seazon.feedme.lib.network.urlEncode
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.rss.service.gr.GrConfig
import com.seazon.feedme.lib.rss.service.gr.GrConstants
import com.seazon.feedme.lib.rss.service.gr.bo.GrStream
import com.seazon.feedme.lib.rss.service.gr.bo.GrSubscriptions
import com.seazon.feedme.lib.rss.service.gr.bo.GrTags
import com.seazon.feedme.lib.rss.service.gr.bo.GrUnreadCounts
import com.seazon.feedme.lib.rss.service.gr.bo.GrUserInfo

class MainApi(token: RssToken, config: GrConfig, api: RssApi) : AuthedApi(token, config, api) {

    suspend fun getUnreadCounts(): GrUnreadCounts? {
        val parameters = listOf(
            NameValuePair("output", "json")
        )
        return execute(
            HttpMethod.GET,
            GrConstants.URL_MARKER_COUNTS,
            parameters,
            null,
            null
        ).convertBody()
    }

    suspend fun getSubscriptions(): GrSubscriptions? {
        val parameters = listOf(
            NameValuePair("output", "json")
        )
        return execute(
            HttpMethod.GET,
            GrConstants.URL_SUBSCRIPTION,
            parameters,
            null,
            null
        ).convertBody()
    }

    suspend fun quickadd(url: String?): String? {
        if (url.isNullOrEmpty()) {
            return null
        }

        val parameters = listOf(
            NameValuePair("quickadd", url),
            NameValuePair("output", "json"),
        )
        val headers = mutableMapOf(
            HttpUtils.HTTP_HEADERS_CONTENT_TYPE to HttpUtils.HTTP_HEADERS_CONTENT_TYPE_WWW_FORM
        )
        return execute(
            HttpMethod.POST, GrConstants.URL_SUBSCRIPTION_QUICKADD, null, headers,
            HttpUtils.format(parameters, HttpUtils.DEFAULT_CHARSET), false
        ).body
    }

    /**
     * @param action            Action. Can be edit, subscribe, or unsubscribe.
     * @param streamId          Stream id in the form feed/feed_url
     * @param subscriptionTitle Subscription title. Omit this parameter to keep the title
     * unchanged
     * @param a                 Add subscription to user/-/label/folder1.
     * @param r                 Remove subscription from user/-/label/folder1.
     * @return
     * @throws HttpException
     */
    suspend fun editSubscription(
        action: String,
        streamId: String,
        subscriptionTitle: String?,
        a: Array<String>?,
        r: Array<String>?
    ): String? {
        if (action.isNullOrEmpty()) {
            return null
        }
        if (streamId.isNullOrEmpty()) {
            return null
        }
        val parameters = mutableListOf(
            NameValuePair("ac", action),
            NameValuePair("s", streamId),
        ).apply {
            if (subscriptionTitle != null) {
                add(NameValuePair("t", subscriptionTitle))
            }
            if (a != null) {
                for (i in a.indices) {
                    if (!a[i].isNullOrEmpty()) {
                        add(NameValuePair("a", "user/-/label/" + a[i]))
                    }
                }
            }
            if (r != null) {
                for (i in r.indices) {
                    if (!r[i].isNullOrEmpty()) {
                        add(NameValuePair("r", "user/-/label/" + r[i]))
                    }
                }
            }
        }
        val headers = mutableMapOf(
            HttpUtils.HTTP_HEADERS_CONTENT_TYPE to HttpUtils.HTTP_HEADERS_CONTENT_TYPE_WWW_FORM
        )
        return execute(
            HttpMethod.POST, GrConstants.URL_SUBSCRIPTION_EDIT, null, headers,
            HttpUtils.format(parameters, HttpUtils.DEFAULT_CHARSET), false
        ).body
    }

    suspend fun getTags(): GrTags? {
        val parameters = listOf(
            NameValuePair("output", "json")
        )
        return execute(HttpMethod.GET, GrConstants.URL_TAG, parameters, null, null).convertBody()
    }

    suspend fun getContents(
        feedId: String?,
        count: Int,
        unreadOnly: Boolean,
        ot: String?,
        continuation: String?
    ): GrStream? {
        var count = count
        if (count <= 0) {
            count = 20
        }
        if (count > 1000) {
            count = 1000
        }
        val parameters: MutableList<NameValuePair> = ArrayList<NameValuePair>()
        parameters.add(NameValuePair("output", "json"))
        parameters.add(NameValuePair("n", count.toString()))
        if (unreadOnly) {
            parameters.add(NameValuePair("xt", "user/-/state/com.google/read"))
        }
        parameters.add(NameValuePair("ot", ot.orEmpty()))
        if (continuation != null) {
            parameters.add(NameValuePair("c", continuation))
        }
        if (feedId != null) {
            parameters.add(NameValuePair("s", feedId))
        }

        return execute(
            HttpMethod.GET,
            GrConstants.URL_STREAM_CONTENTS,
            parameters,
            null,
            null
        ).convertBody()
    }

    suspend fun getContents2(
        feedId: String?,
        count: Int,
        unreadOnly: Boolean,
        ot: String?,
        continuation: String?
    ): GrStream? {
        var feedId = feedId
        var count = count
        if (count <= 0) {
            count = 20
        }
        if (count > 1000) {
            count = 1000
        }
        val parameters: MutableList<NameValuePair> = ArrayList<NameValuePair>()
        parameters.add(NameValuePair("output", "json"))
        parameters.add(NameValuePair("n", count.toString()))
        if (unreadOnly) {
            parameters.add(NameValuePair("xt", "user/-/state/com.google/read"))
        }
        parameters.add(NameValuePair("ot", ot.orEmpty()))
        if (continuation != null) {
            parameters.add(NameValuePair("c", continuation))
        }
        feedId = getStreamId(feedId)
        if (!feedId.isNullOrEmpty()) {
            feedId = "/$feedId"
        }

        return execute(
            HttpMethod.GET,
            GrConstants.URL_STREAM_CONTENTS + feedId,
            parameters,
            null,
            null
        ).convertBody()
    }

    suspend fun getContentsIds(
        streamId: String?,
        count: Int,
        unreadOnly: Boolean,
        continuation: String?
    ): GrStream? {
        var count = count
        if (count <= 0) {
            count = 20
        }
        if (count > 1000) {
            count = 1000
        }
        val parameters: MutableList<NameValuePair> = ArrayList<NameValuePair>()
        parameters.add(NameValuePair("output", "json"))
        parameters.add(NameValuePair("n", count.toString()))
        if (unreadOnly) {
            parameters.add(NameValuePair("xt", "user/-/state/com.google/read"))
        }
        parameters.add(NameValuePair("ot", "0"))
        if (continuation != null) {
            parameters.add(NameValuePair("c", continuation))
        }
        if (streamId != null) {
            parameters.add(NameValuePair("s", streamId))
        }

        return execute(
            HttpMethod.GET,
            GrConstants.URL_STREAM_IDS,
            parameters,
            null,
            null
        ).convertBody()
    }

    suspend fun getContentsByIds(entryIds: Array<String>?): GrStream? {
        if (entryIds.isNullOrEmpty()) {
            return null
        }

        val parameters: MutableList<NameValuePair> = ArrayList<NameValuePair>()
        parameters.add(NameValuePair("output", "json"))

        val parameters2: MutableList<NameValuePair> = ArrayList<NameValuePair>()
        for (i in entryIds.indices) {
            if (entryIds[i].isNullOrEmpty()) {
                continue
            }

            parameters2.add(NameValuePair("i", entryIds[i]))
        }
        val headers = mutableMapOf(
            HttpUtils.HTTP_HEADERS_CONTENT_TYPE to HttpUtils.HTTP_HEADERS_CONTENT_TYPE_WWW_FORM
        )
        return execute(
            HttpMethod.POST, GrConstants.URL_STREAM_ITEMS_CONTENTS, parameters, headers,
            HttpUtils.format(parameters2, HttpUtils.DEFAULT_CHARSET), false
        ).convertBody()
    }

    private fun getStreamId(streamId: String?): String? {
        if (streamId.isNullOrEmpty()) {
            return ""
        }

        return streamId.urlEncode()
    }

    suspend fun editTag(action: String, tag: String, entryIds: Array<String>?): String? {
        if (tag.isNullOrEmpty() || entryIds.isNullOrEmpty()) {
            return null
        }

        val parameters = mutableListOf(NameValuePair(action, tag))
        for (i in entryIds.indices) {
            if (entryIds[i].isNullOrEmpty()) {
                continue
            }

            parameters.add(NameValuePair("i", entryIds[i]))
        }

        val headers = mutableMapOf(
            HttpUtils.HTTP_HEADERS_CONTENT_TYPE to HttpUtils.HTTP_HEADERS_CONTENT_TYPE_WWW_FORM
        )
        return execute(
            httpMethod = HttpMethod.POST,
            url = GrConstants.URL_TAG_EDIT,
            params = null,
            headers = headers,
            body = HttpUtils.format(parameters, HttpUtils.DEFAULT_CHARSET),
            json = false,
        ).body
    }

    suspend fun getUserInfo(): GrUserInfo? {
        val parameters: MutableList<NameValuePair> = ArrayList<NameValuePair>()
        parameters.add(NameValuePair("output", "json"))
        return execute(
            HttpMethod.GET,
            GrConstants.URL_USER_INFO,
            parameters,
            null,
            null
        ).convertBody()
    }
}
