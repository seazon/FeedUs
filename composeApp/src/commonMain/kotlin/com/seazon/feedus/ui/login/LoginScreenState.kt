package com.seazon.feedus.ui.login

data class LoginScreenState(
    val isLoading: Boolean = false,
    val expired: Boolean = false,
    var host: String = "",
    val username: String = "",
    val password: String = "",
    val errorTips: String = "",
)