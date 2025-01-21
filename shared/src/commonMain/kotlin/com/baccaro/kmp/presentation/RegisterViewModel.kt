package com.baccaro.kmp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baccaro.kmp.domain.model.AuthResponse
import com.baccaro.kmp.domain.model.RegisterRequest
import com.baccaro.kmp.domain.model.UserRole
import com.baccaro.kmp.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    fun resetState() {
        _state.value = RegisterState()
    }

    fun onEmailChanged(email: String) {
        _state.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _state.update { it.copy(password = password) }
    }

    fun onNameChanged(name: String) {
        _state.update { it.copy(name = name) }
    }

    fun onPhoneChanged(phone: String) {
        _state.update { it.copy(phone = phone) }
    }

    fun onRoleChanged(role: UserRole) {
        _state.update { it.copy(role = role) }
    }

    fun onLocationChanged(location: String) {
        _state.update { it.copy(location = location) }
    }

    fun onCategoryIdChanged(categoryId: String) {
        _state.update { it.copy(categoryId = categoryId) }
    }

    fun onRegisterClick(onRegisterSuccess: () -> Unit) {
        viewModelScope.launch {
            if (state.value.isLoading) return@launch

            try {
                _state.update { it.copy(isLoading = true, error = null) }

                val request = RegisterRequest(
                    email = state.value.email,
                    password = state.value.password,
                    name = state.value.name,
                    phone = state.value.phone,
                    role = state.value.role,
                    location = state.value.location,
                    categoryId = state.value.categoryId.takeIf { state.value.role == UserRole.WORKER }
                )

                registerUseCase(request)
                    .onSuccess {
                        onRegisterSuccess()
                    }
                    .onFailure { exception ->
                        _state.update { it.copy(error = exception.message) }
                    }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val phone: String = "",
    val role: UserRole = UserRole.CLIENT,
    val location: String = "",
    val categoryId: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)