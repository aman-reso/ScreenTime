package com.telekom.odsystem.atoms.navigationitem

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.badgenumber.ODSBadgeNumber
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.MIN_WIDTH
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.offset
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSNavigationItem composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param accessibilityIndex Current position index for accessibility (0-based).
 * @param accessibilityTotalCount Total number of items for accessibility.
 * @param onClick Callback triggered when action occurs.
 */
@Composable
fun ODSNavigationItem(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSNavigationItemProps = ODSNavigationItemProps(),
    accessibilityIndex: Int? = null,
    accessibilityTotalCount: Int? = null,
    onClick: () -> Unit,
) {
    val context = LocalContext.current
    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val style = ODSNavigationItemStyle().getStyle(
        scheme = scheme,
        props = props,
        state = if (isPressed && !props.disabled) ODSActions.PRESSED else if (isHovered && !props.disabled) ODSActions.HOVERED else ODSActions.DEFAULT
    )

    ODSColumn(
        modifier = modifier
            .sizeWithinBounds(
                minWidth = style.minWidth ?: MIN_WIDTH.dp,
                minHeight = style.minHeight ?: MIN_HEIGHT.dp
            )
            .customClickable(
                role = Role.Button,
                disabled = props.disabled,
                isPressed = {
                    isPressed = it
                },
                onClick = onClick,
                interactionSource = interactionSource
            )
            .semantics(mergeDescendants = true) {
                selected = props.active
            },
        gap = style.gap,
        padding = style.padding,
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment
    ) {
        ODSIconContainer(
            scheme = scheme,
            style = style,
            props = props,
        )
        if (!props.text.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.text,
                style = style.labelStyle,
                color = style.labelColor,
                textAlign = style.labelTextAlign
            )
        }
    }
}

@Composable
private fun ODSIconContainer(
    scheme: ODSTheme,
    style: ODSNavigationItemStyle,
    props: ODSNavigationItemProps,
) {
    ODSBox(
        contentAlignment = style.iconContainerZStackContentAlignment
    ) {
        ODSRow(
            padding = style.iconContainerPadding,
            cornerRadius = style.iconContainerCornerRadius,
            horizontalAlignment = style.iconContainerHorizontalAlignment,
            verticalAlignment = style.iconContainerVerticalAlignment,
            horizontalArrangement = style.iconContainerHorizontalArrangement,
            minWidth = style.iconContainerMinWidth
        ) {
            Icon(props = props, style = style)
        }
        if (!props.active && !props.disabled) {
            props.badgeNumberProps?.let {
                ODSBadgeNumber(
                    scheme = scheme,
                    modifier = Modifier
                        .align(
                            alignment = style.odsBadgeNumberAbsoluteContentAlignment
                                ?: Alignment.TopEnd
                        )
                        .offset(offset = style.odsBadgeNumberAbsoluteOffset),
                    props = it.toODSBadgeNumberProps()
                )
            }
        }
    }
}

@Composable
private fun Icon(props: ODSNavigationItemProps, style: ODSNavigationItemStyle) {
    if (props.active) {
        ODSIcon(
            iconModel = props.iconActive,
            width = style.iconActiveWidth,
            height = style.iconActiveHeight,
            tint = style.iconActiveColor?.getColor(),
        )
    } else {
        ODSIcon(
            iconModel = props.icon,
            width = style.iconWidth,
            height = style.iconHeight,
            tint = style.iconColor?.getColor(),
        )
    }
}
