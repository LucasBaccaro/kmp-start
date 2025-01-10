package com.baccaro.kmp.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baccaro.kmp.GoogleMaps
import com.baccaro.kmp.domain.model.ItemModel
import com.baccaro.kmp.presentation.HomeViewModel

@Composable
fun HomeScreen(homeViewModel: HomeViewModel, onItemTap: (String, String) -> Unit) {
    val state = homeViewModel.listState.collectAsStateWithLifecycle()
    val searchText by homeViewModel.searchText.collectAsStateWithLifecycle()

    // Estado para las coordenadas seleccionadas
    var selectedLat by remember { mutableStateOf(0.0) }
    var selectedLon by remember { mutableStateOf(0.0) }


    BoxWithConstraints(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        val isLandscape = maxWidth > maxHeight

        Column {
            TextField(
                value = searchText,
                onValueChange = homeViewModel::onSearchTextChange,
                label = { Text("Buscar") },
                modifier = Modifier.padding(bottom = 8.dp)
            )
            if (isLandscape) {
                Row(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(end = 8.dp)
                    ) {
                        if (state.value.isLoading) {
                            item {
                                Text(text = "Cargando...")
                            }
                        } else if (state.value.data.isNotEmpty()) {
                            val sortedList = state.value.data.sortedWith(compareBy({ it.name }, { it.country }))
                            items(sortedList) { item ->
                                ItemView(
                                    item,
                                    onItemTap = {
                                        // Actualizar las coordenadas del mapa cuando se hace clic
                                        selectedLat = item.coord.lat
                                        selectedLon = item.coord.lon
                                    },
                                    toggleFavorite = { homeViewModel.toggleFavorite(item, !item.isFavorite) }
                                )
                            }
                        }
                    }

                    // Aquí actualizamos el mapa con las coordenadas nuevas
                    key(selectedLat,selectedLon) {
                        GoogleMaps(
                            lat = selectedLat,
                            lon = selectedLon,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }
                }
            } else {
                if (state.value.isLoading) {
                    Text(text = "Cargando...")
                } else if (state.value.data.isNotEmpty()) {
                    LazyColumn {
                        val sortedList = state.value.data.sortedWith(compareBy({ it.name }, { it.country }))
                        items(sortedList) { item ->
                            ItemView(
                                item,
                                onItemTap = {
                                    onItemTap(item.coord.lon.toString(), item.coord.lat.toString())
                                },
                                toggleFavorite = { homeViewModel.toggleFavorite(item, !item.isFavorite) }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ItemView(item: ItemModel, onItemTap: () -> Unit, toggleFavorite: () -> Unit) {
    Row(modifier = Modifier.clickable(onClick = onItemTap)) {
        Column(modifier = Modifier.weight(1f)) {
            Text("${item.name}, ${item.country}")
        }
        IconButton(onClick = toggleFavorite) {
            Icon(
                imageVector = if (item.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Toggle Favorite"
            )
        }
    }
}