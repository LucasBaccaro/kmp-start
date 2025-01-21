import com.baccaro.kmp.domain.model.Service
import com.baccaro.kmp.util.OperationResult

class ClientRepositoryImpl(
    private val api: ClientApi
) : ClientRepository {
    override suspend fun requestService(request: ServiceRequest): OperationResult<Service> {
        return try {
            val service = api.requestService(request)
            OperationResult.Success(service)
        } catch (e: Exception) {
            OperationResult.Error(e.message ?: "Error desconocido")
        }
    }

    override suspend fun getClientServices(): OperationResult<List<Service>> {
        return try {
            val services = api.getClientServices()
            OperationResult.Success(services)
        } catch (e: Exception) {
            OperationResult.Error(e.message ?: "Error desconocido")
        }
    }

    override suspend fun rateService(serviceId: String, rating: Int): OperationResult<Service> {
        return try {
            val service = api.rateService(serviceId, rating)
            OperationResult.Success(service)
        } catch (e: Exception) {
            OperationResult.Error(e.message ?: "Error desconocido")
        }
    }
} 