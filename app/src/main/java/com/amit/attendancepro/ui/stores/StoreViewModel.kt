package com.amit.attendancepro.ui.stores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amit.attendancepro.data.StoreRepository
import com.amit.attendancepro.domain.model.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class StoreUiState {
    data object Idle : StoreUiState()
    data object Loading : StoreUiState()
    data class StoreList(val stores: List<Store>) : StoreUiState()
    data class SingleStore(val store: Store?) : StoreUiState()
    data class Error(val message: String) : StoreUiState()
}

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val repo: StoreRepository
) : ViewModel() {

    private val _state = MutableStateFlow<StoreUiState>(StoreUiState.Idle)
    val state: StateFlow<StoreUiState> = _state.asStateFlow()

    fun loadAllStores() {
        _state.value = StoreUiState.Loading
        viewModelScope.launch {
            repo.getAllStores().onSuccess {
                _state.value = StoreUiState.StoreList(it)
            }.onFailure {
                _state.value = StoreUiState.Error(it.message ?: "Failed to load")
            }
        }
    }

    fun loadMyStore() {
        _state.value = StoreUiState.Loading
        viewModelScope.launch {
            repo.getMyStore().onSuccess {
                _state.value = StoreUiState.SingleStore(it)
            }.onFailure {
                _state.value = StoreUiState.Error(it.message ?: "Failed to load")
            }
        }
    }

    fun loadStoreById(id: String) {
        _state.value = StoreUiState.Loading
        viewModelScope.launch {
            repo.getStoreById(id).onSuccess {
                _state.value = StoreUiState.SingleStore(it)
            }.onFailure {
                _state.value = StoreUiState.Error(it.message ?: "Failed to load")
            }
        }
    }

    fun createStore(name: String, address: String, onSuccess: (Store) -> Unit) {
        if (name.isBlank() || address.isBlank()) {
            _state.value = StoreUiState.Error("Name and address required")
            return
        }
        _state.value = StoreUiState.Loading
        viewModelScope.launch {
            repo.createStore(name.trim(), address.trim()).onSuccess {
                _state.value = StoreUiState.Idle
                onSuccess(it)
            }.onFailure {
                _state.value = StoreUiState.Error(it.message ?: "Failed to create")
            }
        }
    }
}
