package com.baccaro.kmp.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.baccaro.kmp.GoogleMaps

@Composable
fun DetailScreen(lon: String, lat: String) {
    GoogleMaps(lat.toDouble(), lon.toDouble())
}