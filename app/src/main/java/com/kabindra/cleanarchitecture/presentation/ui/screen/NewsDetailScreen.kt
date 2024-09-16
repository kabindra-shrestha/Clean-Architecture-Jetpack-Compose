@file:OptIn(KoinExperimentalAPI::class)

package com.kabindra.cleanarchitecture.presentation.ui.screen

import Route
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.ImageRequest.*
import coil3.request.crossfade
import com.kabindra.cleanarchitecture.R
import com.kabindra.cleanarchitecture.domain.entity.Article
import com.kabindra.cleanarchitecture.domain.entity.Source
import org.koin.compose.koinInject
import org.koin.core.annotation.KoinExperimentalAPI

/**
 * Main composable function for displaying the news detail screen.
 *
 * This function collects the state of news article from the News List
 * and it displays a detail of news article.
 */
@Composable
fun NewsDetailScreen(
    innerPadding: PaddingValues,
    newsDetailRoute: Route.NewsDetailRoute
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .consumeWindowInsets(innerPadding)
            .padding(innerPadding),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            NewsDetail(newsDetailRoute.article)
        }
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
fun NewsDetail(
    article: Article
) {
    val snackBarHostState: SnackbarHostState = koinInject()
    val scope = rememberCoroutineScope()
    Card(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Placeholder for image loading
            article.urlToImage?.let {
                // Placeholder image loading implementation
                // If you don't want to use coil, comment out this part
                AsyncImage(
                    model = Builder(LocalContext.current)
                        .data(article.urlToImage)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = stringResource(R.string.app_name),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(5.dp))
                )
            }

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

// Preview function to display a sample news item in the Compose preview
@Preview
@Composable
fun NewsDetailPreview() {
    NewsDetail(
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
    )
}