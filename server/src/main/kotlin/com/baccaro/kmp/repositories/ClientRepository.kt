import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class ClientRepository {

    suspend fun <T> dbQuery(block: () -> T): T = transaction { block() }

    suspend fun createClient(client: Client): Client = dbQuery {

        val usuarioId = Users.insert {
            it[nombre] = client.usuario.nombre
            it[apellido] = client.usuario.apellido
            it[telefono] = client.usuario.telefono
            it[correoElectronico] = client.usuario.correoElectronico
            it[contrasena] = client.usuario.contrasena
            it[tipoUsuario] = client.usuario.tipoUsuario
            it[creadoEn] = client.usuario.creadoEn
            it[actualizadoEn] = client.usuario.actualizadoEn
            it[estado] = client.usuario.estado
            it[latitude] = client.usuario.latitude
            it[longitude] = client.usuario.longitude
        } get Users.id

        val insertedId = Clients.insert {

            it[usuario] = usuarioId
            it[direccion] = client.direccion
            it[instruccionesAdicionales] = client.instruccionesAdicionales
        } get Clients.id



        client.copy(id = insertedId.value, usuario = client.usuario.copy(id = usuarioId.value))

    }


    suspend fun getAllClients(): List<Client> = dbQuery {

        Clients.selectAll().map { resultRowToClient(it) }
    }


    suspend fun findClientById(clientId: Int): Client? = dbQuery {

        Clients.selectAll().where { Clients.id eq clientId }
            .singleOrNull()
            ?.let { resultRowToClient(it) }

    }

    private fun resultRowToClient(row: ResultRow): Client {
        val user = Users.selectAll().where { Users.id eq row[Clients.usuario].value }.single().let { resultRowToUser(it)}

        return Client(
            id = row[Clients.id].value,
            usuario = user,
            direccion = row[Clients.direccion],
            instruccionesAdicionales = row[Clients.instruccionesAdicionales]


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
        estado = row[Users.estado],
        latitude = row[Users.latitude],
        longitude = row[Users.longitude]
    )
}