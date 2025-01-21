import com.baccaro.kmp.domain.model.Category
import com.baccaro.kmp.util.OperationResult

interface CategoryRepository {
    suspend fun getCategories(): OperationResult<List<Category>>
    suspend fun getCategoryWorkers(categoryId: String): OperationResult<CategoryWithWorkers>
} 