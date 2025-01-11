package com.baccaro.kmp

import ClientRepository
import WorkerRepository
import com.baccaro.kmp.plugins.configureRouting
import com.baccaro.kmp.plugins.db.DatabaseFactory
import com.baccaro.kmp.services.ClientService
import com.baccaro.kmp.services.WorkerService
import com.zaxxer.hikari.HikariConfig
import configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

const val SERVER_PORT = 8080

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    // Configuración de la base de datos (como antes)
    val config = HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql://localhost:5432/konectAR"
        username = "postgres"
        password = "lbaccaro1"
        driverClassName = "org.postgresql.Driver"
        maximumPoolSize = 3 // Número máximo de conexiones en el pool
        isAutoCommit = false // Desactiva el autocommit para usar transacciones
        transactionIsolation =
            "TRANSACTION_REPEATABLE_READ" // Nivel de aislamiento de transacciones
        validate() // Valida la configuración }
    }
    DatabaseFactory.init(config)
    // Inyección de dependencias (como antes)
    val clientService = ClientService(ClientRepository())
    val workerService = WorkerService(WorkerRepository())
    configureRouting(clientService, workerService)
}
