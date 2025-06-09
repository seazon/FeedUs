package com.seazon.feedme.lib.rss.service

import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.bazqux.BazquxApi
import com.seazon.feedme.lib.rss.service.feedbin.FeedbinApi
import com.seazon.feedme.lib.rss.service.feedly.FeedlyApi
import com.seazon.feedme.lib.rss.service.fever.FeverApi
import com.seazon.feedme.lib.rss.service.folo.FoloApi
import com.seazon.feedme.lib.rss.service.freshrss.FreshRssApi
import com.seazon.feedme.lib.rss.service.gr.GReaderApi
import com.seazon.feedme.lib.rss.service.inoreader.InoreaderApi
import com.seazon.feedme.lib.rss.service.inoreader.InoreaderOldApi
import com.seazon.feedme.lib.rss.service.localrss.LocalRssApi
import com.seazon.feedme.lib.rss.service.theoldreader.TheoldreaderApi
import com.seazon.feedme.lib.rss.service.ttrss.TtrssApi
import com.seazon.feedme.lib.utils.LogUtils.debug

object RssUtil {

    fun newApi(token: RssToken): RssApi {
        debug("get api: " + token.accoutType)
        return when (token.accoutType) {
            Static.ACCOUNT_TYPE_FEEDLY -> FeedlyApi()
            Static.ACCOUNT_TYPE_INOREADER_OAUTH2 -> InoreaderApi(token)
            Static.ACCOUNT_TYPE_INOREADER -> InoreaderOldApi(token)
            Static.ACCOUNT_TYPE_BAZQUX_READER -> BazquxApi(token)
            Static.ACCOUNT_TYPE_THE_OLD_READER -> TheoldreaderApi(token)
            Static.ACCOUNT_TYPE_FRESH_RSS -> FreshRssApi(token)
            Static.ACCOUNT_TYPE_GOOGLE_READER_API -> GReaderApi(token)
            Static.ACCOUNT_TYPE_FEVER -> FeverApi(token)
            Static.ACCOUNT_TYPE_MINIFLUX -> FeverApi(token)
            Static.ACCOUNT_TYPE_FEEDBIN -> FeedbinApi(token)
            Static.ACCOUNT_TYPE_TTRSS -> TtrssApi(token)
            Static.ACCOUNT_TYPE_FOLO -> FoloApi()
            Static.ACCOUNT_TYPE_LOCAL_RSS -> LocalRssApi()
            else -> LocalRssApi()
        }
    }
}