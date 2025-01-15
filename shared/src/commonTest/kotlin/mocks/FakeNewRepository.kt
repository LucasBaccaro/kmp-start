package mocks

import PostDto
import com.baccaro.kmp.domain.model.PostModel
import com.baccaro.kmp.domain.repository.NewsRepository
import com.baccaro.kmp.util.OperationResult
import com.baccaro.kmp.util.PostMapper

// Fake Repositories
class FakeNewsRepository(
    private val newsResult: OperationResult<List<PostDto>> = OperationResult.Success(emptyList()),
    private val newsDetailResult: OperationResult<PostDto> = OperationResult.Success(PostDto(0, "", "", "", ""))
) : NewsRepository {
    override suspend fun getNewsList(): OperationResult<List<PostModel>> {
        return when (newsResult) {
            is OperationResult.Success -> {
                try {
                    val posts = newsResult.data.map { PostMapper().map(it) }
                    OperationResult.Success(posts)
                } catch (e: Exception) {
                    OperationResult.Error("Mapping Error: ${e.message}")
                }
            }
            is OperationResult.Error -> OperationResult.Error(newsResult.exception)
        }
    }

    override suspend fun getNewsDetails(id: Int): OperationResult<PostModel> {
        return when (newsDetailResult) {
            is OperationResult.Success -> {
                try {
                    val post = PostMapper().map(newsDetailResult.data)
                    OperationResult.Success(post)
                } catch (e: Exception) {
                    OperationResult.Error("Mapping Error: ${e.message}")
                }
            }
            is OperationResult.Error -> OperationResult.Error(newsDetailResult.exception)
        }
    }
}