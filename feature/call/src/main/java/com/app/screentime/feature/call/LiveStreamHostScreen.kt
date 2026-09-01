package com.app.screentime.feature.call

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme
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
    var viewerCount by remember { mutableIntStateOf(1) }
    var totalEarned by remember { mutableDoubleStateOf(0.0) }
    var isMuted by remember { mutableStateOf(false) }
    var isCameraFront by remember { mutableStateOf(true) }
    var showEndDialog by remember { mutableStateOf(false) }
    var streamId by remember { mutableStateOf("") }
    var retryTrigger by remember { mutableIntStateOf(0) }

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
        mutableStateListOf(
            LiveChatMessage(
                "1",
                "System",
                "You are now LIVE! Share your stream to invite viewers. 🌟"
            )
        )
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

                // Simulated incoming viewer comments for active demo feel
                if (viewerCount < 145) {
                    viewerCount += (1..3).random()
                }
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
            .background(Color(0xFF0F0A1C))
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
                        .background(Color(0xFF1E1145))
                        .padding(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color(0x33FF334B)),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSText(
                            text = "⚠️",
                            style = ODSTextStyles.bodyL,
                            color = HexColor(0xFFFF334B)
                        )
                    }

                    ODSText(
                        text = "Unable to Start Broadcast",
                        style = ODSTextStyles.bodyMBold,
                        color = HexColor(0xFFFFFFFF)
                    )

                    ODSText(
                        text = errorMessage ?: "Unknown connection failure",
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
                                label = "Go Back",
                                variant = ODSButtonVariant.SECONDARY
                            ),
                            onClick = {
                                viewModel.endStream(streamId)
                                onEndStream()
                            }
                        )

                        ODSButton(
                            modifier = Modifier.weight(1f),
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = "Retry",
                                variant = ODSButtonVariant.PRIMARY
                            ),
                            onClick = {
                                errorMessage = null
                                isConnecting = true
                                retryTrigger++
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

        // ── Top Bar (LIVE Badge, Viewers, Earnings, End Live Button) ──────────
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
                    .background(Color(0x88000000))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF334B))
                )
                ODSText(
                    text = "LIVE",
                    style = ODSTextStyles.microcopyBold,
                    color = HexColor(0xFFFF334B)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = "Viewers",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                ODSText(
                    text = "$viewerCount",
                    style = ODSTextStyles.microcopyBold,
                    color = HexColor(0xFFFFFFFF)
                )
            }

            // Real-time Earnings Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x88000000))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                ODSText(
                    text = "💰 ₹%.2f".format(totalEarned),
                    style = ODSTextStyles.microcopyBold,
                    color = HexColor(0xFFD7FF81)
                )
            }

            // End Live Button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xCCFF334B))
                    .clickable { showEndDialog = true }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                ODSText(
                    text = "End Live",
                    style = ODSTextStyles.microcopyBold,
                    color = HexColor(0xFFFFFFFF)
                )
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

            // Host Quick Controls (Flip Camera, Mic Mute)
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
            }
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
