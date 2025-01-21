package com.baccaro.kmp.data.remote

import com.baccaro.kmp.Constants
import com.baccaro.kmp.domain.model.AuthResponse
import com.baccaro.kmp.domain.model.LoginCredentials
import com.baccaro.kmp.domain.model.RegisterRequest
import com.baccaro.kmp.domain.model.RegisterResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class AuthApi(private val client: HttpClient) {
    suspend fun login(credentials: LoginCredentials): AuthResponse {
        return client.post("${Constants.BASE_URL}/api/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(credentials)
        }.body()
    }

    suspend fun register(request: RegisterRequest): Result<RegisterResponse> {
        val response = client.post("${Constants.BASE_URL}/api/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        
        val registerResponse = response.body<RegisterResponse>()
        return if (response.status == HttpStatusCode.Created) {
            Result.success(registerResponse)
        } else {
            Result.failure(Exception(registerResponse.message))
        }
    }
} 