package com.baccaro.kmp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baccaro.kmp.domain.model.UserModel
import com.baccaro.kmp.domain.usecase.GetUserDetailsUseCase
import com.baccaro.kmp.util.OperationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

data class UserLocationState(
    val user: UserModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)