package com.telekom.odsystem.atoms.button

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
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
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.MIN_WIDTH
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.SCALE_FACTOR
import com.telekom.odsystem.foundations.SINGLE_LINE
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.scaleAnimationSpec
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSButton composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param isPressed Parameter for customization.
 * @param onClick Callback triggered when action occurs.
 */
@Composable
fun ODSButton(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSButtonProps = ODSButtonProps(),
    isPressed: (Boolean) -> Unit = {},
    onClick: () -> Unit
) {
    val pressed = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val state = when {
        pressed.value && !props.disabled -> ODSActions.PRESSED
        isHovered && !props.disabled -> ODSActions.HOVERED
        else -> ODSActions.DEFAULT
    }

    val style = ODSButtonStyle().getStyle(
        scheme = scheme,
        props = props,
        state = state
    )

    ODSButtonContainer(
        modifier = modifier,
        isPressed = {
            pressed.value = it
            isPressed(it)
        },
        interactionSource = interactionSource,
        isHovered = isHovered,
        pressed = pressed.value,
        onClick = onClick,
        style = style,
        props = props
    )
}

@Suppress("LongMethod")
@Composable
/**
 * Layout container used internally by [ODSButton].
 *
 * Handles interaction state, scaling and content arrangement.
 *
 * @param modifier Modifier applied to the outer box.
 * @param isPressed Callback for press state changes.
 * @param onClick Action executed on click.
 * @param interactionSource Interaction source tracking pointer events.
 * @param isHovered Whether the pointer is hovering over the button.
 * @param pressed Current pressed state.
 * @param props Button properties.
 * @param style Visual style derived from the theme.
 */
private fun ODSButtonContainer(
    modifier: Modifier = Modifier,
    isPressed: (Boolean) -> Unit,
    onClick: () -> Unit,
    interactionSource: MutableInteractionSource,
    isHovered: Boolean,
    pressed: Boolean,
    props: ODSButtonProps = ODSButtonProps(),
    style: ODSButtonStyle
) {
    if (props.buttonType == ODSButtonButtonType.ICON_ONLY && props.buttonIcon == null) {
        return
    }

    val scale by animateFloatAsState(
        targetValue = if (isHovered && !pressed && props.variant != ODSButtonVariant.GHOST) {
            style.scaleFactor
                ?: SCALE_FACTOR
        } else {
            DEFAULT_FACTOR
        },
        animationSpec = scaleAnimationSpec,
        label = ""
    )
    ODSBox(
        contentAlignment = style.contentAlignment,
        modifier = modifier
            .sizeWithinBounds(
                minWidth = style.minWidth ?: MIN_WIDTH.dp,
                minHeight = style.minHeight ?: MIN_HEIGHT.dp,
                maxWidth = style.maxWidth ?: Dp.Unspecified,
                maxHeight = style.maxHeight ?: Dp.Unspecified
            )
            .width(IntrinsicSize.Max)
            .customClickable(
                isPressed = { isPressed(it) },
                onClick = onClick,
                interactionSource = interactionSource,
                disabled = props.disabled,
                role = Role.Button
            ),
    ) {
        ODSBox(
            cornerRadius = style.buttonBgBorderRadius,
            border = ODSBorder(
                width = style.buttonBgBorder,
                colorList = style.buttonBgBorderColor
            ),
            background = style.buttonBgBackgroundColor,
            width = style.buttonBgWidth,
            height = style.buttonBgHeight,
            modifier = Modifier
                .scale(scale)
                .then(
                    if (props.buttonType == ODSButtonButtonType.STANDARD) {
                        Modifier.fillMaxWidth()
                    } else {
                        Modifier
                    }
                )
                .align(style.buttonBgContentAlignment ?: Alignment.Center)
        ) {
        }
        ODSRow(
            cornerRadius = style.borderRadius,
            padding = style.padding,
            verticalAlignment = style.verticalAlignment,
            horizontalAlignment = style.horizontalAlignment,
            horizontalArrangement = style.horizontalArrangement,
            modifier = Modifier.sizeWithinBounds(
                minWidth = style.minWidth ?: MIN_WIDTH.dp,
                minHeight = style.minHeight ?: MIN_HEIGHT.dp,
                maxWidth = style.maxWidth ?: Dp.Unspecified,
                maxHeight = style.maxHeight ?: Dp.Unspecified
            )
        ) {
            if (props.buttonType == ODSButtonButtonType.STANDARD) {
                ODSStandardButton(
                    style = style,
                    props = props
                )
            } else {
                ODSIconOnlyButton(
                    style = style,
                    props = props
                )
            }
        }
    }
}

@Composable
/**
 * Renders a button that only displays an icon.
 *
 * @param style Style values applied to the icon container.
 * @param props Configuration describing the icon model and other attributes.
 */
private fun ODSIconOnlyButton(
    style: ODSButtonStyle,
    props: ODSButtonProps,
) {
    ODSRow(
        gap = style.contentGap,
        padding = style.contentPadding,
        verticalAlignment = style.contentVerticalAlignment,
        horizontalAlignment = style.contentHorizontalAlignment,
        horizontalArrangement = style.contentHorizontalArrangement,
    ) {
        props.buttonIcon?.let {
            ODSIcon(
                width = style.buttonIcon3Width,
                height = style.buttonIcon3Height,
                iconModel = it,
                tint = style.buttonIcon3Color?.getColor(),
            )
        }
    }
}

@Composable
/**
 * Standard button layout with optional left and right icons.
 *
 * @param style Styling information for spacing and colors.
 * @param props Properties describing label text and icon configuration.
 */
private fun ODSStandardButton(
    style: ODSButtonStyle,
    props: ODSButtonProps,
) {
    ODSRow(
        gap = style.contentGap,
        padding = style.contentPadding,
        verticalAlignment = style.contentVerticalAlignment,
        horizontalAlignment = style.contentHorizontalAlignment,
        horizontalArrangement = style.contentHorizontalArrangement,
    ) {
        if (props.leftIcon && props.buttonIcon != null) {
            ODSButtonIconContainer(
                width = style.buttonIconWidth,
                height = style.buttonIconHeight,
                horizontalArrangement = style.leftIconHorizontalArrangement,
                horizontalAlignment = style.leftIconHorizontalAlignment,
                verticalAlignment = style.leftIconVerticalAlignment,
                icon = props.buttonIcon,
                color = style.buttonIconColor?.getColor()
            )
        }
        if (!props.label.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.weight(weight = 1f, fill = false),
                text = props.label,
                style = style.buttonLabelTextStyle,
                color = style.buttonLabelColor,
                textAlign = style.buttonLabelTextAlign,
                maxLines = SINGLE_LINE,
                overflow = style.buttonLabelTextOverflow
            )
        }
        if (props.rightIcon && props.buttonIcon != null) {
            ODSButtonIconContainer(
                width = style.buttonIcon2Width,
                height = style.buttonIcon2Height,
                horizontalArrangement = style.rightIconHorizontalArrangement,
                horizontalAlignment = style.rightIconHorizontalAlignment,
                verticalAlignment = style.rightIconVerticalAlignment,
                icon = props.buttonIcon,
                color = style.buttonIcon2Color?.getColor()
            )
        }
    }
}

@Composable
private fun ODSButtonIconContainer(
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
