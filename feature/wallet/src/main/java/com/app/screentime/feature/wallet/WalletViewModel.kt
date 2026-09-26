package com.app.screentime.feature.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.UserRole
import com.app.screentime.core.model.WalletTransaction
import com.app.screentime.core.network.dto.WalletPackDto
import com.app.screentime.core.network.dto.toWalletTransaction
import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.feature.wallet.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WalletUiState(
    val balance: Double = 0.0,
    val creditBalance: Int = 0,
    val voiceMinutesAvailable: Int = 0,
    val videoMinutesAvailable: Int = 0,
    val totalSpent: Double = 0.0,
    val totalEarned: Double = 0.0,
    val welcomeBonus: Double = 0.0,
    val transactions: List<WalletTransaction> = emptyList(),
    val transactionsPage: Int = 1,
    val hasMoreTransactions: Boolean = false,
    val isLoadingTransactions: Boolean = false,
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
    private val getWalletTransactionsUseCase: GetWalletTransactionsUseCase,
    private val getWalletInfoUseCase: GetWalletInfoUseCase,
    private val getWalletPacksUseCase: GetWalletPacksUseCase,
    private val rechargeWalletUseCase: RechargeWalletUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        WalletUiState(
            isModel = sessionManager.userRole == UserRole.MODEL
        )
    )
    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()

    init {
        loadWallet()
        loadPacks()
        loadTransactions(page = 1, refresh = true)
    }

    fun loadWallet() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            getWalletInfoUseCase().onSuccess { info ->
                _uiState.value = _uiState.value.copy(
                    creditBalance = info.credit_balance,
                    voiceMinutesAvailable = info.voice_minutes_available,
                    videoMinutesAvailable = info.video_minutes_available
                )
            }
            getWalletUseCase().onSuccess { response ->
                val serverBalance = response.wallet.balance
                val displayBalance = if (serverBalance > 0.0) serverBalance else (_uiState.value.creditBalance.toDouble())
                _uiState.value = _uiState.value.copy(
                    balance = displayBalance,
                    totalSpent = response.wallet.total_spent,
                    totalEarned = response.wallet.total_earned,
                    welcomeBonus = response.wallet.bonus_given,
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

    fun loadTransactions(page: Int = 1, refresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingTransactions = true)
            getWalletTransactionsUseCase(page = page, limit = 20).onSuccess { data ->
                val txs = data.items.map { it.toWalletTransaction() }
                val merged = if (refresh) txs else (_uiState.value.transactions + txs).distinctBy { it.id }
                
                // If the latest transaction has running balance_after, sync hero balance
                val latestBalance = txs.firstOrNull()?.balanceAfter?.takeIf { it > 0.0 }
                val currentBal = latestBalance ?: _uiState.value.balance

                _uiState.value = _uiState.value.copy(
                    transactions = merged,
                    balance = currentBal,
                    transactionsPage = data.page,
                    hasMoreTransactions = data.has_more,
                    isLoadingTransactions = false
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(isLoadingTransactions = false)
            }
        }
    }

    fun loadNextTransactionsPage() {
        val state = _uiState.value
        if (!state.isLoadingTransactions && state.hasMoreTransactions) {
            loadTransactions(page = state.transactionsPage + 1, refresh = false)
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
                loadTransactions(page = 1, refresh = true)
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
