package com.baccaro.kmp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val state by homeViewModel.listState.collectAsStateWithLifecycle()
    val showLoading = remember { mutableStateOf(true) }

    LaunchedEffect(state) {
        if (state.isLoading) {
            showLoading.value = true
        } else {
            delay(300) // Pequeño retraso para asegurar la visibilidad del estado de carga
            showLoading.value = false
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        when {
            showLoading.value -> {
                ShimmerEffect()
            }
            state.data.isNotEmpty() -> {
                Text(text = "Cantidad de Items: ${state.data.size}")
            }
            state.error != null -> {
                Text(text = "Error: ${state.error}")
            }
            else -> {
                Text(text = "No hay datos disponibles.")
            }
        }
    }
}

@Composable
fun ShimmerEffect() {
    // Implementación de un efecto shimmer o placeholder
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .background(Color.Gray.copy(alpha = 0.3f))
    )
}