package com.app.screentime.feature.call

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.feature.call.components.SelfViewPipCard
import com.app.screentime.feature.call.components.VideoCallBottomOverlay
import com.app.screentime.feature.call.components.VideoCallCheckingBalanceScreen
import com.app.screentime.feature.call.components.VideoCallDialingOverlay
import com.app.screentime.feature.call.components.VideoCallInsufficientBalanceScreen
import com.app.screentime.feature.call.components.VideoCallTopBar
import com.app.screentime.feature.call.components.VideoSurfaceContainer
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * VideoCallScreen
 *
 * 1-on-1 Hardware-Accelerated Video Call Screen (LiveKit SFU with WebRTC Fallback).
 *
 * Architecture & Organization:
 * - Business logic (track discovery, room subscriptions, media hardware manipulation) is delegated
 *   to dedicated UseCases ([com.app.screentime.feature.call.domain.usecase.ObserveVideoTracksUseCase]
 *   and [com.app.screentime.feature.call.domain.usecase.ManageCallMediaUseCase]) accessed via [CallViewModel].
 * - UI is cleanly broken into modular, single-responsibility composables:
 *   1. [VideoSurfaceContainer]: Full-screen video feed rendering
 *   2. [VideoCallTopBar]: Floating header with duration timer and status
 *   3. [SelfViewPipCard]: Picture-in-picture local camera preview window
 *   4. [VideoCallDialingOverlay]: Outgoing calling/ringing center badge
 *   5. [VideoCallBottomOverlay]: Glassmorphic bottom dock housing all in-call action buttons
 *   6. [VideoCallCheckingBalanceScreen]: Pre-call wallet verification loading state
 *   7. [VideoCallInsufficientBalanceScreen]: Coin shortage alert modal
 *   8. [VideoCallEndedScreen]: Call summary card
 *
 * @param modelId Target remote user ID
 * @param modelName Target remote user display name
 * @param ratePerMin Coins charged per minute
 * @param avatarUrl Remote user avatar URL for fallback preview
 * @param scheme Telekom ODS Theme tokens
 * @param onEndCall Callback triggered when user exits or hangs up
 * @param onNavigateToTopUp Callback to navigate to wallet recharge screen
 * @param viewModel Call ViewModel
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

    // ── Reactive Video Tracks from UseCase ──
    val remoteVideoTrack by viewModel.remoteVideoTrack.collectAsState()
    val localVideoTrack by viewModel.localVideoTrack.collectAsState()

    // ── Fallback WebRTC Video Tracks ──
    val localRtcVideoTrack by viewModel.localWebRtcVideoTrack.collectAsState()
    val remoteRtcVideoTrack by viewModel.remoteWebRtcVideoTrack.collectAsState()
    val eglBase = viewModel.webRtcEglBase

    val isRemoteVideoActive =
        remoteVideoTrack != null || (remoteRtcVideoTrack != null && eglBase != null)

    // ── Permission Management ──
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
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    var hasInitiatedCall by remember { mutableStateOf(false) }
    var callHasStarted by remember { mutableStateOf(false) }

    val targetId = when {
        modelId.isNotBlank() -> modelId
        callState.remoteUserId.isNotBlank() -> callState.remoteUserId
        else -> ""
    }

    val isIncomingOrActive =
        (callState.status == CallStatus.INCOMING ||
                callState.status == CallStatus.ACTIVE ||
                callState.status == CallStatus.DIALING) &&
                (callState.remoteUserId.isEmpty() || callState.remoteUserId == targetId || targetId.isBlank())

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val cameraGranted = (result[Manifest.permission.CAMERA] == true ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED) &&
                (result[Manifest.permission.RECORD_AUDIO] == true ||
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.RECORD_AUDIO
                        ) == PackageManager.PERMISSION_GRANTED)
        hasCameraPermission = cameraGranted
        if (cameraGranted) {
            viewModel.setCameraEnabled(true)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            hasNotificationPermission = result[Manifest.permission.POST_NOTIFICATIONS] == true ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
        }
        if (cameraGranted && callState.status == CallStatus.INCOMING) {
            viewModel.acceptIncomingCall()
        } else if (cameraGranted && !hasInitiatedCall && targetId.isNotBlank() && !isIncomingOrActive) {
            hasInitiatedCall = true
            viewModel.startOutgoingCall(targetId, modelName, ratePerMin, CallType.VIDEO)
        }
    }

    LaunchedEffect(Unit) {
        if (callState.status == CallStatus.ENDED) {
            viewModel.resetState()
        }
        if (callState.status == CallStatus.INCOMING) {
            if (hasCameraPermission) {
                viewModel.setCameraEnabled(true)
                viewModel.acceptIncomingCall()
            } else {
                permissionLauncher.launch(permissionsToRequest)
            }
        } else if (callState.status == CallStatus.ACTIVE || callState.status == CallStatus.DIALING) {
            if (hasCameraPermission) {
                viewModel.setCameraEnabled(true)
            } else {
                permissionLauncher.launch(permissionsToRequest)
            }
        } else if (!hasInitiatedCall && !isIncomingOrActive && targetId.isNotBlank()) {
            if (hasCameraPermission && hasNotificationPermission) {
                hasInitiatedCall = true
                viewModel.startOutgoingCall(targetId, modelName, ratePerMin, CallType.VIDEO)
            } else {
                permissionLauncher.launch(permissionsToRequest)
            }
        }
    }

    // ── Auto-navigate back to previous screen immediately when call ends ──
    LaunchedEffect(callState.status) {
        if (callState.status == CallStatus.DIALING ||
            callState.status == CallStatus.ACTIVE ||
            callState.status == CallStatus.INCOMING ||
            callState.status == CallStatus.CHECKING_BALANCE
        ) {
            callHasStarted = true
        } else if (callState.status == CallStatus.ENDED) {
            if (callHasStarted) {
                viewModel.resetState()
                onEndCall()
            } else {
                // Stale ENDED status from previous session; reset state to IDLE so this call can proceed
                viewModel.resetState()
            }
        }
    }

    if (callState.status == CallStatus.CHECKING_BALANCE) {
        VideoCallCheckingBalanceScreen(
            modelName = modelName,
            ratePerMin = ratePerMin,
            scheme = scheme,
            modifier = modifier
        )
        return
    }

    if (callState.status == CallStatus.INSUFFICIENT_BALANCE) {
        VideoCallInsufficientBalanceScreen(
            callState = callState,
            isCurrentUserModel = isCurrentUserModel,
            modelName = modelName,
            scheme = scheme,
            onCancel = {
                viewModel.resetState()
                onEndCall()
            },
            onRecharge = {
                viewModel.resetState()
                onNavigateToTopUp()
            },
            modifier = modifier
        )
        return
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        VideoSurfaceContainer(
            room = room,
            callState = callState,
            remoteVideoTrack = remoteVideoTrack,
            localVideoTrack = localVideoTrack,
            remoteRtcVideoTrack = remoteRtcVideoTrack,
            localRtcVideoTrack = localRtcVideoTrack,
            eglBase = eglBase,
            avatarUrl = avatarUrl,
            modelName = modelName,
            scheme = scheme,
            modifier = Modifier.fillMaxSize()
        )

        if (!isRemoteVideoActive) {
            VideoCallDialingOverlay(
                callState = callState,
                modelName = modelName,
                avatarUrl = avatarUrl,
                scheme = scheme,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        VideoCallTopBar(
            callState = callState,
            modelName = modelName,
            isRemoteVideoActive = isRemoteVideoActive,
            scheme = scheme,
            onFlipCamera = { viewModel.flipCamera() },
            modifier = Modifier.align(Alignment.TopCenter)
        )

        if (isRemoteVideoActive) {
            SelfViewPipCard(
                room = room,
                callState = callState,
                localVideoTrack = localVideoTrack,
                localRtcVideoTrack = localRtcVideoTrack,
                eglBase = eglBase,
                scheme = scheme,
                onFlipCamera = { viewModel.flipCamera() },
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }

        VideoCallBottomOverlay(
            callState = callState,
            modelName = modelName,
            scheme = scheme,
            onToggleMute = { viewModel.toggleMute() },
            onToggleCamera = { viewModel.toggleCamera() },
            onFlipCamera = { viewModel.flipCamera() },
            onToggleSpeaker = { viewModel.toggleSpeaker() },
            onEndCall = {
                viewModel.endCall()
                viewModel.resetState()
                onEndCall()
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
