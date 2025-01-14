package com.baccaro.kmp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.baccaro.kmp.presentation.HomeViewModel
import com.baccaro.kmp.presentation.NewsDetailViewModel
import com.baccaro.kmp.presentation.UserLocationViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

// Routes
@kotlinx.serialization.Serializable
object Home

@kotlinx.serialization.Serializable
data class NewsDetail(val newsId: Int)

@Serializable
data class UserLocation(val userId: Int)

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = Home
    ) {
        composable<Home> {
            val homeViewModel: HomeViewModel = koinViewModel()
            HomeScreen(
                viewModel = homeViewModel,
                onNewsClick = { newsId ->
                    navController.navigate(NewsDetail(newsId))
                },
                onUserLocationClick = { userId ->
                    navController.navigate(UserLocation(userId))
                }
            )
        }

        composable<NewsDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<NewsDetail>()
            val viewModel: NewsDetailViewModel = koinViewModel()
            NewsDetailScreen(
                newsId = args.newsId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<UserLocation> { backStackEntry ->
            val args = backStackEntry.toRoute<UserLocation>()
            val viewModel: UserLocationViewModel = koinViewModel()
            UserLocationScreen(
                userId = args.userId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}



// Screens
@Composable
fun NewsDetailScreen(
    newsId: Int,
    viewModel: NewsDetailViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(newsId) {
        viewModel.loadNewsDetails(newsId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de noticia") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
                state.error != null -> Text(
                    text = state.error!!,
                    modifier = Modifier.padding(16.dp)
                )
                state.news != null -> {
                    Text(state.news.toString())
                }
            }
        }
    }
}

@Composable
fun UserLocationScreen(
    userId: Int,
    viewModel: UserLocationViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.loadUserDetails(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ubicación del usuario") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
                state.error != null -> Text(
                    text = state.error!!,
                    modifier = Modifier.padding(16.dp)
                )
                state.user != null -> {
                    Text(state.user.toString())
                }
            }
        }
    }
}