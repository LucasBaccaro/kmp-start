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