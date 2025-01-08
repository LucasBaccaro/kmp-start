package com.baccaro.kmp.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val state = homeViewModel.listState.collectAsStateWithLifecycle()
    Text(state.value.toString())
}