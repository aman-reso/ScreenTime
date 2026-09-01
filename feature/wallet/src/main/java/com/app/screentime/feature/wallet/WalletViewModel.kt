package com.app.screentime.feature.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.WalletTransaction
import com.app.screentime.core.network.dto.WalletPackDto
import com.app.screentime.core.network.dto.toWalletTransaction
import com.app.screentime.feature.wallet.domain.usecase.GetWalletPacksUseCase
import com.app.screentime.feature.wallet.domain.usecase.GetWalletUseCase
import com.app.screentime.feature.wallet.domain.usecase.RechargeWalletUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.core.model.UserRole

data class WalletUiState(
    val balance: Double = 0.0,
    val totalSpent: Double = 0.0,
    val totalEarned: Double = 0.0,
    val welcomeBonus: Double = 0.0,
    val transactions: List<WalletTransaction> = emptyList(),
    val packs: List<WalletPackDto> = emptyList(),
    val selectedPack: WalletPackDto? = null,
    val isModel: Boolean = false,
    val isLoading: Boolean = false,
    val isRecharging: Boolean = false,
    val rechargeSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val getWalletUseCase: GetWalletUseCase,
    private val getWalletPacksUseCase: GetWalletPacksUseCase,
    private val rechargeWalletUseCase: RechargeWalletUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val isModel = sessionManager.userRole == UserRole.MODEL
    private val _uiState = MutableStateFlow(
        WalletUiState(
            isModel = isModel,
            balance = if (!isModel) 1000.0 else 0.0
        )
    )
    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()

    init {
        loadWallet()
        loadPacks()
    }

    fun loadWallet() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            getWalletUseCase().onSuccess { response ->
                val txs = response.transactions?.map { it.toWalletTransaction() } ?: emptyList()
                val isUser = sessionManager.userRole == UserRole.USER
                val displayBalance = if (isUser && response.wallet.balance < 1000.0) 1000.0 else response.wallet.balance
                _uiState.value = _uiState.value.copy(
                    balance = displayBalance,
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

    fun loadPacks() {
        viewModelScope.launch {
            getWalletPacksUseCase().onSuccess { packList ->
                val popular = packList.firstOrNull { it.is_popular } ?: packList.getOrNull(1)
                _uiState.value = _uiState.value.copy(
                    packs = packList,
                    selectedPack = _uiState.value.selectedPack ?: popular
                )
            }
        }
    }

    fun selectPack(pack: WalletPackDto) {
        _uiState.value = _uiState.value.copy(selectedPack = pack)
    }

    fun recharge(amount: Double, coinsToAdd: Double = amount, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRecharging = true, error = null)
            val rechargeCoins = if (coinsToAdd > 0) coinsToAdd else amount
            rechargeWalletUseCase(rechargeCoins).onSuccess { newBalance ->
                _uiState.value = _uiState.value.copy(
                    balance = newBalance,
                    isRecharging = false,
                    rechargeSuccess = true
                )
                loadWallet()
                onSuccess()
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isRecharging = false,
                    error = error.localizedMessage
                )
            }
        }
    }

    fun rechargeSelectedPack(onSuccess: () -> Unit = {}) {
        val pack = _uiState.value.selectedPack ?: return
        val coinsToAdd = if (pack.total_coins > 0) pack.total_coins.toDouble() else (if (pack.coins > 0) pack.coins.toDouble() else pack.price_inr)
        recharge(pack.price_inr, coinsToAdd = coinsToAdd, onSuccess = onSuccess)
    }

    fun resetRechargeStatus() {
        _uiState.value = _uiState.value.copy(rechargeSuccess = false, error = null)
    }
}
