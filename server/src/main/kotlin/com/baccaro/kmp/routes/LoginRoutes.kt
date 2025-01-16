package com.baccaro.kmp.routes
import AuthResponse
import LoginRequest
import com.baccaro.kmp.plugins.JwtConfig
import com.baccaro.kmp.services.LoginService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

// En loginRoutes
fun Application.loginRoutes(loginService: LoginService) {
    routing {
        route("/auth") {
            post("/login") {
                try {
                    val loginRequest = call.receive<LoginRequest>()
                    val user = loginService.authenticate(loginRequest.email, loginRequest.password)

                    if (user != null) {
                        val token = JwtConfig.makeToken(user)
                        call.respond(HttpStatusCode.OK, AuthResponse(token, user))
                    } else {
                        call.respond(HttpStatusCode.Unauthorized, "Credenciales inválidas")
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Error en el login: ${e.message}")
                }
            }
        }
    }
}