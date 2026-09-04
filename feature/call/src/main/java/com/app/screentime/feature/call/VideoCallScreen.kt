package com.app.screentime.feature.call

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.LocalActivity
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.telekom.odsystem.atoms.*
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.*
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R
import com.app.screentime.feature.call.webrtc.WebRtcVideoSurface
import io.livekit.android.renderer.TextureViewRenderer
import io.livekit.android.room.Room
import io.livekit.android.room.track.LocalVideoTrack
import io.livekit.android.room.track.RemoteVideoTrack
import io.livekit.android.room.track.Track
import io.livekit.android.room.track.VideoTrack
import kotlinx.coroutines.delay

@Composable
fun VideoCallScreen(
    modelId: String,
    modelName: String,
    modifier: Modifier = Modifier,
    ratePerMin: Double = 15.0,
    avatarUrl: String = "",
    scheme: ODSTheme = neutralScheme,
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

    var remoteVideoTrack by remember { mutableStateOf<RemoteVideoTrack?>(null) }
    var localVideoTrack by remember { mutableStateOf<LocalVideoTrack?>(null) }

    // Observe LiveKit room tracks for active video streams
    LaunchedEffect(room) {
        while (true) {
            val localPub = room.localParticipant.getTrackPublication(Track.Source.CAMERA)
            localVideoTrack = localPub?.track as? LocalVideoTrack

            val remotePub = room.remoteParticipants.values
                .firstOrNull()
                ?.getTrackPublication(Track.Source.CAMERA)
            remoteVideoTrack = remotePub?.track as? RemoteVideoTrack

            delay(400)
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
        val isAnsweringCall = callState.status == CallStatus.ACTIVE || callState.status == CallStatus.INCOMING
        if (cameraGranted && !hasInitiatedCall && targetId.isNotBlank() && !isAnsweringCall) {
            hasInitiatedCall = true
            viewModel.startOutgoingCall(targetId, modelName, ratePerMin, CallType.VIDEO)
        }
    }

    LaunchedEffect(Unit) {
        val isAnsweringCall = callState.status == CallStatus.ACTIVE || callState.status == CallStatus.INCOMING
        if (!hasInitiatedCall && targetId.isNotBlank() && !isAnsweringCall) {
            if (hasCameraPermission) {
                hasInitiatedCall = true
                viewModel.startOutgoingCall(targetId, modelName, ratePerMin, CallType.VIDEO)
            } else {
                permissionLauncher.launch(permissionsToRequest)
            }
        }
    }

    // ── 1. CHECKING BALANCE SCREEN (ODS) ──────────────────────────────────────
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
                CircularProgressIndicator(
                    modifier = Modifier.size(54.dp),
                    color = scheme.basicAccent.getColor(),
                    strokeWidth = 4.dp
                )

                ODSText(
                    text = "Checking Wallet Balance...",
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicText
                )

                ODSText(
                    text = "Verifying coins for video call with ${modelName.ifBlank { "Creator" }} (₹${ratePerMin.toInt()}/min)",
                    style = ODSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive
                )
            }
        }
        return
    }

    // ── 2. INSUFFICIENT BALANCE SCREEN (ODS) ──────────────────────────────────
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
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape),
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(imageVector = Icons.Outlined.LocalActivity),
                        tint = scheme.basicAccent.getColor()
                    )
                }

                ODSText(
                    text = if (isCurrentUserModel) stringResource(R.string.call_caller_insufficient_balance) else stringResource(R.string.call_insufficient_balance),
                    style = ODSTextStyles.bodyMBold,
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

                if (isCurrentUserModel) {
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
                } else {
                    ODSRow(
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
        }
        return
    }

    // ── 3. CALL ENDED SCREEN (ODS) ────────────────────────────────────────────
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
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(imageVector = Icons.Filled.CallEnd),
                        tint = scheme.functionalDestructiveStandard.getColor()
                    )
                }

                ODSText(
                    text = stringResource(R.string.call_video_ended),
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicText
                )

                ODSText(
                    text = callState.endReason ?: stringResource(
                        R.string.call_duration_format,
                        "%02d:%02d".format(callState.durationSec / 60, callState.durationSec % 60)
                    ),
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

    // ── 4. ACTIVE / DIALING VIDEO SCREEN (ODS + WebRTC / LiveKit Surfaces) ──
    ODSBox(
        modifier = modifier
            .fillMaxSize()
            .background(scheme.basicBackground.getColor())
    ) {
        // Remote Participant Video Feed (P2P WebRTC first, LiveKit as fallback)
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
        } else {
            // Placeholder avatar when remote video is not yet streaming
            ODSBox(modifier = Modifier.fillMaxSize()) {
                ODSImage(
                    imageModel = ODSImageModel(
                        url = if (avatarUrl.isNotBlank()) avatarUrl
                        else "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=1200&q=85",
                        contentDescription = modelName
                    ),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dialing Ringing Overlay Badge
                if (callState.status == CallStatus.DIALING) {
                    ODSBox(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clip(RoundedCornerShape(20.dp))
                            .background(scheme.basicBackgroundCardSubtle.getColor())
                            .padding(horizontal = 24.dp, vertical = 14.dp)
                    ) {
                        ODSColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            gap = 4.dp
                        ) {
                            ODSText(
                                text = stringResource(R.string.call_calling_target, modelName.ifBlank { "Creator" }),
                                style = ODSTextStyles.bodyMBold,
                                color = scheme.basicText
                            )
                            ODSText(
                                text = stringResource(R.string.call_ringing_p2p),
                                style = ODSTextStyles.microcopyRegular,
                                color = scheme.basicAccentSecondary
                            )
                        }
                    }
                }
            }
        }

        // Local Camera PiP Box (P2P WebRTC first, LiveKit as fallback)
        if (callState.isCameraOn) {
            ODSBox(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(top = 64.dp, end = 16.dp)
                    .width(110.dp)
                    .height(155.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, scheme.basicAccentSecondary.getColor(), RoundedCornerShape(16.dp))
                    .background(scheme.basicBackgroundCard.getColor())
            ) {
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
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // Top Header
        ODSRow(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ODSBox(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable {
                        viewModel.endCall()
                        viewModel.resetState()
                        onEndCall()
                    },
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCardSubtle)),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.arrow_right),
                    tint = scheme.basicText.getColor(),
                    modifier = Modifier.rotate(180f)
                )
            }

            ODSColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                ODSText(
                    text = modelName.ifBlank { stringResource(R.string.call_live_video) },
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicText
                )
                ODSText(
                    text = if (callState.status == CallStatus.ACTIVE) {
                        val durStr = "%02d:%02d".format(callState.durationSec / 60, callState.durationSec % 60)
                        if (callState.isP2PConnected) stringResource(R.string.call_status_p2p_active, durStr)
                        else if (callState.isUsingLiveKitFallback) stringResource(R.string.call_status_livekit_active, durStr)
                        else durStr
                    } else stringResource(R.string.call_ringing),
                    style = ODSTextStyles.microcopyRegular,
                    color = if (callState.isP2PConnected) scheme.basicAccent else scheme.basicAccentSecondary
                )
            }

            // Real-time ₹ Cost Badge
            ODSBox(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(scheme.basicBackgroundCardSubtle.getColor())
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                ODSText(
                    text = "₹%.2f".format(callState.cost),
                    style = ODSTextStyles.microcopyBold,
                    color = scheme.basicAccent
                )
            }
        }

        // Bottom Controls
        ODSRow(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 28.dp, start = 24.dp, end = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Flip Camera
            ODSBox(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(scheme.basicBackgroundCardSubtle.getColor())
                    .clickable { viewModel.flipCamera() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = stringResource(R.string.call_action_flip_camera),
                    tint = scheme.basicText.getColor(),
                    modifier = Modifier.size(26.dp)
                )
            }

            // Toggle Camera On/Off
            ODSBox(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(if (callState.isCameraOn) scheme.basicBackgroundCardSubtle.getColor() else scheme.functionalDestructiveStandard.getColor())
                    .clickable { viewModel.toggleCamera() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (callState.isCameraOn) Icons.Default.Videocam else Icons.Default.VideocamOff,
                    contentDescription = stringResource(R.string.call_action_toggle_camera),
                    tint = scheme.basicText.getColor(),
                    modifier = Modifier.size(26.dp)
                )
            }

            // Toggle Mic Mute/Unmute
            ODSBox(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(if (!callState.isMuted) scheme.basicBackgroundCardSubtle.getColor() else scheme.functionalDestructiveStandard.getColor())
                    .clickable { viewModel.toggleMute() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (!callState.isMuted) Icons.Default.Mic else Icons.Default.MicOff,
                    contentDescription = stringResource(R.string.call_action_toggle_mic),
                    tint = scheme.basicText.getColor(),
                    modifier = Modifier.size(26.dp)
                )
            }

            // End Call Button
            ODSBox(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(scheme.functionalDestructiveStandard.getColor())
                    .clickable {
                        viewModel.endCall()
                        viewModel.resetState()
                        onEndCall()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CallEnd,
                    contentDescription = stringResource(R.string.call_action_end),
                    tint = scheme.basicText.getColor(),
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@Composable
fun LiveKitVideoSurface(
    room: Room,
    videoTrack: VideoTrack?,
    modifier: Modifier = Modifier
) {
    if (videoTrack == null) return
    AndroidView(
        factory = { ctx ->
            TextureViewRenderer(ctx).apply {
                room.initVideoRenderer(this)
                videoTrack.addRenderer(this)
            }
        },
        update = { renderer ->
            videoTrack.addRenderer(renderer)
        },
        onRelease = { renderer ->
            videoTrack.removeRenderer(renderer)
            renderer.release()
        },
        modifier = modifier
    )
}
