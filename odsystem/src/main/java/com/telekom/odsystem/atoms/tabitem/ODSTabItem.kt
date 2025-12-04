package com.telekom.odsystem.atoms.tabitem

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.badgenumber.ODSBadgeNumber
import com.telekom.odsystem.atoms.badgenumber.ODSBadgeNumberProps
import com.telekom.odsystem.atoms.badgenumber.ODSBadgeNumberSize
import com.telekom.odsystem.atoms.badgenumber.ODSBadgeNumberVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.MIN_WIDTH
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSTabItem composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onClick Callback triggered when action occurs.
 */
@Suppress("LongMethod")
@Composable
fun ODSTabItem(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSTabItemProps = ODSTabItemProps(),
    onClick: () -> Unit = { },
    tabItemState: (ODSActions) -> Unit = {},
) {
    var pressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val style =
        ODSTabItemStyle().getStyle(
            scheme = scheme, props = props,
            state = when (true) {
                pressed -> ODSActions.PRESSED
                isHovered -> ODSActions.HOVERED
                else -> ODSActions.DEFAULT
            }
        )
    LaunchedEffect(pressed, isHovered) {
        when (true) {
            pressed -> tabItemState(ODSActions.PRESSED)
            isHovered -> tabItemState(ODSActions.HOVERED)
            else -> tabItemState(ODSActions.DEFAULT)
        }
    }
    ODSColumn(
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        modifier = Modifier
            .sizeWithinBounds(
                minWidth = style.minWidth ?: MIN_WIDTH.dp,
                minHeight = style.minHeight ?: MIN_HEIGHT.dp
            )
            .applyFillMaxWidthIfFillVariant(variant = props.variant)
            .then(modifier)
            .semantics { this.selected = props.selected }
            .customClickable(
                interactionSource = interactionSource,
                isPressed = { pressed = it },
                onClick = onClick,
                role = Role.Tab
            )
    ) {
        ODSContentFrame(scheme = scheme, style = style, props = props)
    }
}

@Composable
private fun ODSContentFrame(scheme: ODSTheme, style: ODSTabItemStyle, props: ODSTabItemProps) {
    ODSRow(
        modifier = Modifier.fillMaxWidth(1f),
        gap = style.contentFrameGap,
        padding = style.contentFramePadding,
        horizontalArrangement = style.contentFrameHorizontalArrangement,
        horizontalAlignment = style.contentFrameHorizontalAlignment,
        verticalAlignment = style.contentFrameVerticalAlignment
    ) {
        if (props.icon != null) {
            ODSIcon(
                iconModel = props.icon,
                tint = style.iconColor?.getColor(),
                width = style.iconWidth,
                height = style.iconHeight
            )
        }
        if (!props.label.isNullOrEmpty()) {
            ODSText(
                modifier = if (props.variant == ODSTabItemVariant.FILL) {
                    Modifier.weight(1f, fill = false)
                } else {
                    Modifier
                },
                text = props.label,
                style = style.labelTextStyle,
                color = style.labelColor,
                textAlign = style.labelTextAlign,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (props.showBadge) {
            ODSBadgeNumber(
                scheme = scheme,
                props = ODSBadgeNumberProps(
                    size = ODSBadgeNumberSize.SMALL,
                    variant = ODSBadgeNumberVariant.NOTIFICATION
                )
            )
        }
    }
}

fun Modifier.applyFillMaxWidthIfFillVariant(variant: ODSTabItemVariant): Modifier =
    if (variant == ODSTabItemVariant.FILL) fillMaxWidth() else this
