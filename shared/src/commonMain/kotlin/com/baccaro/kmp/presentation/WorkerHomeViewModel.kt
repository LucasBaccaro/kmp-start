package com.baccaro.kmp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baccaro.kmp.domain.model.Service
import com.baccaro.kmp.domain.repository.WorkerRepository
import com.baccaro.kmp.util.OperationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkerHomeViewModel(
    private val repository: WorkerRepository
) : ViewModel() {
    private val _state = MutableStateFlow<WorkerHomeState>(WorkerHomeState())
    val state: StateFlow<WorkerHomeState> = _state.asStateFlow()

    fun loadServices() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            when (val result = repository.getServices()) {
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

    fun acceptService(serviceId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = repository.acceptService(serviceId)) {
                is OperationResult.Success -> {
                    loadServices()
                }
                is OperationResult.Error -> {
                    _state.update { it.copy(error = result.exception, isLoading = false) }
                }
            }
        }
    }

    fun rejectService(serviceId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = repository.rejectService(serviceId)) {
                is OperationResult.Success -> {
                    loadServices()
                }
                is OperationResult.Error -> {
                    _state.update { it.copy(error = result.exception, isLoading = false) }
                }
            }
        }
    }

    fun completeService(serviceId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = repository.completeService(serviceId)) {
                is OperationResult.Success -> {
                    loadServices()
                }
                is OperationResult.Error -> {
                    _state.update { it.copy(error = result.exception, isLoading = false) }
                }
            }
        }
    }
}

data class WorkerHomeState(
    val services: List<Service> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) 