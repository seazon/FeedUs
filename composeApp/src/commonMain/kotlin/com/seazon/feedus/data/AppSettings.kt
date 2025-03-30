package com.seazon.feedus.data

import com.russhwolf.settings.Settings

class AppSettings {
    private val settings = Settings()
    private var appPreferences: AppPreferences? = null

    fun getAppPreferences(): AppPreferences {
        if (appPreferences == null) {
            appPreferences = AppPreferences(
                settings.getInt("unreadMax", 0),
            )
        }
        return appPreferences!!
    }


    fun saveAppPreferences(appPreferences: AppPreferences) {
        this.appPreferences = appPreferences
        settings.putInt("unreadMax", appPreferences.unreadMax)
    }

    fun clear() {
        settings.clear()
        appPreferences = null
    }

}

data class AppPreferences(
    val unreadMax: Int,
)