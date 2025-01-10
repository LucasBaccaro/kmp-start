package com.baccaro.kmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.baccaro.kmp.presentation.HomeViewModel
import com.baccaro.kmp.ui.detail.DetailScreen
import com.baccaro.kmp.ui.home.HomeScreen
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        MapComponent()
    }
}

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
            HomeScreen(homeViewModel, onItemTap = { lon, lat ->
                navController.navigate(Detail(lon, lat))
            })
        }
        composable<Detail> { backStackEntry ->
            val args = backStackEntry.toRoute<Detail>()
            DetailScreen(args.lon, args.lat)
        }
    }
}

@Serializable
object Home

@Serializable
data class Detail(val lon: String, val lat: String)