package com.app.screentime.feature.call.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.livekit.android.compose.ui.RendererType
import io.livekit.android.compose.ui.ScaleType
import io.livekit.android.compose.ui.VideoTrackView
import io.livekit.android.room.Room
import io.livekit.android.room.track.VideoTrack

/**
 * LiveKitVideoSurface
 *
 * Hardware-accelerated video rendering view wrapping official LiveKit Compose VideoTrackView.
 * Used for rendering both remote peer streams and local camera preview feeds.
 *
 * @param room Active LiveKit Room instance
 * @param videoTrack The VideoTrack to render (LocalVideoTrack or RemoteVideoTrack)
 * @param isMirror Whether to mirror the video horizontally (typically true for front camera)
 */
@Composable
fun LiveKitVideoSurface(
    room: Room,
    videoTrack: VideoTrack?,
    modifier: Modifier = Modifier,
    isMirror: Boolean = false
) {
    if (videoTrack == null) return
    VideoTrackView(
        videoTrack = videoTrack,
        modifier = modifier,
        passedRoom = room,
        mirror = isMirror,
        scaleType = ScaleType.Fill,
        rendererType = RendererType.Texture
    )
}
