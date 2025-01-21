package com.baccaro.kmp.domain.repository

import com.baccaro.kmp.domain.model.AuthResponse
import com.baccaro.kmp.domain.model.LoginCredentials
import com.baccaro.kmp.domain.model.RegisterRequest

interface AuthRepository {
    suspend fun login(credentials: LoginCredentials): Result<AuthResponse>
    suspend fun register(request: RegisterRequest): Result<Unit>
} 