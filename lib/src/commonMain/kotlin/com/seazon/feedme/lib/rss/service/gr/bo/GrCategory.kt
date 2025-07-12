package com.seazon.feedme.lib.rss.service.gr.bo

import com.seazon.feedme.lib.rss.bo.Entity
import kotlinx.serialization.Serializable

@Serializable
data class GrCategory(
    /**
     * Sample : user/37bdfef5-6c3e-4c33-a405-e40a7be3d9cf/category/global.must
     */
    var id: String? = null,
    var label: String? = null,
) : Entity() {

    fun getLabelFromId(): String {
        return id!!.substring(id!!.lastIndexOf("/") + 1)
    }

}
