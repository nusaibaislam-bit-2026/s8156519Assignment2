package com.example.s8156519assignment2.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.s8156519assignment2.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface LoginUiState {

    data object Idle : LoginUiState

    data object Loading : LoginUiState

    data class Success(
        val keypass: String
    ) : LoginUiState

    data class Error(
        val message: String
    ) : LoginUiState
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val foodRepository: FoodRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<LoginUiState>(LoginUiState.Idle)

    val uiState: StateFlow<LoginUiState> =
        _uiState.asStateFlow()

    fun login(
        username: String,
        password: String
    ) {
        val cleanUsername = username.trim()
        val cleanPassword = password.trim()

        if (cleanUsername.isEmpty()) {
            _uiState.value =
                LoginUiState.Error("Please enter your student ID.")
            return
        }

        if (cleanPassword.isEmpty()) {
            _uiState.value =
                LoginUiState.Error("Please enter your password.")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            foodRepository.login(
                username = cleanUsername,
                password = cleanPassword
            ).fold(
                onSuccess = { response ->
                    _uiState.value =
                        LoginUiState.Success(response.keypass)
                },
                onFailure = { error ->
                    _uiState.value =
                        LoginUiState.Error(
                            error.message ?: "Login failed."
                        )
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}