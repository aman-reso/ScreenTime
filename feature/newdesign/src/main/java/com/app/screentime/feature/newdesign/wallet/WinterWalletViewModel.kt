package com.app.screentime.feature.newdesign.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.network.api.WinterApi
import com.app.screentime.core.network.dto.WinterRechargePackageDto
import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.feature.newdesign.R
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
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<WinterWalletUiState>(WinterWalletUiState.Loading)
    val uiState: StateFlow<WinterWalletUiState> = _uiState.asStateFlow()

    init {
        loadWallet()
    }

    fun loadWallet(category: String = "All") {
        viewModelScope.launch {
            _uiState.value = WinterWalletUiState.Loading
            val token = sessionManager.getToken()
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

                _uiState.value = WinterWalletUiState.Success(
                    coinBalance = data.coin_balance,
                    transactions = mappedTxs,
                    packages = finalPackages,
                    selectedCategory = category
                )
            }.onFailure { err ->
                val fallbackPackages = packagesRes.getOrDefault(emptyList())
                if (fallbackPackages.isNotEmpty()) {
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

    fun purchasePackage(packageId: String) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is WinterWalletUiState.Success) {
                _uiState.value = currentState.copy(isRecharging = true)
            }

            val res = winterApi.purchaseCoins(packageId, sessionManager.getToken(), "google_play")
            res.onSuccess { result ->
                loadWallet()
            }.onFailure { err ->
                if (currentState is WinterWalletUiState.Success) {
                    _uiState.value = currentState.copy(
                        isRecharging = false,
                        userMessage = err.message ?: "Recharge failed"
                    )
                }
            }
        }
    }
}
