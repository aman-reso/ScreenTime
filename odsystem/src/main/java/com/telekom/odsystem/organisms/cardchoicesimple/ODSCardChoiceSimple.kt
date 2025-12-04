package com.telekom.odsystem.organisms.cardchoicesimple

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.radioicon.ODSRadioIcon
import com.telekom.odsystem.atoms.radioicon.ODSRadioIconProps
import com.telekom.odsystem.atoms.radioicon.ODSRadioIconSize
import com.telekom.odsystem.foundations.DEFAULT_FACTOR
import com.telekom.odsystem.foundations.DEFAULT_SCALE_DURATION
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.SCALE_FACTOR
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSCardChoiceSimple composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param bottomContentSlot Parameter for customization.
 * @param rightContentSlot Parameter for customization.
 * @param onClick Callback triggered when action occurs.
 */
@Composable
fun ODSCardChoiceSimple(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardChoiceSimpleProps = ODSCardChoiceSimpleProps(),
    bottomContentSlot: (@Composable () -> Unit)? = null,
    rightContentSlot: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {

    var pressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val style = ODSCardChoiceSimpleStyle().getStyle(
        props = props,
        scheme = scheme,
        state = if (pressed) ODSActions.PRESSED else if (isHovered) ODSActions.HOVERED else ODSActions.DEFAULT
    )

    val scale by animateFloatAsState(
        targetValue = if (isHovered && !pressed) {
            style.scaleFactor
                ?: SCALE_FACTOR
        } else {
            DEFAULT_FACTOR
        },
        animationSpec = tween(durationMillis = DEFAULT_SCALE_DURATION, easing = EaseInOut),
        label = ""
    )

    ODSBox(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .semantics { this.selected = props.selected }
            .customClickable(
                interactionSource = interactionSource,
                isPressed = {
                    pressed = it
                },
                onClick = onClick,
                role = Role.RadioButton
            )
    ) {
        ODSColumn(
            modifier = Modifier
                .matchParentSize()
                .scale(scale),
            cornerRadius = style.cardBgBorderRadius,
            background = style.cardBgBackgroundColor,
            clipContent = style.cardBgClipContent ?: false,
            horizontalAlignment = style.cardBgHorizontalAlignment,
            verticalAlignment = style.cardBgVerticalAlignment,
            border = ODSBorder(
                width = style.cardBgBorder,
                colorList = style.cardBgBorderColor,
            ),
            verticalArrangement = style.cardBgVerticalArrangement,
        ) { }

        ODSCardChoiceSimpleContainer(
            style = style,
            props = props,
            pressed = pressed,
            isHovered = isHovered,
            bottomContentSlot = bottomContentSlot,
            rightContentSlot = rightContentSlot,
            scheme = scheme
        )
    }
}

@Composable
private fun ODSCardChoiceSimpleContainer(
    style: ODSCardChoiceSimpleStyle,
    props: ODSCardChoiceSimpleProps,
    scheme: ODSTheme,
    pressed: Boolean,
    isHovered: Boolean,
    bottomContentSlot: (@Composable () -> Unit)? = null,
    rightContentSlot: (@Composable () -> Unit)? = null,
) {
    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
    ) {
        ODSColumn(
            gap = style.containerGap,
            padding = style.containerPadding,
            verticalArrangement = style.containerVerticalArrangement,
            verticalAlignment = style.containerVerticalAlignment,
            horizontalAlignment = style.containerHorizontalAlignment,
            modifier = Modifier.sizeWithinBounds(
                minHeight = style.containerMinHeight ?: MIN_HEIGHT.dp
            )
        ) {
            ODSCardChoiceSimpleContentContainer(
                props = props,
                style = style,
                pressed = pressed,
                isHovered = isHovered,
                scheme = scheme,
                rightContentSlot = rightContentSlot
            )

            bottomContentSlot?.let {
                ODSRow(
                    horizontalArrangement = style.bottomSlotContainerHorizontalArrangement,
                    horizontalAlignment = style.bottomSlotContainerHorizontalAlignment,
                    verticalAlignment = style.bottomSlotContainerVerticalAlignment,
                ) {
                    it.invoke()
                }
            }
        }
    }
}

@Composable
private fun ODSCardChoiceSimpleContentContainer(
    props: ODSCardChoiceSimpleProps,
    style: ODSCardChoiceSimpleStyle,
    scheme: ODSTheme,
    pressed: Boolean,
    isHovered: Boolean,
    rightContentSlot: (@Composable () -> Unit)? = null,
) {
    ODSRow(
        gap = style.contentGap,
        horizontalArrangement = style.contentHorizontalArrangement,
        horizontalAlignment = style.contentHorizontalAlignment,
        verticalAlignment = style.contentVerticalAlignment
    ) {
        ODSRadioIcon(
            scheme = scheme,
            props = ODSRadioIconProps(
                error = false,
                selected = props.selected,
                state = if (pressed) ODSActions.PRESSED else if (isHovered) ODSActions.HOVERED else ODSActions.DEFAULT,
                size = ODSRadioIconSize.LARGE,
            )
        )

        ODSCardChoiceSimpleLeftContentContainer(
            modifier = Modifier.weight(1f),
            props = props,
            style = style
        )

        rightContentSlot?.let {
            ODSColumn(
                verticalArrangement = style.rightContentContainerVerticalArrangement,
                verticalAlignment = style.rightContentContainerVerticalAlignment,
                horizontalAlignment = style.rightContentContainerHorizontalAlignment
            ) {
                it.invoke()
            }
        }
    }
}

@Composable
private fun ODSCardChoiceSimpleLeftContentContainer(
    modifier: Modifier,
    props: ODSCardChoiceSimpleProps,
    style: ODSCardChoiceSimpleStyle,
) {
    ODSColumn(
        modifier = modifier,
        gap = style.leftContentGap,
        verticalArrangement = style.leftContentVerticalArrangement,
        verticalAlignment = style.leftContentVerticalAlignment,
        horizontalAlignment = style.leftContentHorizontalAlignment
    ) {
        if (!props.labelTop.isNullOrEmpty()) {
            ODSText(
                text = props.labelTop,
                style = style.labelTopTextStyle,
                color = style.labelTopColor,
                textAlign = style.labelTopTextAlign
            )
        }
        if (!props.heading.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.heading,
                style = style.headingTextStyle,
                color = style.headingColor,
                textAlign = style.headingTextAlign
            )
        }
        if (!props.labelBottom.isNullOrEmpty()) {
            ODSText(
                text = props.labelBottom,
                style = style.labelBottomTextStyle,
                color = style.labelBottomColor,
                textAlign = style.labelBottomTextAlign
            )
        }
    }
}
