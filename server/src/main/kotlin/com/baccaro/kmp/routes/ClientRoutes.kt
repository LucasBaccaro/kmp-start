import com.baccaro.kmp.services.ClientService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.SerializationException

fun Application.clientRoutes(clientService: ClientService) {
    routing {
        route("/clients") {
            post {
                try {
                    val client = call.receive<Client>()
                    val createdClient = clientService.createClient(client)
                    call.respond(HttpStatusCode.Created, createdClient)
                } catch (e: SerializationException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error de serialización")))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error desconocido")))
                }
            }

            get("{id?}") {
                val clientId = call.parameters["id"]?.toIntOrNull()
                if (clientId == null) {
                    try {
                        val allClients = clientService.getAllClients()
                        call.respond(allClients)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al obtener todos los clientes")))
                    }
                } else {
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