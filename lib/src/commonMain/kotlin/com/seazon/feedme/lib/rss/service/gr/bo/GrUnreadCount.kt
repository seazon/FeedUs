package com.seazon.feedme.lib.rss.service.gr.bo

import com.seazon.feedme.lib.utils.LongAsStringSerializer
import kotlinx.serialization.Serializable

@Serializable
class GrUnreadCount {
    /**
     * Sample : user/1005921515/label/Earthquakes
     * Sample : feed/http://feeds.feedburner.com/JIRABlog
     * Sample : feed/http://bash.org.pl/rss
     */
    var id: String? = null
    var count: Int = 0
    /**
     * the value is long or string
     */
    @Serializable(with = LongAsStringSerializer::class)
    var newestItemTimestampUsec: String? = null
}
