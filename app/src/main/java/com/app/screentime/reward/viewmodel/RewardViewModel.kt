package com.app.screentime.reward.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.reward.model.RewardClaimRequest
import com.app.screentime.reward.model.RewardUiState
import com.app.screentime.reward.model.SavedClaimDetails
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
                                catalogPairs = catalogItems.chunked(2), // Chunk into pairs for 2 items per row
                                error = null
                            )
                        },
                        onFailure = { catalogException ->
                            _uiState.value = RewardUiState(
                                isLoading = false,
                                totalCoins = totalCoins,
                                catalog = emptyList(),
                                catalogPairs = emptyList(),
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
                                catalogPairs = catalogItems.chunked(2), // Chunk into pairs for 2 items per row
                                error = coinsException.message ?: "Failed to load points"
                            )
                        },
                        onFailure = { catalogException ->
                            _uiState.value = RewardUiState(
                                isLoading = false,
                                totalCoins = 0,
                                catalog = emptyList(),
                                catalogPairs = emptyList(),
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
        recipientEmail: String,
        recipientPhone: String,
        shippingAddress: String?,
        postalCode: String?,
        saveDetails: Boolean = false,
        onSuccess: (Int) -> Unit, // Pass transaction ID
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            // Save details before making API call (regardless of success/failure)
            if (saveDetails) {
                claimRewardUseCase.saveClaimDetails(
                    SavedClaimDetails(
                        name = recipientName,
                        email = recipientEmail,
                        phone = recipientPhone,
                        address = shippingAddress,
                        postalCode = postalCode
                    )
                )
            } else {
                // Clear saved details if checkbox is unchecked
                claimRewardUseCase.clearSavedClaimDetails()
            }

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
                    // Extract only the error message, not the full exception
                    val errorMessage = exception.message?.let { msg ->
                        // If message contains JSON or full error response, extract just the message part
                        when {
                            msg.contains("\"message\"") -> {
                                // Try to extract message from JSON string
                                try {
                                    val messageMatch = Regex("\"message\"\\s*:\\s*\"([^\"]+)\"").find(msg)
                                    messageMatch?.groupValues?.get(1) ?: msg
                                } catch (e: Exception) {
                                    msg
                                }
                            }
                            msg.contains("message") && msg.length > 200 -> {
                                // If message is too long, try to extract just the relevant part
                                msg.substringBefore("\n").substringBefore("\\n").take(200)
                            }
                            else -> msg
                        }
                    } ?: "Failed to claim reward"
                    onError(errorMessage)
                }
            )
        }
    }

    fun getSavedClaimDetails(): SavedClaimDetails? {
        return claimRewardUseCase.getSavedClaimDetails()
    }
}
