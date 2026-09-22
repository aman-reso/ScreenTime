package com.telekom.odsystem.atoms.carouseldot

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
/**
 * ODSCarouselDot composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
fun ODSCarouselDot(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCarouselDotProps = ODSCarouselDotProps()
) {
    val style = ODSCarouselDotStyle().getStyle(scheme = scheme, props = props)
    ODSBox(
        width = style.width,
        height = style.height,
        clipContent = style.clipContent ?: true,
        cornerRadius = style.borderRadius,
        border = ODSBorder(width = style.border, colorList = style.borderColor),
        background = style.backgroundColor,
        modifier = modifier
    ) {
    }
}
