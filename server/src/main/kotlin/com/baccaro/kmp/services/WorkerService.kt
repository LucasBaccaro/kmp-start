package com.baccaro.kmp.services

import Worker
import WorkerRepository

class WorkerService(private val workerRepository: WorkerRepository) {

    suspend fun createWorker(worker: Worker): Worker {
        // Lógica de validación (ej. validar campos obligatorios, formato de email, etc.)
        // ...

        return workerRepository.createWorker(worker)
    }

    suspend fun getAllWorkers(): List<Worker> = workerRepository.getAllWorkers()

    suspend fun findWorkerById(workerId: Int): Worker? = workerRepository.findWorkerById(workerId)

    // ... (Otros métodos del servicio, como actualizar worker, etc.)
}