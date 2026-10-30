package com.app.screentime.feature.call.domain.usecase

import com.app.screentime.feature.call.ActiveCallManager
import javax.inject.Inject

/**
 * UseCase to handle in-call hardware media controls:
 * - Microphone mute / unmute
 * - Speakerphone toggle
 * - Camera enable / disable
 * - Camera toggle & front/back switching
 */
class ManageCallMediaUseCase @Inject constructor(
    private val activeCallManager: ActiveCallManager
) {
    /** Toggle microphone mute state */
    fun toggleMute() {
        activeCallManager.toggleMute()
    }

    /** Toggle speakerphone vs earpiece output */
    fun toggleSpeaker() {
        activeCallManager.toggleSpeaker()
    }

    /** Set camera enabled or disabled explicitly */
    fun setCameraEnabled(enabled: Boolean) {
        activeCallManager.setCameraEnabled(enabled)
    }

    /** Toggle local camera video on or off */
    fun toggleCamera() {
        activeCallManager.toggleCamera()
    }

    /** Flip between front and back facing camera */
    fun flipCamera() {
        activeCallManager.flipCamera()
    }
}
