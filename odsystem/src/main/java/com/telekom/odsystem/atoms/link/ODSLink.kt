package com.telekom.odsystem.atoms.link

import android.content.Context
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.MIN_WIDTH
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.foundations.underline
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSLink composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onClick Callback triggered when action occurs.
 */
@Composable
fun ODSLink(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSLinkProps = ODSLinkProps(),
    onClick: (() -> Unit)? = null
) {
    val isPressed = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val style = ODSLinkStyle().getStyle(
        scheme = scheme,
        props = props,
        state = if (isPressed.value && !props.disabled) ODSActions.PRESSED else if (isHovered && !props.disabled) ODSActions.HOVERED else ODSActions.DEFAULT
    )
    ODSLinkContainer(
        modifier = modifier,
        isPressed = { isPressed.value = it },
        onClick = onClick,
        style = style,
        props = props,
        interactionSource = interactionSource
    )
}

@Composable
private fun ODSLinkContainer(
    modifier: Modifier = Modifier,
    isPressed: (Boolean) -> Unit,
    onClick: (() -> Unit)?,
    props: ODSLinkProps = ODSLinkProps(),
    style: ODSLinkStyle,
    interactionSource: MutableInteractionSource,
    context: Context = LocalContext.current
) {
    ODSRow(
        modifier = modifier
            .sizeWithinBounds(
                minWidth = style.minWidth ?: MIN_WIDTH.dp,
                minHeight = style.minHeight ?: MIN_HEIGHT.dp
            )
            .applySemantics(props = props, context = context)
            .customClickable(
                isPressed = { isPressed(it) },
                onClick = onClick,
                disabled = props.disabled,
                interactionSource = interactionSource
            ),
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        horizontalArrangement = style.horizontalArrangement
    ) {
        ODSLinkContent(props = props, style = style)
    }
}

@Composable
private fun ODSLinkContent(
    props: ODSLinkProps = ODSLinkProps(),
    style: ODSLinkStyle
) {
    val internalLinkTextColor = style.linkColor?.getColor() ?: Color.Transparent
    val internalUnderlinePadding =
        ((style.linkTextStyle?.lineHeight ?: 0) - (style.linkTextStyle?.fontSize ?: 0)) / 2
    ODSRow(
        modifier = Modifier.clearAndSetSemantics { /* Handle Semantics in ODSLinkContainer */ },
        gap = style.linkContainerGap,
        verticalAlignment = style.linkContainerVerticalAlignment,
        horizontalAlignment = style.linkContainerHorizontalAlignment,
        horizontalArrangement = style.linkContainerHorizontalArrangement
    ) {
        props.leftIcon?.let {
            ODSIconContainer(
                width = style.leftIconWidth,
                height = style.leftIconHeight,
                iconColor = style.leftIconColor,
                iconModel = it
            )
        }

        ODSText(
            modifier = Modifier.underline(
                style.underlineThickness ?: 0.dp,
                internalLinkTextColor,
                internalUnderlinePadding.dp
            ),
            text = props.label,
            style = style.linkTextStyle,
            color = style.linkColor,
            textAlign = style.linkTextAlign
        )

        props.rightIcon?.let {
            ODSIconContainer(
                width = style.rightIconWidth,
                height = style.rightIconHeight,
                iconColor = style.rightIconColor,
                iconModel = it
            )
        }
    }
}

@Composable
private fun ODSIconContainer(
    width: Dp? = null,
    height: Dp? = null,
    iconModel: ODSIconModel,
    iconColor: HexColor? = null
) {
    ODSIcon(
        width = width,
        height = height,
        iconModel = iconModel,
        tint = iconColor?.getColor(),
    )
}

private fun Modifier.applySemantics(props: ODSLinkProps, context: Context): Modifier {
    var contentDescription = ""
    return this.semantics {
        if (!props.leftIcon?.contentDescription.isNullOrEmpty()) {
            contentDescription += "${props.leftIcon?.contentDescription}"
        }
        if (!props.label.isNullOrEmpty()) {
            contentDescription += "\n${props.label}"
        }
        if (!props.rightIcon?.contentDescription.isNullOrEmpty()) {
            contentDescription += "\n${props.rightIcon?.contentDescription}"
        }
        this.role = Role.Button
        this.contentDescription = contentDescription
    }
}
