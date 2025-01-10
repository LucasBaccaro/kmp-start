package com.baccaro.kmp

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import cocoapods.GoogleMaps.GMSCameraPosition
import cocoapods.GoogleMaps.GMSCameraUpdate
import cocoapods.GoogleMaps.GMSMapView
import cocoapods.GoogleMaps.GMSMarker
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLLocationCoordinate2DMake

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun GoogleMaps(lat: Double, lon: Double,modifier: Modifier) {
    val mapView = remember { GMSMapView() }

    // 1. Configurar la cámara
    val cameraPosition = GMSCameraPosition.cameraWithLatitude(
        latitude = lat,
        longitude = lon,
        zoom = 10.0f
    )
    val cameraUpdate = GMSCameraUpdate.setCamera(cameraPosition)
    mapView.moveCamera(cameraUpdate)

    // 2. Crear el marcador y asociarlo al mapa
    val marker = GMSMarker.markerWithPosition(CLLocationCoordinate2DMake(lat, lon))
    marker.map = mapView // <<-- ESTE ES EL CAMBIO CRUCIAL
    // mapView.selectedMarker = marker // No necesario para mostrar el marcador
    // mapView.setSelectedMarker(marker) // No necesario para mostrar el marcador


    UIKitView(
        modifier = modifier,
        factory = { mapView }
    )
}