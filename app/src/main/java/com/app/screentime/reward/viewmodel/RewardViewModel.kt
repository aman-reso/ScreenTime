package com.app.screentime.reward.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.reward.model.RewardClaimRequest
import com.app.screentime.reward.model.RewardUiState
import com.app.screentime.reward.usecase.ClaimRewardUseCase
import com.app.screentime.reward.usecase.CoinHistoryUseCase
import com.app.screentime.reward.usecase.RewardCatalogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RewardViewModel @Inject constructor(
    private val coinHistoryUseCase: CoinHistoryUseCase,
    private val rewardCatalogUseCase: RewardCatalogUseCase,
    private val claimRewardUseCase: ClaimRewardUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RewardUiState(isLoading = true))
    val uiState: StateFlow<RewardUiState> = _uiState.asStateFlow()

    init {
        loadRewardData()
    }

    fun loadRewardData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val coinsResult = coinHistoryUseCase.getCoinHistory()
            val catalogResult = rewardCatalogUseCase.getRewardCatalog()

            // Handle both results
            coinsResult.fold(
                onSuccess = { (totalCoins, _) ->
                    catalogResult.fold(
                        onSuccess = { catalogItems ->
                            _uiState.value = RewardUiState(
                                isLoading = false,
                                totalCoins = totalCoins,
                                catalog = catalogItems,
                                error = null
                            )
                        },
                        onFailure = { catalogException ->
                            _uiState.value = RewardUiState(
                                isLoading = false,
                                totalCoins = totalCoins,
                                catalog = emptyList(),
                                error = catalogException.message ?: "Failed to load rewards"
                            )
                        }
                    )
                },
                onFailure = { coinsException ->
                    catalogResult.fold(
                        onSuccess = { catalogItems ->
                            _uiState.value = RewardUiState(
                                isLoading = false,
                                totalCoins = 0,
                                catalog = catalogItems,
                                error = coinsException.message ?: "Failed to load points"
                            )
                        },
                        onFailure = { catalogException ->
                            _uiState.value = RewardUiState(
                                isLoading = false,
                                totalCoins = 0,
                                catalog = emptyList(),
                                error = "Failed to load points and rewards"
                            )
                        }
                    )
                }
            )
        }
    }

    fun loadTotalCoins() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            coinHistoryUseCase.getCoinHistory().fold(
                onSuccess = { (totalCoins, _) ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        totalCoins = totalCoins,
                        error = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        totalCoins = 0,
                        error = exception.message ?: "Failed to load total coins"
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun claimReward(
        rewardCatalogId: Int,
        recipientName: String,
        recipientPhone: String,
        shippingAddress: String?,
        postalCode: String?,
        onSuccess: (Int) -> Unit, // Pass transaction ID
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val request = RewardClaimRequest(
                rewardCatalogId = rewardCatalogId,
                recipientName = recipientName,
                recipientPhone = recipientPhone,
                shippingAddress = shippingAddress,
                city = null,
                state = null,
                postalCode = postalCode,
                country = null
            )

            claimRewardUseCase.claimReward(request).fold(
                onSuccess = { claimData ->
                    // Update total coins from the response
                    _uiState.value = _uiState.value.copy(
                        totalCoins = claimData.remainingCoins
                    )
                    onSuccess(claimData.transactionId)
                },
                onFailure = { exception ->
                    onError(exception.message ?: "Failed to claim reward")
                }
            )
        }
    }
}
