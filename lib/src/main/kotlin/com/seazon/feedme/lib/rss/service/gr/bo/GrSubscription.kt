package com.seazon.feedme.lib.rss.service.gr.bo

import com.seazon.feedme.lib.rss.bo.Entity
import kotlinx.serialization.Serializable

@Serializable
class GrSubscription(
    /**
     * Sample : feed/http://daichuanqing.com/index.php/feed
     */
    var id: String? = null,
    var title: String? = null,
    var firstitemmsec: Long = 0,
    var categories: MutableList<GrCategory>? = null,
    var url: String? = null, // feed地址 bazqux不提供
    var htmlUrl: String? = null, // 网站地址
    var sortid: String? = null,
    var iconUrl: String? = null, //favicon图片地址 bazqux不提供
) : Entity() {
    val favicon: String?
        get() = iconUrl
}
