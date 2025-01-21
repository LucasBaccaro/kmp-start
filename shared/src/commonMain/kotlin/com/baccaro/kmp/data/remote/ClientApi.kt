import com.baccaro.kmp.Constants
import com.baccaro.kmp.domain.model.Service
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

class ClientApi(
    private val client: HttpClient,
    private val tokenManager: TokenManager
) {
    suspend fun requestService(request: ServiceRequest): Service {
        return client.post("${Constants.BASE_URL}/api/services") {
            header(HttpHeaders.Authorization, "Bearer ${tokenManager.getToken()}")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun getClientServices(): List<Service> {
        return client.get("${Constants.BASE_URL}/api/workers/client/services") {
            header(HttpHeaders.Authorization, "Bearer ${tokenManager.getToken()}")
        }.body()
    }

    suspend fun rateService(serviceId: String, rating: Int): Service {
        return client.post("${Constants.BASE_URL}/api/services/$serviceId/rate") {
            header(HttpHeaders.Authorization, "Bearer ${tokenManager.getToken()}")
            contentType(ContentType.Application.Json)
            setBody(RatingRequest(rating))
        }.body()
    }
}

@Serializable
data class RatingRequest(val rating: Int) 