package com.seazon.feedme.lib.utils

import com.seazon.feedme.lib.rss.bo.Feed
import java.net.URI
import java.net.URISyntaxException

object FaviconUtil {

    private const val SIZE_DP = 128

    @JvmStatic
    fun loadFaviconAndSave(feed: Feed): Boolean {
        val imageUrl: String? =
//            getFeedImage(core.httpManager, feed.feedUrl)?.let {
//            LogUtils.debug("get favicon from rss xml, url:$it")
//            it
//        } ?: run {
            if (feed.favicon.isNullOrEmpty()) {
                getStandardFavicon(feed.feedUrl.orEmpty())?.let {
                    LogUtils.debug("get favicon from favicon, url:$it")
                    it
                }
            } else {
                LogUtils.debug("get favicon from rss server, url:${feed.favicon}")
                feed.favicon
            }
//        }

//        if (!Helper.isBlank(imageUrl)) {
//            if (getFaviconByUrl(core, imageUrl!!, feed.id) && checkFavicon(core, feed.id)) {
//                return true
//            }
//        }
        return false
    }

    @JvmStatic
    fun loadFaviconAndSave(id: String, feedUrl: String, favicon: String?): Boolean {
        val imageUrl: String? = getFeedImage(feedUrl)?.let {
            LogUtils.debug("get favicon from rss xml, url:$it")
            it
        } ?: run {
            if (favicon.isNullOrEmpty()) {
                getStandardFavicon(feedUrl)?.let {
                    LogUtils.debug("get favicon from favicon, url:$it")
                    it
                }
            } else {
                LogUtils.debug("get favicon from rss server, url:${favicon}")
                favicon
            }
        }

//        if (!Helper.isBlank(imageUrl)) {
//            if (getFaviconByUrl(core, imageUrl!!, id) && checkFavicon(core, id)) {
//                return true
//            }
//        }
        return false
    }

    @JvmStatic
    private fun getStandardFavicon(feedUrl: String): String? {
        return if (feedUrl.isNullOrEmpty()) {
            null
        } else try {
            val uri = URI(feedUrl)
            val url = uri.scheme + "://" + uri.authority + "/favicon.ico"
            url
        } catch (e: URISyntaxException) {
            null
        }
    }

//    @JvmStatic
//    private fun getFaviconByUrl(core: Core, faviconUrl: String, feedId: String): Boolean {
//        if (Core.PATH_FAVICONS == null) {
//            // 路径都无法访问，也就不需要保存了
//            return false
//        }
//
//        try {
//            LogUtils.debug("getFaviconByUrl, url:$faviconUrl")
//            core.httpManager.saveImage(faviconUrl, Core.PATH_FAVICONS, Helper.md5(feedId), false, false) { uriString, inputStream ->
//                FmFileUtil.deleteDir(core, uriString, true)
//                FmFileUtil.create(core, uriString, inputStream)
//            }
//            return true
//        } catch (e: Exception) {
//            LogUtils.error(e)
//            return false
//        }
//    }

//    @JvmStatic
//    private fun checkFavicon(core: Core, feedId: String): Boolean {
//        return try {
//            if (Core.PATH_FAVICONS == null) {
//                // 路径都无法访问，也就不需要保存了
//                return false
//            }
//            val path = getLocalFaviconsUrl(feedId)
//
//            // check image size
//            val f = File(path)
//            if (f.length() == 0L) {
//                f.delete()
//                return false
//            }
//
//            val type = ImageUtils.checkImageType(path)
//            if (type != ImageUtils.ImageType.UNKNOWN && type != ImageUtils.ImageType.SVG) {
//                // check image width and height
//                val opts = BitmapFactory.Options()
//                opts.inJustDecodeBounds = true
//                BitmapFactory.decodeFile(path, opts)
//                if (opts.outWidth <= 0 || opts.outHeight <= 0) {
//                    f.delete()
//                    return false
//                }
//                val size = core.du.dip2px(SIZE_DP.toFloat())
//                if (opts.outWidth > size) {
//                    // resize
//                    ImageUtils.saveImage(ImageUtils.zoomBitmap(path, size, size), path)
//                }
//            }
//            true
//        } catch (e: Exception) {
//            LogUtils.error(e)
//            false
//        }
//    }

    @JvmStatic
    private fun getFeedImage(feedUrl: String): String? {
//        return try {
//            RssParser().parse(feedUrl, false, object : Requester {
//                override fun get(url: String): InputStream {
//                    return httpManager.stream(HttpMethod.GET, feedUrl)
//                }
//            })?.channel?.imageUrl
//        } catch (e: HttpException) {
//            LogUtils.error(e)
//            null
//        }
        return null
    }

//    @JvmStatic
//    fun getLocalFaviconsUrl(id: String): String {
//        return Core.PATH_FAVICONS + Helper.md5(id)
//    }
}