package com.seazon.feedme.lib.rss.service.gr.bo

import com.seazon.feedme.lib.rss.bo.Entity
import com.seazon.feedme.lib.rss.bo.RssStream
import kotlinx.serialization.Serializable

@Serializable
data class GrStream(
    /**
     * Sample : user/37bdfef5-6c3e-4c33-a405-e40a7be3d9cf/category/global.all
     */
    var id: String? = null,
    var updated: Long = 0,
    var continuation: String? = null,
    var items: MutableList<GrItem>? = null,
    var itemRefs: MutableList<GrItemRefs>? = null,
) : Entity() {

    /**
     * https://code.google.com/p/google-reader-api/wiki/ItemId
     *
     *
     * tag:google.com,2005:reader/item/5d0cfa30041d4348 6705009029382226760
     *
     *
     * tag:google.com,2005:reader/item/024025978b5e50d2 162170919393841362 Long
     * form needs 0-padding
     *
     *
     * tag:google.com,2005:reader/item/fb115bd6d34a8e9f -355401917359550817
     * Short form ends up being negative
     *
     * @param shortFormId
     * @return
     */
    fun convertToLongForm(shortFormId: String): String {
        var hex: String?
        try {
            hex = shortFormId.toLong().toString(16)
            val s = 16 - hex.length
            for (i in s downTo 1) {
                hex = "0$hex"
            }
        } catch (e: Exception) {
            hex = shortFormId
        }
        return "tag:google.com,2005:reader/item/$hex"
    }
}

fun GrStream.convert(): RssStream {
    return RssStream(
        continuation = continuation,
        items = items?.map { it.convert() }.orEmpty(),
        ids = itemRefs?.map {
            convertToLongForm(it.id.orEmpty())
        }.orEmpty()
    )
}
