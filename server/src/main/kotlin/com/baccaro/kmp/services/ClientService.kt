package com.baccaro.kmp.services

import Client
import ClientRepository

class ClientService(private val clientRepository: ClientRepository) {

    suspend fun createClient(client: Client): Client {
        return clientRepository.createClient(client)
    }

    suspend fun getAllClients(): List<Client> = clientRepository.getAllClients()


    suspend fun findClientById(clientId: Int): Client? = clientRepository.findClientById(clientId)

    // ... (Otros métodos del servicio)
}