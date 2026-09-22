package com.telekom.odsystem.atoms.tagdismissible

import android.content.Context
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.MIN_WIDTH
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.SINGLE_LINE
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSTagDismissible composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onDismiss Callback triggered when action occurs.
 */
@Composable
fun ODSTagDismissible(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSTagDismissibleProps = ODSTagDismissibleProps(),
    onDismiss: () -> Unit = {},
) {
    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val state = when {
        isPressed && !props.disabled -> ODSActions.PRESSED
        isHovered && !props.disabled -> ODSActions.HOVERED
        else -> ODSActions.DEFAULT
    }

    val style = ODSTagDismissibleStyle().getStyle(
        scheme = scheme,
        props = props,
        state = state
    )

    ODSTagDismissibleContainer(
        modifier = modifier,
        props = props,
        style = style,
        isPressed = { isPressed = it },
        interactionSource = interactionSource,
        onDismiss = onDismiss,
    )
}

@Composable
private fun ODSTagDismissibleContainer(
    modifier: Modifier,
    props: ODSTagDismissibleProps,
    style: ODSTagDismissibleStyle,
    isPressed: (Boolean) -> Unit,
    interactionSource: MutableInteractionSource,
    onDismiss: () -> Unit
) {
    val localView = LocalView.current
    val context = LocalContext.current

    ODSRow(
        modifier = modifier
            .sizeWithinBounds(
                minWidth = style.minWidth ?: MIN_WIDTH.dp,
                minHeight = style.minHeight ?: MIN_HEIGHT.dp
            )
            .customClickable(
                interactionSource = interactionSource,
                onClick = {
                    localView.announceForAccessibility("${props.label} ${context.getString(R.string.semantic_dismissed_action)}")
                    onDismiss()
                },
                isPressed = { isPressed(it) },
                disabled = props.disabled,
                role = Role.Button,
                onClickLabel = LocalContext.current.getString(R.string.semantic_dismissible_chip_action)
            ),
        gap = style.gap,
        padding = style.padding,
        cornerRadius = style.borderRadius,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
    ) {
        ODSDismissibleTagContainer(style = style, props = props, context = context)
    }
}

@Composable
private fun ODSDismissibleTagContainer(
    style: ODSTagDismissibleStyle,
    props: ODSTagDismissibleProps,
    context: Context
) {
    ODSRow(
        gap = style.dismissibleTagGap,
        padding = style.dismissibleTagPadding,
        cornerRadius = style.dismissibleTagBorderRadius,
        horizontalArrangement = style.dismissibleTagHorizontalArrangement,
        horizontalAlignment = style.dismissibleTagHorizontalAlignment,
        verticalAlignment = style.dismissibleTagVerticalAlignment,
        background = style.dismissibleTagBackgroundColor
    ) {
        ODSIcon(
            iconModel = props.icon,
            tint = style.iconColor?.getColor(),
            width = style.iconWidth,
            height = style.iconHeight
        )
        if (!props.label.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.weight(1f, fill = false),
                text = props.label,
                style = style.labelTextStyle,
                color = style.labelColor,
                textAlign = style.labelTextAlign,
                overflow = style.labelTextOverflow,
                maxLines = SINGLE_LINE
            )
        }
        ODSCloseButtonContainer(style = style, context = context)
    }
}

@Composable
private fun ODSCloseButtonContainer(
    style: ODSTagDismissibleStyle,
    context: Context
) {
    ODSColumn(
        cornerRadius = style.closeButtonBorderRadius,
        verticalArrangement = style.closeButtonVerticalArrangement,
        verticalAlignment = style.closeButtonVerticalAlignment,
        horizontalAlignment = style.closeButtonHorizontalAlignment,
        background = style.closeButtonBackgroundColor
    ) {
        ODSIcon(
            iconModel = ODSIconModel(
                drawableRes = R.drawable.close_type_standard,
                contentDescription = context.getString(R.string.semantic_delete_icon)
            ),
            tint = style.icon2Color?.getColor(),
            width = style.icon2Width,
            height = style.icon2Height
        )
    }
}
