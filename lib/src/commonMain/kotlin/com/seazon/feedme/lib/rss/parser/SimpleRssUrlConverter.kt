package com.seazon.feedme.lib.rss.parser

import com.seazon.feedme.lib.utils.StringUtil

object SimpleRssUrlConverter {

    private val CONFIGS = arrayOf(
        // native - Twitter \ Mastodon \ YouTube \ Rumble \ Meetup \ GitHub \ Ground News \ Grailed
        "https://mastodon.social/@(\\w+)" to "https://mastodon.social/@%s.rss",
        "https://www.ximalaya.com/album/(\\d+)" to "https://www.ximalaya.com/album/%s.xml",
        "https://ximalaya.com/album/(\\d+)" to "https://www.ximalaya.com/album/%s.xml",
        "https://github.com/(\\S+)/releases" to "https://github.com/%s/releases.atom",
        "https://www.tumblr.com/(\\w+)" to "https://%s.tumblr.com/rss",
        // openrss
//        "https://twitter.com/(\\w+)" to "https://openrss.org/twitter.com/%s",
//        "https://x.com/(\\w+)" to "https://openrss.org/x.com/%s",
//        "https://mobile.twitter.com/(\\w+)" to "https://openrss.org/twitter.com/%s",
        "https://github.com/(\\S+)/issues" to "https://openrss.org/github.com/%s/issues",
        "https://rumble.com/c/(\\w+)" to "https://openrss.org/rumble.com/c/%s",
        "https://www.youtube.com/@(\\w+)/videos" to "https://openrss.org/www.youtube.com/user/%s/videos",
        // rsshub
        "https://space.bilibili.com/(\\d+)/video" to "https://rsshub.app/bilibili/user/video/%s",
        "https://www.xiaoyuzhoufm.com/podcast/(\\w+)" to "https://rsshub.app/xiaoyuzhou/podcast/%s",
        // rssfeed
        "https://weibo.com/u/(\\d+)" to "https://rssfeed.today/weibo/rss/%s",
    )

    fun convert(url: String?): String? {
        url ?: return null

        CONFIGS.forEach { (patten, string) ->
            try {
                if (url.matches(Regex(patten))) {
                    Regex(patten).find(url)?.groups?.get(1)?.let {
                        return StringUtil.format(string, it.value)
                    }
                }
            } catch (_: Exception) {
            }
        }
        return null
    }
}