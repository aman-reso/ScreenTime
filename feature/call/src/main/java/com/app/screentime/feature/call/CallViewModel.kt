package com.app.screentime.feature.call

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CallViewModel @Inject constructor(
    private val activeCallManager: ActiveCallManager
) : ViewModel() {

    val callState: StateFlow<CallUiState> = activeCallManager.callState

    fun startOutgoingCall(modelId: String, modelName: String, ratePerMin: Double = 10.0) {
        activeCallManager.startOutgoingCall(modelId, modelName, ratePerMin)
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

    fun resetState() {
        activeCallManager.resetState()
    }
}
