package com.app.screentime.feature.call

import android.content.res.Configuration
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.LocalActivity
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.VolumeDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.ui.components.PompiereTitle
import com.telekom.odsystem.atoms.*
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.*
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.cheddarSecondaryScheme
import kotlin.math.sin

@Composable
fun VoiceCallScreen(
    modelId: String,
    modelName: String,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    onEndCall: () -> Unit = {},
    viewModel: CallViewModel = hiltViewModel()
) {
    val callState by viewModel.callState.collectAsState()
    val configuration = LocalConfiguration.current

    LaunchedEffect(modelId) {
        viewModel.startOutgoingCall(modelId, modelName)
    }

    LaunchedEffect(callState.status) {
        if (callState.status == CallStatus.ENDED) {
            onEndCall()
        }
    }

    val minutes = callState.durationSec / 60
    val seconds = callState.durationSec % 60
    val formattedTime = "%02d:%02d".format(minutes, seconds)
    val statusText = when (callState.status) {
        CallStatus.DIALING -> "CONNECTING…"
        CallStatus.ACTIVE -> "VOICE CALL"
        CallStatus.INCOMING -> "INCOMING CALL"
        CallStatus.ENDED -> "CALL ENDED"
        else -> "CALLING…"
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

                    // Cost Badge
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
                            text = "${callState.cost.toInt()} coins spent · 10/min",
                            style = ODSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
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

