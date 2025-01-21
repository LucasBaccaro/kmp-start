package com.baccaro.kmp.data.remote

import com.baccaro.kmp.Constants
import com.baccaro.kmp.domain.model.Service
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders

class WorkerApi(private val client: HttpClient) {
    suspend fun getServices(token: String): List<Service> {
        return client.get("${Constants.BASE_URL}/api/workers/services") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.body()
    }

    suspend fun acceptService(serviceId: String, token: String) {
        client.post("${Constants.BASE_URL}/api/services/$serviceId/accept") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
    }

    suspend fun rejectService(serviceId: String, token: String) {
        client.post("${Constants.BASE_URL}/api/services/$serviceId/reject") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
    }

    suspend fun completeService(serviceId: String, token: String) {
        client.post("${Constants.BASE_URL}/api/services/$serviceId/complete") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
    }
}