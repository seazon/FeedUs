package com.seazon.feedus.ui.login

import androidx.lifecycle.viewModelScope
import com.seazon.feedme.lib.rss.service.Static
import com.seazon.feedme.lib.network.HttpException
import com.seazon.feedme.lib.rss.bo.RssToken
import com.seazon.feedme.lib.rss.service.RssApi
import com.seazon.feedme.lib.rss.service.SelfHostedRssApi
import com.seazon.feedme.lib.utils.LogUtils
import com.seazon.feedme.lib.utils.orZero
import com.seazon.feedus.data.RssSDK
import com.seazon.feedus.data.TokenSettings
import com.seazon.feedus.ui.BaseViewModel
import feedus.composeapp.generated.resources.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class Event {
    data object OAuthSuccess : Event()
    data class OAuthFailed(val message: String) : Event()
}

class LoginViewModel(
    val rssSDK: RssSDK,
    val tokenSettings: TokenSettings,
) : BaseViewModel() {

    val host: String?
        get() = if (selfHosted) (api as SelfHostedRssApi).getDefaultHost() else null

    private val _state = MutableStateFlow(LoginScreenState().apply {
        if (!this@LoginViewModel.host.isNullOrEmpty()) host = this@LoginViewModel.host.orEmpty()
    })
    val state: StateFlow<LoginScreenState> = _state

    private val _eventFlow = MutableStateFlow<Event?>(null)
    val eventFlow: StateFlow<Event?> = _eventFlow

    val api: RssApi
        get() = rssSDK.getRssApiStatic()

    val selfHosted: Boolean
        get() = api is SelfHostedRssApi

    fun loginForLocalRss(
        onSuccess: () -> Unit,
    ) {
        setTokenAccountTypeAndResetApi(Static.ACCOUNT_TYPE_LOCAL_RSS)
        val token = tokenSettings.getToken()
        token.expiresTimestamp = System.currentTimeMillis() + 1000L * 3600 * 24 * 365 * 99
        tokenSettings.saveToken(token)
        onSuccess()
    }

    fun getUserInfo(
        host: String?,
        username: String,
        password: String,
        httpUsername: String,
        httpPassword: String,
        onSuccess: () -> Unit,
    ) {
        if (username.isEmpty() || password.isEmpty()) return

        var newHost: String? = null
        if (selfHosted) {
            if (host.isNullOrEmpty()) return

            newHost = if (host.endsWith("/")) {
                host.substring(0, host.length.orZero() - 1)
            } else {
                host
            }
            newHost = (api as SelfHostedRssApi).onHostSet(newHost)
        }
        _state.update {
            it.copy(errorTips = "", isLoading = true)
        }
        val token = tokenSettings.getToken()
        token.username = username
        token.password = password
        token.httpUsername = httpUsername
        token.httpPassword = httpPassword
        token.host = newHost
        viewModelScope.launch {
            tokenSettings.saveToken(token)
            getUserInfo(
                onSuccess = {
                    _state.update {
                        it.copy(isLoading = false)
                    }
                    onSuccess()
                },
                onError = { e ->
                    _state.update {
                        it.copy(isLoading = false, errorTips = e.getHumanMessage())
                    }
                }
            )
        }
    }

    suspend fun getUserInfo(
        onSuccess: () -> Unit,
        onError: (e: HttpException) -> Unit
    ) {
        try {
            process {
                val token = tokenSettings.getToken()
                val api = rssSDK.getRssApi(true)
                api.setUserInfo(token)
//                updateProvider(token)
                tokenSettings.saveToken(token)
//                checkAndUpdateSyncMode(api)
            }
            if (tokenSettings.getToken().isAuthed()) {
                onSuccess()
            } else {
                onError(
                    HttpException(
                        HttpException.Type.ELOCAL,
                        "Auth failed due to token is empty"
                    )
                )
            }
        } catch (e: HttpException) {
            LogUtils.error(e)
            onError(e)
        }
    }

    fun setTokenAccountTypeAndResetApi(accountType: String) {
        var token = RssToken()
        if (state.value.expired) {
            token = tokenSettings.getToken()
        }
        token.accoutType = accountType
        tokenSettings.saveToken(token)
        rssSDK.resetApi()
        if (!host.isNullOrEmpty()) {
            _state.update {
                it.copy(host = host.orEmpty())
            }
        }
    }

    fun startOAuth(accountType: String, navToExternalUrl: (url: String) -> Unit) {
        setTokenAccountTypeAndResetApi(accountType)
        val rssApi = rssSDK.getRssApiStatic()
        rssApi.getOAuth2Url("")?.let {
            navToExternalUrl(it)
        }
    }

    fun onOAuth(url: String?) {
        url ?: return
        var code: String? = null
        val url = url.substring(url.indexOf("?") + 1)
        val a = url.split("&".toRegex()).toTypedArray()
        for (i in a.indices) {
            if (a[i].startsWith("code=")) {
                code = a[i].substring(5)
                break
            }
        }
        if (code.isNullOrEmpty()) return

        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            refreshTokenAndGetUserInfo(code, onSuccess = {
                _state.update {
                    it.copy(isLoading = false)
                }
                _eventFlow.value = Event.OAuthSuccess
            }, onError = {
                _state.update {
                    it.copy(isLoading = false)
                }
                _eventFlow.value = Event.OAuthFailed(it.getHumanMessage())
            })
        }
    }


    suspend fun refreshTokenAndGetUserInfo(
        code: String,
        onSuccess: () -> Unit,
        onError: (e: HttpException) -> Unit
    ) {
        try {
            val rssApi = rssSDK.getRssApiStatic()
            LogUtils.debug("get code:$code")
            val getTokenResponse = rssApi.getRefreshToken(code)
            val token = rssApi.getToken()
            rssApi.setUserWithRefreshToken(token!!, getTokenResponse.orEmpty())
            rssApi.setUserInfo(token)
//                updateProvider(token)
            tokenSettings.saveToken(token)
            rssApi.setToken(token)
//                checkAndUpdateSyncMode(rssApi)
            if (tokenSettings.getToken().isAuthed()) {
                onSuccess()
            } else {
                onError(
                    HttpException(
                        HttpException.Type.ELOCAL,
                        "Auth failed due to token is empty"
                    )
                )
            }
        } catch (e: HttpException) {
            LogUtils.error(e)
            onError(e)
        }
    }

    companion object {
        val SERVICE_LIST = mutableListOf(
            LoginRssModel(
                "Feedly",
                Static.ACCOUNT_TYPE_FEEDLY,
                RssTypeGroup.SERVICE,
                Res.drawable.ic_service_feedly,
                listOf("OAuth")
            ),
            LoginRssModel(
                "Inoreader",
                Static.ACCOUNT_TYPE_INOREADER_OAUTH2,
                RssTypeGroup.SERVICE,
                Res.drawable.ic_service_inoreader,
                listOf("OAuth")
            ),
            LoginRssModel("Inoreader", Static.ACCOUNT_TYPE_INOREADER, RssTypeGroup.SERVICE, Res.drawable.ic_service_inoreader),
            LoginRssModel("BazQux", Static.ACCOUNT_TYPE_BAZQUX_READER, RssTypeGroup.SERVICE, Res.drawable.ic_service_bazqux),
            LoginRssModel(
                "The Old Reader",
                Static.ACCOUNT_TYPE_THE_OLD_READER,
                RssTypeGroup.SERVICE,
                Res.drawable.ic_service_theoldreader
            ),
            LoginRssModel("Feedbin", Static.ACCOUNT_TYPE_FEEDBIN, RssTypeGroup.SERVICE, Res.drawable.ic_service_feedbin),
            LoginRssModel("Tiny Tiny RSS", Static.ACCOUNT_TYPE_TTRSS, RssTypeGroup.SELF_HOST, Res.drawable.ic_service_ttrss),
            LoginRssModel(
                "FreshRSS",
                Static.ACCOUNT_TYPE_FRESH_RSS,
                RssTypeGroup.SELF_HOST,
                Res.drawable.ic_service_freshrss,
                listOf("Google Reader API")
            ),
            LoginRssModel("Fever API", Static.ACCOUNT_TYPE_FEVER, RssTypeGroup.SELF_HOST, Res.drawable.ic_service_fever),
            LoginRssModel(
                "Miniflux",
                Static.ACCOUNT_TYPE_MINIFLUX,
                RssTypeGroup.SELF_HOST,
                Res.drawable.ic_service_miniflux,
                listOf("Fever API")
            ),
            LoginRssModel(
                "Google Reader API",
                Static.ACCOUNT_TYPE_GOOGLE_READER_API,
                RssTypeGroup.SELF_HOST,
                Res.drawable.ic_service_google
            ),
            LoginRssModel(
                "Local RSS",
                Static.ACCOUNT_TYPE_LOCAL_RSS,
                RssTypeGroup.LOCAL,
                tags = listOf("beta")
            ),
        ).sortedBy { it.name }
    }
}