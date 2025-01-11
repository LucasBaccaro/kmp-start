import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

enum class TipoUsuario {
    CLIENTE, TRABAJADOR
}

enum class EstadoUsuario {
    ACEPTADO, EN_PROGRESO, RECHAZADO
}

enum class EstadoServiceRequest {
    PENDIENTE, ACEPTADO, COMPLETADO, CANCELADO
}

@Serializable
data class User(
    val id: Int? = null,
    val nombre: String,
    val apellido: String,
    val telefono: String,
    val correoElectronico: String,
    val contrasena: String,
    val tipoUsuario: TipoUsuario,
    val creadoEn: LocalDateTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()),
    val actualizadoEn: LocalDateTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()),
    val estado: EstadoUsuario = EstadoUsuario.EN_PROGRESO
)

@Serializable
data class Client(
    val id: Int? = null,
    val usuario: User,
    val direccion: String,
    val instruccionesAdicionales: String? = null
)

@Serializable
data class Worker(
    val id: Int? = null,
    val usuario: User,
    val profesion: String,
    val fotoPerfil: String? = null,
    val dni: String? = null,
    val zonaCobertura: String,
    val descripcionServicios: String,
    val ratingPromedio: Double = 0.0,
    val disponible: Boolean = true

)

@Serializable
data class ServiceRequest(
    val id: Long? = null,
    val cliente: Client,
    val trabajador: Worker? = null,
    val descripcionProblema: String,
    val estado: EstadoServiceRequest = EstadoServiceRequest.PENDIENTE,
    val fechaHoraSolicitud: LocalDateTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()),
    val fechaHoraInicio: LocalDateTime? = null,
    val fechaHoraFin: LocalDateTime? = null

)

@Serializable
data class Review(
    val id: Long? = null,
    val trabajador: Worker,
    val cliente: Client,
    val calificacion: Int,
    val comentario: String? = null,
    val creadoEn: LocalDateTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()),
)

@Serializable
data class ChatMessage(
    val id: Long? = null,
    val remitente: User,
    val destinatario: User,
    val mensaje: String,
    val fechaHora: LocalDateTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()),

    )