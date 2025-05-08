package com.seazon.feedus

import feedus.composeapp.generated.resources.*
import org.jetbrains.compose.resources.StringResource

enum class Screen(val title: StringResource) {
    Login(title = Res.string.login_login),
    Feeds(title = Res.string.feeds_title),
    Articles(title = Res.string.articles_title),
    Demo(title = Res.string.demo_title),
    Translator(title = Res.string.translator_title),
    Summary(title = Res.string.summary_title),
}