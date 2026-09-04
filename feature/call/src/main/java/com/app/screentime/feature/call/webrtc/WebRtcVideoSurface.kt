package com.app.screentime.feature.call.webrtc

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import livekit.org.webrtc.EglBase
import livekit.org.webrtc.RendererCommon
import livekit.org.webrtc.SurfaceViewRenderer
import livekit.org.webrtc.VideoTrack

@Composable
fun WebRtcVideoSurface(
    videoTrack: VideoTrack?,
    eglBase: EglBase,
    modifier: Modifier = Modifier,
    isMirror: Boolean = false
) {
    if (videoTrack == null) return

    AndroidView(
        factory = { ctx ->
            SurfaceViewRenderer(ctx).apply {
                init(eglBase.eglBaseContext, null)
                setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL)
                setMirror(isMirror)
                setEnableHardwareScaler(true)
                videoTrack.addSink(this)
            }
        },
        update = { renderer ->
            renderer.setMirror(isMirror)
            videoTrack.addSink(renderer)
        },
        onRelease = { renderer ->
            videoTrack.removeSink(renderer)
            renderer.release()
        },
        modifier = modifier
    )
}
