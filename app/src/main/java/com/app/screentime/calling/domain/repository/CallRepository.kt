package com.app.screentime.calling.domain.repository

import com.app.screentime.calling.data.model.CallSocketMessage
import com.app.screentime.calling.domain.model.CallSession
import com.app.screentime.calling.domain.model.CallState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface CallRepository {
    val callState: StateFlow<CallState>
    val incomingEvents: Flow<CallSocketMessage>

    suspend fun connect(baseUrl: String, token: String)
    suspend fun disconnect()

    suspend fun requestCall(receiverId: String, receiverName: String, ratePerMin: Double)
    suspend fun acceptCall(callId: String, callerId: String)
    suspend fun rejectCall(callId: String, callerId: String)
    suspend fun endCall(callId: String, peerId: String)

    suspend fun sendSdpOffer(targetUserId: String, callId: String, sdpJson: String)
    suspend fun sendSdpAnswer(targetUserId: String, callId: String, sdpJson: String)
    suspend fun sendIceCandidate(targetUserId: String, callId: String, candidateJson: String)

    fun updateSession(transform: (CallSession) -> CallSession)
    fun setCallState(state: CallState)
}
