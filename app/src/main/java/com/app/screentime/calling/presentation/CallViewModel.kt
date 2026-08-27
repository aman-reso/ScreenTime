package com.app.screentime.calling.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.calling.domain.model.CallState
import com.app.screentime.calling.domain.usecase.CallUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallViewModel @Inject constructor(
    private val callUseCase: CallUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CallUiState())
    val uiState: StateFlow<CallUiState> = _uiState.asStateFlow()

    init {
        observeCallState()
        observeIncomingSignaling()
    }

    private fun observeCallState() {
        viewModelScope.launch {
            callUseCase.callState.collect { state ->
                _uiState.update { current ->
                    when (state) {
                        is CallState.Idle -> current.copy(
                            callState = state,
                            activeSession = null,
                            isConnecting = false,
                            isIncomingRinging = false,
                            isOutgoingRinging = false,
                            isCallActive = false,
                            isLowBalance = false,
                            lowBalanceWarning = null
                        )
                        is CallState.OutgoingRinging -> current.copy(
                            callState = state,
                            isOutgoingRinging = true,
                            isConnecting = true
                        )
                        is CallState.IncomingRinging -> current.copy(
                            callState = state,
                            isIncomingRinging = true
                        )
                        is CallState.Connecting -> current.copy(
                            callState = state,
                            activeSession = state.session,
                            isConnecting = true
                        )
                        is CallState.Active -> current.copy(
                            callState = state,
                            activeSession = state.session,
                            isCallActive = true,
                            isConnecting = false,
                            isIncomingRinging = false,
                            isOutgoingRinging = false,
                            isLowBalance = state.isLowBalance,
                            lowBalanceWarning = state.warningMessage
                        )
                        is CallState.Ended -> current.copy(
                            callState = state,
                            isCallActive = false,
                            isConnecting = false,
                            isIncomingRinging = false,
                            isOutgoingRinging = false
                        )
                        is CallState.Error -> current.copy(
                            callState = state,
                            errorMessage = state.message
                        )
                    }
                }
            }
        }
    }

    private fun observeIncomingSignaling() {
        viewModelScope.launch {
            callUseCase.incomingEvents.collect { msg ->
                callUseCase.handleIncomingSignaling(msg)
            }
        }
    }

    fun startCall(peerId: String, peerName: String, ratePerMin: Double) {
        viewModelScope.launch {
            callUseCase.startCall(peerId, peerName, ratePerMin)
        }
    }

    fun acceptIncomingCall(callId: String, callerId: String, callerName: String, rate: Double) {
        viewModelScope.launch {
            callUseCase.acceptCall(callId, callerId, callerName, rate)
        }
    }

    fun declineIncomingCall(callId: String, callerId: String) {
        viewModelScope.launch {
            callUseCase.declineCall(callId, callerId)
        }
    }

    fun endCall() {
        viewModelScope.launch {
            callUseCase.endCurrentCall()
        }
    }

    fun toggleMute() {
        callUseCase.toggleAudioMute()
    }

    fun toggleSpeaker() {
        callUseCase.toggleSpeaker()
    }
}
