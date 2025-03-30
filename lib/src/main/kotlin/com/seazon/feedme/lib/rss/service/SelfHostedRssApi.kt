package com.seazon.feedme.lib.rss.service

interface SelfHostedRssApi {
    /**
     * 在设置host后可以修改host，也可以不做修改直接返回参数
     */
    fun onHostSet(host: String): String = host

    /**
     * 获取默认host，可以为空
     */
    fun getDefaultHost(): String? = null
}
