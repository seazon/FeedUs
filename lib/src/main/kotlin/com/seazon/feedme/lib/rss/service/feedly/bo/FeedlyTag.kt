package com.seazon.feedme.lib.rss.service.feedly.bo

import com.seazon.feedme.lib.rss.bo.RssTag
import com.seazon.feedme.lib.rss.service.JSONException
import kotlinx.serialization.Serializable
import java.lang.Exception

@Serializable
class FeedlyTag : RssTag() {
    /**
     * Sample : user/37bdfef5-6c3e-4c33-a405-e40a7be3d9cf/tag/feedme
     */
//    @Throws(JSONException::class)
//    fun parse(json: String?): FeedlyTag? {
//        try {
//            return Gson().fromJson<FeedlyTag?>(json, FeedlyTag::class.java)
//        } catch (e: Exception) {
//            throw wrapException(json, e)
//        }
//    }

//    @Throws(JsonSyntaxException::class)
//    fun parseList(json: String?): MutableList<FeedlyTag?>? {
//        try {
//            return Gson().fromJson<MutableList<FeedlyTag?>?>(json, object : TypeToken<MutableList<FeedlyTag?>?>() {
//            }.getType())
//        } catch (e: Exception) {
//            throw wrapException(json, e)
//        }
//    }
}
