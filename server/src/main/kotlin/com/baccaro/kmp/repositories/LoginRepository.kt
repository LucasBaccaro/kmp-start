package com.baccaro.kmp.repositories

import User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

class LoginRepository {
    suspend fun <T> dbQuery(block: () -> T): T = transaction { block() }

    suspend fun findUserByEmail(email: String): User? = dbQuery {
        Users.selectAll().where { Users.correoElectronico eq email }
            .map { resultRowToUser(it) }
            .singleOrNull()
    }

    private fun resultRowToUser(row: ResultRow): User = User(
        id = row[Users.id].value,
        nombre = row[Users.nombre],
        apellido = row[Users.apellido],
        telefono = row[Users.telefono],
        correoElectronico = row[Users.correoElectronico],
        contrasena = row[Users.contrasena],
        tipoUsuario = row[Users.tipoUsuario],
        creadoEn = row[Users.creadoEn],
        actualizadoEn = row[Users.actualizadoEn],
        estado = row[Users.estado],
        latitude = row[Users.latitude],
        longitude = row[Users.longitude]
    )

    suspend fun validateCredentials(email: String, password: String): User? = dbQuery {
        Users.selectAll().where { Users.correoElectronico eq email }
            .map { resultRowToUser(it) }
            .singleOrNull()
            ?.let { user ->
                if (BCrypt.checkpw(password, user.contrasena)) user else null
            }
    }
}