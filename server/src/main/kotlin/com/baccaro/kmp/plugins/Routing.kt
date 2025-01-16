package com.baccaro.kmp.plugins

import clientRoutes
import com.baccaro.kmp.routes.loginRoutes
import com.baccaro.kmp.services.ClientService
import com.baccaro.kmp.services.LoginService
import com.baccaro.kmp.services.WorkerService
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import workerRoutes

fun Application.configureRouting(
    clientService: ClientService,
    workerService: WorkerService,
    loginService: LoginService
) {
    routing {
        clientRoutes(clientService)
        workerRoutes(workerService)
        loginRoutes(loginService)
        // ... otras rutas
    }
}
