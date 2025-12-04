package com.telekom.odsystem.atoms.logo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.MIN_WIDTH
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSLogo composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onClick Callback triggered when action occurs.
 */
@Composable
fun ODSLogo(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSLogoProps = ODSLogoProps(),
    onClick: () -> Unit = {}
) {
    val pressed = remember { mutableStateOf(false) }
    val style = ODSLogoStyle().getStyle(
        scheme = scheme,
        props = props,
        state = if (pressed.value) ODSActions.PRESSED else ODSActions.DEFAULT
    )

    ODSLogoContainer(
        modifier = modifier,
        isPressed = {
            pressed.value = it
        },
        onClick = onClick,
        style = style
    )
}

@Composable
private fun ODSLogoContainer(
    modifier: Modifier,
    style: ODSLogoStyle,
    isPressed: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    ODSIcon(
        modifier = modifier
            .customClickable(
                role = Role.Button,
                isPressed = { isPressed(it) },
                onClick = onClick
            )
            .sizeWithinBounds(
                minHeight = style.minHeight ?: MIN_HEIGHT.dp,
                minWidth = style.minWidth ?: MIN_WIDTH.dp
            ),
        width = style.width,
        height = style.height,
        iconModel = ODSIconModel(
            drawableRes = R.drawable.dt_logo,
            tint = style.iconColor,
            contentDescription = CONTENT_DESCRIPTION
        )
    )
}

private const val CONTENT_DESCRIPTION = "Deutsche Telekom logo"
