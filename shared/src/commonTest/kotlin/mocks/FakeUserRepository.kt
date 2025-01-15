package mocks

import com.baccaro.kmp.data.dto.AddressDto
import com.baccaro.kmp.data.dto.GeoDto
import com.baccaro.kmp.data.dto.UserDto
import com.baccaro.kmp.domain.model.UserModel
import com.baccaro.kmp.domain.repository.UserRepository
import com.baccaro.kmp.util.OperationResult
import com.baccaro.kmp.util.UserMapper

class FakeUserRepository(
    private val usersResult: OperationResult<List<UserDto>> = OperationResult.Success(emptyList()),
    private val userDetailResult: OperationResult<UserDto> = OperationResult.Success(UserDto(0, "", "", "", AddressDto("", GeoDto("0", "0"))))
) : UserRepository {
    override suspend fun getUsersList(): OperationResult<List<UserModel>> {
        return when (usersResult) {
            is OperationResult.Success -> {
                try {
                    val users = usersResult.data.map { UserMapper().map(it) }
                    OperationResult.Success(users)
                } catch (e: Exception) {
                    OperationResult.Error("Mapping Error: ${e.message}")
                }
            }
            is OperationResult.Error -> OperationResult.Error(usersResult.exception)
        }
    }

    override suspend fun getUserDetails(id: Int): OperationResult<UserModel> {
        return when (userDetailResult) {
            is OperationResult.Success -> {
                try {
                    val user = UserMapper().map(userDetailResult.data)
                    OperationResult.Success(user)
                } catch (e: Exception) {
                    OperationResult.Error("Mapping Error: ${e.message}")
                }
            }
            is OperationResult.Error -> OperationResult.Error(userDetailResult.exception)
        }
    }
}