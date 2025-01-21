import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String,
    val role: String // "WORKER" o "CLIENT"
) 