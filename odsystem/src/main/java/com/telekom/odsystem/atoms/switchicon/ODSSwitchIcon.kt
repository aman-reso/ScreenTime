package com.telekom.odsystem.atoms.switchicon

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.foundations.DEFAULT_ANIMATION_DURATION
import com.telekom.odsystem.foundations.DEFAULT_FACTOR
import com.telekom.odsystem.foundations.DEFAULT_SCALE_DURATION
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.SCALE_FACTOR
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSSwitchIcon composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
@Suppress("LongMethod")
@Composable
fun ODSSwitchIcon(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSSwitchIconProps = ODSSwitchIconProps(),
) {
    val style = ODSSwitchIconStyle().getStyle(
        scheme = scheme,
        props = props,
        state = props.state
    )
    val scale by animateFloatAsState(
        targetValue = if (props.state == ODSActions.HOVERED && !props.disabled && !props.readOnly) {
            style.scaleFactor
                ?: SCALE_FACTOR
        } else {
            DEFAULT_FACTOR
        },
        animationSpec = tween(durationMillis = DEFAULT_SCALE_DURATION, easing = EaseInOut),
        label = ""
    )
    val containerWidth = style.width ?: 0.dp
    val handleWidth = style.handleWidth ?: 0.dp
    val handleContainerPaddingLeftPadding = style.handleContainerPadding?.left ?: 0.dp
    val handleContainerPaddingRightPadding = style.handleContainerPadding?.right ?: 0.dp
    val internalPadding = handleContainerPaddingLeftPadding + handleContainerPaddingRightPadding
    val handleOffset by animateDpAsState(
        animationSpec = tween(DEFAULT_ANIMATION_DURATION),
        targetValue = if (props.selected) containerWidth - handleWidth - internalPadding else 0.dp,
        label = ""
    )
    ODSBox(
        contentAlignment = style.contentAlignment,
        width = style.width,
        height = style.height,
        modifier = modifier
    ) {
        ODSBox(
            cornerRadius = style.strokeBorderRadius,
            clipContent = style.strokeClipContent != false,
            border = ODSBorder(width = style.strokeBorder, colorList = style.strokeBorderColor),
            background = style.strokeBackgroundColor,
            height = style.strokeHeight,
            modifier = Modifier
                .fillMaxWidth()
                .scale(scale)
                .align(alignment = style.strokeContentAlignment ?: Alignment.CenterStart)
        ) {
        }
        ODSColumn(
            verticalAlignment = style.verticalAlignment,
            verticalArrangement = style.verticalArrangement,
            width = style.width,
            height = style.height,
        ) {
            ODSRow(
                modifier = Modifier.offset(x = handleOffset),
                padding = style.handleContainerPadding,
                horizontalArrangement = style.handleContainerHorizontalArrangement,
                horizontalAlignment = style.handleContainerHorizontalAlignment,
                verticalAlignment = style.handleContainerVerticalAlignment
            ) {
                ODSBox(
                    cornerRadius = style.handleBorderRadius,
                    clipContent = style.handleClipContent != false,
                    background = style.handleBackgroundColor,
                    width = style.handleWidth,
                    height = style.handleHeight
                ) {
                }
            }
        }
    }
}
