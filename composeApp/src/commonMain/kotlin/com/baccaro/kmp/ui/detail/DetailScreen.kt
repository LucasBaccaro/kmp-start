package com.baccaro.kmp.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun DetailScreen(lon: String, lat: String) {
    Column {
        Text(lon)
        Text(lat)
    }
}