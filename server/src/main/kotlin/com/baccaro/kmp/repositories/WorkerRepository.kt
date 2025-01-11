import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class WorkerRepository {

    suspend fun <T> dbQuery(block: () -> T): T = transaction { block() }

    suspend fun createWorker(worker: Worker): Worker = dbQuery {

        val usuarioId = Users.insert {
            it[nombre] = worker.usuario.nombre
            it[apellido] = worker.usuario.apellido
            it[telefono] = worker.usuario.telefono
            it[correoElectronico] = worker.usuario.correoElectronico
            it[contrasena] = worker.usuario.contrasena
            it[tipoUsuario] = worker.usuario.tipoUsuario
            it[creadoEn] = worker.usuario.creadoEn
            it[actualizadoEn] = worker.usuario.actualizadoEn
            it[estado] = worker.usuario.estado
        } get Users.id


        val insertedId = Workers.insert {
            it[usuario] = usuarioId
            it[profesion] = worker.profesion
            it[fotoPerfil] = worker.fotoPerfil
            it[dni] = worker.dni
            it[zonaCobertura] = worker.zonaCobertura
            it[descripcionServicios] = worker.descripcionServicios
            it[ratingPromedio] = worker.ratingPromedio
            it[disponible] = worker.disponible
        } get Workers.id

        worker.copy(id = insertedId.value, usuario = worker.usuario.copy(id = usuarioId.value))
    }


    suspend fun getAllWorkers(): List<Worker> = dbQuery {
        Workers.selectAll().map { resultRowToWorker(it) }
    }

    suspend fun findWorkerById(workerId: Int): Worker? = dbQuery {
        Workers.selectAll().where { Workers.id eq workerId }.singleOrNull()
            ?.let { resultRowToWorker(it) }
    }


    private fun resultRowToWorker(row: ResultRow): Worker {
        val userId = row[Workers.usuario].value
        val user = Users.selectAll().where { Users.id eq userId }.singleOrNull()
            ?.let { resultRowToUser(it) } ?: throw Exception("Usuario no encontrado para el worker")



        return Worker(
            id = row[Workers.id].value,
            usuario = user,
            profesion = row[Workers.profesion],
            fotoPerfil = row[Workers.fotoPerfil],
            dni = row[Workers.dni],
            zonaCobertura = row[Workers.zonaCobertura],
            descripcionServicios = row[Workers.descripcionServicios],
            ratingPromedio = row[Workers.ratingPromedio],
            disponible = row[Workers.disponible]
        )
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
        estado = row[Users.estado]
    )

    suspend fun updateWorker(worker: Worker): Worker = dbQuery {
        // 1. Actualizar la tabla Workers (si es necesario)
        Workers.update({ Workers.id eq worker.id }) {
            it[profesion] = worker.profesion
            it[fotoPerfil] = worker.fotoPerfil
            it[dni] = worker.dni
            it[zonaCobertura] = worker.zonaCobertura
            it[descripcionServicios] = worker.descripcionServicios
            it[ratingPromedio] = worker.ratingPromedio
            it[disponible] = worker.disponible
        }

        // 2. Actualizar la tabla Users
        Users.update({ Users.id eq worker.usuario.id }) {
            it[estado] = worker.usuario.estado
            it[actualizadoEn] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) // Actualiza la fecha de actualización
        }

        // 3. Devolver el worker actualizado.  Lo más eficiente es reconstruirlo:
        runBlocking {
            findWorkerById(worker.id!!) ?: throw IllegalStateException("Worker no encontrado después de la actualización")
        }
    }
}