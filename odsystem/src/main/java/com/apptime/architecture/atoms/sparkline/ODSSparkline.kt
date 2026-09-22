package com.telekom.odsystem.atoms.sparkline

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
/**
 * ODSSparkline composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
@Composable
fun ODSSparkline(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSSparklineProps = ODSSparklineProps()
) {
    val context = LocalContext.current
    val style = ODSSparklineStyle().getStyle(scheme = scheme, props = props)
    var width by remember { mutableIntStateOf(0) }
    ODSRow(
        modifier = modifier.applySemantics(context = context, props = props),
        gap = style.gap,
        height = style.height,
        width = style.width,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
    ) {
        if (props.type == ODSSparklineType.BARS) {
            for (i in 0 until MAX_LIST_ITEMS) {
                ODSBox(
                    cornerRadius = style.borderRadius,
                    clipContent = style.barClipContent != false,
                    background = if (getBarProgress(props) > i) style.progressIndicatorBackgroundColor else style.dataProgressTrackBackgroundColor,
                    width = style.barWidth,
                    height = style.barHeight?.get(i),
                    padding = if (i == 0 || i == MAX_LIST_ITEMS - 1) style.barPadding else null,
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    if (i == 0 || i == MAX_LIST_ITEMS - 1) {
                        ODSDotIndicator(style)
                    }
                }
            }
        }

        if (props.type == ODSSparklineType.PROGRESS_BAR) {
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        width = it.size.width
                    },
                cornerRadius = style.borderRadius,
                clipContent = style.clipContent != false,
                background = style.dataProgressTrackBackgroundColor,
                height = style.progressIndicatorHeight,
                contentAlignment = Alignment.CenterStart,
            ) {
                val density = LocalDensity.current // Get Density object
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = style.dotHorizontalArrangement,
                    padding = style.dotHorizontalPadding
                ) {
                    ODSDotIndicator(style = style)
                    ODSDotIndicator(style = style)
                }
                ODSBox(
                    background = style.progressIndicatorBackgroundColor,
                    height = style.progressIndicatorHeight,
                    width = with(density) { (width * getProgressBarProgress(props)).toDp() }
                ) {
                }
            }
        }
    }
}

@Composable
private fun ODSDotIndicator(style: ODSSparklineStyle) {
    ODSBox(
        cornerRadius = style.dotIndicatorBorderRadius,
        background = style.dotIndicatorBackgroundColor,
        width = style.dotIndicatorWidth,
        clipContent = style.dotIndicatorClipContent != false,
        height = style.dotIndicatorHeight
    ) {
    }
}

@Suppress("MagicNumber")
private fun Modifier.applySemantics(context: Context, props: ODSSparklineProps): Modifier {
    return this.semantics {
        this.contentDescription = context.getString(
            R.string.percent_progress, "${(getProgressBarProgress(props = props) * 100).toInt()}"
        )
    }
}

@Suppress("MagicNumber")
private fun getBarProgress(props: ODSSparklineProps): Int {
    val percentage = props.percentage
    return when {
        percentage <= FIRST_BOUNDARY -> 0
        percentage <= SECOND_BOUNDARY -> 2
        percentage <= THIRD_BOUNDARY -> 4
        percentage <= FOURTH_BOUNDARY -> 6
        else -> 8
    }
}

@Suppress("MagicNumber")
private fun getProgressBarProgress(props: ODSSparklineProps): Float {
    val percentage = props.percentage
    return when {
        percentage <= FIRST_BOUNDARY -> 0.0f
        percentage <= SECOND_BOUNDARY -> 0.25f
        percentage <= THIRD_BOUNDARY -> 0.50f
        percentage <= FOURTH_BOUNDARY -> 0.75f
        else -> 1.0f
    }
}

private const val MAX_LIST_ITEMS = 8
private const val FIRST_BOUNDARY = 1.0
private const val SECOND_BOUNDARY = 25.0
private const val THIRD_BOUNDARY = 50.0
private const val FOURTH_BOUNDARY = 75.0
