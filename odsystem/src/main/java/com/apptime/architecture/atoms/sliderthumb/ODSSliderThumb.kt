package com.telekom.odsystem.atoms.sliderthumb

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.MIN_WIDTH
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
/**
 * ODSSliderThumb composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 */
fun ODSSliderThumb(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
) {

    val pressed = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val style = ODSSliderThumbStyle().getStyle(
        scheme = scheme,
        if (pressed.value) ODSActions.PRESSED else if (isHovered) ODSActions.HOVERED else ODSActions.DEFAULT
    )

    ODSSliderThumbContainer(
        modifier = modifier,
        style = style,
        isPressed = {
            pressed.value = it
        },
        interactionSource = interactionSource
    )
}

@Composable
private fun ODSSliderThumbContainer(
    style: ODSSliderThumbStyle,
    modifier: Modifier,
    isPressed: (Boolean) -> Unit,
    interactionSource: MutableInteractionSource
) {

    ODSColumn(
        modifier = modifier
            .customClickable(
                interactionSource = interactionSource,
                onClick = {},
                isPressed = isPressed
            ),
        gap = style.gap,
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
    ) {
        ODSColumn(
            horizontalAlignment = style.thumbHorizontalAlignment,
            verticalAlignment = style.thumbVerticalAlignment,
            verticalArrangement = style.thumbVerticalArrangement,
            clipContent = style.thumbClipContent != false,
            width = (style.thumbWidth ?: MIN_WIDTH.dp),
            height = (style.thumbHeight ?: MIN_HEIGHT.dp),
            background = style.thumbBorderColor,
            cornerRadius = style.thumbBorderRadius
        ) {
            // Animated thumb for hover effect
            val animatedInnerWidth by animateDpAsState(
                style.innerThumbWidth ?: MIN_WIDTH.dp,
                label = ""
            )
            val animatedInnerHeight by animateDpAsState(
                style.innerThumbHeight ?: MIN_HEIGHT.dp,
                label = ""
            )
            ODSBox(
                width = animatedInnerWidth,
                height = animatedInnerHeight,
                background = style.thumbBackgroundColor,
                cornerRadius = style.thumbBorderRadius,
                clipContent = style.thumbClipContent ?: true
            ) { }
        }
    }
}
