package com.seazon.feedus.data

import com.russhwolf.settings.Settings
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.utils.Helper
import com.seazon.feedme.lib.utils.LogUtils.debug
import com.seazon.feedme.lib.utils.orZero

class TokenSettings {
    private val settings = Settings()
    private var token: RssToken? = null

    fun getToken(): RssToken {
        if (token == null) {
            token = RssToken(
                settings.getStringOrNull("accoutType"),
                settings.getStringOrNull("id"),
                settings.getStringOrNull("email"),
                settings.getStringOrNull("picture"),
                settings.getLong("expiresTimestamp", 0),
                settings.getStringOrNull("refreshToken"),
                settings.getStringOrNull("accessToken"),
                settings.getStringOrNull("username"),
                settings.getStringOrNull("password"),
                settings.getStringOrNull("host"),
                settings.getStringOrNull("auth"),
                settings.getStringOrNull("httpUsername"),
                settings.getStringOrNull("httpPassword")
            )
            debug("get user, expire date:" + Helper.formatDateLog(token?.expiresTimestamp.orZero()))
        }
        return token!!
    }


    fun saveToken(token: RssToken) {
        this.token = token
        settings.putString("accoutType", token.accoutType.orEmpty())
        settings.putString("id", token.id.orEmpty())
        settings.putString("email", token.email.orEmpty())
        settings.putLong("expiresTimestamp", token.expiresTimestamp)
        settings.putString("accessToken", token.accessToken.orEmpty())
        settings.putString("refreshToken", token.refreshToken.orEmpty())
        settings.putString("username", token.username.orEmpty())
        settings.putString("password", token.password.orEmpty())
        settings.putString("host", token.host.orEmpty())
        settings.putString("picture", token.picture.orEmpty())
        settings.putString("auth", token.auth.orEmpty())
        settings.putString("httpUsername", token.httpUsername.orEmpty())
        settings.putString("httpPassword", token.httpPassword.orEmpty())
    }

    fun clear() {
        settings.clear()
        token = null
    }

}