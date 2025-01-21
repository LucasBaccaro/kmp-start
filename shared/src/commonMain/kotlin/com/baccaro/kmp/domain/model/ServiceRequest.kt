import kotlinx.serialization.Serializable

@Serializable
data class ServiceRequest(
    val workerId: String,
    val description: String,
    val location: String
) 