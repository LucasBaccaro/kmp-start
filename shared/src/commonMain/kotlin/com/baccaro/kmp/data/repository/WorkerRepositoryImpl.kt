package com.baccaro.kmp.data.repository

import TokenManager
import com.baccaro.kmp.data.remote.WorkerApi
import com.baccaro.kmp.domain.model.Service
import com.baccaro.kmp.domain.repository.WorkerRepository
import com.baccaro.kmp.util.OperationResult

class WorkerRepositoryImpl(
    private val api: WorkerApi,
    private val tokenManager: TokenManager
) : WorkerRepository {
    override suspend fun getServices(): OperationResult<List<Service>> {
        return try {
            val token = tokenManager.getToken() ?: return OperationResult.Error("No hay token disponible")
            val services = api.getServices(token)
            OperationResult.Success(services)
        } catch (e: Exception) {
            OperationResult.Error(e.message ?: "Error desconocido")
        }
    }

    override suspend fun acceptService(serviceId: String): OperationResult<Unit> {
        return try {
            val token = tokenManager.getToken() ?: return OperationResult.Error("No hay token disponible")
            api.acceptService(serviceId, token)
            OperationResult.Success(Unit)
        } catch (e: Exception) {
            OperationResult.Error(e.message ?: "Error desconocido")
        }
    }

    override suspend fun rejectService(serviceId: String): OperationResult<Unit> {
        return try {
            val token = tokenManager.getToken() ?: return OperationResult.Error("No hay token disponible")
            api.rejectService(serviceId, token)
            OperationResult.Success(Unit)
        } catch (e: Exception) {
            OperationResult.Error(e.message ?: "Error desconocido")
        }
    }

    override suspend fun completeService(serviceId: String): OperationResult<Unit> {
        return try {
            val token = tokenManager.getToken() ?: return OperationResult.Error("No hay token disponible")
            api.completeService(serviceId, token)
            OperationResult.Success(Unit)
        } catch (e: Exception) {
            OperationResult.Error(e.message ?: "Error desconocido")
        }
    }
} 