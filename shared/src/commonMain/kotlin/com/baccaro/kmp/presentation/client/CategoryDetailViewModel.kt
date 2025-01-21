import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baccaro.kmp.util.OperationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow

class CategoryDetailViewModel(
    private val categoryRepository: CategoryRepository,
    private val clientRepository: ClientRepository
) : ViewModel() {
    private val _state = MutableStateFlow<CategoryDetailState>(CategoryDetailState())
    val state: StateFlow<CategoryDetailState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<CategoryDetailEvent>()
    val eventFlow = _eventFlow

    fun loadCategory(categoryId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            when (val result = categoryRepository.getCategoryWorkers(categoryId)) {
                is OperationResult.Success -> {
                    _state.update { 
                        it.copy(
                            category = result.data,
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

    fun requestService(request: ServiceRequest) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            when (val result = clientRepository.requestService(request)) {
                is OperationResult.Success -> {
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            error = null,
                            showSuccessMessage = true
                        )
                    }
                    _eventFlow.emit(CategoryDetailEvent.RefreshServices)
                }
                is OperationResult.Error -> {
                    _state.update { 
                        it.copy(
                            error = result.exception,
                            isLoading = false,
                            showSuccessMessage = false
                        )
                    }
                }
            }
        }
    }

    fun onSuccessMessageShown() {
        _state.update { it.copy(showSuccessMessage = false) }
    }
}

data class CategoryDetailState(
    val category: CategoryWithWorkers? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showSuccessMessage: Boolean = false
)

sealed class CategoryDetailEvent {
    object RefreshServices : CategoryDetailEvent()
} 