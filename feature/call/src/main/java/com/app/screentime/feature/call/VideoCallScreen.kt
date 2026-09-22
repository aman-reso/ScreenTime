package com.app.screentime.feature.call

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
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
import com.app.screentime.core.ui.theme.ZonaColors
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
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme
import io.livekit.android.renderer.TextureViewRenderer
import io.livekit.android.room.Room
import io.livekit.android.room.track.LocalVideoTrack
import io.livekit.android.room.track.RemoteVideoTrack
import io.livekit.android.room.track.Track
import io.livekit.android.room.track.VideoTrack
import kotlinx.coroutines.delay

/**
 * 1-on-1 Video Call Screen (Matching media_1789898718933.png new-video-call).
 * 100% constructed using Telekom ODS components and Zona design tokens.
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

    // ── 1. CHECKING BALANCE SCREEN ──
    if (callState.status == CallStatus.CHECKING_BALANCE) {
        ODSBox(
            modifier = modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            background = listOf(ODSColorModel(hexColor = ZonaColors.Background)),
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
                    color = ZonaColors.TextPrimary
                )

                ODSText(
                    text = "Verifying coins for video call with ${modelName.ifBlank { "Jessica Maple" }} ($${ratePerMin.toInt()}/min)",
                    style = ODSTextStyles.bodyMRegular,
                    color = ZonaColors.LavenderAlt
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
            background = listOf(ODSColorModel(hexColor = ZonaColors.Background)),
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
                    background = listOf(ODSColorModel(hexColor = ZonaColors.SurfaceRaised)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_zap),
                        tint = ZonaColors.ActiveLime.getColor(),
                        modifier = Modifier.size(40.dp)
                    )
                }

                ODSText(
                    text = if (isCurrentUserModel) stringResource(R.string.call_caller_insufficient_balance) else stringResource(R.string.call_insufficient_balance),
                    style = ODSTextStyles.bodyL,
                    color = ZonaColors.TextPrimary
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
                    color = ZonaColors.LavenderAlt
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
            background = listOf(ODSColorModel(hexColor = ZonaColors.Background)),
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
                    background = listOf(ODSColorModel(hexColor = ZonaColors.SurfaceRaised)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_phone_off),
                        tint = ZonaColors.ActionPrimary.getColor(),
                        modifier = Modifier.size(36.dp)
                    )
                }

                ODSText(
                    text = stringResource(R.string.call_video_ended),
                    style = ODSTextStyles.bodyL,
                    color = ZonaColors.TextPrimary
                )

                val durStr = "%02d:%02d".format(callState.durationSec / 60, callState.durationSec % 60)
                ODSText(
                    text = callState.endReason ?: stringResource(R.string.call_duration_format, durStr),
                    style = ODSTextStyles.bodyMRegular,
                    color = ZonaColors.LavenderAlt
                )

                if (callState.cost > 0) {
                    ODSText(
                        text = stringResource(R.string.call_total_charged, callState.cost),
                        style = ODSTextStyles.bodyMBold,
                        color = ZonaColors.ActiveLime
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

    // ── 4. ACTIVE VIDEO CALL SCREEN (new-video-call) ──
    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = ZonaColors.Background))
    ) {
        // ── Full-Screen Remote Participant Video Feed ──
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
            // Fullscreen portrait image fallback (Jessica Maple)
            ODSImage(
                imageModel = ODSImageModel(
                    url = avatarUrl.ifBlank {
                        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=1200&q=85"
                    },
                    contentDescription = modelName
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Dialing / Ringing Scrim Overlay
            if (callState.status == CallStatus.DIALING) {
                ODSBox(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 24.dp, vertical = 14.dp),
                    cornerRadius = ODSCorners(all = 20.dp),
                    background = listOf(ODSColorModel(hexColor = ZonaColors.OverlayLegacy))
                ) {
                    ODSColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        gap = 6.dp
                    ) {
                        ODSText(
                            text = "Calling ${modelName.ifBlank { "Jessica Maple" }}...",
                            style = ODSTextStyles.bodyMBold,
                            color = ZonaColors.TextPrimary
                        )
                        ODSText(
                            text = "Connecting secure video stream",
                            style = ODSTextStyles.microcopyRegular,
                            color = ZonaColors.ActiveLime
                        )
                    }
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
            // Top-Left Status Pill: "● Jessica • 02:40"
            ODSRow(
                cornerRadius = ODSCorners(all = 20.dp),
                background = listOf(ODSColorModel(hexColor = ZonaColors.MediaOverlay)),
                padding = ODSPadding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                gap = 8.dp
            ) {
                // Neon Lime active indicator dot
                ODSBox(
                    modifier = Modifier.size(7.dp),
                    cornerRadius = ODSCorners(all = 4.dp),
                    background = listOf(ODSColorModel(hexColor = ZonaColors.ActiveLime))
                )

                ODSText(
                    text = "${modelName.ifBlank { "Jessica" }} • $formattedTime",
                    style = ODSTextStyles.bodySBold,
                    color = ZonaColors.TextPrimary
                )
            }

            // Top-Right Circular Camera Flip Button
            ODSBox(
                modifier = Modifier
                    .size(42.dp)
                    .clickable { viewModel.flipCamera() },
                cornerRadius = ODSCorners(all = 21.dp),
                background = listOf(ODSColorModel(hexColor = ZonaColors.MediaOverlay)),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_refresh_cw),
                    tint = ZonaColors.TextPrimary.getColor(),
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // ── Floating Top-Right PiP Self-View Video Card with Glowing Neon Lime Border ──
        ODSBox(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 68.dp, end = 16.dp)
                .width(108.dp)
                .height(148.dp),
            cornerRadius = ODSCorners(all = 16.dp),
            border = ODSBorder(
                width = 3.dp,
                colorList = listOf(ODSColorModel(hexColor = ZonaColors.ActiveLime))
            ),
            background = listOf(ODSColorModel(hexColor = ZonaColors.Background)),
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
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    ODSImage(
                        imageModel = ODSImageModel(
                            url = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?auto=format&fit=crop&w=400&q=80",
                            contentDescription = "Self Preview"
                        ),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                ODSBox(
                    modifier = Modifier.fillMaxSize(),
                    background = listOf(ODSColorModel(hexColor = ZonaColors.SurfaceRaised)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_video),
                        tint = ZonaColors.LavenderAlt.getColor(),
                        modifier = Modifier.size(32.dp)
                    )
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
            background = listOf(ODSColorModel(hexColor = ZonaColors.OverlayLegacy)),
            padding = ODSPadding(top = 22.dp, bottom = 24.dp, left = 32.dp, right = 32.dp)
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                gap = 20.dp
            ) {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. Mute Audio Button (Dark circle with outline)
                    ODSBox(
                        modifier = Modifier
                            .size(62.dp)
                            .clickable { viewModel.toggleMute() },
                        cornerRadius = ODSCorners(all = 31.dp),
                        background = listOf(
                            ODSColorModel(
                                hexColor = if (callState.isMuted) ZonaColors.SurfaceRaised else ZonaColors.MediaOverlay
                            )
                        ),
                        border = ODSBorder(
                            width = 1.5.dp,
                            colorList = listOf(ODSColorModel(hexColor = ZonaColors.Border))
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = if (callState.isMuted) ODSR.drawable.ic_mic_off else ODSR.drawable.ic_mic
                            ),
                            tint = ZonaColors.TextPrimary.getColor(),
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    // 2. Centered End Call Button (Large Vibrant Coral)
                    ODSBox(
                        modifier = Modifier
                            .size(76.dp)
                            .clickable {
                                viewModel.endCall()
                                viewModel.resetState()
                                onEndCall()
                            },
                        cornerRadius = ODSCorners(all = 38.dp),
                        background = listOf(ODSColorModel(hexColor = ZonaColors.ActionPrimary)),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_phone_off),
                            tint = ZonaColors.TextPrimary.getColor(),
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    // 3. Camera Toggle Button (Bright Neon Lime when Active!)
                    ODSBox(
                        modifier = Modifier
                            .size(62.dp)
                            .clickable { viewModel.toggleCamera() },
                        cornerRadius = ODSCorners(all = 31.dp),
                        background = listOf(
                            ODSColorModel(
                                hexColor = if (callState.isCameraOn) ZonaColors.ActiveLime else ZonaColors.SurfaceRaised
                            )
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_video),
                            tint = if (callState.isCameraOn) ZonaColors.TextInverse.getColor() else ZonaColors.TextPrimary.getColor(),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                // Bottom Home Bar Indicator
                ODSBox(
                    modifier = Modifier
                        .width(134.dp)
                        .height(4.dp),
                    cornerRadius = ODSCorners(all = 2.dp),
                    background = listOf(ODSColorModel(hexColor = ZonaColors.Border))
                )
            }
        }
    }
}

/**
 * LiveKit hardware-accelerated video rendering view wrapper.
 */
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
        update = { _ -> },
        onRelease = { renderer ->
            videoTrack.removeRenderer(renderer)
            renderer.release()
        },
        modifier = modifier
    )
}
