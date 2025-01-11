import com.baccaro.kmp.services.WorkerService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.SerializationException

fun Application.workerRoutes(workerService: WorkerService) {
    routing {
        route("/workers") {
            post {
                try {
                    val worker = call.receive<Worker>()
                    val createdWorker = workerService.createWorker(worker)
                    call.respond(HttpStatusCode.Created, createdWorker)
                } catch (e: SerializationException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error de serialización")))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error desconocido")))
                }
            }

            get { // Ruta para obtener TODOS los workers
                try {
                    val allWorkers = workerService.getAllWorkers()
                    call.respond(allWorkers)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al obtener todos los workers")))
                }
            }

            get("{id}") { // Ruta para obtener un worker por ID
                val workerId = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
                try {
                    val worker = workerService.findWorkerById(workerId)
                    if (worker == null) {
                        call.respond(HttpStatusCode.NotFound)
                    } else {
                        call.respond(worker)
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Error al obtener el worker")))
                }
            }
            put("{id}/validate") { // Nueva ruta para validación
                val workerId = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)
                val updatedWorker = workerService.validateWorker(workerId)
                if (updatedWorker != null) {
                    call.respond(HttpStatusCode.OK, updatedWorker)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}