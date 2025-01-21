package com.baccaro.kmp.domain.usecase

import com.baccaro.kmp.domain.model.AuthResponse
import com.baccaro.kmp.domain.model.RegisterRequest
import com.baccaro.kmp.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(request: RegisterRequest): Result<Unit> {
        return repository.register(request)
    }
} 