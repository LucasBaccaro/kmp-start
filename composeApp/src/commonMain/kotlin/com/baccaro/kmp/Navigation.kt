package com.baccaro.kmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.baccaro.kmp.presentation.HomeViewModel
import com.baccaro.kmp.presentation.NewsDetailViewModel
import com.baccaro.kmp.presentation.UserLocationViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

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

@Serializable
object Home

@Serializable
data class NewsDetail(val newsId: Int)

@Serializable
data class UserLocation(val userId: Int)