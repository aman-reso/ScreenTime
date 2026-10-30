package com.app.screentime.feature.call.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.app.screentime.feature.call.CallUiState
import com.app.screentime.feature.call.webrtc.WebRtcVideoSurface
import com.telekom.odsystem.R as ODSR
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.tokens.tokens.ODSTheme
import io.livekit.android.room.Room
import io.livekit.android.room.track.VideoTrack as LiveKitVideoTrack
import livekit.org.webrtc.EglBase
import livekit.org.webrtc.VideoTrack as WebRtcVideoTrack

/**
 * VideoSurfaceContainer
 *
 * Full-screen video display container.
 * Determines the priority feed to show:
 * 1. Active Remote Video (LiveKit SFU or WebRTC P2P fallback)
 * 2. Full-screen Local Camera Preview while dialing / before remote connects
 * 3. Fallback Remote Avatar / Placeholder when camera feeds are unavailable.
 *
 * @param room Active LiveKit Room instance
 * @param callState Current state of the active call
 * @param remoteVideoTrack Active LiveKit remote video track, if subscribed
 * @param localVideoTrack Active LiveKit local video track, if publishing
 * @param remoteRtcVideoTrack Active WebRTC fallback remote track
 * @param localRtcVideoTrack Active WebRTC fallback local track
 * @param eglBase WebRTC EglBase context
 * @param avatarUrl Remote user avatar URL for fallback
 * @param modelName Remote user display name
 * @param scheme Telekom ODS Theme tokens
 */
@Composable
fun VideoSurfaceContainer(
    room: Room,
    callState: CallUiState,
    remoteVideoTrack: LiveKitVideoTrack?,
    localVideoTrack: LiveKitVideoTrack?,
    remoteRtcVideoTrack: WebRtcVideoTrack?,
    localRtcVideoTrack: WebRtcVideoTrack?,
    eglBase: EglBase?,
    avatarUrl: String,
    modelName: String,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    val isRemoteVideoActive = remoteVideoTrack != null || (remoteRtcVideoTrack != null && eglBase != null)

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        if (isRemoteVideoActive) {
            // ── Primary Remote Video Feed ──
            if (remoteRtcVideoTrack != null && eglBase != null) {
                WebRtcVideoSurface(
                    videoTrack = remoteRtcVideoTrack,
                    eglBase = eglBase,
                    modifier = Modifier.fillMaxSize()
                )
            } else if (remoteVideoTrack != null) {
                LiveKitVideoSurface(
                    room = room,
                    videoTrack = remoteVideoTrack,
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            // ── Full-Screen Local Camera Feed while Dialing / Ringing ──
            if (localVideoTrack != null && callState.isCameraOn) {
                LiveKitVideoSurface(
                    room = room,
                    videoTrack = localVideoTrack,
                    modifier = Modifier.fillMaxSize(),
                    isMirror = callState.isFrontCamera
                )
            } else if (localRtcVideoTrack != null && eglBase != null && callState.isCameraOn) {
                WebRtcVideoSurface(
                    videoTrack = localRtcVideoTrack,
                    eglBase = eglBase,
                    modifier = Modifier.fillMaxSize(),
                    isMirror = callState.isFrontCamera
                )
            } else {
                // ── Fallback Placeholder / Avatar ──
                if (avatarUrl.isNotBlank()) {
                    ODSImage(
                        imageModel = ODSImageModel(
                            url = avatarUrl,
                            contentDescription = modelName
                        ),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    ODSBox(
                        modifier = Modifier.fillMaxSize(),
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_user),
                            tint = scheme.basicTextRecessive.getColor(),
                            modifier = Modifier.size(96.dp)
                        )
                    }
                }
            }
        }
    }
}
