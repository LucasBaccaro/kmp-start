package com.baccaro.kmp.plugins.db

import ChatMessages
import Clients
import Reviews
import ServiceRequests
import Users
import Workers
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(config: HikariConfig) {
        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)
        // Crea las tablas dentro de una transacción
        transaction {
            SchemaUtils.create(Users, Clients, Workers, ServiceRequests, Reviews, ChatMessages)
        }
    }
}