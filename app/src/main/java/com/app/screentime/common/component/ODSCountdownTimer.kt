package com.app.screentime.common.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.delay

/**
 * Display format for the countdown timer
 */
enum class ODSCountdownTimerFormat {
    /** Display as seconds only (e.g., "60", "45", "30") */
    SECONDS_ONLY,

    /** Display as MM:SS format (e.g., "05:00", "01:30") */
    MM_SS,

    /** Display as HH:MM:SS format (e.g., "01:05:00") */
    HH_MM_SS
}

/**
 * Visual style for the countdown timer
 */
enum class ODSCountdownTimerStyle {
    /** Circular progress indicator with text in center */
    CIRCULAR,

    /** Linear progress indicator with text below */
    LINEAR,

    /** Text only, no progress indicator */
    TEXT_ONLY
}

/**
 * Properties for configuring the countdown timer
 */
data class ODSCountdownTimerProps(
    /** Total duration in seconds */
    val totalSeconds: Int = 60,

    /** Display format */
    val format: ODSCountdownTimerFormat = ODSCountdownTimerFormat.MM_SS,

    /** Visual style */
    val style: ODSCountdownTimerStyle = ODSCountdownTimerStyle.CIRCULAR,

    /** Size of circular progress indicator (only for CIRCULAR style) */
    val circularSize: androidx.compose.ui.unit.Dp = DSVariables.sizingComponent13,

    /** Stroke width for circular progress (only for CIRCULAR style) */
    val circularStrokeWidth: androidx.compose.ui.unit.Dp = 3.dp,

    /** Height for linear progress (only for LINEAR style) */
    val linearHeight: androidx.compose.ui.unit.Dp = 4.dp,

    /** Show progress indicator */
    val showProgress: Boolean = true,

    /** Custom text style for the countdown text */
    val textStyle: ODSTextStyle? = null,

    /** Custom text color */
    val textColor: HexColor? = null,

    /** Custom progress color */
    val progressColor: HexColor? = null,

    /** Custom track color for progress indicator */
    val trackColor: HexColor? = null,

    /** Label text to display above the timer (optional) */
    val label: String? = null,

    /** Callback when countdown reaches zero */
    val onCountdownComplete: () -> Unit = {}
)

/**
 * ODS Countdown Timer Component
 *
 * A reusable countdown timer component that displays time remaining with optional progress indicators.
 * Supports multiple display formats and visual styles.
 *
 * @param modifier Modifier applied to this component
 * @param scheme ODS theme scheme
 * @param props Configuration properties
 * @param isRunning Whether the countdown is currently running
 * @param remainingSeconds Current remaining seconds (controlled externally)
 */
@Composable
fun ODSCountdownTimer(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCountdownTimerProps = ODSCountdownTimerProps(),
    isRunning: Boolean = true,
    remainingSeconds: Int = props.totalSeconds
) {
    val currentSeconds = remember { mutableIntStateOf(remainingSeconds) }
    currentSeconds.intValue = remainingSeconds

    val progress = if (props.totalSeconds > 0) {
        (remainingSeconds.toFloat() / props.totalSeconds.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500),
        label = "Countdown Progress"
    )

    val displayText = formatTime(remainingSeconds, props.format)

    val textStyle = props.textStyle ?: DSTextStyles.bodyMBold
    val textColor = props.textColor ?: scheme.basicText
    val progressColor =
        props.progressColor?.getColor() ?: scheme.functionalSuccessStandard.getColor()
    val trackColor = props.trackColor?.getColor()
        ?: scheme.basicTextRecessive.getColor().copy(alpha = 0.3f)

    ODSColumn(
        modifier = modifier,
        gap = DSVariables.spacingComponent2,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Label (if provided)
        props.label?.let { label ->
            ODSText(
                text = label,
                style = DSTextStyles.bodyMRegular,
                color = scheme.basicTextRecessive
            )
        }

        when (props.style) {
            ODSCountdownTimerStyle.CIRCULAR -> {
                if (props.showProgress) {
                    ODSBox(
                        modifier = Modifier.size(props.circularSize),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier.matchParentSize(),
                            color = progressColor,
                            strokeWidth = props.circularStrokeWidth,
                            trackColor = trackColor
                        )
                        ODSText(
                            text = displayText,
                            style = textStyle,
                            color = textColor
                        )
                    }
                } else {
                    ODSText(
                        text = displayText,
                        style = textStyle,
                        color = textColor
                    )
                }
            }

            ODSCountdownTimerStyle.LINEAR -> {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = DSVariables.spacingComponent2,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (props.showProgress) {
                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(props.linearHeight),
                            color = progressColor,
                            trackColor = trackColor
                        )
                    }
                    ODSText(
                        text = displayText,
                        style = textStyle,
                        color = textColor
                    )
                }
            }

            ODSCountdownTimerStyle.TEXT_ONLY -> {
                ODSText(
                    text = displayText,
                    style = textStyle,
                    color = textColor
                )
            }
        }
    }

    // Handle countdown completion
    LaunchedEffect(remainingSeconds) {
        if (remainingSeconds <= 0 && isRunning) {
            props.onCountdownComplete()
        }
    }
}

/**
 * Formats seconds into the specified format
 */
private fun formatTime(seconds: Int, format: ODSCountdownTimerFormat): String {
    return when (format) {
        ODSCountdownTimerFormat.SECONDS_ONLY -> {
            seconds.toString()
        }

        ODSCountdownTimerFormat.MM_SS -> {
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60
            String.format("%02d:%02d", minutes, remainingSeconds)
        }

        ODSCountdownTimerFormat.HH_MM_SS -> {
            val hours = seconds / 3600
            val minutes = (seconds % 3600) / 60
            val remainingSeconds = seconds % 60
            String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
        }
    }
}

/**
 * Hook to manage countdown timer state
 *
 * @param totalSeconds Total duration in seconds
 * @param isRunning Whether the countdown should run
 * @param onComplete Callback when countdown reaches zero
 * @return Pair of (remainingSeconds, resetCountdown function)
 */
@Composable
fun useCountdownTimer(
    totalSeconds: Int,
    isRunning: Boolean = true,
    onComplete: () -> Unit = {}
): Pair<Int, () -> Int> {
    var remainingSeconds by remember { mutableIntStateOf(totalSeconds) }
    var resetKey by remember { mutableIntStateOf(0) }

    // Reset when resetKey changes
    LaunchedEffect(resetKey) {
        remainingSeconds = totalSeconds
    }

    LaunchedEffect(isRunning, remainingSeconds) {
        if (isRunning && remainingSeconds > 0) {
            delay(1000)
            remainingSeconds = (remainingSeconds - 1).coerceAtLeast(0)
            if (remainingSeconds == 0) {
                onComplete()
            }
        }
    }

    val resetCountdown = remember {
        {
            resetKey++
        }
    }

    return Pair(remainingSeconds, resetCountdown)
}

