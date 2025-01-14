package com.baccaro.kmp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baccaro.kmp.domain.model.Coordinates
import com.baccaro.kmp.domain.model.PostModel
import com.baccaro.kmp.domain.model.UserModel
import com.baccaro.kmp.presentation.HomeViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
@Preview
fun App() {
    MaterialTheme {
        HomeScreen(
            onNewsClick = {},
            onUserLocationClick = {}
        )
    }
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNewsClick: (PostModel) -> Unit,
    onUserLocationClick: (UserModel) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        /*SearchBar(
            query = state.searchQuery,
            onQueryChange = viewModel::onSearchQueryChanged,
            modifier = Modifier.fillMaxWidth()
        )*/

        TabRow(
            selectedTabIndex = state.selectedTab.ordinal
        ) {
            com.baccaro.kmp.presentation.Tab.entries.forEach { tab ->
                Tab(
                    selected = state.selectedTab == tab,
                    onClick = { viewModel.onTabSelected(tab) },
                    text = { Text(text = tab.name) }
                )
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        state.error?.let { error ->
            Text(
                text = error,
                modifier = Modifier.padding(16.dp)
            )
        }

        when (state.selectedTab) {
            com.baccaro.kmp.presentation.Tab.NEWS -> NewsList(
                news = state.news,
                onNewsClick = onNewsClick
            )

            com.baccaro.kmp.presentation.Tab.USERS -> UsersList(
                users = state.users,
                onUserLocationClick = onUserLocationClick
            )
        }
    }
}

@Composable
fun NewsList(
    news: List<PostModel>,
    onNewsClick: (PostModel) -> Unit
) {
    LazyColumn {
        items(news) { newsItem ->
            NewsItem(
                news = newsItem,
                onClick = { onNewsClick(newsItem) }
            )
        }
    }
}

@Composable
fun UsersList(
    users: List<UserModel>,
    onUserLocationClick: (UserModel) -> Unit
) {
    LazyColumn {
        items(users) { user ->
            UserItem(
                user = user,
                onLocationClick = { onUserLocationClick(user) }
            )
        }
    }
}


@Composable
fun NewsItem(
    news: PostModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Thumbnail
            Text(news.thumbnail)

            // Contenido
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = news.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = news.content,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = news.category,
                )
            }
        }
    }
}

@Composable
fun UserItem(
    user: UserModel,
    onLocationClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = user.fullName,
                    )

                    Text(
                        text = user.email,
                    )

                    Text(
                        text = user.city,
                    )
                }

                IconButton(
                    onClick = onLocationClick
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Ver ubicación",
                    )
                }
            }
        }
    }
}

// Preview
@Composable
fun NewsItemPreview() {
    NewsItem(
        news = PostModel(
            id = 1,
            title = "Este es un título de ejemplo largo para ver cómo se ve",
            content = "Este es un contenido de ejemplo largo para ver cómo se corta el texto cuando es demasiado largo y necesita más de dos líneas",
            thumbnail = "",
            category = "Tecnología"
        ),
        onClick = {}
    )
}

@Composable
fun UserItemPreview() {
    UserItem(
        user = UserModel(
            id = 1,
            fullName = "John Doe",
            email = "john.doe@example.com",
            city = "New York",
            location = Coordinates(40.7128, -74.0060)
        ),
        onLocationClick = {}
    )
}