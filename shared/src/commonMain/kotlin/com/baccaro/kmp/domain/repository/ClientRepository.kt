import com.baccaro.kmp.domain.model.Service
import com.baccaro.kmp.util.OperationResult

interface ClientRepository {
    suspend fun requestService(request: ServiceRequest): OperationResult<Service>
    suspend fun getClientServices(): OperationResult<List<Service>>
    suspend fun rateService(serviceId: String, rating: Int): OperationResult<Service>
} 