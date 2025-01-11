package com.baccaro.kmp.services

import Worker
import WorkerRepository

class WorkerService(private val workerRepository: WorkerRepository, private val emailService: EmailService) {

    suspend fun createWorker(worker: Worker): Worker {
        val createdWorker = workerRepository.createWorker(worker)
        emailService.sendWorkerValidationEmail(createdWorker, false)
        return createdWorker
    }

    suspend fun getAllWorkers(): List<Worker> = workerRepository.getAllWorkers()

    suspend fun findWorkerById(workerId: Int): Worker? = workerRepository.findWorkerById(workerId)

    // ... (Otros métodos del servicio, como actualizar worker, etc.)

    suspend fun validateWorker(workerId: Int): Worker? {
        val worker = workerRepository.findWorkerById(workerId) ?: return null

        if (worker.usuario.estado == EstadoUsuario.EN_PROGRESO) {
            val updatedUser = worker.usuario.copy(estado = EstadoUsuario.ACEPTADO)
            val updatedWorker = worker.copy(usuario = updatedUser) // No es estrictamente necesario, pero mantiene la consistencia

            workerRepository.updateWorker(updatedWorker) // Pasa updatedUser al repositorio
            emailService.sendWorkerValidationEmail(updatedWorker, true)

            return workerRepository.findWorkerById(workerId)
        } else {
            return worker
        }
    }
}