package com.baccaro.kmp.services

import User
import com.baccaro.kmp.repositories.LoginRepository

class LoginService(private val userRepository: LoginRepository) {
    suspend fun authenticate(email: String, password: String): User? {
        return userRepository.validateCredentials(email, password)
    }

    suspend fun findUserByEmail(email: String): User? {
        return userRepository.findUserByEmail(email)
    }
}