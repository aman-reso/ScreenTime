package com.app.screentime.feature.call

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.feature.call.domain.usecase.ManageCallMediaUseCase
import com.app.screentime.feature.call.domain.usecase.ObserveVideoTracksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.livekit.android.room.Room
import io.livekit.android.room.track.VideoTrack as LiveKitVideoTrack
import kotlinx.coroutines.flow.StateFlow
import livekit.org.webrtc.VideoTrack as WebRtcVideoTrack
import javax.inject.Inject

/**
 * ViewModel orchestrating voice and video call states, media hardware control,
 * and LiveKit / WebRTC video stream publication.
 */
@HiltViewModel
class CallViewModel @Inject constructor(
    private val activeCallManager: ActiveCallManager,
    private val manageCallMediaUseCase: ManageCallMediaUseCase,
    private val observeVideoTracksUseCase: ObserveVideoTracksUseCase
) : ViewModel() {

    init {
        // Automatically start observing LiveKit room video tracks
        observeVideoTracksUseCase.startObserving(viewModelScope)
    }

    val callState: StateFlow<CallUiState> = activeCallManager.callState

    val room: Room
        get() = activeCallManager.getRoom()

    // LiveKit Video Tracks observed via ObserveVideoTracksUseCase
    val remoteVideoTrack: StateFlow<LiveKitVideoTrack?> = observeVideoTracksUseCase.remoteVideoTrack
    val localVideoTrack: StateFlow<LiveKitVideoTrack?> = observeVideoTracksUseCase.localVideoTrack

    // WebRTC Video Tracks (Fallback)
    val localWebRtcVideoTrack: StateFlow<WebRtcVideoTrack?> = activeCallManager.localWebRtcVideoTrack
    val remoteWebRtcVideoTrack: StateFlow<WebRtcVideoTrack?> = activeCallManager.remoteWebRtcVideoTrack
    val webRtcEglBase: livekit.org.webrtc.EglBase?
        get() = activeCallManager.webRtcEglBase

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
        manageCallMediaUseCase.toggleMute()
    }

    fun toggleSpeaker() {
        manageCallMediaUseCase.toggleSpeaker()
    }

    fun setCameraEnabled(enabled: Boolean) {
        manageCallMediaUseCase.setCameraEnabled(enabled)
    }

    fun toggleCamera() {
        manageCallMediaUseCase.toggleCamera()
    }

    fun flipCamera() {
        manageCallMediaUseCase.flipCamera()
    }

    fun isCurrentUserModel(): Boolean {
        return activeCallManager.isCurrentUserModel()
    }

    fun resetState() {
        activeCallManager.resetState()
    }
}
