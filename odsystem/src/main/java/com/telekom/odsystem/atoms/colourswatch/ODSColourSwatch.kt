package com.telekom.odsystem.atoms.colourswatch

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ODSColourSwatch(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSColourSwatchProps = ODSColourSwatchProps()
) {

    val style = ODSColourSwatchStyle().getStyle(scheme = scheme)

    ODSBox(
        modifier = modifier,
        cornerRadius = style.cornerRadius,
        clipContent = style.clipContent != false,
        border = ODSBorder(width = style.border, colorList = style.borderColor),
        width = style.width,
        height = style.height,
        background = props.backgroundColor
    ) { }
}
