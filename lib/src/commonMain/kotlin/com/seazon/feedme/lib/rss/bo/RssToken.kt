package com.seazon.feedme.lib.rss.bo

import com.seazon.feedme.lib.rss.service.Static
import kotlinx.serialization.Serializable

@Serializable
data class RssToken(
    var accoutType: String? = null,
    var id: String? = null,
    var email: String? = null,
    var picture: String? = null,
    var expiresTimestamp: Long = 0,
    // oauth2
    var refreshToken: String? = null,
    var accessToken: String? = null,
    // basic
    var username: String? = null,
    var password: String? = null,
    var host: String? = null, // not end with / when save
    var auth: String? = null,  // for save basic auth value, then no need to save password for token never expired case
    // HTTP Basic Authentication
    var httpUsername: String? = null,
    var httpPassword: String? = null,
) : Entity() {

    fun isAuthed(): Boolean {
        return Static.ACCOUNT_TYPE_LOCAL_RSS == accoutType
                || !auth.isNullOrEmpty()
                || !accessToken.isNullOrEmpty()
    }

    fun getAccountTypeName(): String? {
        return if (Static.ACCOUNT_TYPE_INOREADER_OAUTH2 == accoutType)
            Static.ACCOUNT_TYPE_INOREADER
        else
            accoutType
    }
}