import com.baccaro.kmp.data.dto.AddressDto
import com.baccaro.kmp.data.dto.GeoDto
import com.baccaro.kmp.data.dto.UserDto
import com.baccaro.kmp.domain.model.UserModel
import com.baccaro.kmp.domain.usecase.GetUserDetailsUseCase
import com.baccaro.kmp.domain.usecase.GetUsersUseCase
import com.baccaro.kmp.util.OperationResult
import com.baccaro.kmp.util.UserMapper
import kotlinx.coroutines.runBlocking
import mocks.FakeUserRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class GetUsersUseCaseTest {
    private val sampleUserDtos = listOf(
        UserDto(1, "John", "Doe", "john@email.com", AddressDto("New York", GeoDto("40.7128", "-74.0060"))),
        UserDto(2, "Jane", "Smith", "jane@email.com", AddressDto("Los Angeles", GeoDto("34.0522", "-118.2437")))
    )
    private val sampleUsers = sampleUserDtos.map { UserMapper().map(it) }

    @Test
    fun `getUsers returns success with list of users`() = runBlocking {
        val fakeRepository = FakeUserRepository(OperationResult.Success(sampleUserDtos))
        val useCase = GetUsersUseCase(fakeRepository)

        val result = useCase()

        assertIs<OperationResult.Success<List<UserModel>>>(result)
        assertEquals(sampleUsers, (result as OperationResult.Success).data)
    }

    @Test
    fun `getUsers returns error when repository fails`() = runBlocking {
        val fakeRepository = FakeUserRepository(OperationResult.Error("Network error"))
        val useCase = GetUsersUseCase(fakeRepository)

        val result = useCase()

        assertIs<OperationResult.Error>(result)
        assertEquals("Network error", (result as OperationResult.Error).exception)
    }
}

class GetUserDetailsUseCaseTest {
    private val sampleUserDto = UserDto(
        1, "John", "Doe", "john@email.com",
        AddressDto("New York", GeoDto("40.7128", "-74.0060"))
    )
    private val sampleUser = UserMapper().map(sampleUserDto)

    @Test
    fun `getUserDetails returns success with user details`() = runBlocking {
        val fakeRepository = FakeUserRepository(userDetailResult = OperationResult.Success(sampleUserDto))
        val useCase = GetUserDetailsUseCase(fakeRepository)

        val result = useCase(1)

        assertIs<OperationResult.Success<UserModel>>(result)
        assertEquals(sampleUser, (result as OperationResult.Success).data)
    }

    @Test
    fun `getUserDetails returns error when repository fails`() = runBlocking {
        val fakeRepository = FakeUserRepository(userDetailResult = OperationResult.Error("User not found"))
        val useCase = GetUserDetailsUseCase(fakeRepository)

        val result = useCase(1)

        assertIs<OperationResult.Error>(result)
        assertEquals("User not found", (result as OperationResult.Error).exception)
    }
}