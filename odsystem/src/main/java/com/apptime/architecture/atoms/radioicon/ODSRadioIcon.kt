package com.telekom.odsystem.atoms.radioicon

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.foundations.DEFAULT_ANIMATION_DURATION
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSRadioIcon composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
@Composable
fun ODSRadioIcon(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSRadioIconProps = ODSRadioIconProps(),
) {
    val style = ODSRadioIconStyle().getStyle(
        scheme = scheme,
        props = props,
        state = props.state
    )

    ODSRadioIconContainer(
        modifier = modifier,
        style = style,
        props = props,
    )
}

@Composable
private fun ODSRadioIconContainer(
    modifier: Modifier,
    style: ODSRadioIconStyle,
    props: ODSRadioIconProps = ODSRadioIconProps(),
) {
    val topPadding by animateDpAsState(style.padding?.top ?: 0.dp, label = "")
    val bottomPadding by animateDpAsState(style.padding?.bottom ?: 0.dp, label = "")
    val leftPadding by animateDpAsState(style.padding?.left ?: 0.dp, label = "")
    val rightPadding by animateDpAsState(style.padding?.right ?: 0.dp, label = "")
    ODSRow(
        modifier = modifier,
        clipContent = style.clipContent != false,
        width = style.width,
        height = style.height,
        padding = ODSPadding(
            top = topPadding,
            bottom = bottomPadding,
            left = leftPadding,
            right = rightPadding
        ),
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        horizontalArrangement = style.horizontalArrangement
    ) {
        ODSCustomRadioIcon(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            style = style,
            props = props
        )
    }
}

@Composable
private fun ODSCustomRadioIcon(
    modifier: Modifier,
    style: ODSRadioIconStyle,
    props: ODSRadioIconProps
) {
    ODSRow(
        modifier = modifier,
        cornerRadius = style.iconBorderRadius,
        border = ODSBorder(width = style.iconBorder, colorList = style.iconBorderColor),
        horizontalAlignment = style.iconHorizontalAlignment,
        verticalAlignment = style.iconVerticalAlignment,
        horizontalArrangement = style.iconHorizontalArrangement,
        background = style.iconBackgroundColor,
    ) {
        val width by animateDpAsState(style.innerCircleWidth ?: 0.dp, label = "")
        val height by animateDpAsState(style.innerCircleHeight ?: 0.dp, label = "")
        AnimatedVisibility(
            visible = props.selected,
            enter = scaleIn(initialScale = 0f, animationSpec = tween(DEFAULT_ANIMATION_DURATION)),
            exit = scaleOut(targetScale = 0f, animationSpec = tween(DEFAULT_ANIMATION_DURATION))
        ) {
            ODSBox(
                cornerRadius = style.innerCircleBorderRadius,
                background = style.innerCircleBackgroundColor,
                width = width,
                height = height,
                clipContent = style.innerCircleClipContent != false
            ) {
            }
        }
    }
}
