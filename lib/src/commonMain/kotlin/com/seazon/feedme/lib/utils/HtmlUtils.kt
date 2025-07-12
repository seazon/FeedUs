package com.seazon.feedme.lib.utils

import com.mohamedrejeb.ksoup.entities.KsoupEntities
import io.ktor.http.Url
import io.ktor.http.authority

object HtmlUtils {

    const val URL_DATA = "data:"
    const val URL_FILE = "file://"
    private const val SLASH = "/"
//
//    fun getImgSrc(
//        html: String,
//        list: MutableList<String>,
//        htmlUrl: String,
//        srcKey: String
//    ): String {
//        if (Helper.isBlank(html)) return html
//        val baseUrl = getBaseUrl(html)
//        var newHtml = html
//        var indexStart = html.indexOf("<img ", 0)
//        var slash: String
//        var newUrl: String
//        val offsetString = " $srcKey=\""
//        val offset = offsetString.length
//        while (indexStart != -1) {
//            indexStart = html.indexOf(offsetString, indexStart)
//            if (indexStart != -1) {
//                val indexEnd = html.indexOf("\"", indexStart + offset)
//                if (indexEnd != -1) {
//                    var url = html.substring(indexStart + offset, indexEnd)
//                    if (!isProcessedImgUrl(url)) {
//                        /*
//                         * img的src可能是URL_FEEDME_SHOW_IMAGE， 也可能是原始的值
//                         */
//                        if (url.startsWith(Core.URL_FEEDME_SHOW_IMAGE)) {
//                            url = url.replace(Core.URL_FEEDME_SHOW_IMAGE, "")
//                        }
//                        if (!isRightImageUrl(url)) {
//                            if (url.startsWith("//")) {
//                                newUrl = getScheme(htmlUrl) + url
//                            } else if (!Helper.isBlank(baseUrl)) {
//                                newUrl = baseUrl + url
//                            } else {
//                                slash = SLASH
//                                if (url.startsWith(SLASH)) {
//                                    slash = ""
//                                }
//                                newUrl = getAuthority(htmlUrl) + slash + url
//                            }
//                            try {
//                                newHtml = newHtml.replace("\"$url\"", "\"$newUrl\"")
//                            } catch (e: OutOfMemoryError) {
//                                LogUtils.error("url0:$url, url1:$newUrl, link:$htmlUrl", e)
//                            }
//                            url = newUrl
//                        }
//                        list.add(url)
//                    }
//                    indexStart = html.indexOf("<img ", indexEnd)
//                }
//            }
//        }
//        return newHtml
//    }
//
//    fun getImgSrc(html:String):List<String>{
//        val regex = "src=\"(.*?)\""
//        val list: MutableList<String> = ArrayList()
//
//        val pa: Pattern = Pattern.compile(regex, Pattern.DOTALL)
//        val ma: Matcher = pa.matcher(html)
//        while (ma.find()) {
//            val src = ma.group()
//            val regex1 = "http(.*?)(.jpg|.png|.gif|.jpeg|=jpg|=png|=gif|=jpeg)(.*)*"
//            val pa1: Pattern = Pattern.compile(regex1, Pattern.DOTALL)
//            val ma1: Matcher = pa1.matcher(src)
//            while (ma1.find()) {
//                list.add(ma1.group().let {
//                    if (it.endsWith("\"")) {
//                        it.substring(0, it.length - 1)
//                    } else {
//                        it
//                    }
//                })
//            }
//        }
//        return list
//    }

    fun getFirstImage(html: String?, htmlUrl: String?): String? {
        html ?: return null

        val baseUrl = getBaseUrl(html)
        var indexStart = html.indexOf("<img ", 0)
        var slash: String
        val newUrl: String
        while (indexStart != -1) {
            indexStart = html.indexOf(" src=\"", indexStart)
            if (indexStart != -1) {
                val indexEnd = html.indexOf("\"", indexStart + 6)
                if (indexEnd != -1) {
                    var url = html.substring(indexStart + 6, indexEnd)

                    if (!isRightImageUrl(url) && !htmlUrl.isNullOrBlank()) {
                        if (url.startsWith("//")) {
                            newUrl = getScheme(htmlUrl) + url
                        } else if (!baseUrl.isNullOrEmpty()) {
                            newUrl = baseUrl + url
                        } else {
                            slash = SLASH
                            if (url.startsWith(SLASH)) {
                                slash = ""
                            }
                            newUrl = getAuthority(htmlUrl) + slash + url
                        }
                        url = newUrl
                    }
                    return url
                    indexStart = html.indexOf("<img ", indexEnd)
                }
            }
        }
        return null
    }

    fun isHttpUrl(url: String?): Boolean {
        url ?: return false
        return url.startsWith("http://") || url.startsWith("https://")
    }

    /**
     * 是不是格式标准
     *
     * @param url
     * @return
     */
    private fun isRightImageUrl(url: String): Boolean {
        url.lowercase().run {
            return if (isHttpUrl(url)) {
                true
            } else url.startsWith(URL_DATA)
        }
    }

    //    fun isImageFile(fileName: String): Boolean {
//        return fileName.endsWith(".png")
//                || fileName.endsWith(".jpg")
//                || fileName.endsWith(".gif")
//                || fileName.endsWith(".jpeg")
//    }
//
//    fun isImageUrl(urlString: String): Boolean {
//        return urlString.contains(".png")
//                || urlString.contains(".jpg")
//                || urlString.contains(".gif")
//                || urlString.contains(".jpeg")
//                || urlString.contains("=png")
//                || urlString.contains("=jpg")
//                || urlString.contains("=gif")
//                || urlString.contains("=jpeg")
//                || urlString.startsWith(Core.URL_DATA_IMAGE)
//    }
//
    private fun getBaseUrl(html: String): String {
        var baseUrl = ""
        val base_tag_start = "<base href=\""
        val base_tag_end = "\""
        val baseIndex = html.indexOf(base_tag_start, 0)
        if (baseIndex != -1) {
            baseUrl = html.substring(
                baseIndex + base_tag_start.length,
                html.indexOf(base_tag_end, baseIndex + base_tag_start.length)
            )
        }
        return baseUrl
    }

    //
    private fun getAuthority(url: String): String {
        return try {
            val uri = Url(url)
            uri.protocol.name + "://" + uri.authority
        } catch (e: Exception) {
//            LogUtils.error(e)
            url
        }
    }

    //
    private fun getScheme(url: String): String {
        return try {
            val uri = Url(url)
            uri.protocol.name + ":"
        } catch (e: Exception) {
//            LogUtils.error(e)
            url
        }
    }
//
//    /**
//     * 保存data:image/png;base64,开始的img标签的图片内容
//     */
//    @Throws(HtmlParseException::class)
//    fun parseImageData(imgUrl: String): ImageData {
//        // data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAACsYAAAZ+CAYAAA...
//        val PRE = "image/"
//        val SP = ","
//        val SP2 = ";"
//        val splitIndex = imgUrl.indexOf(SP)
//        val params = imgUrl.substring(Core.URL_DATA.length, splitIndex).split(SP2).toTypedArray()
//        val content = imgUrl.substring(splitIndex + SP.length)
//        if (params.size < 2) {
//            throw HtmlParseException("not acceptable params length:" + params.size)
//        }
//        val imageType = params[0]
//        val encodeType = params[1]
//        if (!imageType.startsWith(PRE)) {
//            throw HtmlParseException("not acceptable image type:$imageType")
//        }
//        if (encodeType != "base64") {
//            throw HtmlParseException("not acceptable encode type:$encodeType")
//        }
//        return ImageData(Base64.decode(content, Base64.DEFAULT), imageType.substring(PRE.length))
//    }
//
//    class HtmlParseException(msg: String?) : Exception(msg)
//
//    fun cropReturn(html: String): String {
//        return html
////                .replace("\n", "<br>")
////                .replace("\r", "<br>")
//            .replace(Regex("(<br *>(</br>)* *){2,}"), "<br><br>")
//    }
//
//    fun getWebContent4Share(context: Core, item: Item, feed: Feed?, showType: ShowType): String {
//        val header = String.format(
//            context.resources.getString(R.string.article_sendto_email_header),
//            "<a href=\"" + item.link + "\">" + (feed?.title ?: item.title) + "</a>"
//        )
//        val footer = String.format(
//            context.resources.getString(R.string.article_sendto_email_footer),
//            "<a href=\"" + Core.APP_URL + "\">FeedMe</a>"
//        )
//        var article: Article? = null
//        try {
//            article = getBaseWebContent(item, showType, context)
//        } catch (e: ForceShowWebException) {
//            LogUtils.error(e)
//            article = Article(item.id, "", ShowType.WEB)
//        }
//        return (article?.content.orEmpty() + "<br /><br />--------<br /><br />" + header + "<br /><br />[ " + footer
//                + " ]<br />")
//    }
//
//    @JvmStatic
//    @Throws(ForceShowWebException::class)
//    fun getWebContent4View(
//        layoutInfo: ArticlePageLayoutInfo,
//        core: Core,
//        item: Item,
//        showType: ShowType,
//        feed: Feed?,
//        showHeader: Boolean,
//        processForPodcast: Boolean,
//        withCardStyle: Boolean,
//    ): Article {
//        val article = getBaseWebContent(item, showType, core)
//        var content = article.content
//        if (processForPodcast) {
//            content = processTime(content)
//        }
//        val type = ShowType.getLink(article.showType)
//        val ltr = layoutInfo.isLayoutDirectionLtr
//        return Article(
//            item.id,
//            getHtml(
//                core = core,
//                item = item,
//                sideWidthDpi = layoutInfo.paddingHorizontal,
//                screenWidthDpi = core.du.px2dip(layoutInfo.width.toFloat()),
//                feed = feed,
//                content = content,
//                type = type,
//                paddingTop = layoutInfo.paddingTop,
//                paddingBottom = layoutInfo.paddingBottom,
//                showHeader = showHeader,
//                ltr = ltr,
//                withCardStyle = withCardStyle,
//            ), article.showType
//        )
//    }
//
//    private fun getArticleMargin(core: Core): Int {
//        return core.mainPreferences.ui_artdtl_margin * 8
//    }
//
//    private fun getHtml(
//        core: Core,
//        item: Item,
//        sideWidthDpi: Int,
//        screenWidthDpi: Int,
//        feed: Feed?,
//        content: String,
//        type: String,
//        paddingTop: Int,
//        paddingBottom: Int,
//        showHeader: Boolean,
//        ltr: Boolean,
//        withCardStyle: Boolean
//    ): String {
//        var direction = "ltr"
//        if (!ltr) direction = "rtl"
//        val feedConfig = core.getFeedConfig(item.fid, FeedConfig.TYPE_FEED)
//        val title =
//            getHighlightString2(item.title, core.instanceManager.highlighterIO.getHighlighters())
//        val url = item.link
//        val themeBean = core.themeBean
//        val font = core.fontManager.getFontBean()
//        val fontSize = core.mainPreferences.ui_artdtl_fontsize
//        var overflowCss = ""
//        if (!core.mainPreferences.ui_artdtl_overflow) {
//            overflowCss = "word-wrap: break-word;word-break: break-word;"
//        }
//        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm")
//        val date = sdf.format(item.publisheddate)
//        val b = StringBuilder()
//        b.append("<html><head>")
//        if (Helper.canUseTraffic(core.mainPreferences.ui_artdtl_downloadfont, core)) {
//            b.append(font.getWebFontCode())
//        }
//        b.append("<style>")
//        // 基础样式
//        val mainFont = font.mainFont
//        val contentFont = font.contentFont
//        val commentFont = font.commentFont
//        val quoteFont = font.quoteFont
//        var maxWidthStyle = ""
//        if (screenWidthDpi >= 640) {
//            maxWidthStyle = "max-width: 640px;margin: 0 auto;"
//        }
//        val videoHeight: Int = min(640, screenWidthDpi) * 9 / 16
//        val bodyBg = if (withCardStyle) "background:${themeBean.basegroundColor};" else ""
//        b.append(
//            "body{" + maxWidthStyle + bodyBg + "text-align: " + core.mainPreferences.ui_artdtl_align
//                    + ";font-family:\"" + contentFont + "\";color:" + themeBean.primaryTextColor + ";font-size: " + fontSize
//                    + "px;line-height: " + core.mainPreferences.ui_artdtl_line_height + "%;" + overflowCss + "}"
//        )
//        if (core.isEinkTheme) {
//            b.append("a{color:" + themeBean.primaryString + ";text-decoration: underline;" + overflowCss + "}")
//        } else {
//            b.append("a{color:" + themeBean.primaryString + ";text-decoration: none;" + overflowCss + "}")
//        }
//        b.append("div{direction: $direction;}")
//        b.append("h1{font-size: " + (fontSize + 5) + "px;}")
//        b.append("h2{font-size: " + (fontSize + 4) + "px;}")
//        b.append("h3{font-size: " + (fontSize + 3) + "px;}")
//        b.append("h4{font-size: " + (fontSize + 2) + "px;}")
//        b.append("h5{font-size: " + (fontSize + 1) + "px;}")
//        b.append("h6{font-size: " + (fontSize) + "px;}")
//        //        b.append(".icon-audio {background-color: " + themeBean.mainColor + ";border-radius: 50%;width: 56px;height: 56px;background-image: url('file:///android_asset/audio.png');background-repeat: no-repeat;background-position: center;background-size: 24px;box-shadow: 2px 2px 6px 2px #BFBFBF;}");
////        b.append(".icon-video {background-color: " + themeBean.mainColor + ";border-radius: 50%;width: 56px;height: 56px;background-image: url('file:///android_asset/video.png');background-repeat: no-repeat;background-position: center;background-size: 24px;box-shadow: 2px 2px 6px 2px #BFBFBF;}");
//        b.append(".icon-audio {display: inline-block;width: 100%; height: 112px; background: url(file:///android_asset/audio.png) no-repeat center;background-size: 36px;}")
//        b.append(".icon-video {display: inline-block;width: 100%; height: 112px; background: url(file:///android_asset/video.png) no-repeat center;background-size: 36px;}")
//        b.append("pre{white-space: pre-wrap;line-height: 1.25em;overflow: auto;overflow-y: hidden;color:" + themeBean.secondaryTextColor + ";border-radius: 4px;padding: 0.8em 0.8em;font-size: 0.8em;background-color:" + themeBean.basegroundColor + ";}")
//        b.append("code{color:" + themeBean.secondaryTextColor + ";border-radius: 4px;padding: 0.2em 0.4em;font-size: 0.8em;background-color:" + themeBean.basegroundColor + ";}")
//        b.append(".header{margin-bottom:30px;}")
//        b.append(".topper{display: inline-block;width: 100%;}")
//        b.append(
//            (".text-primary{font-family:\"" + mainFont + "\";font-weight: bold;font-size: " + (fontSize + 6) + "px;border-bottom: 1px solid "
//                    + themeBean.secondaryTextColor + ";padding:10px 0;margin-bottom:10px;line-height: 1.25em;}")
//        )
//        b.append(".text-secondary{font-family:\"" + commentFont + "\";color:" + themeBean.secondaryTextColor + ";font-size: " + (fontSize - 4) + "px;}")
//        // img的样式
//        b.append(".img-wraper{text-align:center;padding:16px 0 16px 0;margin:0px 0px;border-radius:12px;}")
//        b.append(".img-container{-webkit-tap-highlight-color:rgba(0,0,0,0);border-bottom: 0px;text-decoration: none;border-radius:12px;}")
//        b.append(".img-box{border-radius:12px;}")
//        // b.append(".img-box{display: block;}"); // 小图无法居中
//        b.append("img{max-width:100%;border-radius:12px;}")
//        if (feedConfig != null && feedConfig.isShowImgAlt == 1) {
//            b.append(".img-alt{font-family:\"" + commentFont + "\";font-style: italic;font-size: 80%;color: " + themeBean.secondaryTextColor + "}")
//        } else {
//            b.append(".img-alt{display:none;}")
//        }
//        // svg的样式
//        b.append("svg{width:16px;height:16px;}")
//        // video的样式
//        b.append("video{margin:0px 0px;background-color:#000000;border-radius:12px;}")
//        b.append(".video{text-align:center;margin:0px 0px;border-radius:12px;}")
//        b.append(".video-wraper{margin:0px 0px;position:relative;background-color:#000000;border-radius:12px;}")
//        b.append(".video-wraper-img{position:absolute;display: inline-block;width:100%;height: 100%;}")
//        b.append(".video-wraper-indicator{position:absolute;display: inline-block;width:100%;height: 100%;background: url(file:///android_asset/video.png) no-repeat center;background-size: 36px;}")
//        b.append(".video-disable{margin:0px 0px;background-color:#000000;border-radius:12px;padding:24px;color:#ffffff;}")
//
//        // rss feed中 enclosure的样式
//        b.append(
//            (".enclosure{font-family:\"" + commentFont + "\";font-style: italic;color:" + themeBean.secondaryTextColor + ";font-size: "
//                    + (fontSize - 4) + "px;line-height: 100%;margin:20px 0;}")
//        )
//        // quote的样式
//        b.append(
//            ("blockquote{font-family:\"" + quoteFont + "\";margin: 0px 8px;border-left: 1px solid " + themeBean.secondaryTextColor
//                    + ";padding-left: 8px;font-style: italic;font-size: " + (fontSize - 2) + "px;}")
//        )
//        // mobilizer引擎说明的样式
//        b.append(
//            (".mobilizer{font-family:\"" + commentFont + "\";font-style: italic;color:" + themeBean.secondaryTextColor + ";font-size: "
//                    + (fontSize - 4) + "px;}")
//        )
//        // 阅读时间预估框
//        b.append(
//            (".play-time{font-family:\"" + commentFont + "\";font-style: italic;color:" + themeBean.secondaryTextColor + ";font-size: "
//                    + (fontSize - 4) + "px;background-color:" + themeBean.basegroundColor + ";margin:16px;padding:16px;align:center;border-radius:12px;}")
//        )
//        b.append(".video-out-link{text-align:center;}")
//        // 段首缩进
//        if (core.mainPreferences.ui_artdtl_indent) {
//            b.append("p{text-indent: 2em;}")
//        }
//        b.append("figcaption{text-align: center;font-family:\"" + commentFont + "\";color:" + themeBean.secondaryTextColor + ";font-size: " + (fontSize - 2) + "px;}")
//        b.append("</style>")
//        if (core.mainPreferences.ui_show_toc) {
//            b.append("""
//<link href="file:///android_asset/toc/css/autoc.css" rel="stylesheet"><style>.paper{
//  padding:0px;
//  overflow:hidden;
//}
//</style>
//""")
//        }
//        b.append("</head>")
//        b.append("<body>")
//        if (core.mainPreferences.ui_show_toc) {
//            b.append("<div class=\"paper\"><article class=\"article\" id=\"article\">")
//        }
//        val cardStyle = if (withCardStyle) {
//            "margin: " + paddingTop + "px " + 0 + "px " + paddingBottom + "px " + 0 + "px;border-radius:12px;"
//        } else {
//            ""
//        }
//        b.append(
//            "<div style=\"padding: " + sideWidthDpi + "px "
//                    + sideWidthDpi + "px " + (sideWidthDpi + 56) + "px " + sideWidthDpi + "px;background:" + themeBean.backgroundColor + ";" + cardStyle + "\">"
//        )
//        // TODO use this if width is not correct
////        b.append("<body style=\"width: " + (screenWidthDpi - sideWidth * 2) + "px;margin: " + paddingTop + "px " + sideWidth + "px " + paddingBottom + "px " + sideWidth + "px;\">");
////        b.append("<div style=\"padding: " + sideWidth + "px " + sideWidth + "px " + sideWidth + "px " + sideWidth + "px;background:" + themeBean.backgroundColor + ";border-radius:12px;\">");
//        if (showHeader) {
//            b.append("<div class=\"header\">")
//            b.append("<div class=\"topper\">")
//            b.append("<div class=\"text-secondary\" style=\"float:left;\">$date</div>")
//            b.append("<div class=\"text-secondary\" style=\"float:right;\">$type</div>")
//            b.append("</div>")
//            b.append("<div class=\"text-primary\">" + ShowType.getLink2(title, url) + "</div>")
//            b.append(
//                "<div class=\"text-secondary\">${item.author} @ <a href=\"feedme://article?feedId=${
//                    URLEncoder.encode(
//                        feed?.id.orEmpty()
//                    )
//                }&title=${URLEncoder.encode(feed?.title.orEmpty())}\">${feed?.title.orEmpty()}</a></div>"
//            )
//            b.append("</div>")
//            if (core.mainPreferences.ui_show_readtime) {
//                val cnt = StringUtils.calcWordCount(StringUtils.getSummary(content))
//                b.append(
//                    "<div class=\"play-time\">" + core.getString(
//                        R.string.article_reading_time_tip, cnt.toString(),
//                        PlayUtils.formatAudioDuration2(item.getPlayTime(core, cnt))
//                    ) + "</div>"
//                )
//            }
//        }
//        b.append(getHighlightString2(content, core.instanceManager.highlighterIO.getHighlighters()))
//        b.append("</div>")
//        if (core.mainPreferences.ui_show_toc) {
//            b.append("""
//</article>
//</div>
//<script src="file:///android_asset/toc/js/autoc.js"></script>
//<script>let navigation = new AutocJs()</script>
//""")
//        }
//        b.append("</body>")
//        b.append("</html>")
//        return b.toString().replace(EmbedDefault.SCREEN_HEIGHT_PLACEHOLDER, "${videoHeight}px")
//    }
//
//    @JvmStatic
//    fun toHtml(href: String, type: String?): String? {
//        if (type == null) {
//            return ""
//        } else if (type.startsWith("audio/")) {
//            return FeedMeUrlManager.getAudioHtmlCode(href)
//        } else return if (type.startsWith("image/")) {
//            "<img src=\"$href\" />"
//        } else {
//            ""
//        }
//    }
//
//    @JvmStatic
//    fun getWebContentLoading4View(
//        activity: BaseActivity,
//        core: Core,
//        item: Item,
//        paddingTop: Int,
//        paddingBottom: Int
//    ): String {
//        val ltr = activity.window.decorView.layoutDirection == View.LAYOUT_DIRECTION_LTR
//        val content = ("<div align=\"center\" style=\"padding: 20px;\"><img src=\"file:///android_asset/"
//                + core.themeBean.ajaxLoaderPacman + "\" /></div>")
//        return getHtml(
//            core = core,
//            item = item,
//            sideWidthDpi = getArticleMargin(core),
//            screenWidthDpi = core.du.px2dip(activity.getActivityWidth().toFloat()),
//            feed = null,
//            content = content,
//            type = "",
//            paddingTop = paddingTop,
//            paddingBottom = paddingBottom,
//            showHeader = true,
//            ltr = ltr,
//            withCardStyle = true,
//        )
//    }
//
//    @JvmStatic
//    fun getWebContentFailed4View(
//        activity: BaseActivity,
//        core: Core,
//        item: Item,
//        paddingTop: Int,
//        paddingBottom: Int
//    ): String {
//        val type = ShowType.getLink(ShowType.WEB)
//        val ltr = activity.window.decorView.layoutDirection == View.LAYOUT_DIRECTION_LTR
//        val content = ("<div align=\"center\" class=\"text-secondary\" style=\"padding: 20px;\">"
//                + core.getString(R.string.mobilizer_failed)
//                + "<br/><p class=\"mobilizer\">" + item.mobilizerInfo?.errorMsg
//                + "</div>" + MobilizerTag.getMobilizerHtml(item.mobilizerInfo?.mobilizer.orEmpty()))
//        return getHtml(
//            core = core,
//            item = item,
//            sideWidthDpi = getArticleMargin(core),
//            screenWidthDpi = core.du.px2dip(activity.getActivityWidth().toFloat()),
//            feed = null,
//            content = content,
//            type = type,
//            paddingTop = paddingTop,
//            paddingBottom = paddingBottom,
//            showHeader = true,
//            ltr = ltr,
//            withCardStyle = true,
//        )
//    }
//
//    @JvmStatic
//    fun getSimpleWebContent(item: Item, core: Core): String {
//        try {
//            return getBaseWebContent(item, ShowType.AVAILABLE, core).content
//        } catch (e: ForceShowWebException) {
//            return ""
//        }
//    }
//
//    @JvmStatic
//    @Throws(ForceShowWebException::class)
//    fun getBaseWebContent(item: Item, showType: ShowType, core: Core): Article {
//        return getBaseWebContent(item.fid, item.id, item.md5Id, showType, core)
//    }
//
//    @JvmStatic
//    @Throws(ForceShowWebException::class)
//    fun getBaseWebContent(
//        feedId: String?,
//        itemId: String,
//        itemMd5Id: String,
//        showType: ShowType,
//        core: Core
//    ): Article {
//        val feedConfig = core.getFeedConfig(feedId, FeedConfig.TYPE_FEED)
//        val canShowWeb =
//            (((showType == ShowType.AUTO || showType == ShowType.AVAILABLE) && (feedConfig.isDownloadWebWhenSync == 1 || feedConfig.isDownloadWebWhenRead == 1))
//                    || showType == ShowType.WEB)
//        val forceShowWeb =
//            showType != ShowType.AVAILABLE && (((showType == ShowType.AUTO && feedConfig.isDownloadWebWhenRead == 1)
//                    || showType == ShowType.WEB))
//        if (canShowWeb) {
//            // Helper.debug("show HTML_WEB");
//            getDescriptionFrom(core, itemMd5Id, Core.HTML_WEB)?.let {
//                if (it.isNotBlank()) {
//                    return Article(itemId, it, ShowType.WEB)
//                }
//            }
//
//            try {
//                // Helper.debug("show HTML_WEB_ORI");
//                getDescriptionFrom(core, itemMd5Id, Core.HTML_WEB_ORI)?.let {
//                    if (it.isNotBlank()) {
//                        val description = cleanHtml(it, core.mainPreferences)
//                        if (core.cachePath != null) {
//                            create(
//                                core,
//                                core.cachePath + itemMd5Id + "/" + itemMd5Id + Core.HTML_WEB,
//                                (description)
//                            )
//                        }
//                        return Article(itemId, description, ShowType.WEB)
//                    }
//                }
//
//            } catch (e: Exception) {
//                LogUtils.error(e)
//            }
//            if (forceShowWeb) {
//                throw ForceShowWebException("")
//            }
//        }
//
//        // Helper.debug("show HTML_FEED");
//        getDescriptionFrom(core, itemMd5Id, Core.HTML_FEED)?.let {
//            if (it.isNotBlank()) {
//                return Article(itemId, it, ShowType.FEED)
//            }
//        }
//
//        try {
//            // Helper.debug("show HTML_FEED_ORI");
//            getDescriptionFrom(core, itemMd5Id, Core.HTML_FEED_ORI)?.let {
//                if (it.isNotBlank()) {
//                    val description = cleanHtml(it, core.mainPreferences)
//                    if (core.cachePath != null) {
//                        create(
//                            core, core.cachePath + itemMd5Id + "/" + itemMd5Id + Core.HTML_FEED,
//                            (description)
//                        )
//                    }
//                    return Article(itemId, description, ShowType.FEED)
//                }
//            }
//        } catch (e: Exception) {
//            LogUtils.error(e)
//        }
//        LogUtils.warn("show null")
//        return Article(itemId, "", ShowType.FEED)
//    }
//
//    @JvmStatic
//    fun cleanHtml(description: String?, mp: MainPreferences?): String {
//        return cropReturn(PageCleaner.parseArticle(description, mp))
//    }
//
//    @JvmStatic
//    fun getDescriptionFrom(core: Core, md5Id: String, type: String): String? {
//        try {
//            // LogUtils.debug("getDescriptionFrom, type:" + type);
//            return if (core.cachePath == null) {
//                null
//            } else read(core, core.cachePath + md5Id + "/" + md5Id + type)
//        } catch (e: Exception) {
//            LogUtils.error(e)
//            return null
//        }
//    }

    fun encode(s: String): String {
        return KsoupEntities.encodeHtml(s)
    }

    fun decode(s: String): String {
        return KsoupEntities.decodeHtml(s)
    }
}

//@Keep
//data class ImageData(val data: ByteArray, val type: String)