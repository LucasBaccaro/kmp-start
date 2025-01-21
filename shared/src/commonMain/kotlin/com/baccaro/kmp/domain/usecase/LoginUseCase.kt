package com.baccaro.kmp.domain.usecase

import com.baccaro.kmp.domain.model.AuthResponse
import com.baccaro.kmp.domain.model.LoginCredentials
import com.baccaro.kmp.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<AuthResponse> {
        return repository.login(LoginCredentials(email, password))
    }
} 