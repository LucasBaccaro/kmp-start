import com.baccaro.kmp.domain.model.Category
import kotlinx.serialization.Serializable

@Serializable
data class Worker(
    val id: String,
    val email: String,
    val password: String,
    val name: String,
    val phone: String,
    val role: String,
    val status: String,
    val location: String,
    val rating: Float,
    val category: Category? = null
) 