package com.baccaro.kmp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baccaro.kmp.domain.model.PostModel
import com.baccaro.kmp.domain.model.Tab
import com.baccaro.kmp.domain.model.UserModel
import com.baccaro.kmp.domain.usecase.GetNewsUseCase
import com.baccaro.kmp.domain.usecase.GetUsersUseCase
import com.baccaro.kmp.domain.usecase.SearchNewsUseCase
import com.baccaro.kmp.util.OperationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getNewsUseCase: GetNewsUseCase,
    private val searchNewsUseCase: SearchNewsUseCase,
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = getNewsUseCase()) {
                is OperationResult.Success -> {
                    _state.update {
                        it.copy(
                            news = result.data,
                            isLoading = false
                        )
                    }
                }

                is OperationResult.Error -> {
                    _state.update {
                        it.copy(
                            error = result.exception,
                            isLoading = false
                        )
                    }
                }
            }

            when (val result = getUsersUseCase()) {
                is OperationResult.Success -> {
                    _state.update {
                        it.copy(
                            users = result.data,
                            isLoading = false
                        )
                    }
                }

                is OperationResult.Error -> {
                    _state.update {
                        it.copy(
                            error = result.exception,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onTabSelected(tab: Tab) {
        _state.update { it.copy(selectedTab = tab) }
    }

    fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }

        if (query.isEmpty()) {
            loadInitialData()
            return
        }

        viewModelScope.launch {
            when (state.value.selectedTab) {
                Tab.NEWS -> {
                    when (val result = searchNewsUseCase(query)) {
                        is OperationResult.Success -> {
                            _state.update { it.copy(news = result.data) }
                        }

                        is OperationResult.Error -> {
                            _state.update { it.copy(error = result.exception) }
                        }
                    }
                }

                Tab.USERS -> {}
            }
        }
    }
}


data class HomeState(
    val selectedTab: Tab = Tab.NEWS,
    val news: List<PostModel> = emptyList(),
    val users: List<UserModel> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)