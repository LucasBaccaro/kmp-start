package com.baccaro.kmp.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt {
            verifier(JwtConfig.verifier)
            validate { credential ->
                if (credential.payload.getClaim("id").asInt() != 0) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}
