package com.baccaro.kmp.data.repository

import com.baccaro.kmp.data.remote.AuthApi
import com.baccaro.kmp.domain.model.AuthResponse
import com.baccaro.kmp.domain.model.LoginCredentials
import com.baccaro.kmp.domain.model.RegisterRequest
import com.baccaro.kmp.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: AuthApi
) : AuthRepository {
    override suspend fun login(credentials: LoginCredentials): Result<AuthResponse> {
        return try {
            Result.success(api.login(credentials))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            api.register(request)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 