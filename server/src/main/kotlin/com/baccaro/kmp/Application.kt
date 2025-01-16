package com.baccaro.kmp

import ClientRepository
import WorkerRepository
import com.baccaro.kmp.plugins.configureAuthentication
import com.baccaro.kmp.plugins.configureRouting
import com.baccaro.kmp.plugins.db.DatabaseFactory
import com.baccaro.kmp.repositories.LoginRepository
import com.baccaro.kmp.services.ClientService
import com.baccaro.kmp.services.EmailService
import com.baccaro.kmp.services.LoginService
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
        jdbcUrl = System.getenv("JDBC_URL")
        username = System.getenv("USERNAME")
        password = System.getenv("PASSWORD")
        driverClassName = "org.postgresql.Driver"
        maximumPoolSize = 3 // Número máximo de conexiones en el pool
        maximumPoolSize = 3
        isAutoCommit = false // Desactiva el autocommit para usar transacciones
        transactionIsolation =
            "TRANSACTION_REPEATABLE_READ" // Nivel de aislamiento de transacciones
        validate() // Valida la configuración }
    }
    val emailConfig = EmailService.EmailConfig(
        host = System.getenv("HOST"),
        port = System.getenv("PORT").toInt(),
        username = System.getenv("USERNAME_EMAIL"),
        password = System.getenv("PASSWORD_EMAIL"),
        from = System.getenv("FROM"),
    )
    DatabaseFactory.init(config)
    // Inyección de dependencias (como antes)
    val clientService = ClientService(ClientRepository())
    val emailService = EmailService(emailConfig)
    val workerService = WorkerService(WorkerRepository(), emailService)
    val loginService = LoginService(LoginRepository())
    configureAuthentication()
    configureRouting(clientService, workerService, loginService)

}
