package com.baccaro.kmp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baccaro.kmp.domain.model.PostModel
import com.baccaro.kmp.domain.usecase.GetNewsDetailsUseCase
import com.baccaro.kmp.util.OperationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

data class NewsDetailState(
    val news: PostModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)