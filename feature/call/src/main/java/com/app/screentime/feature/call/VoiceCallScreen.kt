package com.app.screentime.feature.call

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.LocalActivity
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.VolumeDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.ui.components.PompiereTitle
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.cheddarSecondaryScheme
import kotlinx.coroutines.delay
import kotlin.math.sin
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun VoiceCallScreen(
    modelId: String,
    modelName: String,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    isInPipMode: Boolean = false,
    onEndCall: () -> Unit = {},
    onNavigateToTopUp: () -> Unit = {},
    viewModel: CallViewModel = hiltViewModel()
) {
    val callState by viewModel.callState.collectAsState()
    val configuration = LocalConfiguration.current
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
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val micGranted = result[Manifest.permission.RECORD_AUDIO] == true ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        hasMicPermission = micGranted
        if (micGranted) {
            viewModel.startOutgoingCall(modelId, modelName)
        }
    }

    LaunchedEffect(modelId) {
        if (hasMicPermission) {
            viewModel.startOutgoingCall(modelId, modelName)
        } else {
            permissionLauncher.launch(permissionsToRequest)
        }
    }

    LaunchedEffect(callState.status) {
        if (callState.status == CallStatus.ENDED) {
            delay(2500L.milliseconds)
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

                PompiereTitle(
                    text = "Call Ended",
                    scheme = scheme,
                    style = ODSTextStyles.pompiereDisplay
                )

                ODSText(
                    text = callState.endReason ?: "Call with $modelName has ended.",
                    style = ODSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive
                )

                Spacer(Modifier.height(16.dp))

                ODSButton(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = "Close",
                        variant = ODSButtonVariant.PRIMARY
                    ),
                    onClick = onEndCall
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

                PompiereTitle(
                    text = "Insufficient Balance",
                    scheme = scheme,
                    style = ODSTextStyles.pompiereDisplay
                )

                ODSText(
                    text = callState.balanceMessage.ifBlank {
                        "You need at least ₹${callState.minRequiredBalance.toInt()} coins for a 1-minute voice call with $modelName. Current balance: ₹${callState.currentBalance.toInt()}."
                    },
                    style = ODSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive
                )

                Spacer(Modifier.height(16.dp))

                ODSRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSButton(
                        modifier = Modifier.weight(1f),
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Cancel",
                            variant = ODSButtonVariant.SECONDARY,
                        ),
                        onClick = onEndCall
                    )

                    ODSButton(
                        modifier = Modifier.weight(1f),
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Top Up Wallet",
                            variant = ODSButtonVariant.PRIMARY
                        ),
                        onClick = onNavigateToTopUp
                    )
                }
            }
        }
        return
    }

    val minutes = callState.durationSec / 60
    val seconds = callState.durationSec % 60
    val formattedTime = "%02d:%02d".format(minutes, seconds)
    val statusText = when (callState.status) {
        CallStatus.CHECKING_BALANCE -> "CHECKING BALANCE…"
        CallStatus.DIALING -> "CONNECTING…"
        CallStatus.ACTIVE -> "VOICE CALL"
        CallStatus.INCOMING -> "INCOMING CALL"
        CallStatus.ENDED -> "CALL ENDED"
        else -> "CALLING…"
    }

    if (isActuallyInPip) {
        VoiceCallPipLayout(
            modelName = modelName,
            statusText = statusText,
            formattedTime = formattedTime,
            callState = callState,
            scheme = scheme,
            onEndCall = {
                viewModel.endCall("Ended from PiP")
                onEndCall()
            },
            onToggleMute = { viewModel.toggleMute() }
        )
        return
    }

    if (!hasMicPermission) {
        // Permission Explanation Screen
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
                        iconModel = ODSIconModel(imageVector = Icons.Filled.Mic),
                        tint = scheme.basicAccent.getColor()
                    )
                }

                PompiereTitle(
                    text = "Microphone Access Required",
                    scheme = scheme,
                    style = ODSTextStyles.pompiereDisplay
                )

                ODSText(
                    text = "Connect needs microphone permission to transmit your voice during calls.",
                    style = ODSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive
                )

                Spacer(Modifier.height(16.dp))

                ODSRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSButton(
                        modifier = Modifier.weight(1f),
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Cancel",
                            variant = ODSButtonVariant.SECONDARY,
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

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val isAdaptive = maxWidth >= 600.dp || configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        ODSBox(
            modifier = Modifier.fillMaxSize(),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackground)),
            contentAlignment = Alignment.Center
        ) {
            if (isAdaptive) {
                // ── Adaptive 2-Part Split Layout ──
                ODSRow(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // LEFT SIDE: Waveform & Avatar (50% Width)
                    ODSBox(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            gap = 16.dp
                        ) {
                            AudioWaveformVisualizer(
                                modelName = modelName,
                                scheme = scheme,
                                isActive = callState.status == CallStatus.ACTIVE
                            )

                            // Cost Badge
                            ODSRow(
                                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                                cornerRadius = ODSCorners(all = 16.dp),
                                padding = ODSPadding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                gap = 6.dp
                            ) {
                                ODSIcon(
                                    iconModel = ODSIconModel(imageVector = Icons.Outlined.LocalActivity),
                                    tint = scheme.basicAccent.getColor()
                                )
                                ODSText(
                                    text = "${callState.cost.toInt()} coins spent · 10/min",
                                    style = ODSTextStyles.microcopyRegular,
                                    color = scheme.basicTextRecessive
                                )
                            }
                        }
                    }

                    // RIGHT SIDE: Details, Name, Timing, Action Buttons (50% Width)
                    ODSBox(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            gap = 20.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        ) {
                            ODSColumn(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                gap = 6.dp
                            ) {
                                ODSText(
                                    text = statusText,
                                    style = ODSTextStyles.microcopyBold,
                                    color = scheme.basicAccent
                                )
                                PompiereTitle(
                                    text = modelName,
                                    scheme = scheme,
                                    style = ODSTextStyles.pompiereDisplayL
                                )
                                ODSText(
                                    text = formattedTime,
                                    style = ODSTextStyles.bodyLBold,
                                    color = scheme.basicText
                                )
                            }

                            Spacer(Modifier.height(12.dp))

                            // Action Controls
                            ODSRow(
                                horizontalArrangement = Arrangement.spacedBy(24.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CallControlButton(
                                    icon = if (callState.isMuted) Icons.Filled.MicOff else Icons.Outlined.Mic,
                                    label = if (callState.isMuted) "Unmute" else "Mute",
                                    isActive = callState.isMuted,
                                    scheme = scheme,
                                    size = 56.dp
                                ) { viewModel.toggleMute() }

                                ODSBox(
                                    modifier = Modifier
                                        .size(76.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            viewModel.endCall()
                                            onEndCall()
                                        },
                                    background = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ODSIcon(
                                        iconModel = ODSIconModel(imageVector = Icons.Filled.CallEnd),
                                        tint = scheme.basicTextOnAccent.getColor()
                                    )
                                }

                                CallControlButton(
                                    icon = if (callState.isSpeaker) Icons.Filled.VolumeUp else Icons.Outlined.VolumeDown,
                                    label = if (callState.isSpeaker) "Speaker" else "Earpiece",
                                    isActive = callState.isSpeaker,
                                    scheme = scheme,
                                    size = 56.dp
                                ) { viewModel.toggleSpeaker() }
                            }
                        }
                    }
                }
            } else {
                // ── Portrait Standard Layout ──
                ODSColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                ) {
                    Spacer(Modifier.height(28.dp))

                    ODSText(
                        text = statusText,
                        style = ODSTextStyles.microcopyBold,
                        color = scheme.basicAccent
                    )
                    Spacer(Modifier.height(8.dp))
                    PompiereTitle(
                        text = modelName,
                        scheme = scheme,
                        style = ODSTextStyles.pompiereDisplay
                    )
                    Spacer(Modifier.height(4.dp))
                    ODSText(
                        text = formattedTime,
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )

                    Spacer(Modifier.weight(1f))

                    // Waveform with Avatar in Center
                    AudioWaveformVisualizer(
                        modelName = modelName,
                        scheme = scheme,
                        isActive = callState.status == CallStatus.ACTIVE
                    )

                    Spacer(Modifier.height(24.dp))

                    // Cost Badge & Low Balance Warning
                    ODSColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        gap = 8.dp
                    ) {
                        ODSRow(
                            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                            cornerRadius = ODSCorners(all = 16.dp),
                            padding = ODSPadding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            gap = 6.dp
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(imageVector = Icons.Outlined.LocalActivity),
                                tint = scheme.basicAccent.getColor()
                            )
                            ODSText(
                                text = "${"%.2f".format(callState.cost)} coins spent · ${callState.ratePerMin.toInt()}/min",
                                style = ODSTextStyles.microcopyRegular,
                                color = scheme.basicTextRecessive
                            )
                        }

                        if (callState.isLowBalanceWarning) {
                            ODSRow(
                                background = listOf(ODSColorModel(hexColor = cheddarSecondaryScheme.basicBackgroundSubtle)),
                                cornerRadius = ODSCorners(all = 12.dp),
                                padding = ODSPadding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ODSText(
                                    text = "⚠️ Low balance: ${callState.remainingSec}s remaining",
                                    style = ODSTextStyles.microcopyBold,
                                    color = scheme.basicAccent
                                )
                            }
                        }
                    }

                    Spacer(Modifier.weight(1f))

                    // Action Controls Row
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 36.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CallControlButton(
                            icon = if (callState.isMuted) Icons.Filled.MicOff else Icons.Outlined.Mic,
                            label = if (callState.isMuted) "Unmute" else "Mute",
                            isActive = callState.isMuted,
                            scheme = scheme,
                            size = 60.dp
                        ) { viewModel.toggleMute() }

                        ODSBox(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .clickable {
                                    viewModel.endCall()
                                    onEndCall()
                                },
                            background = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard)),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(imageVector = Icons.Filled.CallEnd),
                                tint = scheme.basicTextOnAccent.getColor()
                            )
                        }

                        CallControlButton(
                            icon = if (callState.isSpeaker) Icons.Filled.VolumeUp else Icons.Outlined.VolumeDown,
                            label = if (callState.isSpeaker) "Speaker" else "Earpiece",
                            isActive = callState.isSpeaker,
                            scheme = scheme,
                            size = 60.dp
                        ) { viewModel.toggleSpeaker() }
                    }
                }
            }
        }
    }
}

/**
 * Dynamic Audio Waveform & Equalizer Visualizer around the model avatar.
 */
@Composable
private fun AudioWaveformVisualizer(
    modelName: String,
    scheme: ODSTheme,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "audioWaveform")

    // Concentric acoustic pulse waves
    val wavePhase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wavePhase"
    )

    val equalizerPhase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28318f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "equalizerPhase"
    )

    val accentColor = scheme.basicAccent.getColor()
    val peachColor = cheddarSecondaryScheme.basicBackgroundSubtle.getColor()

    ODSColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        gap = 18.dp
    ) {
        // Avatar with Acoustic Ripple Rings
        ODSBox(
            modifier = Modifier.size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val baseRadius = size.width * 0.35f

                if (isActive) {
                    // 3 animated expanding wave rings
                    for (i in 0..2) {
                        val phase = (wavePhase + i * 0.33f) % 1f
                        val radius = baseRadius + phase * (size.width * 0.22f)
                        val alpha = (1f - phase) * 0.45f

                        drawCircle(
                            color = accentColor.copy(alpha = alpha),
                            radius = radius,
                            center = center,
                            style = Stroke(width = 3.dp.toPx())
                        )
                    }
                }
            }

            // Inner Pulsing Avatar Halo
            ODSBox(
                modifier = Modifier
                    .size(136.dp)
                    .clip(CircleShape),
                background = listOf(ODSColorModel(hexColor = cheddarSecondaryScheme.basicBackgroundSubtle)),
                contentAlignment = Alignment.Center
            ) {
                ODSText(
                    text = modelName.firstOrNull()?.toString() ?: "M",
                    style = ODSTextStyles.pompiereDisplayL,
                    color = scheme.basicText
                )
            }
        }

        // Dynamic Frequency Equalizer Waveform Bars
        Canvas(
            modifier = Modifier
                .width(180.dp)
                .height(28.dp)
        ) {
            val barCount = 18
            val totalWidth = size.width
            val barWidth = 4.dp.toPx()
            val spacing = (totalWidth - (barCount * barWidth)) / (barCount - 1)
            val maxHeight = size.height

            for (i in 0 until barCount) {
                val barProgress = if (isActive) {
                    val sine = sin(equalizerPhase + i * 0.45f)
                    val base = 0.25f + 0.65f * ((sine + 1f) / 2f)
                    base
                } else 0.2f

                val barHeight = (maxHeight * barProgress).coerceAtLeast(6.dp.toPx())
                val x = i * (barWidth + spacing)
                val y = (maxHeight - barHeight) / 2f

                drawRoundRect(
                    color = if (isActive) accentColor else scheme.basicStrokeSubtle.getColor(),
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )
            }
        }
    }
}

@Composable
private fun CallControlButton(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    scheme: ODSTheme,
    size: Dp = 60.dp,
    onClick: () -> Unit
) {
    ODSColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        gap = 6.dp
    ) {
        ODSBox(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .clickable(onClick = onClick),
            background = listOf(
                ODSColorModel(
                    hexColor = if (isActive) scheme.basicAccent else scheme.basicBackgroundCard
                )
            ),
            contentAlignment = Alignment.Center
        ) {
            ODSIcon(
                iconModel = ODSIconModel(imageVector = icon),
                tint = if (isActive) scheme.basicTextOnAccent.getColor() else scheme.basicText.getColor()
            )
        }
        ODSText(
            text = label,
            style = ODSTextStyles.microcopyRegular,
            color = scheme.basicTextRecessive
        )
    }
}

/**
 * Compact, high-aesthetic Picture-in-Picture layout for floating window.
 */
@Composable
private fun VoiceCallPipLayout(
    modelName: String,
    statusText: String,
    formattedTime: String,
    callState: CallUiState,
    scheme: ODSTheme,
    onEndCall: () -> Unit,
    onToggleMute: () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "pipWave")
    val wavePhase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pipWavePhase"
    )

    val accentColor = scheme.basicAccent.getColor()

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
                    text = modelName,
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicText
                )
            }

            // Compact Avatar with pulsing wave ring
            ODSBox(
                modifier = Modifier.size(72.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val baseRadius = size.width * 0.35f
                    if (callState.status == CallStatus.ACTIVE) {
                        val radius = baseRadius + wavePhase * (size.width * 0.15f)
                        val alpha = (1f - wavePhase) * 0.6f
                        drawCircle(
                            color = accentColor.copy(alpha = alpha),
                            radius = radius,
                            center = center,
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }
                }

                ODSBox(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape),
                    background = listOf(ODSColorModel(hexColor = cheddarSecondaryScheme.basicBackgroundSubtle)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = modelName.firstOrNull()?.toString() ?: "M",
                        style = ODSTextStyles.bodyLBold,
                        color = scheme.basicText
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
                        .clip(CircleShape)
                        .clickable(onClick = onToggleMute),
                    background = listOf(
                        ODSColorModel(
                            hexColor = if (callState.isMuted) scheme.basicAccent else scheme.basicBackgroundCard
                        )
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            imageVector = if (callState.isMuted) Icons.Filled.MicOff else Icons.Outlined.Mic
                        ),
                        tint = if (callState.isMuted) scheme.basicTextOnAccent.getColor() else scheme.basicText.getColor()
                    )
                }

                // Mini Hang Up
                ODSBox(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onEndCall),
                    background = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(imageVector = Icons.Filled.CallEnd),
                        tint = scheme.basicTextOnAccent.getColor()
                    )
                }
            }
        }
    }
}


