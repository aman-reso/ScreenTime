package com.app.screentime.feature.call

import android.Manifest
import android.app.Activity
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
import androidx.compose.foundation.layout.width
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
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.config.R
import com.app.screentime.core.ui.theme.zonaODSTheme
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
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.delay
import com.telekom.odsystem.R as ODSR

/**
 * 1-on-1 Voice Call Screen.
 * Uses Telekom ODS components and ODSTheme scheme consistently.
 */
@Composable
fun VoiceCallScreen(
    modelId: String,
    modelName: String,
    modifier: Modifier = Modifier,
    avatarUrl: String = "",
    scheme: ODSTheme = zonaODSTheme,
    isInPipMode: Boolean = false,
    onBack: () -> Unit = {},
    onEndCall: () -> Unit = {},
    onNavigateToTopUp: () -> Unit = {},
    viewModel: CallViewModel = hiltViewModel()
) {
    val callState by viewModel.callState.collectAsState()
    val context = LocalContext.current
    val isActuallyInPip = (context as? Activity)?.isInPictureInPictureMode == true || isInPipMode

    val permissionsToRequest = remember {
        val list = mutableListOf(Manifest.permission.RECORD_AUDIO)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            list.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            list.add(Manifest.permission.BLUETOOTH_CONNECT)
        }
        list.toTypedArray()
    }

    var hasMicPermission by remember {
        mutableStateOf(
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

    val isCurrentUserModel = viewModel.isCurrentUserModel()
    val targetId = when {
        modelId.isNotBlank() -> modelId
        callState.remoteUserId.isNotBlank() -> callState.remoteUserId
        else -> ""
    }
    val isIncomingOrActive =
        (callState.status == CallStatus.INCOMING || callState.status == CallStatus.ACTIVE) &&
                callState.remoteUserId.isNotEmpty() && (callState.remoteUserId == targetId || targetId.isBlank())

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val micGranted = result[Manifest.permission.RECORD_AUDIO] == true ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
        hasMicPermission = micGranted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            hasNotificationPermission = result[Manifest.permission.POST_NOTIFICATIONS] == true ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
        }
        if (micGranted && callState.status == CallStatus.INCOMING) {
            viewModel.acceptIncomingCall()
        } else if (micGranted && !hasInitiatedCall && !isIncomingOrActive && targetId.isNotBlank()) {
            hasInitiatedCall = true
            viewModel.startOutgoingCall(targetId, modelName)
        }
    }

    LaunchedEffect(Unit) {
        if (callState.status == CallStatus.ENDED) {
            viewModel.resetState()
        }
        if (callState.status == CallStatus.INCOMING) {
            if (hasMicPermission) {
                viewModel.acceptIncomingCall()
            } else {
                permissionLauncher.launch(permissionsToRequest)
            }
        } else if (!hasInitiatedCall && !isIncomingOrActive && targetId.isNotBlank()) {
            if (hasMicPermission && hasNotificationPermission) {
                hasInitiatedCall = true
                viewModel.startOutgoingCall(targetId, modelName)
            } else {
                permissionLauncher.launch(permissionsToRequest)
            }
        }
    }

    LaunchedEffect(callState.status) {
        if (callState.status == CallStatus.DIALING ||
            callState.status == CallStatus.ACTIVE ||
            callState.status == CallStatus.INCOMING ||
            callState.status == CallStatus.CHECKING_BALANCE
        ) {
            callHasStarted = true
        } else if (callState.status == CallStatus.ENDED) {
            if (callHasStarted) {
                callHasStarted = false
                viewModel.resetState()
                onEndCall()
            } else {
                viewModel.resetState()
            }
        }
    }

    if (callState.status == CallStatus.INSUFFICIENT_BALANCE) {
        ODSBox(
            modifier = modifier
                .fillMaxSize(),
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
                    text = if (isCurrentUserModel) stringResource(R.string.call_caller_insufficient_balance) else stringResource(
                        R.string.call_insufficient_balance
                    ),
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
        }
        return
    }

    val minutes = callState.durationSec / 60
    val seconds = callState.durationSec % 60
    val formattedTime = "%02d:%02d".format(minutes, seconds)
    val statusText = when (callState.status) {
        CallStatus.CHECKING_BALANCE -> "Checking Balance..."
        CallStatus.DIALING -> "Ringing..."
        CallStatus.ACTIVE -> "Connected • $formattedTime"
        CallStatus.INCOMING -> "Incoming Call..."
        CallStatus.ENDED -> "Call Ended"
        else -> "Connected • $formattedTime"
    }

    if (isActuallyInPip) {
        VoiceCallPipLayout(
            modelName = modelName,
            statusText = statusText,
            formattedTime = formattedTime,
            callState = callState,
            avatarUrl = avatarUrl,
            scheme = scheme,
            onEndCall = {
                viewModel.endCall("Ended from PiP")
            },
            onToggleMute = { viewModel.toggleMute() }
        )
        return
    }

    if (!hasMicPermission) {
        ODSBox(
            modifier = modifier
                .fillMaxSize(),
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
                        iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_mic),
                        tint = scheme.basicAccent.getColor(),
                        modifier = Modifier.size(36.dp)
                    )
                }

                ODSText(
                    text = "Microphone Access Required",
                    style = ODSTextStyles.bodyL,
                    color = scheme.basicText
                )

                ODSText(
                    text = "Winter needs microphone permission to transmit your voice during audio calls.",
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
                            label = "Cancel",
                            variant = ODSButtonVariant.SECONDARY
                        ),
                        onClick = onEndCall
                    )

                    ODSButton(
                        modifier = Modifier.weight(1f),
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Allow Access",
                            variant = ODSButtonVariant.PRIMARY
                        ),
                        onClick = { permissionLauncher.launch(permissionsToRequest) }
                    )
                }
            }
        }
        return
    }

    // ── MAIN AUDIO CALL SCREEN ──
    ODSBox(
        modifier = modifier
            .fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Top Bar ──
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ODSBox(
                    modifier = Modifier
                        .size(42.dp)
                        .clickable { onBack() },
                    cornerRadius = ODSCorners(all = 21.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_arrow_left),
                        tint = scheme.basicText.getColor(),
                        modifier = Modifier.size(20.dp)
                    )
                }

                ODSText(
                    text = "Active Call",
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicText
                )
                ODSBox(modifier = Modifier.size(42.dp))
            }

            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ODSBox(
                    modifier = Modifier.size(80.dp),
                    cornerRadius = ODSCorners(all = 88.dp),
                    border = ODSBorder(
                        width = 1.5.dp,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicAccent))
                    ),
                    clipContent = true,
                    contentAlignment = Alignment.Center
                ) {
                    if (avatarUrl.isNotBlank()) {
                        ODSImage(
                            imageModel = ODSImageModel(
                                url = avatarUrl,
                                contentDescription = modelName
                            ),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        ODSIcon(
                            iconModel = ODSIconModel(drawableRes = ODSR.drawable.profile),
                            tint = scheme.basicTextRecessive.getColor(),
                            modifier = Modifier.size(56.dp)
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                ODSText(
                    text = modelName.ifBlank { "User" },
                    style = ODSTextStyles.bodyL,
                    color = scheme.basicText
                )

                Spacer(Modifier.height(12.dp))

                ODSRow(
                    cornerRadius = ODSCorners(all = 20.dp),
                    border = ODSBorder(
                        width = 1.dp,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                    ),
                    padding = ODSPadding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    gap = 8.dp
                ) {
                    ODSBox(
                        modifier = Modifier.size(7.dp),
                        cornerRadius = ODSCorners(all = 4.dp),
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccent))
                    )

                    ODSText(
                        text = when (callState.status) {
                            CallStatus.ACTIVE -> "Connected • $formattedTime"
                            CallStatus.DIALING -> "Ringing..."
                            else -> "Connected • $formattedTime"
                        },
                        style = ODSTextStyles.bodySBold,
                        color = scheme.basicText
                    )
                }
            }

            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                gap = 26.dp
            ) {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalAlignment = Alignment.CenterVertically,
                    gap = 24.dp
                ) {
                    AudioCallControlItem(
                        iconRes = if (callState.isMuted) ODSR.drawable.ic_mic_off else ODSR.drawable.ic_mic,
                        label = "Mute",
                        isActive = callState.isMuted,
                        scheme = scheme,
                        onClick = { viewModel.toggleMute() }
                    )

                    AudioCallControlItem(
                        iconRes = ODSR.drawable.ic_volume_2,
                        label = "Speaker",
                        isActive = callState.isSpeaker,
                        scheme = scheme,
                        onClick = { viewModel.toggleSpeaker() }
                    )

                    AudioCallControlItem(
                        iconRes = ODSR.drawable.ic_phone_off,
                        label = "End",
                        isActive = true,
                        scheme = scheme,
                        onClick = {
                            viewModel.endCall()
                        })
                }
            }
        }
    }
}

/**
 * Control action button with circular container and text label below using ODSTheme scheme.
 */
@Composable
private fun AudioCallControlItem(
    iconRes: Int,
    label: String,
    isActive: Boolean,
    scheme: ODSTheme,
    onClick: () -> Unit
) {
    ODSColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        gap = 8.dp
    ) {
        ODSBox(
            modifier = Modifier
                .size(62.dp)
                .clickable(onClick = onClick),
            cornerRadius = ODSCorners(all = 31.dp),
            contentAlignment = Alignment.Center
        ) {
            ODSIcon(
                iconModel = ODSIconModel(drawableRes = iconRes),
                tint = if (isActive) scheme.basicText.getColor() else scheme.basicTextRecessive.getColor(),
                modifier = Modifier.size(26.dp)
            )
        }

        ODSText(
            text = label,
            style = ODSTextStyles.microcopyRegular,
            color = if (isActive) scheme.basicText else scheme.basicTextRecessive
        )
    }
}

/**
 * Picture-in-Picture layout for floating window using ODS components.
 */
@Composable
private fun VoiceCallPipLayout(
    modelName: String,
    statusText: String,
    formattedTime: String,
    callState: CallUiState,
    avatarUrl: String,
    scheme: ODSTheme,
    onEndCall: () -> Unit,
    onToggleMute: () -> Unit
) {
    ODSBox(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground)),
        contentAlignment = Alignment.Center
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: Status + Name
            ODSColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                gap = 2.dp
            ) {
                ODSText(
                    text = if (callState.status == CallStatus.ACTIVE) formattedTime else statusText,
                    style = ODSTextStyles.microcopyBold,
                    color = scheme.basicAccent
                )
                ODSText(
                    text = modelName.ifBlank { "User" },
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicText
                )
            }

            // Compact Avatar with accent ring
            ODSBox(
                modifier = Modifier.size(64.dp),
                cornerRadius = ODSCorners(all = 32.dp),
                border = ODSBorder(
                    width = 2.dp,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicAccent))
                ),
                clipContent = true,
                contentAlignment = Alignment.Center
            ) {
                if (avatarUrl.isNotBlank()) {
                    ODSImage(
                        imageModel = ODSImageModel(
                            url = avatarUrl,
                            contentDescription = modelName
                        ),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_user),
                        tint = scheme.basicTextRecessive.getColor(),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            // Mini Actions: Mute & End Call
            ODSRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mini Mute
                ODSBox(
                    modifier = Modifier
                        .size(36.dp)
                        .clickable(onClick = onToggleMute),
                    cornerRadius = ODSCorners(all = 18.dp),
                    background = listOf(
                        ODSColorModel(
                            hexColor = if (callState.isMuted) scheme.basicAccent else scheme.basicBackgroundSubtle
                        )
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = if (callState.isMuted) ODSR.drawable.ic_mic_off else ODSR.drawable.ic_mic
                        ),
                        tint = if (callState.isMuted) scheme.basicTextOnAccent.getColor() else scheme.basicText.getColor(),
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Mini Hang Up
                ODSBox(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { viewModel.endCall() },
                    cornerRadius = ODSCorners(all = 20.dp),
                    background = listOf(ODSColorModel(hexColor = HexColor("#FF4D4F", 1.0f))),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_phone_off),
                        tint = HexColor("#FFFFFF", 1.0f).getColor(),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
