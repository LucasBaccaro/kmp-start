package com.baccaro.kmp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baccaro.kmp.domain.model.PostModel
import com.baccaro.kmp.domain.model.UserModel
import com.baccaro.kmp.domain.usecase.GetNewsDetailsUseCase
import com.baccaro.kmp.domain.usecase.GetNewsUseCase
import com.baccaro.kmp.domain.usecase.GetUserDetailsUseCase
import com.baccaro.kmp.domain.usecase.GetUsersUseCase
import com.baccaro.kmp.domain.usecase.SearchNewsUseCase
import com.baccaro.kmp.util.OperationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeState(
    val selectedTab: Tab = Tab.NEWS,
    val news: List<PostModel> = emptyList(),
    val users: List<UserModel> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class Tab {
    NEWS, USERS
}

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

                Tab.USERS -> {
                    // Implementar búsqueda de usuarios si es necesario
                }
            }
        }
    }
}





// ViewModels para las pantallas de detalle
class NewsDetailViewModel(
    private val getNewsDetailsUseCase: GetNewsDetailsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(NewsDetailState())
    val state: StateFlow<NewsDetailState> = _state.asStateFlow()

    fun loadNewsDetails(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = getNewsDetailsUseCase(id)) {
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
        }
    }
}

class UserLocationViewModel(
    private val getUserDetailsUseCase: GetUserDetailsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(UserLocationState())
    val state: StateFlow<UserLocationState> = _state.asStateFlow()

    fun loadUserDetails(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = getUserDetailsUseCase(id)) {
                is OperationResult.Success -> {
                    _state.update {
                        it.copy(
                            user = result.data,
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
}

data class NewsDetailState(
    val news: PostModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class UserLocationState(
    val user: UserModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
