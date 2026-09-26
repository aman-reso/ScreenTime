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
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.app.screentime.core.ui.theme.ZonaColors
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
import kotlin.time.Duration.Companion.milliseconds
import com.telekom.odsystem.R as ODSR

/**
 * 1-on-1 Voice Call Screen (Matching media_1789898718933.png new-audio-call).
 * 100% constructed using Telekom ODS components and Zona design tokens.
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
    var showKeypad by remember { mutableStateOf(false) }
    var dialedDigits by remember { mutableStateOf("") }

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

    val isCurrentUserModel = viewModel.isCurrentUserModel()
    val targetId = when {
        modelId.isNotBlank() -> modelId
        callState.remoteUserId.isNotBlank() -> callState.remoteUserId
        else -> ""
    }
    val isIncomingOrActive = (callState.status == CallStatus.INCOMING || callState.status == CallStatus.ACTIVE) &&
            callState.remoteUserId.isNotEmpty() && (callState.remoteUserId == targetId || targetId.isBlank())

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val micGranted = result[Manifest.permission.RECORD_AUDIO] == true ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        hasMicPermission = micGranted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            hasNotificationPermission = result[Manifest.permission.POST_NOTIFICATIONS] == true ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        }
        if (micGranted && !hasInitiatedCall && !isIncomingOrActive && targetId.isNotBlank()) {
            hasInitiatedCall = true
            viewModel.startOutgoingCall(targetId, modelName)
        }
    }

    LaunchedEffect(Unit) {
        if (!hasInitiatedCall && !isIncomingOrActive && targetId.isNotBlank()) {
            if (hasMicPermission && hasNotificationPermission) {
                hasInitiatedCall = true
                viewModel.startOutgoingCall(targetId, modelName)
            } else {
                permissionLauncher.launch(permissionsToRequest)
            }
        }
    }

    LaunchedEffect(callState.status) {
        if (callState.status == CallStatus.ENDED) {
            delay(2500L.milliseconds)
            viewModel.resetState()
            onEndCall()
        }
    }

    // ── CALL ENDED SCREEN ──
    if (callState.status == CallStatus.ENDED) {
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
                    text = "Call Ended",
                    style = ODSTextStyles.bodyL,
                    color = ZonaColors.TextPrimary
                )

                ODSText(
                    text = callState.endReason ?: "Call with ${modelName.ifBlank { "User" }} has ended.",
                    style = ODSTextStyles.bodyMRegular,
                    color = ZonaColors.LavenderAlt
                )

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

    // ── INSUFFICIENT BALANCE SCREEN ──
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
            onEndCall = {
                viewModel.endCall("Ended from PiP")
                onEndCall()
            },
            onToggleMute = { viewModel.toggleMute() }
        )
        return
    }

    if (!hasMicPermission) {
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
                        iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_mic),
                        tint = ZonaColors.ActiveLime.getColor(),
                        modifier = Modifier.size(36.dp)
                    )
                }

                ODSText(
                    text = "Microphone Access Required",
                    style = ODSTextStyles.bodyL,
                    color = ZonaColors.TextPrimary
                )

                ODSText(
                    text = "Winter needs microphone permission to transmit your voice during audio calls.",
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

    // ── MAIN AUDIO CALL SCREEN (new-audio-call) ──
    ODSBox(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        background = listOf(ODSColorModel(hexColor = ZonaColors.CanvasDark))
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
                // Circular Back Button
                ODSBox(
                    modifier = Modifier
                        .size(42.dp)
                        .clickable {
                            onBack()
                        },
                    cornerRadius = ODSCorners(all = 21.dp),
                    background = listOf(ODSColorModel(hexColor = ZonaColors.SurfaceRaised)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_arrow_left),
                        tint = ZonaColors.TextPrimary.getColor(),
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Centered "Active Call"
                ODSText(
                    text = "Active Call",
                    style = ODSTextStyles.bodyMBold,
                    color = ZonaColors.TextAccent
                )

                // Placeholder for symmetrical center alignment
                ODSBox(modifier = Modifier.size(42.dp))
            }

            // ── Center Content: Avatar with Neon Lime Ring, Name, Status Pill ──
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Large Avatar with Glowing Neon Lime Ring
                ODSBox(
                    modifier = Modifier.size(176.dp),
                    cornerRadius = ODSCorners(all = 88.dp),
                    border = ODSBorder(
                        width = 3.5.dp,
                        colorList = listOf(ODSColorModel(hexColor = ZonaColors.ActiveLime))
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
                            tint = ZonaColors.LavenderAlt.getColor(),
                            modifier = Modifier.size(56.dp)
                        )
                    }
                }

                Spacer(Modifier.height(28.dp))

                // Display Name
                ODSText(
                    text = modelName.ifBlank { "User" },
                    style = ODSTextStyles.bodyL,
                    color = ZonaColors.TextPrimary
                )

                Spacer(Modifier.height(12.dp))

                // Status Pill ("Connected • 04:25")
                ODSRow(
                    cornerRadius = ODSCorners(all = 20.dp),
                    background = listOf(ODSColorModel(hexColor = ZonaColors.SurfaceRaised)),
                    padding = ODSPadding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    gap = 8.dp
                ) {
                    // Active green/lime dot
                    ODSBox(
                        modifier = Modifier.size(7.dp),
                        cornerRadius = ODSCorners(all = 4.dp),
                        background = listOf(ODSColorModel(hexColor = ZonaColors.ActiveLime))
                    )

                    ODSText(
                        text = if (callState.status == CallStatus.ACTIVE) "Connected • $formattedTime"
                               else if (callState.status == CallStatus.DIALING) "Ringing..."
                               else "Connected • $formattedTime",
                        style = ODSTextStyles.bodySBold,
                        color = ZonaColors.TextPrimary
                    )
                }
            }

            // ── Bottom Control Panel ──
            ODSBox(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = ODSCorners(topLeft = 32.dp, topRight = 32.dp),
                background = listOf(ODSColorModel(hexColor = ZonaColors.Background)),
                padding = ODSPadding(top = 32.dp, bottom = 24.dp, left = 24.dp, right = 24.dp)
            ) {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    gap = 26.dp
                ) {
                    // Row 1: Mute, Keypad, Speaker Controls
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Mute Button
                        AudioCallControlItem(
                            iconRes = if (callState.isMuted) ODSR.drawable.ic_mic_off else ODSR.drawable.ic_mic,
                            label = "Mute",
                            isActive = callState.isMuted,
                            activeBg = ZonaColors.Border,
                            inactiveBg = ZonaColors.SurfaceRaised,
                            iconTint = ZonaColors.TextPrimary,
                            labelColor = ZonaColors.LavenderAlt,
                            onClick = { viewModel.toggleMute() }
                        )

                        // Keypad Button
                        AudioCallControlItem(
                            iconRes = ODSR.drawable.ic_grid,
                            label = "Keypad",
                            isActive = showKeypad,
                            activeBg = ZonaColors.Border,
                            inactiveBg = ZonaColors.SurfaceRaised,
                            iconTint = ZonaColors.TextPrimary,
                            labelColor = ZonaColors.LavenderAlt,
                            onClick = { showKeypad = !showKeypad }
                        )

                        // Speaker Button (Active Neon Lime!)
                        AudioCallControlItem(
                            iconRes = ODSR.drawable.ic_volume_2,
                            label = "Speaker",
                            isActive = callState.isSpeaker,
                            activeBg = ZonaColors.ActiveLime,
                            inactiveBg = ZonaColors.SurfaceRaised,
                            iconTint = if (callState.isSpeaker) ZonaColors.TextPrimary else ZonaColors.TextPrimary,
                            labelColor = if (callState.isSpeaker) ZonaColors.ActiveLime else ZonaColors.LavenderAlt,
                            onClick = { viewModel.toggleSpeaker() }
                        )
                    }

                    // Row 2: Large Circular Coral End Call Button
                    ODSBox(
                        modifier = Modifier
                            .size(76.dp)
                            .clickable {
                                viewModel.endCall()
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

                    // Home Bottom Indicator Pill
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

        // ── Interactive Keypad Bottom Overlay ──
        if (showKeypad) {
            ODSBox(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { showKeypad = false },
                background = listOf(ODSColorModel(hexColor = ZonaColors.OverlayLegacy)),
                contentAlignment = Alignment.BottomCenter
            ) {
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = false) {},
                    cornerRadius = ODSCorners(topLeft = 28.dp, topRight = 28.dp),
                    background = listOf(ODSColorModel(hexColor = ZonaColors.Surface)),
                    padding = ODSPadding(all = 24.dp)
                ) {
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        gap = 16.dp
                    ) {
                        ODSRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ODSText(
                                text = "Keypad",
                                style = ODSTextStyles.bodyMBold,
                                color = ZonaColors.TextPrimary
                            )

                            ODSBox(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clickable { showKeypad = false },
                                cornerRadius = ODSCorners(all = 18.dp),
                                background = listOf(ODSColorModel(hexColor = ZonaColors.SurfaceRaised)),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSIcon(
                                    iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_close),
                                    tint = ZonaColors.TextPrimary.getColor(),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Dialed digits display
                        ODSRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            gap = 8.dp
                        ) {
                            ODSText(
                                text = dialedDigits.ifBlank { "Dial a number" },
                                style = ODSTextStyles.bodyL,
                                color = if (dialedDigits.isBlank()) ZonaColors.LavenderAlt else ZonaColors.TextPrimary
                            )
                            if (dialedDigits.isNotEmpty()) {
                                ODSBox(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clickable {
                                            dialedDigits = dialedDigits.dropLast(1)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    ODSIcon(
                                        iconModel = ODSIconModel(drawableRes = ODSR.drawable.remove_type_standard),
                                        tint = ZonaColors.LavenderAlt.getColor(),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        // Keypad grid 1-9, *, 0, #
                        val keys = listOf(
                            listOf("1", "2", "3"),
                            listOf("4", "5", "6"),
                            listOf("7", "8", "9"),
                            listOf("*", "0", "#")
                        )

                        keys.forEach { rowKeys ->
                            ODSRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                rowKeys.forEach { key ->
                                    ODSBox(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clickable { dialedDigits += key },
                                        cornerRadius = ODSCorners(all = 28.dp),
                                        background = listOf(ODSColorModel(hexColor = ZonaColors.SurfaceRaised)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        ODSText(
                                            text = key,
                                            style = ODSTextStyles.bodyL,
                                            color = ZonaColors.TextPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Control action button with circular container and text label below.
 */
@Composable
private fun AudioCallControlItem(
    iconRes: Int,
    label: String,
    isActive: Boolean,
    activeBg: HexColor,
    inactiveBg: HexColor,
    iconTint: HexColor,
    labelColor: HexColor,
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
            background = listOf(
                ODSColorModel(hexColor = if (isActive) activeBg else inactiveBg)
            ),
            contentAlignment = Alignment.Center
        ) {
            ODSIcon(
                iconModel = ODSIconModel(drawableRes = iconRes),
                tint = iconTint.getColor(),
                modifier = Modifier.size(26.dp)
            )
        }

        ODSText(
            text = label,
            style = ODSTextStyles.microcopyRegular,
            color = labelColor
        )
    }
}

/**
 * High-aesthetic Picture-in-Picture layout for floating window using 100% ODS components.
 */
@Composable
private fun VoiceCallPipLayout(
    modelName: String,
    statusText: String,
    formattedTime: String,
    callState: CallUiState,
    avatarUrl: String,
    onEndCall: () -> Unit,
    onToggleMute: () -> Unit
) {
    ODSBox(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        background = listOf(ODSColorModel(hexColor = ZonaColors.Background)),
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
                    color = ZonaColors.ActiveLime
                )
                ODSText(
                    text = modelName.ifBlank { "User" },
                    style = ODSTextStyles.bodyMBold,
                    color = ZonaColors.TextPrimary
                )
            }

            // Compact Avatar with neon lime ring
            ODSBox(
                modifier = Modifier.size(64.dp),
                cornerRadius = ODSCorners(all = 32.dp),
                border = ODSBorder(
                    width = 2.dp,
                    colorList = listOf(ODSColorModel(hexColor = ZonaColors.ActiveLime))
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
                        tint = ZonaColors.LavenderAlt.getColor(),
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
                            hexColor = if (callState.isMuted) ZonaColors.ActiveLime else ZonaColors.SurfaceRaised
                        )
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = if (callState.isMuted) ODSR.drawable.ic_mic_off else ODSR.drawable.ic_mic
                        ),
                        tint = if (callState.isMuted) ZonaColors.TextPrimary.getColor() else ZonaColors.TextPrimary.getColor(),
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Mini Hang Up
                ODSBox(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable(onClick = onEndCall),
                    cornerRadius = ODSCorners(all = 20.dp),
                    background = listOf(ODSColorModel(hexColor = ZonaColors.ActionPrimary)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_phone_off),
                        tint = ZonaColors.TextPrimary.getColor(),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
