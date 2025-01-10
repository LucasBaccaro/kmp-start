package com.baccaro.kmp.ui.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.baccaro.kmp.GoogleMaps

@Composable
fun DetailScreen(lon: String, lat: String, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            DetailAppBar(onBack)
        }
    ) { paddingValues ->
        GoogleMaps(lat = lat.toDouble(), lon = lon.toDouble(), modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun DetailAppBar(
    onBack: () -> Unit
) {
    TopAppBar(
        title = { Text("Details") },
        navigationIcon = {
            IconButton(onClick = { onBack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    )
}