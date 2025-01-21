package com.baccaro.kmp.data.remote

import CategoryWithWorkers
import TokenManager
import com.baccaro.kmp.Constants
import com.baccaro.kmp.domain.model.Category
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

class CategoryApi(private val client: HttpClient, private val tokenManager: TokenManager) {
    suspend fun getCategories(): List<Category> {
        return client.get("${Constants.BASE_URL}/api/categories") {
            header(HttpHeaders.Authorization, "Bearer ${tokenManager.getToken()}")
        }.body()
    }

    suspend fun getCategoryWorkers(categoryId: String): CategoryWithWorkers {
        val categories = client.get("${Constants.BASE_URL}/api/workers/categories") {
            header(HttpHeaders.Authorization, "Bearer ${tokenManager.getToken()}")
        }.body<List<CategoryWithWorkers>>()

        return categories.firstOrNull { it.id == categoryId }
            ?: CategoryWithWorkers(
                id = categoryId,
                name = "Categoría",
                description = "Descripción no disponible",
                workers = emptyList()
            )
    }
} 