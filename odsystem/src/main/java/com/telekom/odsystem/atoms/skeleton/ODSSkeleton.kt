package com.telekom.odsystem.atoms.skeleton

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.delay

/**
 * ODSSkeleton composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Suppress("LongMethod")
@Composable
fun ODSSkeleton(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSSkeletonProps = ODSSkeletonProps()
) {
    val style = ODSSkeletonStyle().getStyle(scheme = scheme, props = props)
    val context = LocalContext.current
    ODSBox(
        modifier = modifier
            .sizeWithinBounds(
                minHeight = style.minHeight ?: MIN_HEIGHT.dp,
                maxHeight = style.maxHeight ?: Dp.Unspecified,
            )
            .semantics {
                contentDescription = context.getString(R.string.semantics_skeleton)
            },
        clipContent = style.clipContent != false,
        cornerRadius = style.borderRadius,
        background = style.backgroundColor,
        width = style.width,
        height = style.height
    ) {
        var count by remember { mutableIntStateOf(0) }
        LaunchedEffect(Unit) {
            while (true) {
                if (count != 0) {
                    delay(ANIMATION_DELAY_MS)
                }
                count = (count % NUMBER_OF_ANIMATION_STEPS) + 1
            }
        }
        val gradientAnimOffset by animateFloatAsState(
            targetValue = if (count % 2 != 0) -1f else 1f,
            animationSpec = tween(durationMillis = ANIMATION_DURATION_MS),
            label = ""
        )
        SharedTransitionLayout(
            modifier = Modifier
                .matchParentSize()
                .gradientLayout(offset = gradientAnimOffset)
        ) {
            AnimatedContent(
                targetState = count,
                modifier = Modifier.matchParentSize(),
                label = ""
            ) { target ->
                ODSBox(
                    modifier = Modifier
                        .fillMaxSize()
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(SHARED_CONTENT_KEY),
                            animatedVisibilityScope = this,
                        ),
                    background = shimmerBackground(target = target, style = style)
                ) {
                }
            }
        }
    }
}

/** Animation steps where the first gradient frame (Frame 1) is visible.*/
const val STEP_ONE = 1
const val STEP_TWO = 2
const val STEP_SEVEN = 7
const val STEP_EIGHT = 8

private const val NUMBER_OF_ANIMATION_STEPS = 8
private const val ANIMATION_DURATION_MS: Int = 1300
private const val ANIMATION_DELAY_MS: Long = 600 + 1300

/** The gradient's width is set to 5x the width of the parent composable,
this ensures the gradient extends beyond the visible bounds of the parent.*/
private const val GRADIENT_WIDTH_MULTIPLIER = 5
private const val SHARED_CONTENT_KEY = "bounds"

private fun Modifier.gradientLayout(offset: Float): Modifier {
    return layout { measurable, constraints ->
        val gradientWidth = constraints.maxWidth * GRADIENT_WIDTH_MULTIPLIER
        val placeable = measurable.measure(
            constraints.copy(
                minWidth = gradientWidth,
                maxWidth = gradientWidth
            )
        )
        layout(placeable.width, placeable.height) {
            placeable.place(
                -(((placeable.width - constraints.maxWidth) / 2) * offset).toInt(),
                0
            )
        }
    }
}

private fun shimmerBackground(target: Int, style: ODSSkeletonStyle): List<ODSColorModel>? {
    return when (target) {
        STEP_ONE, STEP_TWO, STEP_SEVEN, STEP_EIGHT -> style.frame1Background
        else -> style.frame2Background
    }
}
