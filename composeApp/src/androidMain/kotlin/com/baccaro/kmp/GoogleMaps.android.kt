package com.baccaro.kmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
actual fun GoogleMaps(lat: Double, lon: Double,modifier: Modifier) {
    val place = LatLng(lat, lon)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(place, 10f)
    }
    val markerState = rememberMarkerState(position = place)
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState
    ){
        Marker(markerState)
    }
}