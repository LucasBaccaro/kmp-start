package com.baccaro.kmp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.baccaro.kmp.ui.HomeScreen
import com.baccaro.kmp.ui.HomeViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import generickmp.composeapp.generated.resources.Res
import generickmp.composeapp.generated.resources.compose_multiplatform
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigation()
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
            HomeScreen(homeViewModel)
        }
    }
}

@Serializable
object Home