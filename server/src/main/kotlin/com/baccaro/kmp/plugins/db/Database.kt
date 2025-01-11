import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Users : IntIdTable() {  // Usamos IntIdTable para IDs autoincrementales
    val nombre = varchar("nombre", 255).index()
    val apellido = varchar("apellido", 255)
    val telefono = varchar("telefono", 20)
    val correoElectronico = varchar("correo_electronico", 255).uniqueIndex()
    val contrasena = varchar("contrasena", 255)
    val tipoUsuario = enumeration("tipo_usuario", TipoUsuario::class)
    val creadoEn = datetime("creado_en").default(
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )
    val actualizadoEn = datetime("actualizado_en").default(
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )
    val estado = enumeration("estado", EstadoUsuario::class).default(EstadoUsuario.EN_PROGRESO)
}

object Clients : IntIdTable() {
    val usuario = reference("usuario_id", Users).uniqueIndex()  // Relación con Users
    val direccion = text("direccion")
    val instruccionesAdicionales = text("instrucciones_adicionales").nullable()
}

object Workers : IntIdTable() {
    val usuario = reference("usuario_id", Users).uniqueIndex()
    val profesion = varchar("profesion", 255)
    val fotoPerfil = text("foto_perfil").nullable()
    val dni = varchar("dni", 20).uniqueIndex().nullable()
    val zonaCobertura = text("zona_cobertura")
    val descripcionServicios = text("descripcion_servicios")
    val ratingPromedio = double("rating_promedio").default(0.0)
    val disponible = bool("disponible").default(true)
}

object ServiceRequests : IntIdTable() {
    val cliente = reference("cliente_id", Clients)
    val trabajador = reference("trabajador_id", Workers).nullable()
    val descripcionProblema = text("descripcion_problema")
    val estado =
        enumeration("estado", EstadoServiceRequest::class).default(EstadoServiceRequest.PENDIENTE)
    val fechaHoraSolicitud = datetime("fecha_hora_solicitud").default(
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )
    val fechaHoraInicio = datetime("fecha_hora_inicio").nullable()
    val fechaHoraFin = datetime("fecha_hora_fin").nullable()
}

object Reviews : IntIdTable() {
    val trabajador = reference("trabajador_id", Workers)
    val cliente = reference("cliente_id", Clients)
    val calificacion = integer("calificacion")
    val comentario = text("comentario").nullable()
    val creadoEn = datetime("creado_en").default(
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )
}

object ChatMessages : IntIdTable() {
    val remitente = reference("remitente_id", Users)
    val destinatario = reference("destinatario_id", Users)
    val mensaje = text("mensaje")
    val fechaHora = datetime("fecha_hora").default(
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )
}