package com.app.screentime.feature.call.domain.usecase

import android.util.Log
import com.app.screentime.feature.call.ActiveCallManager
import io.livekit.android.events.RoomEvent
import io.livekit.android.room.Room
import io.livekit.android.room.track.RemoteTrackPublication
import io.livekit.android.room.track.Track
import io.livekit.android.room.track.VideoTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

/**
 * UseCase responsible for discovering, subscribing to, and monitoring
 * remote and local video tracks in the active LiveKit room session.
 * Exposes reactive StateFlows consumed by the video call UI.
 */
class ObserveVideoTracksUseCase @Inject constructor(
    private val activeCallManager: ActiveCallManager
) {
    private val _remoteVideoTrack = MutableStateFlow<VideoTrack?>(null)
    val remoteVideoTrack: StateFlow<VideoTrack?> = _remoteVideoTrack.asStateFlow()

    private val _localVideoTrack = MutableStateFlow<VideoTrack?>(null)
    val localVideoTrack: StateFlow<VideoTrack?> = _localVideoTrack.asStateFlow()

    /**
     * Start observing video tracks from the room reactively and with periodic sync fallback.
     */
    fun startObserving(scope: CoroutineScope) {
        val room = activeCallManager.getRoom()

        // 1. Reactive room events listener
        scope.launch(Dispatchers.Main) {
            try {
                room.events.events.collect { event ->
                    when (event) {
                        is RoomEvent.TrackSubscribed -> {
                            val vt = event.track as? VideoTrack
                            if (vt != null) {
                                Log.i("ObserveVideoTracks", "🎥 TrackSubscribed: $vt")
                                _remoteVideoTrack.value = vt
                            }
                        }
                        is RoomEvent.TrackUnsubscribed -> {
                            if (event.track == _remoteVideoTrack.value) {
                                Log.i("ObserveVideoTracks", "🎥 TrackUnsubscribed: ${event.track}")
                                _remoteVideoTrack.value = findRemoteVideoTrack(room)
                            }
                        }
                        is RoomEvent.TrackPublished -> {
                            (event.publication as? RemoteTrackPublication)?.setSubscribed(true)
                            val r = findRemoteVideoTrack(room)
                            if (r != null) _remoteVideoTrack.value = r
                        }
                        is RoomEvent.ParticipantConnected, is RoomEvent.Connected -> {
                            val r = findRemoteVideoTrack(room)
                            if (r != null) _remoteVideoTrack.value = r
                            val l = findLocalVideoTrack(room)
                            if (l != null) _localVideoTrack.value = l
                        }
                        else -> {
                            val r = findRemoteVideoTrack(room)
                            if (r != null && r != _remoteVideoTrack.value) {
                                _remoteVideoTrack.value = r
                            }
                            val l = findLocalVideoTrack(room)
                            if (l != null && l != _localVideoTrack.value) {
                                _localVideoTrack.value = l
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.w("ObserveVideoTracks", "Room events collection ended: ${e.message}")
            }
        }

        // 2. Periodic sync check (every 250ms) to ensure timely track binding and camera activation
        scope.launch(Dispatchers.Main) {
            while (isActive) {
                val r = findRemoteVideoTrack(room)
                if (r != _remoteVideoTrack.value) {
                    _remoteVideoTrack.value = r
                }

                val l = findLocalVideoTrack(room)
                if (l != _localVideoTrack.value) {
                    _localVideoTrack.value = l
                }

                val callState = activeCallManager.callState.value
                if (callState.isCameraOn && l == null && room.state == Room.State.CONNECTED) {
                    try {
                        room.localParticipant.setCameraEnabled(true)
                    } catch (_: Exception) {}
                }

                delay(250.milliseconds)
            }
        }
    }

    private fun findRemoteVideoTrack(r: Room): VideoTrack? {
        for (participant in r.remoteParticipants.values) {
            val camPub = participant.getTrackPublication(Track.Source.CAMERA)
            if (camPub is RemoteTrackPublication) {
                if (!camPub.subscribed) {
                    camPub.setSubscribed(true)
                }
                val t = camPub.track as? VideoTrack
                if (t != null) return t
            }

            for (pair in participant.videoTrackPublications) {
                val pub = pair.first as? RemoteTrackPublication
                if (pub != null && !pub.subscribed) {
                    pub.setSubscribed(true)
                }
                val t = (pair.second as? VideoTrack) ?: (pub?.track as? VideoTrack)
                if (t != null) return t
            }

            for (pub in participant.trackPublications.values) {
                val remotePub = pub as? RemoteTrackPublication
                if (remotePub != null && remotePub.kind == Track.Kind.VIDEO) {
                    if (!remotePub.subscribed) {
                        remotePub.setSubscribed(true)
                    }
                    val t = remotePub.track as? VideoTrack
                    if (t != null) return t
                }
            }
        }
        return null
    }

    private fun findLocalVideoTrack(r: Room): VideoTrack? {
        val camPub = r.localParticipant.getTrackPublication(Track.Source.CAMERA)
        val camTrack = camPub?.track as? VideoTrack
        if (camTrack != null) return camTrack

        for (pair in r.localParticipant.videoTrackPublications) {
            val t = (pair.second as? VideoTrack) ?: (pair.first.track as? VideoTrack)
            if (t != null) return t
        }
        for (pub in r.localParticipant.trackPublications.values) {
            val t = pub.track as? VideoTrack
            if (t != null) return t
        }
        return null
    }
}
