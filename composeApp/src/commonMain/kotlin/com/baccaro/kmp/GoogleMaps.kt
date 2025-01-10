package com.baccaro.kmp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun GoogleMaps(lat:Double,lon:Double,modifier: Modifier = Modifier)