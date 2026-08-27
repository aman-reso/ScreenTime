package com.app.screentime.calling.presentation

import com.app.screentime.calling.domain.model.CallSession
import com.app.screentime.calling.domain.model.CallState

data class CallUiState(
    val callState: CallState = CallState.Idle,
    val activeSession: CallSession? = null,
    val isConnecting: Boolean = false,
    val isIncomingRinging: Boolean = false,
    val isOutgoingRinging: Boolean = false,
    val isCallActive: Boolean = false,
    val isLowBalance: Boolean = false,
    val lowBalanceWarning: String? = null,
    val errorMessage: String? = null
)
