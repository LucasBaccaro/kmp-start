import com.baccaro.kmp.data.remote.CategoryApi
import com.baccaro.kmp.domain.model.Category
import com.baccaro.kmp.util.OperationResult

class CategoryRepositoryImpl(
    private val api: CategoryApi,
    private val tokenManager: TokenManager
) : CategoryRepository {
    override suspend fun getCategories(): OperationResult<List<Category>> {
        return try {
            val categories = api.getCategories()
            OperationResult.Success(categories)
        } catch (e: Exception) {
            OperationResult.Error(e.message ?: "Error desconocido")
        }
    }

    override suspend fun getCategoryWorkers(categoryId: String): OperationResult<CategoryWithWorkers> {
        return try {
            val categoryWithWorkers = api.getCategoryWorkers(categoryId)
            OperationResult.Success(categoryWithWorkers)
        } catch (e: Exception) {
            OperationResult.Error(e.message ?: "Error desconocido")
        }
    }
} 