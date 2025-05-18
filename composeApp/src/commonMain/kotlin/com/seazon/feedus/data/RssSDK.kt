package com.seazon.feedus.data

import com.seazon.feedme.lib.network.HttpException
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.rss.service.RssUtil

class RssSDK(val tokenSettings: TokenSettings) {

    private var api: RssApi? = null

    /**
     * 可以调用需要authed api的接口
     */
    suspend fun getRssApi(forceExpire: Boolean): RssApi {
        val token: RssToken = tokenSettings.getToken()
        if (forceExpire || token.expiresTimestamp < System.currentTimeMillis()) {
            api = initRssApi(token)
        }
        if (api == null) {
            api = initRssApiStatic(token)
        }
        if (api!!.getToken() == null) {
            api!!.setToken(token)
        }
        return api!!
    }

    /**
     * 初始化rss api，刷新refresh token
     */
    private suspend fun initRssApi(token: RssToken): RssApi {
        val api = RssUtil.newApi(token)
        try {
            val accessTokenResponse = api.getAccessToken(token)
            api.setUserWithAccessToken(token, accessTokenResponse!!)
            tokenSettings.saveToken(token)
        } catch (e: Exception) {
            throw HttpException(HttpException.Type.EEXPIRED, e)
        }

        return api
    }

    fun getRssApiStatic(): RssApi {
        val token: RssToken = tokenSettings.getToken()
        if (api == null) {
            api = initRssApiStatic(token)
        }
        if (api?.getToken() == null) {
            api?.setToken(token)
        }
        return api!!
    }

    fun resetApi() {
        api = null
    }

    private fun initRssApiStatic(token: RssToken): RssApi {
        val api = RssUtil.newApi(token)
        api.setToken(token)
        return api
    }

}