package com.seazon.feedme.lib.rss.bo

import kotlinx.serialization.Serializable

@Serializable
open class HttpProxy(
    var host: String?,
    var port: Int = 0,
) : Entity()