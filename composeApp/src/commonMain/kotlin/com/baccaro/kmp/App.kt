package com.baccaro.kmp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.annotation.ExperimentalCoilApi
import com.baccaro.kmp.domain.model.Coordinates
import com.baccaro.kmp.domain.model.PostModel
import com.baccaro.kmp.domain.model.Tab
import com.baccaro.kmp.domain.model.UserModel
import com.baccaro.kmp.presentation.HomeState
import com.baccaro.kmp.presentation.HomeViewModel
import com.seiko.imageloader.rememberImagePainter
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
expect fun GoogleMaps(lat: Double, lon: Double, modifier: Modifier = Modifier)

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigation()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNewsClick: (Int) -> Unit,
    onUserLocationClick: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Noticias y Usuarios") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = state.selectedTab.ordinal,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab.entries.forEach { tab ->
                    Tab(
                        selected = state.selectedTab == tab,
                        onClick = { viewModel.onTabSelected(tab) },
                        text = { Text(text = tab.name) }
                    )
                }

            }
            HomeTabContent(
                state = state,
                onSearchQueryChanged = viewModel::onSearchQueryChanged,
                onNewsClick = onNewsClick,
                onUserLocationClick = onUserLocationClick
            )
            state.error?.let {
                Text(
                    "Error: $it",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun HomeTabContent(
    state: HomeState,
    onSearchQueryChanged: (String) -> Unit,
    onNewsClick: (Int) -> Unit,
    onUserLocationClick: (Int) -> Unit
) {
    when (state.selectedTab) {
        Tab.NEWS -> NewsTab(
            news = state.news,
            searchQuery = state.searchQuery,
            onSearchQueryChanged = onSearchQueryChanged,
            onNewsClick = onNewsClick
        )

        Tab.USERS -> UsersList(
            users = state.users,
            onUserLocationClick = onUserLocationClick
        )
    }
}

@Composable
private fun NewsTab(
    news: List<PostModel>,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onNewsClick: (Int) -> Unit
) {
    Column {
        SearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChanged,
            modifier = Modifier.fillMaxWidth()
        )
        NewsList(
            news = news,
            onNewsClick = onNewsClick
        )
    }
}


@Composable
fun NewsList(news: List<PostModel>, onNewsClick: (Int) -> Unit) {
    LazyColumn {
        items(news) { newsItem ->
            NewsItem(news = newsItem, onClick = { onNewsClick(newsItem.id) })
        }
    }
}


@Composable
fun UsersList(users: List<UserModel>, onUserLocationClick: (Int) -> Unit) {
    LazyColumn {
        items(users) { user ->
            UserItem(user = user, onLocationClick = { onUserLocationClick(user.id) })
        }
    }
}

@Composable
fun NewsItem(news: PostModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val painter = rememberImagePainter(news.thumbnail)
            Image(
                painter = painter,
                contentDescription = "image",
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))


                Text(
                    text = news.content,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )


            }
        }
    }
}

@Composable
fun UserItem(user: UserModel, onLocationClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { }, // Make the whole card clickable, if needed
        shape = RoundedCornerShape(8.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Add a placeholder/default image if user image is not available
            Image(
                imageVector = Icons.Default.Person,  // Replace with actual image logic if available
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color.LightGray) // Placeholder background
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(text = user.fullName, fontWeight = FontWeight.Bold)
                Text(text = user.email)
                Text(text = user.city) // Now included
            }


            IconButton(onClick = onLocationClick) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Location")
            }
        }
    }
}


@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .padding(16.dp),
        placeholder = {
            Text(
                text = "Buscar...",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Limpiar búsqueda",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
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