package com.app.screentime.feature.wallet

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.UserRole
import com.app.screentime.core.model.WalletTransaction
import com.app.screentime.core.network.dto.WalletPackDto
import com.app.screentime.core.network.dto.toWalletTransaction
import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.feature.wallet.billing.BillingPurchaseResult
import com.app.screentime.feature.wallet.billing.GooglePlayBillingManager
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
    val isVerifyingPayment: Boolean = false,
    val rechargeSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val getWalletUseCase: GetWalletUseCase,
    private val getWalletTransactionsUseCase: GetWalletTransactionsUseCase,
    private val getWalletInfoUseCase: GetWalletInfoUseCase,
    private val getWalletPacksUseCase: GetWalletPacksUseCase,
    private val verifyGooglePlayPurchaseUseCase: VerifyGooglePlayPurchaseUseCase,
    val googlePlayBillingManager: GooglePlayBillingManager,
    sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        WalletUiState(
            isModel = sessionManager.userRole == UserRole.MODEL
        )
    )
    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()

    init {
        loadWallet()
        loadTransactions(page = 1, refresh = true)
        observeBillingEvents()
    }

    private fun observeBillingEvents() {
        viewModelScope.launch {
            googlePlayBillingManager.purchaseEvents.collect { result ->
                when (result) {
                    is BillingPurchaseResult.Success -> {
                        handleBillingSuccess(result)
                    }

                    is BillingPurchaseResult.Cancelled -> {
                        _uiState.value = _uiState.value.copy(
                            isRecharging = false,
                            error = "Payment cancelled"
                        )
                    }

                    is BillingPurchaseResult.Failed -> {
                        _uiState.value = _uiState.value.copy(
                            isRecharging = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }

    private fun handleBillingSuccess(result: BillingPurchaseResult.Success) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isVerifyingPayment = true, error = null)
            val pack = _uiState.value.packs.firstOrNull { it.id == result.productId }
                ?: _uiState.value.selectedPack

            val credits = pack?.effectiveCredits ?: 0
            val amount = pack?.effectivePrice ?: 0.0
            val packId = pack?.id ?: result.productId
            val type = if (result.productId.contains("sub", ignoreCase = true) || packId.contains(
                    "sub",
                    ignoreCase = true
                )
            ) "subs" else "inapp"

            val verifyRes = verifyGooglePlayPurchaseUseCase(
                purchaseToken = result.purchaseToken,
                productId = result.productId,
                type = type,
                orderId = result.orderId,
                packId = packId,
                credits = credits,
                amount = amount
            )

            val data = verifyRes.getOrNull()
            if (verifyRes.isSuccess && data != null && data.success) {
                val updatedCreditBalance =
                    if (data.credit_balance > 0) data.credit_balance else (_uiState.value.creditBalance + (if (data.credits_added > 0) data.credits_added else credits))
                _uiState.value = _uiState.value.copy(
                    isVerifyingPayment = false,
                    isRecharging = false,
                    rechargeSuccess = true,
                    creditBalance = updatedCreditBalance
                )
                loadWallet()
                loadTransactions(page = 1, refresh = true)
            } else {
                _uiState.value = _uiState.value.copy(
                    isVerifyingPayment = false,
                    isRecharging = false,
                    error = verifyRes.exceptionOrNull()?.localizedMessage ?: data?.message
                    ?: "Failed to verify purchase"
                )
            }
        }
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
                val serverBalance = response.wallet?.balance ?: 0.0
                val displayBalance =
                    if (serverBalance > 0.0) serverBalance else (_uiState.value.creditBalance.toDouble())
                _uiState.value = _uiState.value.copy(
                    balance = displayBalance,
                    totalSpent = response.wallet?.total_spent ?: 0.0,
                    totalEarned = response.wallet?.total_earned ?: 0.0,
                    welcomeBonus = response.wallet?.bonus_given ?: 0.0,
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
                val merged =
                    if (refresh) txs else (_uiState.value.transactions + txs).distinctBy { it.id }

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
                val popular = packList.firstOrNull { it.isPopularPack } ?: packList.getOrNull(1)
                ?: packList.firstOrNull()
                _uiState.value = _uiState.value.copy(
                    packs = packList,
                    selectedPack = _uiState.value.selectedPack ?: popular
                )

                // Query Google Play Product Details in background
                if (packList.isNotEmpty()) {
                    val productIds = packList.map { it.id }.filter { it.isNotBlank() }
                    googlePlayBillingManager.queryProducts(productIds)
                }
            }
        }
    }

    fun selectPack(pack: WalletPackDto) {
        _uiState.value = _uiState.value.copy(selectedPack = pack)
    }

    fun buyWithGooglePlay(
        activity: Activity,
        pack: WalletPackDto,
        onError: (String) -> Unit = {}
    ) {
        _uiState.value = _uiState.value.copy(isRecharging = true, selectedPack = pack, error = null)
        googlePlayBillingManager.launchBillingFlow(
            activity = activity,
            productId = pack.id,
            onLaunchFailed = { err ->
                _uiState.value = _uiState.value.copy(isRecharging = false, error = err)
                onError(err)
            }
        )
    }

    fun resetRechargeStatus() {
        _uiState.value = _uiState.value.copy(rechargeSuccess = false, error = null)
    }
}
