package com.telekom.odsystem.slots.contentlabel

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
/**
 * ODSContentLabel composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
fun ODSContentLabel(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSContentLabelProps = ODSContentLabelProps()
) {

    val style = ODSContentLabelStyle().getStyle(scheme = scheme)

    ODSRow(
        modifier = modifier,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
    ) {
        if (!props.text.isNullOrEmpty()) {
            ODSText(
                text = props.text,
                style = style.rightTextTextStyle,
                color = style.rightTextColor,
                textAlign = style.rightTextTextAlign
            )
        }
    }
}
