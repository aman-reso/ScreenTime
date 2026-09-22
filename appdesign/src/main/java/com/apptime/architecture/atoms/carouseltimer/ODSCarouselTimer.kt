package com.telekom.odsystem.atoms.carouseltimer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.timersegment.ODSTimerSegment
import com.telekom.odsystem.atoms.timersegment.ODSTimerSegmentProps
import com.telekom.odsystem.atoms.timersegment.ODSTimerSegmentStatus
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSCarouselTimer composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param isInProgressElementIndex Parameter for customization.
 * @param isRunning Parameter for customization.
 * @param segmentCompleted Parameter for customization.
 */
@Composable
fun ODSCarouselTimer(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCarouselTimerProps = ODSCarouselTimerProps(),
    isInProgressElementIndex: Int = 0,
    isRunning: Boolean = true,
    segmentCompleted: (Int) -> Unit = {}
) {
    var currentSegment by remember { mutableIntStateOf(0) }
    currentSegment = isInProgressElementIndex

    val style = ODSCarouselTimerStyle().getStyle()
    ODSCarouselTimerContainer(
        modifier = modifier,
        scheme = scheme,
        props = props,
        style = style,
        currentSegment = currentSegment,
        onSegmentCompleted = {
            segmentCompleted(it)
        },
        isRunning = isRunning
    )
}

@Composable
fun ODSCarouselTimerContainer(
    modifier: Modifier = Modifier,
    scheme: ODSTheme,
    props: ODSCarouselTimerProps,
    style: ODSCarouselTimerStyle,
    currentSegment: Int,
    onSegmentCompleted: (Int) -> Unit,
    isRunning: Boolean
) {
    ODSRow(
        modifier = modifier.fillMaxWidth(),
        gap = style.gap,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
    ) {
        for (i in 0 until props.segmentsDuration.size) {
            val status = if (i == currentSegment) {
                ODSTimerSegmentStatus.IN_PROGRESS
            } else if (i < currentSegment) {
                ODSTimerSegmentStatus.COMPLETE
            } else {
                ODSTimerSegmentStatus.IDLE
            }

            val timerSegmentProps = ODSTimerSegmentProps(
                duration = props.segmentsDuration[i].duration,
                status = status
            )

            ODSTimerSegment(
                modifier = Modifier.weight(1f),
                scheme = scheme,
                props = timerSegmentProps,
                isRunning = isRunning,
                segmentCompleted = {
                    if (i == currentSegment) {
                        onSegmentCompleted(i)
                    }
                }
            )
        }
    }
}
