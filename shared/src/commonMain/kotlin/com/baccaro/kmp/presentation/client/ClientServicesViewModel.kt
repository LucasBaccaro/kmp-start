import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baccaro.kmp.domain.model.Service
import com.baccaro.kmp.util.OperationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ClientServicesViewModel(
    private val repository: ClientRepository
) : ViewModel() {
    private val _state = MutableStateFlow<ClientServicesState>(ClientServicesState())
    val state: StateFlow<ClientServicesState> = _state.asStateFlow()

    init {
        loadServices()
    }

    fun loadServices() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            when (val result = repository.getClientServices()) {
                is OperationResult.Success -> {
                    _state.update { 
                        it.copy(
                            services = result.data,
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

    fun rateService(serviceId: String, rating: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            when (val result = repository.rateService(serviceId, rating)) {
                is OperationResult.Success -> {
                    loadServices() // Recargar la lista después de calificar
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

data class ClientServicesState(
    val services: List<Service> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) 