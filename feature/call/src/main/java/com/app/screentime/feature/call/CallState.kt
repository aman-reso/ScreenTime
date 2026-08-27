package com.app.screentime.feature.call

enum class CallStatus {
    IDLE,
    CHECKING_BALANCE,      // Pre-call balance check in progress
    INSUFFICIENT_BALANCE,  // Caller has insufficient balance to initiate call
    DIALING,               // Outgoing call waiting for real remote response
    INCOMING,              // Receiving call with accept/reject buttons
    ACTIVE,                // In active call, WebRTC connected, live ticker running
    ENDED                  // Call ended with summary
}

data class CallUiState(
    val status: CallStatus = CallStatus.IDLE,
    val callId: String? = null,
    val remoteUserId: String = "",
    val remoteUserName: String = "",
    val remoteUserAvatar: String? = null,
    val ratePerMin: Double = 10.0,
    val currentBalance: Double = 0.0,
    val minRequiredBalance: Double = 10.0,
    val balanceMessage: String = "",
    val durationSec: Int = 0,
    val remainingSec: Int = 300,
    val cost: Double = 0.0,
    val isMuted: Boolean = false,
    val isSpeaker: Boolean = false,
    val isLowBalanceWarning: Boolean = false,
    val endReason: String? = null
)

