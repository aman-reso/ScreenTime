package com.app.screentime.feature.call

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.livekit.android.room.Room
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CallViewModel @Inject constructor(
    private val activeCallManager: ActiveCallManager
) : ViewModel() {

    val callState: StateFlow<CallUiState> = activeCallManager.callState

    val room: Room
        get() = activeCallManager.getRoom()

    fun startOutgoingCall(
        modelId: String,
        modelName: String,
        ratePerMin: Double = 10.0,
        callType: CallType = CallType.VOICE
    ) {
        activeCallManager.startOutgoingCall(modelId, modelName, ratePerMin, callType)
    }

    fun acceptIncomingCall() {
        activeCallManager.acceptIncomingCall()
    }

    fun rejectIncomingCall() {
        activeCallManager.rejectIncomingCall()
    }

    fun endCall(reason: String? = null) {
        activeCallManager.endCall(reason)
    }

    fun toggleMute() {
        activeCallManager.toggleMute()
    }

    fun toggleSpeaker() {
        activeCallManager.toggleSpeaker()
    }

    fun toggleCamera() {
        activeCallManager.toggleCamera()
    }

    fun flipCamera() {
        activeCallManager.flipCamera()
    }

    fun isCurrentUserModel(): Boolean {
        return activeCallManager.isCurrentUserModel()
    }

    fun resetState() {
        activeCallManager.resetState()
    }
}
