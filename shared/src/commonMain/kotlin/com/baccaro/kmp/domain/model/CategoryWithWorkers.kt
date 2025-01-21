import kotlinx.serialization.Serializable

@Serializable
data class CategoryWithWorkers(
    val id: String,
    val name: String,
    val description: String,
    val workers: List<Worker>
) 