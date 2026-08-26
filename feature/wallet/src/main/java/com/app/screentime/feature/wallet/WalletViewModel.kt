package com.app.screentime.feature.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.WalletTransaction
import com.app.screentime.core.network.dto.toWalletTransaction
import com.app.screentime.feature.wallet.domain.usecase.GetWalletUseCase
import com.app.screentime.feature.wallet.domain.usecase.RechargeWalletUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WalletUiState(
    val balance: Double = 0.0,
    val totalSpent: Double = 0.0,
    val totalEarned: Double = 0.0,
    val welcomeBonus: Double = 0.0,
    val transactions: List<WalletTransaction> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val getWalletUseCase: GetWalletUseCase,
    private val rechargeWalletUseCase: RechargeWalletUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WalletUiState())
    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()

    init {
        loadWallet()
    }

    fun loadWallet() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            getWalletUseCase().onSuccess { response ->
                val txs = response.transactions?.map { it.toWalletTransaction() } ?: emptyList()
                _uiState.value = _uiState.value.copy(
                    balance = response.wallet.balance,
                    totalSpent = response.wallet.total_spent,
                    totalEarned = response.wallet.total_earned,
                    welcomeBonus = response.wallet.bonus_given,
                    transactions = txs,
                    isLoading = false
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.localizedMessage
                )
            }
        }
    }

    fun recharge(amount: Double) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            rechargeWalletUseCase(amount).onSuccess { newBalance ->
                _uiState.value = _uiState.value.copy(
                    balance = newBalance,
                    isLoading = false
                )
                loadWallet() // Refresh transactions
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.localizedMessage
                )
            }
        }
    }
}
