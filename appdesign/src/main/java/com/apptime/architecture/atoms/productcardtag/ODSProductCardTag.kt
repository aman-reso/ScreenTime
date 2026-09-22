package com.telekom.odsystem.atoms.productcardtag

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.SINGLE_LINE
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ODSProductCardTag(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSProductCardTagProps = ODSProductCardTagProps()
) {

    val style = ODSProductCardTagStyle().getStyle(scheme = scheme, props = props)

    ODSRow(
        modifier = modifier,
        padding = style.padding,
        cornerRadius = style.cornerRadius,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        horizontalArrangement = style.horizontalArrangement,
        background = style.background,
        height = style.height
    ) {
        if (!props.labelText.isNullOrEmpty()) {
            ODSText(
                text = props.labelText,
                style = style.labelStyle,
                color = style.labelColor,
                textAlign = style.labelTextAlign,
                overflow = style.labelTextOverflow,
                maxLines = SINGLE_LINE
            )
        }
    }
}
