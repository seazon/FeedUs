package com.seazon.feedus.ui.login

import org.jetbrains.compose.resources.DrawableResource

class LoginRssModel(
    val name: String,
    val type: String,
    val group: RssTypeGroup = RssTypeGroup.SERVICE,
    val logo: DrawableResource? = null,
    val tags: List<String>? = null,
)

enum class RssTypeGroup {
    SERVICE, SELF_HOST, LOCAL
}