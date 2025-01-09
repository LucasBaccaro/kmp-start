package com.baccaro.kmp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baccaro.kmp.domain.model.ItemModel
import com.baccaro.kmp.domain.usecase.GetListUseCase
import com.baccaro.kmp.domain.usecase.SearchListUseCase
import com.baccaro.kmp.domain.usecase.UpdateFavoriteUseCase
import com.baccaro.kmp.util.OperationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeViewModel() : ViewModel(), KoinComponent {
    private val getListUseCase: GetListUseCase by inject()
    private val searchListUseCase: SearchListUseCase by inject()
    private val updateFavoriteUseCase: UpdateFavoriteUseCase by inject()

    private val _listState = MutableStateFlow(HomeUiState())
    val listState: StateFlow<HomeUiState> = _listState

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    init {
        loadList()
    }

    fun loadList() {
        viewModelScope.launch {
            _listState.update { it.copy(isLoading = true) }
            try {
                val result = getListUseCase()
                _listState.update {
                    when (result) {
                        is OperationResult.Success -> {
                            it.copy(isLoading = false, data = result.data, error = null)
                        }
                        is OperationResult.Error -> {
                            it.copy(isLoading = false, error = result.exception)
                        }
                    }
                }
            } catch (e: Exception) {
                _listState.update { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
            }
        }
    }

    fun search(text: String) {
        viewModelScope.launch {
            _listState.update { it.copy(isLoading = true) }
            try {
                val result = searchListUseCase(text)
                _listState.update {
                    when (result) {
                        is OperationResult.Success -> {
                            it.copy(isLoading = false, data = result.data, error = null)
                        }
                        is OperationResult.Error -> {
                            it.copy(isLoading = false, error = result.exception)
                        }
                    }
                }
            } catch (e: Exception) {
                _listState.update { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
            }
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.update { text }
        search(text)
    }

    fun toggleFavorite(item: ItemModel, isFavorite: Boolean) {
        viewModelScope.launch {
            updateFavoriteUseCase(item._id, isFavorite)
            updateItem(item.copy(isFavorite = isFavorite))
        }
    }

    private fun updateItem(item: ItemModel){
        val currentList = _listState.value.data.toMutableList()
        val index = currentList.indexOfFirst { it._id == item._id }
        if (index != -1) {
            currentList[index] = item
            _listState.update { it.copy(data = currentList) }
        }
    }
}

data class HomeUiState(
    val isLoading: Boolean = true,
    val data: List<ItemModel> = emptyList(),
    val error: String? = null,
    val loadingMessage: String = ""
)