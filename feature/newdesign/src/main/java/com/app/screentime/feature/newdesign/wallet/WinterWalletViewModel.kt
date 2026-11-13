package com.app.screentime.feature.newdesign.wallet

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.network.api.WinterApi
import com.app.screentime.core.network.dto.VerifyGooglePurchaseRequest
import com.app.screentime.core.network.dto.WinterRechargePackageDto
import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.feature.newdesign.R
import com.app.screentime.feature.wallet.billing.BillingPurchaseResult
import com.app.screentime.feature.wallet.billing.GooglePlayBillingManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface WinterWalletUiState {
    data object Loading : WinterWalletUiState
    data class Success(
        val coinBalance: Int,
        val transactions: List<CoinTransactionItem>,
        val packages: List<WinterRechargePackageDto>,
        val selectedCategory: String = "All",
        val isRecharging: Boolean = false,
        val userMessage: String? = null
    ) : WinterWalletUiState
    data class Error(val message: String) : WinterWalletUiState
}

@HiltViewModel
class WinterWalletViewModel @Inject constructor(
    private val winterApi: WinterApi,
    private val sessionManager: SessionManager,
    val googlePlayBillingManager: GooglePlayBillingManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<WinterWalletUiState>(WinterWalletUiState.Loading)
    val uiState: StateFlow<WinterWalletUiState> = _uiState.asStateFlow()

    init {
        loadWallet()
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
                        val current = _uiState.value
                        if (current is WinterWalletUiState.Success) {
                            _uiState.value = current.copy(
                                isRecharging = false,
                                userMessage = "Payment cancelled"
                            )
                        }
                    }
                    is BillingPurchaseResult.Failed -> {
                        val current = _uiState.value
                        if (current is WinterWalletUiState.Success) {
                            _uiState.value = current.copy(
                                isRecharging = false,
                                userMessage = result.message
                            )
                        }
                    }
                }
            }
        }
    }

    private fun handleBillingSuccess(result: BillingPurchaseResult.Success) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is WinterWalletUiState.Success) {
                _uiState.value = currentState.copy(isRecharging = true, userMessage = null)
            }

            val token = sessionManager.getToken().orEmpty()
            val verifyReq = VerifyGooglePurchaseRequest(
                package_name = "com.app.screentime",
                product_id = result.productId,
                purchase_token = result.purchaseToken,
                type = "inapp"
            )

            val verifyRes = winterApi.verifyGooglePurchase(token, verifyReq)
            verifyRes.onSuccess { data ->
                // Consume purchase on Google Play so user can purchase again
                googlePlayBillingManager.consumePurchase(result.purchaseToken)

                val newBalance = if (data.credit_balance > 0) {
                    data.credit_balance
                } else {
                    val currentBal = (currentState as? WinterWalletUiState.Success)?.coinBalance ?: 0
                    currentBal + data.credits_added
                }

                if (currentState is WinterWalletUiState.Success) {
                    _uiState.value = currentState.copy(
                        coinBalance = newBalance,
                        isRecharging = false,
                        userMessage = "Recharge successful! +${data.credits_added} coins added."
                    )
                }
                loadWallet()
            }.onFailure { err ->
                if (currentState is WinterWalletUiState.Success) {
                    _uiState.value = currentState.copy(
                        isRecharging = false,
                        userMessage = err.message ?: "Purchase verification failed"
                    )
                }
            }
        }
    }

    fun loadWallet(category: String = "All") {
        viewModelScope.launch {
            val token = sessionManager.getToken().orEmpty()
            val walletRes = winterApi.getWinterWalletDetails(token, category)
            val packagesRes = winterApi.getWinterRechargePackages()

            walletRes.onSuccess { data ->
                val mappedTxs = data.transactions.map { dto ->
                    CoinTransactionItem(
                        id = dto.id,
                        title = dto.title.ifBlank { if (dto.is_positive) "Coin recharge" else "Call spending" },
                        timestampDetails = dto.timestamp_details.ifBlank { "Recent · Completed" },
                        amountFormatted = dto.amount_formatted.ifBlank { if (dto.is_positive) "+${dto.amount}" else "${dto.amount}" },
                        isPositive = dto.is_positive,
                        iconRes = if (dto.is_positive) R.drawable.ic_recharge_plus_green else R.drawable.ic_phone_call,
                        category = dto.category
                    )
                }

                val finalPackages = if (data.packages.isNotEmpty()) {
                    data.packages
                } else {
                    packagesRes.getOrDefault(emptyList())
                }

                // Query product details from Google Play in background
                queryGooglePlayProducts(finalPackages)

                _uiState.value = WinterWalletUiState.Success(
                    coinBalance = data.coin_balance,
                    transactions = mappedTxs,
                    packages = finalPackages,
                    selectedCategory = category
                )
            }.onFailure { err ->
                val fallbackPackages = packagesRes.getOrDefault(emptyList())
                if (fallbackPackages.isNotEmpty()) {
                    queryGooglePlayProducts(fallbackPackages)
                    _uiState.value = WinterWalletUiState.Success(
                        coinBalance = 0,
                        transactions = emptyList(),
                        packages = fallbackPackages,
                        selectedCategory = category
                    )
                } else {
                    _uiState.value = WinterWalletUiState.Error(err.message ?: "Failed to load wallet")
                }
            }
        }
    }

    fun loadPackages() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState !is WinterWalletUiState.Success || currentState.packages.isEmpty()) {
                _uiState.value = WinterWalletUiState.Loading
            }

            val packagesRes = winterApi.getWinterRechargePackages()
            packagesRes.onSuccess { packs ->
                queryGooglePlayProducts(packs)

                when (val state = _uiState.value) {
                    is WinterWalletUiState.Success -> {
                        _uiState.value = state.copy(packages = packs)
                    }
                    else -> {
                        _uiState.value = WinterWalletUiState.Success(
                            coinBalance = 0,
                            transactions = emptyList(),
                            packages = packs
                        )
                    }
                }
            }.onFailure { err ->
                if (_uiState.value !is WinterWalletUiState.Success) {
                    _uiState.value = WinterWalletUiState.Error(err.message ?: "Failed to load packages")
                }
            }
        }
    }

    private fun queryGooglePlayProducts(packages: List<WinterRechargePackageDto>) {
        if (packages.isEmpty()) return
        val productSkus = packages.map { mapToGooglePlaySku(it.id) }.distinct()
        viewModelScope.launch {
            googlePlayBillingManager.queryProducts(productSkus)
        }
    }

    fun launchGooglePlayPurchase(
        activity: Activity,
        packageId: String,
        onError: (String) -> Unit = {}
    ) {
        val sku = mapToGooglePlaySku(packageId)
        val currentState = _uiState.value
        if (currentState is WinterWalletUiState.Success) {
            _uiState.value = currentState.copy(isRecharging = true, userMessage = null)
        }

        googlePlayBillingManager.launchBillingFlow(
            activity = activity,
            productId = sku,
            onLaunchFailed = { errorMsg ->
                val s = _uiState.value
                if (s is WinterWalletUiState.Success) {
                    _uiState.value = s.copy(isRecharging = false, userMessage = errorMsg)
                }
                onError(errorMsg)
            }
        )
    }

    private fun mapToGooglePlaySku(packageId: String): String {
        return when (packageId) {
            "pack_99" -> "product_99_rupees"
            "pack_199" -> "product_199_rupees"
            else -> packageId
        }
    }

    fun clearUserMessage() {
        val current = _uiState.value
        if (current is WinterWalletUiState.Success) {
            _uiState.value = current.copy(userMessage = null)
        }
    }
}
