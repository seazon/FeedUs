package com.seazon.feedme.lib.rss.service.ttrss.bo

import com.seazon.feedme.lib.rss.bo.Entity

data class TtrssAttachments(
    var content_url: String? = null,
    var content_type: String? = null,
) : Entity()
