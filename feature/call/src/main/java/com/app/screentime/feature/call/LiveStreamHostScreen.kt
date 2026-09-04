package com.app.screentime.feature.call

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.ui.graphics.Brush
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CancellationException
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.ExperimentalMaterial3Api
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.switch.ODSSwitch
import com.telekom.odsystem.atoms.switch.ODSSwitchProps
import com.telekom.odsystem.atoms.switch.ODSSwitchSize
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheetProps
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeader
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeaderProps
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeaderSize
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R
import io.livekit.android.LiveKit
import io.livekit.android.room.track.LocalVideoTrack
import io.livekit.android.room.track.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.cheddarSecondaryScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveStreamHostScreen(
    streamTitle: String,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    viewModel: LiveViewModel = hiltViewModel(),
    onEndStream: () -> Unit
) {
    val api = viewModel.api
    val sessionManager = viewModel.sessionManager
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val room = remember { LiveKit.create(context) }

    var localVideoTrack by remember { mutableStateOf<LocalVideoTrack?>(null) }
    var isLive by remember { mutableStateOf(false) }
    var viewerCount by remember { mutableIntStateOf(0) }
    var totalEarned by remember { mutableDoubleStateOf(0.0) }
    var isMuted by remember { mutableStateOf(false) }
    var isCameraFront by remember { mutableStateOf(true) }
    var showEndDialog by remember { mutableStateOf(false) }
    var streamId by remember { mutableStateOf("") }
    var retryTrigger by remember { mutableIntStateOf(0) }

    // Token Deduction / Paid Mode States
    var isPaidMode by remember { mutableStateOf(false) }
    var coinRatePerMin by remember { mutableDoubleStateOf(10.0) }
    var showPaidConfigDialog by remember { mutableStateOf(false) }
    var statusToast by remember { mutableStateOf<String?>(null) }

    // Loading & Error States
    var isConnecting by remember { mutableStateOf(true) }
    var connectionStatusText by remember { mutableStateOf("Preparing broadcast studio...") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var hasPermissions by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        )
    }

    val chatMessages = remember {
        mutableStateListOf<LiveChatMessage>()
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val cam = perms[Manifest.permission.CAMERA] == true
        val mic = perms[Manifest.permission.RECORD_AUDIO] == true
        if (cam && mic) {
            hasPermissions = true
            errorMessage = null
            retryTrigger++
        } else {
            hasPermissions = false
            isConnecting = false
            errorMessage = "Camera and Microphone permissions are required to start your broadcast."
        }
    }

    LaunchedEffect(hasPermissions, retryTrigger) {
        if (!hasPermissions) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
                )
            )
            return@LaunchedEffect
        }

        isConnecting = true
        errorMessage = null

        try {
            connectionStatusText = "Registering broadcast on server..."
            val token = sessionManager.token ?: ""
            val startResp = try {
                api.startLiveStream(token, streamTitle.ifBlank { "My Live Show" })
            } catch (e: Exception) {
                null
            }

            streamId = startResp?.stream?.stream_id
                ?: "live_${sessionManager.userId}_${System.currentTimeMillis()}"
            val livekitUrl = startResp?.livekit_url ?: "wss://connecto-7sxi06vp.livekit.cloud"
            val roomToken = startResp?.token?.ifBlank { null } ?: generateDirectLiveKitHostToken(
                apiKey = "APImr59LGqwEVuj",
                apiSecret = "cvdsoq3pKQusl4HfAHPxSeGXvHcM5atVOWQ2WozyxF2",
                identity = sessionManager.userId ?: "host_${System.currentTimeMillis()}",
                roomName = streamId
            )

            connectionStatusText = "Connecting to LiveKit room..."
            room.connect(livekitUrl, roomToken)

            connectionStatusText = "Starting camera & audio..."
            try {
                room.localParticipant.setMicrophoneEnabled(true)
            } catch (_: Exception) {}

            try {
                room.localParticipant.setCameraEnabled(true)
            } catch (e: Exception) {
                errorMessage = "Failed to enable camera: ${e.localizedMessage}"
            }

            isLive = true

            // Observe local video track
            while (true) {
                val pub = room.localParticipant.getTrackPublication(Track.Source.CAMERA)
                localVideoTrack = pub?.track as? LocalVideoTrack

                if (localVideoTrack != null) {
                    isConnecting = false
                    errorMessage = null
                }

                // Real-time viewer count based on actual connected viewers
                viewerCount = room.remoteParticipants.size
                delay(1000)
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            isConnecting = false
            errorMessage = e.localizedMessage ?: "Failed to connect to broadcast server"
        }
    }

    val currentStreamId by rememberUpdatedState(streamId)
    DisposableEffect(Unit) {
        onDispose {
            val sid = currentStreamId
            if (sid.isNotBlank()) {
                viewModel.endStream(sid)
            }
            try {
                room.disconnect()
                room.release()
            } catch (_: Exception) {
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(scheme.basicBackground.getColor())
    ) {
        // ── Fullscreen Host Camera Stream or Loading / Error State ───────────
        if (localVideoTrack != null) {
            LiveKitVideoSurface(
                room = room,
                videoTrack = localVideoTrack,
                modifier = Modifier.fillMaxSize()
            )
        } else if (errorMessage != null) {
            // ── Error State UI with Retry & Exit ─────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(scheme.basicBackgroundCard.getColor())
                        .padding(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(scheme.functionalDestructiveSubtle.getColor()),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSText(
                            text = "⚠️",
                            style = ODSTextStyles.bodyL,
                            color = scheme.functionalDestructiveStandard
                        )
                    }

                    ODSText(
                        text = stringResource(R.string.live_stream_ended),
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )

                    ODSText(
                        text = errorMessage ?: "Unknown connection failure",
                        style = ODSTextStyles.bodyMRegular,
                        color = scheme.basicTextRecessive
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ODSButton(
                            modifier = Modifier.weight(1f),
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = stringResource(R.string.retry),
                                variant = ODSButtonVariant.SECONDARY
                            ),
                            onClick = {
                                errorMessage = null
                                isConnecting = true
                                retryTrigger++
                            }
                        )

                        ODSButton(
                            modifier = Modifier.weight(1f),
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = stringResource(R.string.close),
                                variant = ODSButtonVariant.PRIMARY
                            ),
                            onClick = {
                                viewModel.endStream(streamId)
                                onEndStream()
                            }
                        )
                    }
                }
            }
        } else {
            // ── ODS Loading Spinner State ─────────────────────────────────────
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ODSLoadingSpinner(
                    props = ODSLoadingSpinnerProps(
                        labelText = connectionStatusText,
                        labelAlignment = ODSLoadingSpinnerLabelAlignment.VERTICAL
                    ),
                    scheme = scheme
                )
            }
        }

        // ── Top Bar (LIVE Badge, Paid Mode Pill, Earnings, End Live Button) ──
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Live Badge + Viewer Count
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(scheme.basicBackgroundCardSubtle.getColor())
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(scheme.functionalDestructiveStandard.getColor())
                )
                ODSText(
                    text = "LIVE",
                    style = ODSTextStyles.microcopyBold,
                    color = scheme.functionalDestructiveStandard
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = "Viewers",
                    tint = scheme.basicText.getColor(),
                    modifier = Modifier.size(14.dp)
                )
                ODSText(
                    text = "$viewerCount",
                    style = ODSTextStyles.microcopyBold,
                    color = scheme.basicText
                )
            }

            // Monetization Mode Pill (Free vs Paid)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (isPaidMode) Brush.horizontalGradient(listOf(scheme.basicAccent.getColor(), scheme.basicAccentSecondary.getColor()))
                        else Brush.horizontalGradient(listOf(scheme.basicBackgroundCardSubtle.getColor(), scheme.basicBackgroundCardSubtle.getColor()))
                    )
                    .clickable { showPaidConfigDialog = true }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                ODSText(
                    text = if (isPaidMode) "🪙 ${coinRatePerMin.toInt()}c/min" else "🪙 Free",
                    style = ODSTextStyles.microcopyBold,
                    color = if (isPaidMode) scheme.basicAccent else scheme.basicText
                )
            }

            // Real-time Earnings Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(scheme.basicBackgroundCardSubtle.getColor())
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                ODSText(
                    text = "💰 ₹%.2f".format(totalEarned),
                    style = ODSTextStyles.microcopyBold,
                    color = scheme.basicAccent
                )
            }

            // End Live Button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(scheme.functionalDestructiveStandard.getColor())
                    .clickable { showEndDialog = true }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                ODSText(
                    text = stringResource(R.string.call_action_end),
                    style = ODSTextStyles.microcopyBold,
                    color = scheme.basicText
                )
            }
        }

        // ── Toast Status Banner ───────────────────────────────────────────────
        AnimatedVisibility(
            visible = statusToast != null,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 64.dp)
        ) {
            statusToast?.let { toast ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF7B1FA2), Color(0xFFE91E63))
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    ODSText(
                        text = toast,
                        style = ODSTextStyles.bodyMBold,
                        color = HexColor(0xFFFFFFFF)
                    )
                }
            }
        }

        // ── Bottom Content: Incoming Chat & Camera Controls ──────────────────
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 20.dp, start = 16.dp, end = 16.dp)
        ) {
            // Live Messages list
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 160.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(chatMessages) { msg ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x66000000))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ODSText(
                                text = "${msg.senderName}: ",
                                style = ODSTextStyles.microcopyBold,
                                color = HexColor(0xFFBC96FF)
                            )
                            ODSText(
                                text = msg.text,
                                style = ODSTextStyles.microcopyRegular,
                                color = HexColor(0xFFFFFFFF)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Host Quick Controls (Flip Camera, Mic Mute, Token Deduction)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Flip Camera
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color(0x882B1764))
                        .clickable {
                            isCameraFront = !isCameraFront
                            localVideoTrack?.capturer?.let {
                                // flip camera
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Cameraswitch,
                        contentDescription = "Flip Camera",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Mute Mic
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(if (!isMuted) Color(0x882B1764) else Color(0x88FF4365))
                        .clickable {
                            isMuted = !isMuted
                            scope.launch(Dispatchers.Default) {
                                room.localParticipant.setMicrophoneEnabled(!isMuted)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (!isMuted) Icons.Default.Mic else Icons.Default.MicOff,
                        contentDescription = "Mute",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Token Deduction / Paid Mode Button
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(
                            if (isPaidMode) Brush.linearGradient(listOf(Color(0xFF8E24AA), Color(0xFFFF6D00)))
                            else Brush.linearGradient(listOf(Color(0x882B1764), Color(0x882B1764)))
                        )
                        .clickable { showPaidConfigDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = "🪙",
                        style = ODSTextStyles.bodyMBold,
                        color = HexColor(0xFFFFFFFF)
                    )
                }
            }
        }

        // ── Token Deduction (Paid Mode) Config Bottom Sheet (ODS Design System) ──
        if (showPaidConfigDialog) {
            var tempPaidMode by remember { mutableStateOf(isPaidMode) }
            var tempRate by remember { mutableDoubleStateOf(coinRatePerMin) }

            ODSBottomSheet(
                scheme = scheme,
                showBottomSheet = true,
                props = ODSBottomSheetProps(showHandle = true),
                onDismissRequest = { showPaidConfigDialog = false },
                onCloseClicked = { showPaidConfigDialog = false },
                titleSlot = {
                    ODSBottomSheetHeader(
                        scheme = scheme,
                        props = ODSBottomSheetHeaderProps(
                            largeHeading = "Token Deduction",
                            subtitle = "Per-minute stream monetization",
                            size = ODSBottomSheetHeaderSize.SMALL
                        )
                    )
                },
                contentSlot = {
                    ODSColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        gap = 16.dp
                    ) {
                        // Switch Card
                        ODSBox(
                            modifier = Modifier.fillMaxWidth(),
                            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                            cornerRadius = ODSCorners(all = 16.dp),
                            padding = ODSPadding(horizontal = 16.dp, vertical = 14.dp)
                        ) {
                            ODSRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ODSColumn(gap = 2.dp, modifier = Modifier.weight(1f)) {
                                    ODSText(
                                        text = "Paid Broadcast",
                                        style = ODSTextStyles.bodyMBold,
                                        color = scheme.basicText
                                    )
                                    ODSText(
                                        text = if (tempPaidMode) "Active (Tokens deducted per minute)" else "Disabled (Free for all viewers)",
                                        style = ODSTextStyles.microcopyRegular,
                                        color = if (tempPaidMode) scheme.basicAccent else scheme.basicTextRecessive
                                    )
                                }
                                ODSSwitch(
                                    scheme = scheme,
                                    props = ODSSwitchProps(
                                        selected = tempPaidMode,
                                        size = ODSSwitchSize.LARGE
                                    ),
                                    onCheckedChange = { tempPaidMode = it }
                                )
                            }
                        }

                        if (tempPaidMode) {
                            ODSText(
                                text = "Charge Rate (Default: 10 Coins/min)",
                                style = ODSTextStyles.bodyMBold,
                                color = scheme.basicText
                            )

                            // Quick Rate Selectors
                            ODSRow(
                                modifier = Modifier.fillMaxWidth(),
                                gap = 8.dp
                            ) {
                                listOf(10.0, 20.0, 50.0, 100.0).forEach { rate ->
                                    val isSelected = tempRate == rate
                                    ODSBox(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { tempRate = rate },
                                        background = listOf(
                                            ODSColorModel(
                                                hexColor = if (isSelected) scheme.basicAccent
                                                else scheme.basicBackgroundCard
                                            )
                                        ),
                                        cornerRadius = ODSCorners(all = 12.dp),
                                        padding = ODSPadding(vertical = 12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        ODSText(
                                            text = "${rate.toInt()}c/min",
                                            style = ODSTextStyles.microcopyBold,
                                            color = if (isSelected) scheme.basicTextOnAccent else scheme.basicText
                                        )
                                    }
                                }
                            }

                            // Stepper Row
                            ODSBox(
                                modifier = Modifier.fillMaxWidth(),
                                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                                cornerRadius = ODSCorners(all = 16.dp),
                                padding = ODSPadding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                ODSRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ODSText(
                                        text = "Custom Rate:",
                                        style = ODSTextStyles.bodyMRegular,
                                        color = scheme.basicText
                                    )
                                    ODSRow(
                                        verticalAlignment = Alignment.CenterVertically,
                                        gap = 12.dp
                                    ) {
                                        ODSBox(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .clickable { if (tempRate > 5.0) tempRate -= 5.0 },
                                            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            ODSText(
                                                text = "−",
                                                style = ODSTextStyles.bodyMBold,
                                                color = scheme.basicText
                                            )
                                        }
                                        ODSText(
                                            text = "${tempRate.toInt()} Coins/min",
                                            style = ODSTextStyles.bodyMBold,
                                            color = scheme.basicAccent
                                        )
                                        ODSBox(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .clickable { tempRate += 5.0 },
                                            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            ODSText(
                                                text = "+",
                                                style = ODSTextStyles.bodyMBold,
                                                color = scheme.basicText
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Apply Button
                        ODSButton(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = if (tempPaidMode) "Apply Token Deduction (${tempRate.toInt()} Coins/min)" else "Disable Token Deduction (Set Free)",
                                variant = ODSButtonVariant.PRIMARY
                            ),
                            onClick = {
                                isPaidMode = tempPaidMode
                                coinRatePerMin = tempRate
                                showPaidConfigDialog = false

                                scope.launch {
                                    try {
                                        val token = sessionManager.token ?: ""
                                        api.setLivePaidMode(token, streamId, isPaidMode, coinRatePerMin)
                                        statusToast = if (isPaidMode) {
                                            "🪙 Token deduction active (${coinRatePerMin.toInt()} coins/min)"
                                        } else {
                                            "🪙 Stream is now free for all viewers"
                                        }
                                        delay(3000)
                                        statusToast = null
                                    } catch (e: Exception) {
                                        statusToast = "Failed to update settings: ${e.localizedMessage}"
                                        delay(3000)
                                        statusToast = null
                                    }
                                }
                            }
                        )
                    }
                }
            )
        }

        // ── End Live Confirmation Modal ──────────────────────────────────────
        if (showEndDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xAA000000)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFF1E1145))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ODSText(
                        text = "End Live Stream?",
                        style = ODSTextStyles.bodyMRegular,
                        color = HexColor(0xFFFFFFFF)
                    )
                    ODSText(
                        text = "Are you sure you want to end your broadcast with $viewerCount viewers?",
                        style = ODSTextStyles.bodyMRegular,
                        color = HexColor(0xFFBC96FF)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ODSButton(
                            modifier = Modifier.weight(1f),
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = "Continue",
                                variant = ODSButtonVariant.SECONDARY
                            ),
                            onClick = { showEndDialog = false }
                        )

                        ODSButton(
                            modifier = Modifier.weight(1f),
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = "End Show",
                                variant = ODSButtonVariant.PRIMARY
                            ),
                            onClick = {
                                showEndDialog = false
                                viewModel.endStream(streamId)
                                try {
                                    room.disconnect()
                                    room.release()
                                } catch (_: Exception) {
                                }
                                onEndStream()
                            }
                        )
                    }
                }
            }
        }
    }
}

private fun generateDirectLiveKitHostToken(
    apiKey: String,
    apiSecret: String,
    identity: String,
    roomName: String
): String {
    val header = """{"alg":"HS256","typ":"JWT"}"""
    val iat = System.currentTimeMillis() / 1000
    val exp = iat + (6 * 3600)
    val payload =
        """{"iss":"$apiKey","sub":"$identity","name":"$identity","iat":$iat,"exp":$exp,"nbf":$iat,"video":{"roomJoin":true,"room":"$roomName","canPublish":true,"canSubscribe":true}}"""

    val base64Header = android.util.Base64.encodeToString(
        header.toByteArray(),
        android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP or android.util.Base64.NO_PADDING
    )
    val base64Payload = android.util.Base64.encodeToString(
        payload.toByteArray(),
        android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP or android.util.Base64.NO_PADDING
    )
    val dataToSign = "$base64Header.$base64Payload"

    val mac = javax.crypto.Mac.getInstance("HmacSHA256")
    val secretKey = javax.crypto.spec.SecretKeySpec(apiSecret.toByteArray(), "HmacSHA256")
    mac.init(secretKey)
    val signature = mac.doFinal(dataToSign.toByteArray())
    val base64Signature = android.util.Base64.encodeToString(
        signature,
        android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP or android.util.Base64.NO_PADDING
    )

    return "$dataToSign.$base64Signature"
}
