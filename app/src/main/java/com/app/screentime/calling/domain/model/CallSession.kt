package com.app.screentime.calling.domain.model

/**
 * Domain representation of an active call session.
 */
data class CallSession(
    val callId: String,
    val peerId: String,
    val peerName: String,
    val peerAvatar: String? = null,
    val isIncoming: Boolean = false,
    val ratePerMin: Double = 0.0,
    val durationSec: Int = 0,
    val remainingSec: Int = 0,
    val totalCost: Double = 0.0,
    val isAudioMuted: Boolean = false,
    val isSpeakerOn: Boolean = false
) {
    val durationFormatted: String
        get() {
            val minutes = durationSec / 60
            val seconds = durationSec % 60
            return String.format("%02d:%02d", minutes, seconds)
        }

    val remainingFormatted: String
        get() {
            val minutes = remainingSec / 60
            val seconds = remainingSec % 60
            return String.format("%02d:%02d", minutes, seconds)
        }
}
