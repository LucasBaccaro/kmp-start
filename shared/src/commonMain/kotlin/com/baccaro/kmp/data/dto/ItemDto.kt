import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    @SerialName("country") val country: String,
    @SerialName("name") val name: String,
    @SerialName("_id") val _id: Int,
    @SerialName("coord") val coord: CoordDto,
)

@Serializable
data class CoordDto(
    @SerialName("lon") val lon: Double,
    @SerialName("lat") val lat: Double,
)