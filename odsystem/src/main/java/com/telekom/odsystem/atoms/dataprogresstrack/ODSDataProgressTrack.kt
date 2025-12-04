package com.telekom.odsystem.atoms.dataprogresstrack

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.foundations.DEFAULT_ANIMATION_DURATION
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
/**
 * ODSDataProgressTrack composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
fun ODSDataProgressTrack(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSDataProgressTrackProps = ODSDataProgressTrackProps()
) {
    var progress by remember { mutableFloatStateOf(0f) }
    val progressAnimDuration = DEFAULT_ANIMATION_DURATION
    val progressAnimation by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(progressAnimDuration, easing = FastOutSlowInEasing), label = ""
    )

    LaunchedEffect(props.progress) {
        progress = props.progress ?: 0f
    }

    val style = ODSDataProgressTrackStyle().getStyle(scheme = scheme, props = props)

    var width by remember { mutableIntStateOf(0) }
    ODSBox(
        modifier = modifier.onGloballyPositioned {
            width = it.size.width
        },
        clipContent = style.clipContent != false,
        cornerRadius = style.borderRadius,
        background = style.backgroundColor,
        contentAlignment = Alignment.CenterStart,
        height = style.height,
    ) {
        val density = LocalDensity.current
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = style.dotHorizontalArrangement,
            padding = style.dotHorizontalPadding
        ) {
            ODSStartIndicator(style = style)
            ODSEndIndicator(style = style)
        }
        if (props.mode != ODSDataProgressTrackMode.DISABLED) {
            ODSBox(
                background = style.progressBackgroundColor,
                height = style.progressHeight,
                width = with(density) { (width * (progressAnimation)).toDp() }
            ) {
            }
        }
    }
}

@Composable
private fun ODSStartIndicator(style: ODSDataProgressTrackStyle) {
    ODSBox(
        cornerRadius = style.indicatorStartBorderRadius,
        background = style.indicatorStartBackgroundColor,
        width = style.indicatorStartWidth,
        clipContent = style.indicatorStartClipContent != false,
        height = style.indicatorStartHeight,
    ) {
    }
}

@Composable
private fun ODSEndIndicator(style: ODSDataProgressTrackStyle) {
    ODSBox(
        cornerRadius = style.indicatorEndBorderRadius,
        background = style.indicatorEndBackgroundColor,
        width = style.indicatorEndWidth,
        clipContent = style.indicatorEndClipContent != false,
        height = style.indicatorEndHeight,
    ) {
    }
}
