package com.amit.attendancepro.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amit.attendancepro.data.AuthRepository
import com.amit.attendancepro.domain.model.UserProfile
import com.amit.attendancepro.domain.model.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class Authenticated(val profile: UserProfile) : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun checkAuth() {
        if (repo.isLoggedIn) loadProfile() else _state.value = AuthState.Idle
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.value = AuthState.Error("Email and password required")
            return
        }
        _state.value = AuthState.Loading
        viewModelScope.launch {
            repo.login(email.trim(), password).onSuccess {
                loadProfile()
            }.onFailure {
                _state.value = AuthState.Error(it.message ?: "Login failed")
            }
        }
    }

    fun signup(name: String, email: String, password: String, role: UserRole) {
        if (name.isBlank() || email.isBlank() || password.length < 6) {
            _state.value = AuthState.Error("Name, valid email, and 6+ char password required")
            return
        }
        _state.value = AuthState.Loading
        viewModelScope.launch {
            repo.signUp(name.trim(), email.trim(), password, role).onSuccess {
                loadProfile()
            }.onFailure {
                _state.value = AuthState.Error(it.message ?: "Signup failed")
            }
        }
    }

    fun logout() {
        repo.logout()
        _state.value = AuthState.Idle
    }

    private fun loadProfile() {
        viewModelScope.launch {
            repo.fetchProfile().onSuccess {
                _state.value = AuthState.Authenticated(it)
            }.onFailure {
                _state.value = AuthState.Error(it.message ?: "Failed to load profile")
            }
        }
    }

    fun clearError() { if (_state.value is AuthState.Error) _state.value = AuthState.Idle }
}
