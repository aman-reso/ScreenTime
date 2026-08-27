package com.app.screentime.calling.domain.model

/**
 * Sealed hierarchy representing all phases of a Call lifecycle.
 */
sealed interface CallState {
    data object Idle : CallState

    data class OutgoingRinging(
        val peerId: String,
        val peerName: String,
        val ratePerMin: Double
    ) : CallState

    data class IncomingRinging(
        val callId: String,
        val callerId: String,
        val callerName: String,
        val callerAvatar: String?,
        val ratePerMin: Double
    ) : CallState

    data class Connecting(
        val session: CallSession
    ) : CallState

    data class Active(
        val session: CallSession,
        val isLowBalance: Boolean = false,
        val warningMessage: String? = null
    ) : CallState

    data class Ended(
        val reason: String,
        val session: CallSession? = null
    ) : CallState

    data class Error(
        val message: String
    ) : CallState
}
