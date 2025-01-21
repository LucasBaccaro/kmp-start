package com.baccaro.kmp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
expect fun GoogleMaps(lat: Double, lon: Double, modifier: Modifier = Modifier)

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigation()
    }
}
