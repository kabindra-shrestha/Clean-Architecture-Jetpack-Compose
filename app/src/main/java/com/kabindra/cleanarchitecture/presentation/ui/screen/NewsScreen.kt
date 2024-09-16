@file:OptIn(KoinExperimentalAPI::class)

package com.kabindra.cleanarchitecture.presentation.ui.screen

import Route
import Screens
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kabindra.cleanarchitecture.domain.entity.Article
import com.kabindra.cleanarchitecture.domain.entity.News
import com.kabindra.cleanarchitecture.domain.entity.Source
import com.kabindra.cleanarchitecture.presentation.ui.component.AlertDialog
import com.kabindra.cleanarchitecture.presentation.ui.component.CustomProgressDialog
import com.kabindra.cleanarchitecture.presentation.ui.component.TopAppBarComponent
import com.kabindra.cleanarchitecture.presentation.viewmodel.NewsViewModel
import com.kabindra.cleanarchitecture.utils.Connectivity
import com.kabindra.cleanarchitecture.utils.NetworkResult
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import kotlin.reflect.typeOf

@Composable
fun NewsScreen(
    navController: NavHostController = rememberNavController()
) {
    val snackBarHostState: SnackbarHostState = koinInject()

// Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
// Get the name of the current screen
    val currentRoute = backStackEntry?.destination?.route ?: ""
    val currentScreen =
        if (currentRoute.contains("Route.NewsListRoute")) {
            Screens.News.title
        } else if (currentRoute.contains("Route.NewsDetailRoute")) {
            Screens.NewsDetails.title
        } else {
            ""
        }

    Scaffold(
        topBar = {
            TopAppBarComponent(
                title = currentScreen,
                iconButton = Icons.Filled.ArrowBack,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.NewsListRoute,
            modifier = Modifier
                .fillMaxSize()
            // .verticalScroll(rememberScrollState())
            // .padding(innerPadding)
        ) {
            composable<Route.NewsListRoute> {
                NewsListScreen(innerPadding = innerPadding)
                {
                    navController.navigate(Route.NewsDetailRoute(article = it))
                }
            }
            composable<Route.NewsDetailRoute>(
                typeMap = mapOf(
                    typeOf<Article>() to NewsNavType.ArticleType
                )
            ) { entry ->
                val arguments = entry.toRoute<Route.NewsDetailRoute>()
                NewsDetailScreen(innerPadding = innerPadding, newsDetailRoute = arguments)
            }
        }
    }
}