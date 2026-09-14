package com.example.s8156519assignment2.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.s8156519assignment2.data.model.Food
import com.example.s8156519assignment2.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface DashboardUiState {

    data object Idle : DashboardUiState

    data object Loading : DashboardUiState

    data class Success(
        val foods: List<Food>,
        val entityTotal: Int
    ) : DashboardUiState

    data class Error(
        val message: String
    ) : DashboardUiState
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val foodRepository: FoodRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<DashboardUiState>(
            DashboardUiState.Idle
        )

    val uiState: StateFlow<DashboardUiState> =
        _uiState.asStateFlow()

    fun loadFoods(keypass: String) {
        if (keypass.isBlank()) {
            _uiState.value =
                DashboardUiState.Error(
                    "The dashboard keypass is missing."
                )
            return
        }

        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading

            foodRepository.getFoods(keypass).fold(
                onSuccess = { response ->
                    if (response.entities.isEmpty()) {
                        _uiState.value =
                            DashboardUiState.Error(
                                "No food information was found."
                            )
                    } else {
                        _uiState.value =
                            DashboardUiState.Success(
                                foods = response.entities,
                                entityTotal = response.entityTotal
                            )
                    }
                },
                onFailure = { error ->
                    _uiState.value =
                        DashboardUiState.Error(
                            error.message
                                ?: "Unable to load food information."
                        )
                }
            )
        }
    }
}