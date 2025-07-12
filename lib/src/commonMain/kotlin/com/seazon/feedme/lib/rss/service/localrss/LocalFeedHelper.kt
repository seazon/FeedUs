package com.seazon.feedme.lib.rss.service.localrss

import com.seazon.feedme.lib.rss.service.localrss.bo.LocalRssSubscription
import com.seazon.feedme.lib.utils.LogUtils
import com.seazon.feedme.lib.utils.PersistenceUtils

object LocalFeedHelper {

    const val KEY = "local-feeds.config"

    var feedMap: MutableMap<String, LocalRssSubscription>? = null

    fun getLocalFeedMap(): MutableMap<String, LocalRssSubscription> {
        if (feedMap == null) {
            feedMap = getFeedConfigMap(getFeedConfigs())
        }
        return feedMap!!
    }

    fun saveLocalFeed(feedConfigs: List<LocalRssSubscription>) {
        saveFeedConfig(feedConfigs)
        feedMap = getFeedConfigMap(feedConfigs)
    }

    fun saveLocalFeed(feedConfig: LocalRssSubscription?) {
        saveFeedConfig(  feedConfig)
    }

    fun resetLocalFeedClawTime() {
        val list = getFeedConfigs()
        for (s in list!!) {
            s.lastClawTime = 0
        }
        saveLocalFeed(list)
    }

    fun getFeedConfigs(): List<LocalRssSubscription> {
        try {
            val data = PersistenceUtils.getData<List<LocalRssSubscription>?>(KEY)
            if (data.isNullOrEmpty()) {
                LogUtils.warn("localfeeds.config is empty or not exists, path:$KEY")
                return ArrayList<LocalRssSubscription>()
            }

            return data
        } catch (e: Exception) {
            LogUtils.error(e)
            return emptyList()
        }
    }

    fun getFeedConfigMap(feedConfigs: List<LocalRssSubscription>): MutableMap<String, LocalRssSubscription> {
        val list = mutableMapOf<String, LocalRssSubscription>()
        for (feedConfig in feedConfigs) {
            list.put(feedConfig.id.orEmpty(), feedConfig)
        }
        return list
    }

    @Suppress("UNCHECKED_CAST")
    fun saveFeedConfig(f: LocalRssSubscription?) {
        if (f == null) return

        val map = getLocalFeedMap()
        map[f.id.orEmpty()] = f

        saveFeedConfig(map.values as? List<LocalRssSubscription>)
    }

    fun saveFeedConfig(feedConfigs: List<LocalRssSubscription>?) {
        try {
            PersistenceUtils.setData(KEY, feedConfigs)
        } catch (e: Exception) {
            LogUtils.error(e)
        }
    }
}
