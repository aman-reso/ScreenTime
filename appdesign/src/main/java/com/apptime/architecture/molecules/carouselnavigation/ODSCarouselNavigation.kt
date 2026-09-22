package com.telekom.odsystem.molecules.carouselnavigation

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.carouseldot.ODSCarouselDot
import com.telekom.odsystem.atoms.carouseldot.ODSCarouselDotProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

private const val MAX_VISIBLE_DOTS = 6

@Suppress("LongMethod")
/**
 * ODSCarouselNavigation composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onClickPreviousButton Callback triggered when action occurs.
 * @param onClickNextButton Callback triggered when action occurs.
 */
@Composable
fun ODSCarouselNavigation(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCarouselNavigationProps = ODSCarouselNavigationProps(),
    onClickPreviousButton: () -> Unit,
    onClickNextButton: () -> Unit,
) {
    val style = ODSCarouselNavigationStyle().getStyle(scheme = scheme)
    val density = LocalDensity.current
    val scrollState = rememberScrollState()
    var dotWidth by remember { mutableIntStateOf(0) }
    val visibleDotCount = MAX_VISIBLE_DOTS.coerceAtMost(props.dots)
    val gapDp = style.containerGap ?: 0.dp
    val gapPx = with(density) { gapDp.roundToPx() }

    // Calculate width for the visible portion of the dot list
    val rowWidthDp = remember(dotWidth, visibleDotCount) {
        with(density) {
            ((dotWidth * visibleDotCount) + (gapPx * (visibleDotCount - 1))).toDp()
        }
    }

    // Scroll to center the selected dot
    if (dotWidth > 0) {
        LaunchedEffect(props.selectedIndex) {
            val totalDotWidth = dotWidth + gapPx
            val centeredIndex = props.selectedIndex - (visibleDotCount / 2)
            val scrollOffset = totalDotWidth * centeredIndex.coerceAtLeast(0)
            if (props.selectedIndex == 0 || (props.selectedIndex == (props.dots - 1))) {
                scrollState.scrollTo(scrollOffset)
            } else {
                scrollState.animateScrollTo(scrollOffset)
            }
        }
    }

    // Render a hidden dot for measurement only if it was not measured before
    if (dotWidth == 0) {
        DotMeasurer(
            scheme = scheme,
            onDotMeasured = { width ->
                if (width > 0) dotWidth = width
            }
        )
    }

    ODSRow(
        modifier = modifier,
        gap = style.gap,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment
    ) {
        val context = LocalContext.current
        props.navigationLeftButtonProps?.toODSButtonProps(context)?.let {
            ODSButton(
                scheme = scheme,
                props = it,
                isPressed = {},
                onClick = onClickPreviousButton
            )
        }
        ODSRow(
            gap = style.containerGap,
            horizontalArrangement = style.containerHorizontalArrangement,
            horizontalAlignment = style.containerHorizontalAlignment,
            verticalAlignment = style.containerVerticalAlignment,
            modifier = Modifier
                .width(rowWidthDp)
                .horizontalScroll(scrollState, enabled = false)
        ) {
            for (i in 0 until props.dots) {
                ODSCarouselDot(
                    scheme = scheme,
                    props = ODSCarouselDotProps(selected = i == props.selectedIndex)
                )
            }
        }
        props.navigationRightButtonProps?.toODSButtonProps(context)?.let {
            ODSButton(
                scheme = scheme,
                props = it,
                isPressed = {},
                onClick = onClickNextButton
            )
        }
    }
}

@Composable
private fun DotMeasurer(
    scheme: ODSTheme,
    onDotMeasured: (Int) -> Unit,
) {
    ODSCarouselDot(
        scheme = scheme,
        props = ODSCarouselDotProps(selected = false),
        modifier = Modifier
            .alpha(0f)
            .onGloballyPositioned { coordinates ->
                onDotMeasured(coordinates.size.width)
            }
    )
}
