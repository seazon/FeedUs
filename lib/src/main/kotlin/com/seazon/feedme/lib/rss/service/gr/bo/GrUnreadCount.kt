package com.seazon.feedme.lib.rss.service.gr.bo

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
    var newestItemTimestampUsec: String? = null
}
