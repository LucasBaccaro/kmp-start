package com.baccaro.kmp.domain.model

import Worker
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Service(
    val id: String,
    val description: String,
    val location: String,
    val status: ServiceStatus,
    val createdAt: String,
    val rating: Int?,
    @SerialName("worker") private val _worker: Worker? = null,
    @SerialName("client") private val _client: Worker? = null,
    val category: Category? = null
) {
    val worker: Worker
        get() = _worker ?: _client ?: error("Service must have either worker or client")
}

@Serializable
enum class ServiceStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    REJECTED
}

@Serializable
data class ClientInfo(
    val id: String,
    val email: String,
    val name: String,
    val phone: String,
    val location: String,
    val rating: Int
) 