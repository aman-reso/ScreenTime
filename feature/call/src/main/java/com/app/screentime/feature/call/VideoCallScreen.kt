package com.app.screentime.feature.call

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import com.telekom.odsystem.R as ODSR
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.config.R
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.feature.call.webrtc.WebRtcVideoSurface
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme
import io.livekit.android.compose.ui.RendererType
import io.livekit.android.compose.ui.ScaleType
import io.livekit.android.compose.ui.VideoTrackView
import io.livekit.android.events.RoomEvent
import io.livekit.android.renderer.TextureViewRenderer
import io.livekit.android.room.Room
import io.livekit.android.room.track.LocalVideoTrack
import io.livekit.android.room.track.RemoteTrackPublication
import io.livekit.android.room.track.RemoteVideoTrack
import io.livekit.android.room.track.Track
import io.livekit.android.room.track.VideoTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

/**
 * 1-on-1 Video Call Screen.
 * Uses Telekom ODS components and ODSTheme scheme consistently.
 */
@Composable
fun VideoCallScreen(
    modelId: String,
    modelName: String,
    modifier: Modifier = Modifier,
    ratePerMin: Double = 15.0,
    avatarUrl: String = "",
    scheme: ODSTheme = zonaODSTheme,
    onEndCall: () -> Unit = {},
    onNavigateToTopUp: () -> Unit = {},
    viewModel: CallViewModel = hiltViewModel()
) {
    val callState by viewModel.callState.collectAsState()
    val context = LocalContext.current
    val room = viewModel.room
    val isCurrentUserModel = viewModel.isCurrentUserModel()

    val localRtcVideoTrack by viewModel.localWebRtcVideoTrack.collectAsState()
    val remoteRtcVideoTrack by viewModel.remoteWebRtcVideoTrack.collectAsState()
    val eglBase = viewModel.webRtcEglBase

    var remoteVideoTrack by remember { mutableStateOf<VideoTrack?>(null) }
    var localVideoTrack by remember { mutableStateOf<VideoTrack?>(null) }

    fun findRemoteVideoTrack(r: Room): VideoTrack? {
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

    fun findLocalVideoTrack(r: Room): VideoTrack? {
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

    // Observe LiveKit room tracks for active video streams both reactively via events and periodic scan
    LaunchedEffect(room) {
        launch {
            try {
                room.events.events.collect { event ->
                    when (event) {
                        is RoomEvent.TrackSubscribed -> {
                            val vt = event.track as? VideoTrack
                            if (vt != null) {
                                Log.i("VideoCallScreen", "🎥 TrackSubscribed: $vt")
                                remoteVideoTrack = vt
                            }
                        }
                        is RoomEvent.TrackUnsubscribed -> {
                            if (event.track == remoteVideoTrack) {
                                Log.i("VideoCallScreen", "🎥 TrackUnsubscribed: ${event.track}")
                                remoteVideoTrack = findRemoteVideoTrack(room)
                            }
                        }
                        is RoomEvent.TrackPublished -> {
                            (event.publication as? RemoteTrackPublication)?.setSubscribed(true)
                            val r = findRemoteVideoTrack(room)
                            if (r != null) remoteVideoTrack = r
                        }
                        is RoomEvent.ParticipantConnected, is RoomEvent.Connected -> {
                            val r = findRemoteVideoTrack(room)
                            if (r != null) remoteVideoTrack = r
                            val l = findLocalVideoTrack(room)
                            if (l != null) localVideoTrack = l
                        }
                        else -> {
                            val r = findRemoteVideoTrack(room)
                            if (r != null && r != remoteVideoTrack) {
                                remoteVideoTrack = r
                            }
                            val l = findLocalVideoTrack(room)
                            if (l != null && l != localVideoTrack) {
                                localVideoTrack = l
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.w("VideoCallScreen", "Room events collection ended: ${e.message}")
            }
        }

        while (true) {
            val r = findRemoteVideoTrack(room)
            if (r != remoteVideoTrack) {
                remoteVideoTrack = r
            }

            val l = findLocalVideoTrack(room)
            if (l != localVideoTrack) {
                localVideoTrack = l
            }

            if (callState.isCameraOn && l == null && room.state == Room.State.CONNECTED) {
                try {
                    room.localParticipant.setCameraEnabled(true)
                } catch (_: Exception) {}
            }

            delay(250.milliseconds)
        }
    }

    val permissionsToRequest = remember {
        val list = mutableListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            list.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            list.add(Manifest.permission.BLUETOOTH_CONNECT)
        }
        list.toTypedArray()
    }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        )
    }
    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }
    var hasInitiatedCall by rememberSaveable { mutableStateOf(false) }

    val targetId = when {
        modelId.isNotBlank() -> modelId
        callState.remoteUserId.isNotBlank() -> callState.remoteUserId
        else -> ""
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val cameraGranted = (result[Manifest.permission.CAMERA] == true ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                (result[Manifest.permission.RECORD_AUDIO] == true ||
                        ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
        hasCameraPermission = cameraGranted
        if (cameraGranted) {
            viewModel.setCameraEnabled(true)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            hasNotificationPermission = result[Manifest.permission.POST_NOTIFICATIONS] == true ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        }
        val isAnsweringCall = callState.status == CallStatus.ACTIVE || callState.status == CallStatus.INCOMING
        if (cameraGranted && callState.status == CallStatus.INCOMING) {
            viewModel.acceptIncomingCall()
        } else if (cameraGranted && !hasInitiatedCall && targetId.isNotBlank() && !isAnsweringCall) {
            hasInitiatedCall = true
            viewModel.startOutgoingCall(targetId, modelName, ratePerMin, CallType.VIDEO)
        }
    }

    LaunchedEffect(Unit) {
        if (callState.status == CallStatus.INCOMING) {
            if (hasCameraPermission) {
                viewModel.setCameraEnabled(true)
                viewModel.acceptIncomingCall()
            } else {
                permissionLauncher.launch(permissionsToRequest)
            }
        } else if (callState.status == CallStatus.ACTIVE) {
            if (hasCameraPermission) {
                viewModel.setCameraEnabled(true)
            } else {
                permissionLauncher.launch(permissionsToRequest)
            }
        } else if (!hasInitiatedCall && targetId.isNotBlank()) {
            if (hasCameraPermission && hasNotificationPermission) {
                hasInitiatedCall = true
                viewModel.startOutgoingCall(targetId, modelName, ratePerMin, CallType.VIDEO)
            } else {
                permissionLauncher.launch(permissionsToRequest)
            }
        }
    }

    // ── 1. CHECKING BALANCE SCREEN ──
    if (callState.status == CallStatus.CHECKING_BALANCE) {
        ODSBox(
            modifier = modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackground)),
            contentAlignment = Alignment.Center
        ) {
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                gap = 20.dp
            ) {
                ODSLoadingSpinner(
                    scheme = scheme,
                    props = ODSLoadingSpinnerProps(size = ODSLoadingSpinnerSize.LARGE)
                )

                ODSText(
                    text = "Checking Wallet Balance...",
                    style = ODSTextStyles.bodyL,
                    color = scheme.basicText
                )

                ODSText(
                    text = "Verifying coins for video call with ${modelName.ifBlank { "User" }} ($${ratePerMin.toInt()}/min)",
                    style = ODSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive
                )
            }
        }
        return
    }

    // ── 2. INSUFFICIENT BALANCE SCREEN ──
    if (callState.status == CallStatus.INSUFFICIENT_BALANCE) {
        ODSBox(
            modifier = modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackground)),
            contentAlignment = Alignment.Center
        ) {
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                gap = 20.dp
            ) {
                ODSBox(
                    modifier = Modifier.size(88.dp),
                    cornerRadius = ODSCorners(all = 44.dp),
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_zap),
                        tint = scheme.basicAccent.getColor(),
                        modifier = Modifier.size(40.dp)
                    )
                }

                ODSText(
                    text = if (isCurrentUserModel) stringResource(R.string.call_caller_insufficient_balance) else stringResource(R.string.call_insufficient_balance),
                    style = ODSTextStyles.bodyL,
                    color = scheme.basicText
                )

                ODSText(
                    text = if (isCurrentUserModel) {
                        callState.balanceMessage.ifBlank { stringResource(R.string.call_insufficient_balance_caller_msg) }
                    } else {
                        callState.balanceMessage.ifBlank {
                            stringResource(
                                R.string.call_insufficient_balance_msg,
                                callState.minRequiredBalance.toInt(),
                                modelName,
                                callState.currentBalance.toInt()
                            )
                        }
                    },
                    style = ODSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive
                )

                Spacer(Modifier.height(16.dp))

                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSButton(
                        modifier = Modifier.weight(1f),
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = stringResource(R.string.call_action_cancel),
                            variant = ODSButtonVariant.SECONDARY
                        ),
                        onClick = {
                            viewModel.resetState()
                            onEndCall()
                        }
                    )

                    ODSButton(
                        modifier = Modifier.weight(1f),
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = stringResource(R.string.call_action_recharge),
                            variant = ODSButtonVariant.PRIMARY
                        ),
                        onClick = {
                            viewModel.resetState()
                            onNavigateToTopUp()
                        }
                    )
                }
            }
        }
        return
    }

    // ── 3. CALL ENDED SCREEN ──
    if (callState.status == CallStatus.ENDED && hasInitiatedCall) {
        ODSBox(
            modifier = modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackground)),
            contentAlignment = Alignment.Center
        ) {
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                gap = 20.dp
            ) {
                ODSBox(
                    modifier = Modifier.size(80.dp),
                    cornerRadius = ODSCorners(all = 40.dp),
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_phone_off),
                        tint = HexColor("#FF4D4F", 1.0f).getColor(),
                        modifier = Modifier.size(36.dp)
                    )
                }

                ODSText(
                    text = stringResource(R.string.call_video_ended),
                    style = ODSTextStyles.bodyL,
                    color = scheme.basicText
                )

                val durStr = "%02d:%02d".format(callState.durationSec / 60, callState.durationSec % 60)
                ODSText(
                    text = callState.endReason ?: stringResource(R.string.call_duration_format, durStr),
                    style = ODSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive
                )

                if (callState.cost > 0) {
                    ODSText(
                        text = stringResource(R.string.call_total_charged, callState.cost),
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicAccent
                    )
                }

                Spacer(Modifier.height(16.dp))

                ODSButton(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = stringResource(R.string.call_action_close),
                        variant = ODSButtonVariant.PRIMARY
                    ),
                    onClick = {
                        viewModel.resetState()
                        onEndCall()
                    }
                )
            }
        }
        return
    }

    val minutes = callState.durationSec / 60
    val seconds = callState.durationSec % 60
    val formattedTime = "%02d:%02d".format(minutes, seconds)

    // ── 4. ACTIVE VIDEO CALL SCREEN ──
    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        val isRemoteVideoActive = remoteVideoTrack != null || (remoteRtcVideoTrack != null && eglBase != null)

        // ── Main Video Feed ──
        // When remote is connected, show remote video.
        // While dialing / before answer, show local camera preview full-screen (WhatsApp / FaceTime behavior)!
        if (isRemoteVideoActive) {
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
            // Local Camera Preview on Full Screen
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
                // Fallback while camera starts: remote avatar or placeholder
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

        // ── Dialing / Ringing Center Overlay (when waiting for remote user to answer) ──
        if (!isRemoteVideoActive) {
            ODSColumn(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                gap = 14.dp
            ) {
                if (avatarUrl.isNotBlank()) {
                    ODSBox(
                        modifier = Modifier.size(96.dp),
                        cornerRadius = ODSCorners(all = 48.dp),
                        border = ODSBorder(
                            width = 2.5.dp,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicAccent))
                        ),
                        clipContent = true
                    ) {
                        ODSImage(
                            imageModel = ODSImageModel(url = avatarUrl, contentDescription = modelName),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                ODSText(
                    text = modelName.ifBlank { "User" },
                    style = ODSTextStyles.bodyL,
                    color = scheme.basicText
                )
                ODSRow(
                    cornerRadius = ODSCorners(all = 16.dp),
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                    border = ODSBorder(
                        width = 1.dp,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                    ),
                    padding = ODSPadding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    gap = 8.dp
                ) {
                    ODSBox(
                        modifier = Modifier.size(8.dp),
                        cornerRadius = ODSCorners(all = 4.dp),
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccent))
                    )
                    ODSText(
                        text = if (callState.status == CallStatus.DIALING) "Ringing..." else "Calling...",
                        style = ODSTextStyles.bodySBold,
                        color = scheme.basicText
                    )
                }
            }
        }

        // ── Top Bar Overlay: Status Pill (Left) & Camera Switch (Right) ──
        ODSRow(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Top-Left Status Pill
            ODSRow(
                cornerRadius = ODSCorners(all = 20.dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                border = ODSBorder(
                    width = 1.dp,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                ),
                padding = ODSPadding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                gap = 8.dp
            ) {
                // Active indicator dot
                ODSBox(
                    modifier = Modifier.size(7.dp),
                    cornerRadius = ODSCorners(all = 4.dp),
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccent))
                )

                val pillText = when {
                    callState.status == CallStatus.DIALING -> "${modelName.ifBlank { "User" }} • Ringing..."
                    isRemoteVideoActive -> "${modelName.ifBlank { "User" }} • $formattedTime"
                    else -> "${modelName.ifBlank { "User" }} • $formattedTime"
                }

                ODSText(
                    text = pillText,
                    style = ODSTextStyles.bodySBold,
                    color = scheme.basicText
                )
            }

            // Top-Right Circular Camera Flip Button
            ODSBox(
                modifier = Modifier
                    .size(42.dp)
                    .clickable { viewModel.flipCamera() },
                cornerRadius = ODSCorners(all = 21.dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                border = ODSBorder(
                    width = 1.dp,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                ),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_refresh_cw),
                    tint = scheme.basicText.getColor(),
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // ── Floating Top-Right PiP Self-View Video Card (Visible only when Remote Video is active) ──
        if (isRemoteVideoActive) {
            ODSBox(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(top = 68.dp, end = 16.dp)
                    .width(108.dp)
                    .height(148.dp)
                    .clickable { viewModel.flipCamera() },
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

        // ── Bottom Translucent Controls Dock ──
        ODSBox(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding(),
            cornerRadius = ODSCorners(topLeft = 32.dp, topRight = 32.dp),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            border = ODSBorder(
                width = 1.dp,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
            ),
            padding = ODSPadding(top = 22.dp, bottom = 24.dp, left = 24.dp, right = 24.dp)
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                gap = 16.dp
            ) {
                // Calling & Connecting status text without outline, positioned right above action buttons
                if (callState.status == CallStatus.DIALING) {
                    ODSColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        gap = 4.dp
                    ) {
                        ODSText(
                            text = "Calling ${modelName.ifBlank { "User" }}...",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )
                        ODSText(
                            text = "Connecting secure video stream",
                            style = ODSTextStyles.microcopyRegular,
                            color = scheme.basicAccent
                        )
                    }
                }

                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. Mute Audio Button
                    ODSBox(
                        modifier = Modifier
                            .size(56.dp)
                            .clickable { viewModel.toggleMute() },
                        cornerRadius = ODSCorners(all = 28.dp),
                        background = listOf(
                            ODSColorModel(
                                hexColor = if (callState.isMuted) scheme.basicAccent else scheme.basicBackgroundSubtle
                            )
                        ),
                        border = ODSBorder(
                            width = 1.dp,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = if (callState.isMuted) ODSR.drawable.ic_mic_off else ODSR.drawable.ic_mic
                            ),
                            tint = if (callState.isMuted) scheme.basicTextOnAccent.getColor() else scheme.basicText.getColor(),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // 2. Camera Toggle (On/Off) Button
                    ODSBox(
                        modifier = Modifier
                            .size(56.dp)
                            .clickable { viewModel.toggleCamera() },
                        cornerRadius = ODSCorners(all = 28.dp),
                        background = listOf(
                            ODSColorModel(
                                hexColor = if (callState.isCameraOn) scheme.basicAccent else scheme.basicBackgroundSubtle
                            )
                        ),
                        border = ODSBorder(
                            width = 1.dp,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(drawableRes = ODSR.drawable.video),
                            tint = if (callState.isCameraOn) scheme.basicTextOnAccent.getColor() else scheme.basicText.getColor(),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // 3. Camera Flip / Rotate Button
                    ODSBox(
                        modifier = Modifier
                            .size(56.dp)
                            .clickable { viewModel.flipCamera() },
                        cornerRadius = ODSCorners(all = 28.dp),
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                        border = ODSBorder(
                            width = 1.dp,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_refresh_cw),
                            tint = scheme.basicText.getColor(),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // 4. Centered End Call Button (Red/Vibrant)
                    ODSBox(
                        modifier = Modifier
                            .size(66.dp)
                            .clickable {
                                viewModel.endCall()
                                viewModel.resetState()
                                onEndCall()
                            },
                        cornerRadius = ODSCorners(all = 33.dp),
                        background = listOf(ODSColorModel(hexColor = HexColor("#FF4D4F", 1.0f))),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_phone_off),
                            tint = HexColor("#FFFFFF", 1.0f).getColor(),
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                // Bottom Indicator Pill
                ODSBox(
                    modifier = Modifier
                        .width(134.dp)
                        .height(4.dp),
                    cornerRadius = ODSCorners(all = 2.dp),
                    background = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                )
            }
        }
    }
}

/**
 * LiveKit hardware-accelerated video rendering view using official LiveKit Compose VideoTrackView.
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
