package com.telekom.odsystem.atoms.starlistitem

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
/**
 * ODSStarListItem composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param onClick Callback triggered when action occurs.
 * @param onPressed Callback triggered when action occurs.
 * @param onHovered Callback triggered when action occurs.
 * @param starState Parameter for customization.
 * @param props Visual configuration for the component.
 */
fun ODSStarListItem(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    onClick: () -> Unit,
    onPressed: (Boolean) -> Unit,
    onHovered: (Boolean) -> Unit = {},
    starState: ODSActions? = null,
    props: ODSStarListItemProps = ODSStarListItemProps()
) {
    val pressed = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val shouldNotifyHover = remember(isHovered) {
        derivedStateOf { isHovered }
    }
    LaunchedEffect(key1 = shouldNotifyHover.value) {
        onHovered(shouldNotifyHover.value)
    }
    val state = starState
        ?: if (pressed.value) ODSActions.PRESSED else if (isHovered) ODSActions.HOVERED else ODSActions.DEFAULT
    val style = ODSStarListItemStyle().getStyle(
        scheme = scheme, props = props, state = state
    )
    ODSStarListItemContainer(style = style,
        modifier = modifier, isPressed = {
            onPressed(it)
            pressed.value = it
        }, onClick = onClick, props = props, interactionSource = interactionSource, state = state
    )
}

@Suppress("ComplexCondition")
@Composable
fun ODSStarListItemContainer(
    style: ODSStarListItemStyle,
    modifier: Modifier,
    isPressed: (Boolean) -> Unit,
    onClick: () -> Unit,
    props: ODSStarListItemProps,
    interactionSource: MutableInteractionSource,
    state: ODSActions
) {
    ODSRow(
        width = style.width,
        height = style.height,
        modifier = modifier
            .customClickable(
                interactionSource = interactionSource,
                isPressed = { isPressed(it) },
                onClick = onClick,
                disabled = props.disabled,
                readOnly = props.readOnly
            )
            .clearAndSetSemantics { },
        verticalAlignment = style.verticalAlignment,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment
    ) {
        val localContext = LocalContext.current
        if ((props.selected || props.disabled || props.readOnly || state == ODSActions.PRESSED || state == ODSActions.HOVERED)) {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.star_type_bold,
                    tint = style.starColor,
                    contentDescription = localContext.getString(R.string.semantic_star_icon)
                ), width = style.starWidth, height = style.starHeight
            )
        } else {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.star_type_standard,
                    tint = style.starColor,
                    contentDescription = localContext.getString(R.string.semantic_star_icon)
                ), width = style.starWidth, height = style.starHeight

            )
        }
    }
}
