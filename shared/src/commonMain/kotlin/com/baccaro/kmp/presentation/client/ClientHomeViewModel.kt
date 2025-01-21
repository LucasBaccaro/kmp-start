import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baccaro.kmp.domain.model.Category
import com.baccaro.kmp.util.OperationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ClientHomeViewModel(
    private val repository: CategoryRepository
) : ViewModel() {
    private val _state = MutableStateFlow<ClientHomeState>(ClientHomeState())
    val state: StateFlow<ClientHomeState> = _state.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            when (val result = repository.getCategories()) {
                is OperationResult.Success -> {
                    _state.update { 
                        it.copy(
                            categories = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                is OperationResult.Error -> {
                    _state.update { 
                        it.copy(
                            error = result.exception,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}

data class ClientHomeState(
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) 