import com.baccaro.kmp.domain.model.PostModel
import com.baccaro.kmp.domain.usecase.GetNewsDetailsUseCase
import com.baccaro.kmp.domain.usecase.GetNewsUseCase
import com.baccaro.kmp.domain.usecase.SearchNewsUseCase
import com.baccaro.kmp.util.OperationResult
import com.baccaro.kmp.util.PostMapper
import kotlinx.coroutines.runBlocking
import mocks.FakeNewsRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

// Test Classes
class GetNewsUseCaseTest {
    private val samplePostDtos = listOf(
        PostDto(1, "Title 1", "Content 1", "Thumbnail 1", "Category 1"),
        PostDto(2, "Title 2", "Content 2", "Thumbnail 2", "Category 2")
    )
    private val samplePosts = samplePostDtos.map { PostMapper().map(it) }

    @Test
    fun `getNews returns success with list of posts`() = runBlocking {
        val fakeRepository = FakeNewsRepository(OperationResult.Success(samplePostDtos))
        val useCase = GetNewsUseCase(fakeRepository)

        val result = useCase()

        assertIs<OperationResult.Success<List<PostModel>>>(result)
        assertEquals(samplePosts, (result as OperationResult.Success).data)
    }

    @Test
    fun `getNews returns error when repository fails`() = runBlocking {
        val fakeRepository = FakeNewsRepository(OperationResult.Error("Network error"))
        val useCase = GetNewsUseCase(fakeRepository)

        val result = useCase()

        assertIs<OperationResult.Error>(result)
        assertEquals("Network error", (result as OperationResult.Error).exception)
    }
}

class GetNewsDetailsUseCaseTest {
    private val samplePostDto = PostDto(1, "Title 1", "Content 1", "Thumbnail 1", "Category 1")
    private val samplePost = PostMapper().map(samplePostDto)

    @Test
    fun `getNewsDetails returns success with post details`() = runBlocking {
        val fakeRepository = FakeNewsRepository(newsDetailResult = OperationResult.Success(samplePostDto))
        val useCase = GetNewsDetailsUseCase(fakeRepository)

        val result = useCase(1)

        assertIs<OperationResult.Success<PostModel>>(result)
        assertEquals(samplePost, (result as OperationResult.Success).data)
    }

    @Test
    fun `getNewsDetails returns error when repository fails`() = runBlocking {
        val fakeRepository = FakeNewsRepository(newsDetailResult = OperationResult.Error("Post not found"))
        val useCase = GetNewsDetailsUseCase(fakeRepository)

        val result = useCase(1)

        assertIs<OperationResult.Error>(result)
        assertEquals("Post not found", (result as OperationResult.Error).exception)
    }
}

class SearchNewsUseCaseTest {
    private val samplePostDtos = listOf(
        PostDto(1, "Android News", "Android content", "thumb1", "tech"),
        PostDto(2, "iOS Update", "iOS content", "thumb2", "tech")
    )

    @Test
    fun `searchNews returns filtered results`() = runBlocking {
        val fakeRepository = FakeNewsRepository(OperationResult.Success(samplePostDtos))
        val useCase = SearchNewsUseCase(fakeRepository)

        val result = useCase("Android")

        assertIs<OperationResult.Success<List<PostModel>>>(result)
        assertEquals(1, (result as OperationResult.Success).data.size)
        assertEquals("Android News", result.data[0].title)
    }

    @Test
    fun `searchNews returns empty list when no matches`() = runBlocking {
        val fakeRepository = FakeNewsRepository(OperationResult.Success(samplePostDtos))
        val useCase = SearchNewsUseCase(fakeRepository)

        val result = useCase("Windows")

        assertIs<OperationResult.Success<List<PostModel>>>(result)
        assertEquals(0, (result as OperationResult.Success).data.size)
    }
}
