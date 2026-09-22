package com.telekom.odsystem.atoms.productcardcolors

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.colourswatch.ODSColourSwatch
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ODSProductCardColors(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSProductCardColorsProps = ODSProductCardColorsProps()
) {
    val style = ODSProductCardColorsStyle().getStyle(scheme = scheme)
    props.colourSwatchProps?.let {
        ODSRow(
            modifier = modifier.fillMaxWidth(),
            gap = style.gap,
            padding = style.padding,
            horizontalAlignment = style.horizontalAlignment,
            verticalAlignment = style.verticalAlignment,
            horizontalArrangement = style.horizontalArrangement,
        ) {
            val visibleSwatches = props.colourSwatchProps.takeLast(MAX_VISIBLE_COLORS)
            val overFlowCount = props.colourSwatchProps.size - MAX_VISIBLE_COLORS
            ODSRow(
                gap = style.listContainerGap,
                horizontalAlignment = style.listContainerHorizontalAlignment,
                verticalAlignment = style.listContainerVerticalAlignment,
                horizontalArrangement = style.listContainerHorizontalArrangement
            ) {
                visibleSwatches.forEachIndexed { index, swatchProp ->
                    ODSColourSwatch(scheme = scheme, props = swatchProp)
                }
            }
            if (overFlowCount > 0) {
                ODSText(
                    text = "+$overFlowCount",
                    style = style.overflowCountStyle,
                    color = style.overflowCountColor,
                    textAlign = style.overflowCountTextAlign
                )
            }
        }
    }
}

private const val MAX_VISIBLE_COLORS = 5
