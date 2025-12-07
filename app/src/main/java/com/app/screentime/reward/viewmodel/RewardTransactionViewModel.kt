package com.app.screentime.reward.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.reward.model.RewardTransaction
import com.app.screentime.reward.usecase.RewardTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RewardTransactionUiState(
    val isLoading: Boolean = false,
    val transactions: List<RewardTransaction> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class RewardTransactionViewModel @Inject constructor(
    private val rewardTransactionUseCase: RewardTransactionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RewardTransactionUiState(isLoading = true))
    val uiState: StateFlow<RewardTransactionUiState> = _uiState.asStateFlow()

    init {
        loadTransactions()
    }

    fun loadTransactions() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            rewardTransactionUseCase.getRewardTransactions().fold(
                onSuccess = { transactions ->
                    _uiState.value = RewardTransactionUiState(
                        isLoading = false,
                        transactions = transactions,
                        error = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value = RewardTransactionUiState(
                        isLoading = false,
                        transactions = emptyList(),
                        error = exception.message ?: "Failed to load transactions"
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

