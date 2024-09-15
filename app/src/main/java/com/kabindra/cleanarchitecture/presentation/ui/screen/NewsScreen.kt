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
                NewsDetailScreen(newsDetailRoute = arguments)
            }
        }
    }
}

/**
 * Main composable function for displaying the news screen.
 *
 * This function collects the state of news articles from the ViewModel
 * and fetches the news when it is first launched. It displays a list
 * of news articles using a LazyColumn for efficient scrolling.
 */
@Composable
fun NewsListScreen(
    viewModel: NewsViewModel = koinViewModel(),
    innerPadding: PaddingValues,
    onNewsClicked: (Article) -> Unit
) {
    val connectivity = remember { Connectivity() }

    val isConnected by connectivity.isConnectedState.collectAsState()
    val connectionStatus by connectivity.currentNetworkConnectionState.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }

    // Collecting the state of news articles from the ViewModel
    val newsState by viewModel.newsState.collectAsState()

    if (isConnected) {
        // Fetching news when the composable is launched
        LaunchedEffect(Unit) {
            viewModel.loadNews()
        }

        showErrorDialog = false
    } else {
        showErrorDialog = true
    }

    if (showErrorDialog) {
        AlertDialog(
            title = "No Network Connection",
            message = "Please check you internet connection.\nPlease try again.",
            onConfirm = {
                showErrorDialog = false
            },
            onDismiss = {
                showErrorDialog = false
            }
        )
    }

    val news: MutableList<Article>

    when (newsState) {
        is NetworkResult.Initial -> {}
        is NetworkResult.Loading -> {
            CustomProgressDialog(isVisible = true)
        }

        is NetworkResult.Success -> {
            news = (newsState as NetworkResult.Success<News>).data.articles as MutableList<Article>

            // Text(DeviceDetails().deviceDetails())

            // Displaying the list of news articles
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(innerPadding)
                    .padding(innerPadding),
                contentPadding = PaddingValues(12.dp), // Adds padding around LazyColumn (top, bottom, start, end)
                verticalArrangement = Arrangement.spacedBy(12.dp), // Adds space between items vertically
            ) {
                items(news.size) { item ->
                    NewsItem(news[item]) { item ->
                        onNewsClicked(item)
                    }
                }
            }
        }

        is NetworkResult.Error -> Text("Error: ${(newsState as NetworkResult.Error).exception.message}")
    }
}

/**
 * Composable function to display a single news item.
 *
 * This function creates a vertical layout for the news article,
 * displaying the title, author, publication date, and description.
 * An optional image can be loaded if a URL is provided.
 */
@Composable
fun NewsItem(
    article: Article,
    onNewsClicked: (Article) -> Unit
) {
    val snackBarHostState: SnackbarHostState = koinInject()
    val scope = rememberCoroutineScope()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                scope.launch {
                    /*snackBarHostState.showSnackbar(
                        message = "You clicked ${article.title}",
                        actionLabel = "Dismiss"
                    )*/
                    onNewsClicked(article)
                }
            }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Display the title of the article with bold text and larger font size
            Text(
                text = article.title!!,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            )

            // Display the author if available with normal weight
            article.author?.let {
                Text(
                    text = "by $it",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }

            // Display the publication date with smaller font size
            Text(
                text = article.publishedAt!!,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
            )

            // Placeholder for image loading
            article.urlToImage?.let {
                // Placeholder image loading implementation
                // If you don't want to use coil, comment out this part
            }

            // Display the description if available with normal weight
            article.description?.let {
                Text(
                    text = it,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
    }
}

/**
 * Main composable function for displaying the news detail screen.
 *
 * This function collects the state of news article from the News List
 * and it displays a detail of news article.
 */
@Composable
fun NewsDetailScreen(
    newsDetailRoute: Route.NewsDetailRoute
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${newsDetailRoute.article.title} with the author of ${newsDetailRoute.article.author}"
        )
    }
}

// Preview function to display a sample news item in the Compose preview
@Preview
@Composable
fun NewsItemPreview() {
    NewsItem(
        Article(
            source = Source(id = "1", name = "Example Source"),
            author = "Author Name",
            title = "Sample Title",
            description = "Sample Description",
            url = "https://example.com",
            urlToImage = "https://example.com/image.jpg",
            publishedAt = "2024-01-01T00:00:00Z",
            content = "Sample Content"
        )
    ) {}
}