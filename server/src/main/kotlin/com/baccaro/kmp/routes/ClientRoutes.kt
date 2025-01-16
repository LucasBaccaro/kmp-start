import com.baccaro.kmp.plugins.JwtConfig
import com.baccaro.kmp.services.ClientService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.SerializationException

fun Application.clientRoutes(clientService: ClientService) {
    routing {
        // Ruta de registro (pública)
        post("/register/client") {
            try {
                val client = call.receive<Client>()
                val createdClient = clientService.createClient(client)
                val token = JwtConfig.makeToken(createdClient.usuario)
                call.respond(HttpStatusCode.Created, ClientRegistrationResponse(token, createdClient))
            } catch (e: SerializationException) {
                call.respond(HttpStatusCode.BadRequest, "Error de serialización: ${e.message}")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Error desconocido: ${e.message}")
            }
        }

        // Rutas protegidas
        authenticate {
            route("/clients") {
                get {
                    try {
                        val allClients = clientService.getAllClients()
                        call.respond(allClients)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al obtener todos los clientes")))
                    }
                }

                get("{id}") {
                    val clientId = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
                    try {
                        val client = clientService.findClientById(clientId)
                        if (client == null) {
                            call.respond(HttpStatusCode.NotFound)
                        } else {
                            call.respond(client)
                        }
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al obtener el cliente")))
                    }
                }
            }
        }
    }
}