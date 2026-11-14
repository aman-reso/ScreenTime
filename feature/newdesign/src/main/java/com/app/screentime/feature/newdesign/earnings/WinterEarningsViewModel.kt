package com.app.screentime.feature.newdesign.earnings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.network.api.WinterApi
import com.app.screentime.core.network.dto.CreatorEarningsDto
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.feature.newdesign.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

data class WinterEarningsUiState(
    val isLoading: Boolean = true,
    val availableEarnings: String = "₹0",
    val rawAvailableBalance: Int = 0,
    val totalEarnedSummary: String = "₹0 earned · ₹0 withdrawn",
    val audioCallDuration: String = "0m",
    val videoCallDuration: String = "0m",
    val savedUpiId: String = "",
    val allItems: List<EarningHistoryItem> = emptyList(),
    val selectedFilter: String = "All activity",
    val isSubmittingWithdrawal: Boolean = false,
    val withdrawalMessage: String? = null,
    val withdrawalSuccess: Boolean = false,
    val lastWithdrawnAmount: Int = 0,
    val errorMessage: String? = null
) {
    val filteredItems: List<EarningHistoryItem>
        get() = when (selectedFilter) {
            "All activity" -> allItems
            else -> allItems.filter { it.category == selectedFilter }
        }
}

@HiltViewModel
class WinterEarningsViewModel @Inject constructor(
    private val winterApi: WinterApi,
    private val sessionManager: SessionManager,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        WinterEarningsUiState(
            savedUpiId = preferencesManager.getSavedUpiId()
        )
    )
    val uiState: StateFlow<WinterEarningsUiState> = _uiState.asStateFlow()

    init {
        loadEarnings()
    }

    fun loadEarnings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val token = sessionManager.getToken().orEmpty()
            val result = winterApi.getCreatorEarnings(token)

            result.onSuccess { dto ->
                val mappedItems = mapDtoToHistoryItems(dto)
                val availableFormatted = if (dto.available_earnings_formatted.isNotBlank()) {
                    dto.available_earnings_formatted
                } else {
                    String.format(Locale.US, "₹%,d", dto.available_balance)
                }

                val totalSummary = if (dto.total_earned_summary.isNotBlank()) {
                    dto.total_earned_summary
                } else {
                    String.format(
                        Locale.US,
                        "₹%,d earned · ₹%,d withdrawn",
                        dto.total_earnings,
                        dto.total_withdrawals
                    )
                }

                val audioDur = if (dto.audio_call_duration.isNotBlank()) {
                    dto.audio_call_duration
                } else {
                    formatMinutes(dto.voice_call_minutes)
                }

                val videoDur = if (dto.video_call_duration.isNotBlank()) {
                    dto.video_call_duration
                } else {
                    formatMinutes(dto.video_call_minutes)
                }

                val savedUpi = if (dto.saved_upi_id.isNotBlank()) {
                    preferencesManager.setSavedUpiId(dto.saved_upi_id)
                    dto.saved_upi_id
                } else {
                    preferencesManager.getSavedUpiId()
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        availableEarnings = availableFormatted,
                        rawAvailableBalance = dto.available_balance,
                        totalEarnedSummary = totalSummary,
                        audioCallDuration = audioDur,
                        videoCallDuration = videoDur,
                        savedUpiId = savedUpi,
                        allItems = mappedItems,
                        errorMessage = null
                    )
                }
            }.onFailure { err ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = err.message ?: "Failed to load creator earnings"
                    )
                }
            }
        }
    }

    fun setFilter(filter: String) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    fun submitWithdrawal(amount: Int, upiId: String = "", onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            if (amount < 50) {
                _uiState.update { it.copy(withdrawalMessage = "Minimum withdrawal amount is ₹50") }
                return@launch
            }
            val balance = _uiState.value.rawAvailableBalance
            if (amount > balance) {
                _uiState.update { it.copy(withdrawalMessage = "Insufficient balance (Available: ₹$balance)") }
                return@launch
            }

            val finalUpi = if (upiId.isNotBlank()) {
                upiId.trim()
            } else {
                _uiState.value.savedUpiId.ifBlank { preferencesManager.getSavedUpiId() }
            }

            if (finalUpi.isNotBlank() && !finalUpi.contains("@")) {
                _uiState.update { it.copy(withdrawalMessage = "Please enter a valid UPI ID (e.g. name@upi)") }
                return@launch
            }

            if (finalUpi.isNotBlank()) {
                preferencesManager.setSavedUpiId(finalUpi)
            }
            _uiState.update {
                it.copy(
                    isSubmittingWithdrawal = true,
                    savedUpiId = finalUpi,
                    withdrawalMessage = null
                )
            }

            val token = sessionManager.getToken().orEmpty()
            val res = winterApi.withdrawFunds(token, amount, finalUpi)

            res.onSuccess { resp ->
                _uiState.update {
                    it.copy(
                        isSubmittingWithdrawal = false,
                        withdrawalSuccess = true,
                        lastWithdrawnAmount = amount,
                        withdrawalMessage = resp.message.ifBlank { "Withdrawal request submitted successfully" }
                    )
                }
                loadEarnings()
                onSuccess()
            }.onFailure { err ->
                _uiState.update {
                    it.copy(
                        isSubmittingWithdrawal = false,
                        withdrawalSuccess = false,
                        withdrawalMessage = err.message ?: "Failed to submit withdrawal request"
                    )
                }
            }
        }
    }

    fun clearWithdrawalMessage() {
        _uiState.update {
            it.copy(
                withdrawalMessage = null,
                withdrawalSuccess = false,
                lastWithdrawnAmount = 0
            )
        }
    }

    private fun mapDtoToHistoryItems(dto: CreatorEarningsDto): List<EarningHistoryItem> {
        if (dto.activity_items.isNotEmpty()) {
            return dto.activity_items.map { act ->
                val icon = when (act.icon_type) {
                    "video" -> R.drawable.ic_call_video_cam
                    "audio" -> R.drawable.ic_phone_accept
                    "withdrawal" -> R.drawable.ic_withdrawal_history_arrow
                    else -> R.drawable.ic_sparkles
                }
                EarningHistoryItem(
                    id = act.id,
                    title = act.title,
                    details = act.details,
                    amountFormatted = act.amount_formatted,
                    isPositive = act.is_positive,
                    iconRes = icon,
                    category = act.category
                )
            }
        }

        // Fallback mapping from recent_activity if activity_items is empty
        return dto.recent_activity.map { tx ->
            val isPos = tx.amount > 0
            val txType = tx.transaction_type.orEmpty()
            val desc = tx.description.orEmpty()
            val status = tx.status.orEmpty()

            val isWithdrawal = txType.contains("withdrawal", ignoreCase = true) || desc.contains("withdrawal", ignoreCase = true)
            val isVoice = desc.contains("voice", ignoreCase = true) || desc.contains("audio", ignoreCase = true)
            val isVideo = desc.contains("video", ignoreCase = true)

            val (category, icon, title) = when {
                isWithdrawal -> Triple("Withdrawals", R.drawable.ic_withdrawal_history_arrow, "Withdrawal")
                isVideo -> Triple("Calls", R.drawable.ic_call_video_cam, "Video call earnings")
                isVoice -> Triple("Calls", R.drawable.ic_phone_accept, "Audio call earnings")
                else -> Triple("All activity", R.drawable.ic_sparkles, if (isPos) "Weekly bonus" else "Adjustment")
            }

            val amountStr = if (isPos) "+₹${tx.amount}" else "−₹${Math.abs(tx.amount)}"
            val detailsStr = if (isWithdrawal) {
                status.ifBlank { "Pending" }
            } else {
                desc.ifBlank { "Completed" }
            }

            EarningHistoryItem(
                id = tx.id,
                title = title,
                details = detailsStr,
                amountFormatted = amountStr,
                isPositive = isPos,
                iconRes = icon,
                category = category
            )
        }
    }

    private fun formatMinutes(minutes: Int): String {
        if (minutes <= 0) return "0m"
        val hours = minutes / 60
        val mins = minutes % 60
        return when {
            hours > 0 && mins > 0 -> "${hours}h ${mins}m"
            hours > 0 -> "${hours}h"
            else -> "${mins}m"
        }
    }
}
