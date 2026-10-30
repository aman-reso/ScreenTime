package com.app.screentime.feature.call.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.screentime.feature.call.CallUiState
import com.app.screentime.feature.call.webrtc.WebRtcVideoSurface
import com.telekom.odsystem.R as ODSR
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.tokens.tokens.ODSTheme
import io.livekit.android.room.Room
import io.livekit.android.room.track.VideoTrack as LiveKitVideoTrack
import livekit.org.webrtc.EglBase
import livekit.org.webrtc.VideoTrack as WebRtcVideoTrack

/**
 * SelfViewPipCard
 *
 * Floating Picture-in-Picture (PiP) self-view window placed in the top-right
 * corner of the screen when remote video stream is active.
 * Allows the user to monitor their own camera feed and tap to flip camera front/back.
 *
 * @param room Active LiveKit Room instance
 * @param callState Current state of the active call
 * @param localVideoTrack Active local LiveKit video track
 * @param localRtcVideoTrack Active local WebRTC fallback video track
 * @param eglBase WebRTC EglBase context
 * @param scheme Telekom ODS Theme tokens
 * @param onFlipCamera Action triggered when PiP window is tapped
 */
@Composable
fun SelfViewPipCard(
    room: Room,
    callState: CallUiState,
    localVideoTrack: LiveKitVideoTrack?,
    localRtcVideoTrack: WebRtcVideoTrack?,
    eglBase: EglBase?,
    scheme: ODSTheme,
    onFlipCamera: () -> Unit,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier
            .statusBarsPadding()
            .padding(top = 68.dp, end = 16.dp)
            .width(108.dp)
            .height(148.dp)
            .clickable { onFlipCamera() },
        cornerRadius = ODSCorners(all = 16.dp),
        border = ODSBorder(
            width = 2.dp,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicAccent))
        ),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        clipContent = true
    ) {
        if (callState.isCameraOn) {
            if (localRtcVideoTrack != null && eglBase != null) {
                WebRtcVideoSurface(
                    videoTrack = localRtcVideoTrack,
                    eglBase = eglBase,
                    modifier = Modifier.fillMaxSize(),
                    isMirror = callState.isFrontCamera
                )
            } else if (localVideoTrack != null) {
                LiveKitVideoSurface(
                    room = room,
                    videoTrack = localVideoTrack,
                    modifier = Modifier.fillMaxSize(),
                    isMirror = callState.isFrontCamera
                )
            } else {
                ODSBox(
                    modifier = Modifier.fillMaxSize(),
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = ODSR.drawable.video),
                        tint = scheme.basicTextRecessive.getColor(),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        } else {
            ODSBox(
                modifier = Modifier.fillMaxSize(),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = ODSR.drawable.video),
                    tint = scheme.basicTextRecessive.getColor(),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
