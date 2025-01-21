package com.baccaro.kmp.domain.repository

import com.baccaro.kmp.domain.model.Service
import com.baccaro.kmp.util.OperationResult

interface WorkerRepository {
    suspend fun getServices(): OperationResult<List<Service>>
    suspend fun acceptService(serviceId: String): OperationResult<Unit>
    suspend fun rejectService(serviceId: String): OperationResult<Unit>
    suspend fun completeService(serviceId: String): OperationResult<Unit>
} 