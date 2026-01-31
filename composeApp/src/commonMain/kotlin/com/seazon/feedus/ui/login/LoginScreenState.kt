package com.seazon.feedus.ui.login

import com.seazon.feedme.lib.LocalConstants

data class LoginScreenState(
    val isLoading: Boolean = false,
    val expired: Boolean = false,
    var host: String = LocalConstants.LOGIN_HOST,
    val username: String = LocalConstants.LOGIN_USERNAME,
    val password: String = LocalConstants.LOGIN_PASSWORD,
    val errorTips: String = "",
)