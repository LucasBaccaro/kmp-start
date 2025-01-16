package com.baccaro.kmp.services

import Client
import ClientRepository
import org.mindrot.jbcrypt.BCrypt

class ClientService(private val clientRepository: ClientRepository) {

    suspend fun createClient(client: Client): Client {
        val hashedPassword = BCrypt.hashpw(client.usuario.contrasena, BCrypt.gensalt())
        val clientWithHashedPassword = client.copy(
            usuario = client.usuario.copy(contrasena = hashedPassword, estado = EstadoUsuario.ACEPTADO)
        )
        return clientRepository.createClient(clientWithHashedPassword)
    }

    suspend fun getAllClients(): List<Client> = clientRepository.getAllClients()


    suspend fun findClientById(clientId: Int): Client? = clientRepository.findClientById(clientId)

    // ... (Otros métodos del servicio)
}