package com.baccaro.kmp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baccaro.kmp.domain.model.ItemModel

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val state = homeViewModel.listState.collectAsStateWithLifecycle()
    val searchText = homeViewModel.searchText.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = searchText.value,
            onValueChange = homeViewModel::onSearchTextChange,
            label = { Text("Buscar") },
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (state.value.isLoading) {
            Text(text = "Cargando...")
        } else if (state.value.data.isNotEmpty()) {
            LazyColumn {
                val sortedList = state.value.data.sortedWith(compareBy({ it.name }, { it.country }))
                items(sortedList) { item ->
                    ItemView(
                        item,
                        onItemTap = { },
                        toggleFavorite = { homeViewModel.toggleFavorite(item, !item.isFavorite) })
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
            Text(text = "Lon: ${item.coord.lon}, Lat: ${item.coord.lat}")
        }
        IconButton(onClick = toggleFavorite) {
            Icon(
                imageVector = if (item.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Toggle Favorite"
            )
        }
    }
}