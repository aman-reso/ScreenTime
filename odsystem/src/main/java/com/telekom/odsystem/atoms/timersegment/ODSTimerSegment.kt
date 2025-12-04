package com.telekom.odsystem.atoms.timersegment

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults.drawStopIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSTimerSegment composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param isRunning Parameter for customization.
 * @param segmentCompleted Parameter for customization.
 */
@Suppress("LongMethod")
@Composable
fun ODSTimerSegment(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSTimerSegmentProps = ODSTimerSegmentProps(),
    isRunning: Boolean = true,
    segmentCompleted: () -> Unit = {}
) {

    val indicatorProgress = 1f
    var progress by remember { mutableFloatStateOf(0f) }
    var pausedProgress by remember { mutableFloatStateOf(0f) }
    var remainingDuration by remember { mutableIntStateOf(props.duration) }
    var hasBeenCompleted by remember { mutableStateOf(false) }

    val progressAnimation by animateFloatAsState(
        targetValue = if (!isRunning && props.status == ODSTimerSegmentStatus.IN_PROGRESS) pausedProgress else progress,
        animationSpec = tween(
            durationMillis = if (props.status == ODSTimerSegmentStatus.IDLE) 0 else remainingDuration,
            easing = LinearEasing,
            delayMillis = 0
        ), label = ""
    )

    val style = ODSTimerSegmentStyle().getStyle(scheme = scheme)

    LinearProgressIndicator(
        progress = { progressAnimation },
        modifier = modifier
            .clearAndSetSemantics { }
            .clip(
                shape = style.borderRadius?.getRoundedCornerShape()
                    ?: ODSCorners(all = 0.dp).getRoundedCornerShape()
            )
            .height(style.progressHeight ?: 0.dp)
            .fillMaxWidth(),
        gapSize = 0.dp,
        drawStopIndicator = {
            drawStopIndicator(
                drawScope = this,
                stopSize = style.indicatorWidth ?: 0.dp,
                color = style.indicatorBackgroundColor?.get(0)?.hexColor?.getColor() ?: Color.Green,
                strokeCap = StrokeCap.Round,
            )
        },
        strokeCap = StrokeCap.Butt,
        color = style.progressBackgroundColor?.get(0)?.hexColor?.getColor() ?: Color.Green,
        trackColor = style.backgroundColor?.get(0)?.hexColor?.getColor() ?: Color.Green,
    )
    LaunchedEffect(key1 = isRunning, key2 = hasBeenCompleted, key3 = props.status) {
        when (props.status) {
            ODSTimerSegmentStatus.IDLE -> {
                progress = 0f
                remainingDuration = props.duration
                pausedProgress = 0f
                hasBeenCompleted = false
            }

            ODSTimerSegmentStatus.IN_PROGRESS -> {
                if (hasBeenCompleted) {
                    progress = 0f
                    remainingDuration = 0
                    hasBeenCompleted = false
                } else if (isRunning) {
                    progress = indicatorProgress
                    remainingDuration = props.duration
                } else {
                    pausedProgress = progressAnimation
                    remainingDuration = ((1 - progressAnimation) * props.duration).toInt()
                }
            }

            ODSTimerSegmentStatus.COMPLETE -> {
                progress = indicatorProgress + 1
                remainingDuration = 0
                hasBeenCompleted = true
                pausedProgress = 0f
            }
        }
    }

    DisposableEffect(progressAnimation) {
        if (progressAnimation >= 1f) {
            segmentCompleted()
        }
        onDispose { }
    }
}
