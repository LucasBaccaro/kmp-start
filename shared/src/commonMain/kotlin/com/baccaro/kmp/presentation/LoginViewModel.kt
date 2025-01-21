package com.baccaro.kmp.presentation

import TokenManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baccaro.kmp.domain.model.AuthResponse
import com.baccaro.kmp.domain.model.LoginCredentials
import com.baccaro.kmp.domain.model.UserRole
import com.baccaro.kmp.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun onEmailChanged(email: String) {
        _state.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _state.update { it.copy(password = password) }
    }

    fun onLoginClick(onLoginSuccess: (UserRole) -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            loginUseCase(state.value.email, state.value.password)
                .onSuccess { response ->
                    tokenManager.saveToken(response.token)
                    _state.update { it.copy(isLoading = false) }
                    onLoginSuccess(response.user.role)
                }
                .onFailure { exception ->
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                }
        }
    }
}

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val authResponse: AuthResponse? = null
) 