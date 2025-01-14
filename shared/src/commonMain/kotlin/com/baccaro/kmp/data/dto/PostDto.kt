import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("content") val content: String,
    @SerialName("thumbnail") val thumbnail: String,
    @SerialName("category") val category: String,
)

@Serializable
data class UserDto(
    @SerialName("id") val id: Int,
    @SerialName("firstname") val firstName: String,
    @SerialName("lastname") val lastName: String,
    @SerialName("email") val email: String,
    @SerialName("address") val address: AddressDto
)

@Serializable
data class AddressDto(
    @SerialName("city") val city: String,
    @SerialName("geo") val geo: GeoDto
)

@Serializable
data class GeoDto(
    @SerialName("lat") val lat: String,
    @SerialName("lng") val lng: String
)