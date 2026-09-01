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
import com.telekom.odsystem.tokens.tokens.cheddarSecondaryScheme
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
        if (cameraGranted && !hasInitiatedCall && targetId.isNotBlank()) {
            hasInitiatedCall = true
            viewModel.startOutgoingCall(targetId, modelName, ratePerMin, CallType.VIDEO)
        }
    }

    LaunchedEffect(Unit) {
        if (!hasInitiatedCall && targetId.isNotBlank()) {
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
                    background = listOf(ODSColorModel(hexColor = cheddarSecondaryScheme.basicBackgroundSubtle)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(imageVector = Icons.Outlined.LocalActivity),
                        tint = scheme.basicAccent.getColor()
                    )
                }

                ODSText(
                    text = if (isCurrentUserModel) "Caller Insufficient Balance" else "Insufficient Balance",
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicText
                )

                ODSText(
                    text = if (isCurrentUserModel) {
                        callState.balanceMessage.ifBlank { "The caller does not have sufficient balance for a video call." }
                    } else {
                        callState.balanceMessage.ifBlank {
                            "You need at least ₹${callState.minRequiredBalance.toInt()} coins for a 1-minute video call with $modelName. Current balance: ₹${callState.currentBalance.toInt()}."
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
                            label = "Close",
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
                                label = "Cancel",
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
                                label = "Recharge Now",
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
                    background = listOf(ODSColorModel(hexColor = cheddarSecondaryScheme.basicBackgroundSubtle)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(imageVector = Icons.Filled.CallEnd),
                        tint = scheme.functionalDestructiveStandard.getColor()
                    )
                }

                ODSText(
                    text = "Video Call Ended",
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicText
                )

                ODSText(
                    text = callState.endReason ?: "Duration: %02d:%02d".format(
                        callState.durationSec / 60,
                        callState.durationSec % 60
                    ),
                    style = ODSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive
                )

                if (callState.cost > 0) {
                    ODSText(
                        text = "Total Charged: ₹%.2f".format(callState.cost),
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicAccent
                    )
                }

                Spacer(Modifier.height(16.dp))

                ODSButton(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = "Close",
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

    // ── 4. ACTIVE / DIALING VIDEO SCREEN (ODS + LiveKit Surfaces) ─────────────
    ODSBox(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F0A1C))
    ) {
        // Remote Participant Video Feed
        if (remoteVideoTrack != null) {
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
                            .background(Color(0xCC1A0E38))
                            .padding(horizontal = 24.dp, vertical = 14.dp)
                    ) {
                        ODSColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            gap = 4.dp
                        ) {
                            ODSText(
                                text = "Calling ${modelName.ifBlank { "Creator" }}...",
                                style = ODSTextStyles.bodyMBold,
                                color = HexColor(0xFFFFFFFF)
                            )
                            ODSText(
                                text = "Ringing · LiveKit Room Ready",
                                style = ODSTextStyles.microcopyRegular,
                                color = HexColor(0xFFBC96FF)
                            )
                        }
                    }
                }
            }
        }

        // Local Camera PiP Box
        if (callState.isCameraOn) {
            ODSBox(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(top = 64.dp, end = 16.dp)
                    .width(110.dp)
                    .height(155.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, Color(0xFFBC96FF), RoundedCornerShape(16.dp))
                    .background(Color(0xFF1E1145))
            ) {
                if (localVideoTrack != null) {
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
                background = listOf(ODSColorModel(hexColor = HexColor(0x991E1145))),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.arrow_right),
                    tint = HexColor(0xFFFFFFFF).getColor(),
                    modifier = Modifier.rotate(180f)
                )
            }

            ODSColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                ODSText(
                    text = modelName.ifBlank { "Live Video" },
                    style = ODSTextStyles.bodyMBold,
                    color = HexColor(0xFFFFFFFF)
                )
                ODSText(
                    text = if (callState.status == CallStatus.ACTIVE)
                        "%02d:%02d".format(callState.durationSec / 60, callState.durationSec % 60)
                    else "Ringing...",
                    style = ODSTextStyles.microcopyRegular,
                    color = HexColor(0xFFBC96FF)
                )
            }

            // Real-time ₹ Cost Badge
            ODSBox(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x66000000))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                ODSText(
                    text = "₹%.2f".format(callState.cost),
                    style = ODSTextStyles.microcopyBold,
                    color = HexColor(0xFFD7FF81)
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
                    .background(Color(0x882B1764))
                    .clickable { viewModel.flipCamera() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = "Flip Camera",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            // Toggle Camera On/Off
            ODSBox(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(if (callState.isCameraOn) Color(0x882B1764) else Color(0x88FF4365))
                    .clickable { viewModel.toggleCamera() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (callState.isCameraOn) Icons.Default.Videocam else Icons.Default.VideocamOff,
                    contentDescription = "Camera Toggle",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            // Toggle Mic Mute/Unmute
            ODSBox(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(if (!callState.isMuted) Color(0x882B1764) else Color(0x88FF4365))
                    .clickable { viewModel.toggleMute() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (!callState.isMuted) Icons.Default.Mic else Icons.Default.MicOff,
                    contentDescription = "Mic Toggle",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            // End Call Button
            ODSBox(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF334B))
                    .clickable {
                        viewModel.endCall()
                        viewModel.resetState()
                        onEndCall()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CallEnd,
                    contentDescription = "End Call",
                    tint = Color.White,
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
