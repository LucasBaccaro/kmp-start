package com.baccaro.kmp.plugins

import clientRoutes
import com.baccaro.kmp.services.ClientService
import com.baccaro.kmp.services.WorkerService
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.routing
import workerRoutes

fun Application.configureRouting(clientService: ClientService, workerService: WorkerService) {
    routing {
        clientRoutes(clientService)
        workerRoutes(workerService)
        // ... otras rutas
    }
}
