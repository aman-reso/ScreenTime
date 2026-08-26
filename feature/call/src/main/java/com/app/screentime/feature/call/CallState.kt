package com.app.screentime.feature.call

enum class CallStatus {
    IDLE,
    DIALING,           // Outgoing call waiting for response
    INCOMING,          // Receiving call with accept/reject buttons
    ACTIVE,            // In active call, live ticker running
    ENDED              // Call ended with summary
}

data class CallUiState(
    val status: CallStatus = CallStatus.IDLE,
    val callId: String? = null,
    val remoteUserId: String = "",
    val remoteUserName: String = "",
    val remoteUserAvatar: String? = null,
    val ratePerMin: Double = 10.0,
    val durationSec: Int = 0,
    val remainingSec: Int = 300,
    val cost: Double = 0.0,
    val isMuted: Boolean = false,
    val isSpeaker: Boolean = false,
    val isLowBalanceWarning: Boolean = false,
    val endReason: String? = null
)
