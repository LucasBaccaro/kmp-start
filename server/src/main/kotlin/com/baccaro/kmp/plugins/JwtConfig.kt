package com.baccaro.kmp.plugins

import User
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

object JwtConfig {
    private const val secret = "tu_secreto_muy_seguro" // En producción usar variable de entorno
    private const val issuer = "http://0.0.0.0:8080/"
    private const val validityInMs = 36_000_00 * 10 // 10 horas
    private val algorithm = Algorithm.HMAC256(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun makeToken(user: User): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("id", user.id)
        .withClaim("email", user.correoElectronico)
        .withClaim("type", user.tipoUsuario.name)
        .withExpiresAt(Date(System.currentTimeMillis() + validityInMs))
        .sign(algorithm)
}