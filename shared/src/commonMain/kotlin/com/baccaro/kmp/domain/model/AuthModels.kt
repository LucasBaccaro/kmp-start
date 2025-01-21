package com.baccaro.kmp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val name: String,
    val role: UserRole,
    val location: String
)

@Serializable
enum class UserRole {
    WORKER,
    CLIENT
}

@Serializable
data class AuthResponse(
    val token: String,
    val user: User
)

@Serializable
data class LoginCredentials(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val phone: String? = null,
    val role: UserRole,
    val location: String,
    val categoryId: String? = null
) 