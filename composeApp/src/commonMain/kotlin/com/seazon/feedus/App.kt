package com.seazon.feedus

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.seazon.feedus.ui.articles.ArticlesScreen
import com.seazon.feedus.ui.demo.DemoScreen
import com.seazon.feedus.ui.demo.TranslatorScreen
import com.seazon.feedus.ui.feeds.FeedsScreen
import com.seazon.feedus.ui.login.LoginScreen

object A {
    var categoryId: String? = null
    var feedId: String? = null
}

@Composable
fun App() {
    val navController: NavHostController = rememberNavController()
    val uriHandler = LocalUriHandler.current

    NavHost(
        navController = navController,
        startDestination = Screen.Feeds.name,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        composable(route = Screen.Login.name) {
            LoginScreen(
                navToFeeds = {
                    navController.popBackStack()
                },
                navToDemo = {
                    navController.navigate(Screen.Demo.name)
                },
                navToExternalUrl = {
                    uriHandler.openUri(it)
                }
            )
        }
        composable(route = Screen.Feeds.name) {
            FeedsScreen(
                navToArticles = { categoryId, feedId ->
                    A.categoryId = categoryId
                    A.feedId = feedId
                    navController.navigate(Screen.Articles.name)
                },
                navToLogin = {
                    navController.navigate(Screen.Login.name)
                },
                navToDemo = {
                    navController.navigate(Screen.Demo.name)
                },
            )
        }
        composable(route = Screen.Articles.name) {
            ArticlesScreen(
                A.categoryId,
                A.feedId,
                navBack = {
                    navController.popBackStack()
                },
                navToArticle = {
                    uriHandler.openUri(it.link.orEmpty())
                }
            )
        }
        composable(route = Screen.Demo.name) {
            DemoScreen(
                navToTranslator = {
                    navController.navigate(Screen.Translator.name)
                }
            )
        }
        composable(route = Screen.Translator.name) {
            TranslatorScreen()
        }
    }
}
