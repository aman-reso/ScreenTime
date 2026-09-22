package com.telekom.odsystem.atoms.floatingactionbutton

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.DEFAULT_FACTOR
import com.telekom.odsystem.foundations.DEFAULT_SCALE_DURATION
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.MIN_WIDTH
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.SCALE_FACTOR
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
/**
 * ODSFloatingActionButton composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onClick Callback triggered when action occurs.
 */
@Composable
fun ODSFloatingActionButton(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSFloatingActionButtonProps = ODSFloatingActionButtonProps(),
    onClick: () -> Unit,
) {

    val pressed = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val style = ODSFloatingActionButtonStyle().getStyle(
        scheme = scheme,
        props = props,
        state = if (pressed.value && !props.disabled) ODSActions.PRESSED else if (isHovered && !props.disabled) ODSActions.HOVERED else ODSActions.DEFAULT
    )

    val scale by animateFloatAsState(
        targetValue = if (isHovered && !pressed.value) {
            style.scaleFactor
                ?: SCALE_FACTOR
        } else {
            DEFAULT_FACTOR
        },
        animationSpec = tween(durationMillis = DEFAULT_SCALE_DURATION, easing = EaseInOut),
        label = ""
    )

    ODSBox(
        modifier = modifier
            .minMaxSize(style, props)
            .width(IntrinsicSize.Max)
            .customClickable(
                isPressed = { pressed.value = it },
                onClick = onClick,
                interactionSource = interactionSource,
                disabled = props.disabled,
                role = Role.Button
            ),
        cornerRadius = style.borderRadius,
        contentAlignment = Alignment.Center,
    ) {
        ODSRow(
            modifier = Modifier
                .scale(scale)
                .then(
                    if (props.type == ODSFloatingActionButtonType.EXTENDED) {
                        Modifier.fillMaxWidth()
                    } else {
                        Modifier
                    }
                ),
            border = ODSBorder(
                width = style.buttonBgBorder,
                colorList = style.buttonBgBorderColor,
            ),
            effect = style.buttonBgBoxShadow,
            height = style.buttonBgHeight,
            width = if (props.type == ODSFloatingActionButtonType.STANDARD) {
                style.buttonBgWidth
            } else {
                Dp.Unspecified
            },
            background = style.buttonBgBackgroundColor,
            cornerRadius = style.buttonBgBorderRadius,
            verticalAlignment = style.buttonBgVerticalAlignment,
            horizontalAlignment = style.buttonBgHorizontalAlignment,
            horizontalArrangement = style.buttonBgHorizontalArrangement,
        ) {
        }
        if (props.type == ODSFloatingActionButtonType.STANDARD) {
            ODSStandardFloatingActionButton(
                style = style,
                props = props
            )
        } else {
            ODSExtendedFloatingActionButton(
                style = style,
                props = props
            )
        }
    }
}

@Composable
private fun ODSStandardFloatingActionButton(
    style: ODSFloatingActionButtonStyle,
    props: ODSFloatingActionButtonProps = ODSFloatingActionButtonProps()
) {
    ODSRow(
        gap = style.contentGap,
        padding = style.contentPadding,
        horizontalArrangement = style.contentHorizontalArrangement,
        horizontalAlignment = style.contentHorizontalAlignment,
        verticalAlignment = style.contentVerticalAlignment
    ) {
        ODSIcon(
            iconModel = props.icon,
            width = style.buttonIconWidth,
            height = style.buttonIconHeight,
            tint = style.buttonIconColor?.getColor()
        )
    }
}

@Composable
private fun ODSExtendedFloatingActionButton(
    style: ODSFloatingActionButtonStyle,
    props: ODSFloatingActionButtonProps = ODSFloatingActionButtonProps()
) {
    ODSRow(
        gap = style.contentGap,
        padding = style.contentPadding,
        horizontalArrangement = style.contentHorizontalArrangement,
        horizontalAlignment = style.contentHorizontalAlignment,
        verticalAlignment = style.contentVerticalAlignment
    ) {
        if (props.leftIcon && props.icon != null) {
            ODSFloatingButtonIconContainer(
                icon = props.icon,
                color = style.buttonIconColor?.getColor(),
                width = style.buttonIconWidth,
                height = style.buttonIconHeight,
                horizontalArrangement = style.leftIconHorizontalArrangement,
                horizontalAlignment = style.leftIconHorizontalAlignment,
                verticalAlignment = style.leftIconVerticalAlignment
            )
        }
        if (!props.label.isNullOrEmpty()) {
            ODSText(
                text = props.label,
                style = style.labelTextStyle,
                color = style.labelColor,
                maxLines = SINGLE_LINE,
                textAlign = style.labelTextAlign
            )
        }
        if (props.rightIcon && props.icon != null) {
            ODSFloatingButtonIconContainer(
                icon = props.icon,
                color = style.buttonIconColor?.getColor(),
                width = style.buttonIconWidth,
                height = style.buttonIconHeight,
                horizontalArrangement = style.rightIconHorizontalArrangement,
                horizontalAlignment = style.rightIconHorizontalAlignment,
                verticalAlignment = style.rightIconVerticalAlignment
            )
        }
    }
}

@Composable
private fun ODSFloatingButtonIconContainer(
    width: Dp? = null,
    height: Dp? = null,
    horizontalArrangement: Arrangement.Horizontal? = null,
    horizontalAlignment: Alignment.Horizontal? = null,
    verticalAlignment: Alignment.Vertical? = null,
    icon: ODSIconModel?,
    color: Color?
) {
    ODSRow(
        horizontalArrangement = horizontalArrangement,
        horizontalAlignment = horizontalAlignment,
        verticalAlignment = verticalAlignment,
    ) {
        ODSIcon(
            width = width,
            height = height,
            iconModel = icon,
            tint = color,
        )
    }
}

private fun Modifier.minMaxSize(
    style: ODSFloatingActionButtonStyle,
    props: ODSFloatingActionButtonProps
): Modifier {
    val internalModifier = Modifier
        .heightIn(
            min = style.minHeight ?: MIN_HEIGHT.dp,
            max = if (props.type == ODSFloatingActionButtonType.STANDARD) {
                style.maxHeight
                    ?: Dp.Unspecified
            } else {
                Dp.Unspecified
            }
        )
        .widthIn(
            min = style.minWidth ?: MIN_WIDTH.dp,
            max = if (props.type == ODSFloatingActionButtonType.STANDARD) {
                style.maxWidth
                    ?: Dp.Unspecified
            } else {
                Dp.Unspecified
            }
        )
    return internalModifier.then(this)
}

private const val SINGLE_LINE = 1
