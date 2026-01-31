package com.seazon.feedus

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.seazon.feedus.ui.articles.ArticlesScreen
import com.seazon.feedus.ui.demo.DemoScreen
import com.seazon.feedus.ui.demo.AIScreen
import com.seazon.feedus.ui.feeds.FeedsScreen
import com.seazon.feedus.ui.login.LoginScreen

object A {
    var categoryId: String? = null
    var feedId: String? = null
    var starred: Boolean = false
    var labelId: String? = null
}

@Composable
fun App() {
    val navController: NavHostController = rememberNavController()
    val uriHandler = LocalUriHandler.current
    NavHost(
        navController = navController,
        startDestination = Screen.Feeds.name,
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
                navToArticles = { categoryId, feedId, starred, labelId ->
                    A.categoryId = categoryId
                    A.feedId = feedId
                    A.starred = starred
                    A.labelId = labelId
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
                categoryId = A.categoryId,
                feedId = A.feedId,
                starred = A.starred,
                labelId = A.labelId,
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
                navBack = {
                    navController.popBackStack()
                },
                navToAI = {
                    navController.navigate(Screen.AI.name)
                },
            )
        }
        composable(route = Screen.AI.name) {
            AIScreen()
        }
    }
}
