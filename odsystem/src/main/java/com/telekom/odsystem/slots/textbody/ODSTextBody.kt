package com.telekom.odsystem.slots.textbody

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
/**
 * ODSTextBody composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
fun ODSTextBody(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSTextBodyProps = ODSTextBodyProps()
) {

    val style = ODSTextBodyStyle().getStyle(scheme = scheme)

    ODSColumn(
        modifier = modifier,
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
    ) {
        if (!props.text.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.text,
                style = style.paragraphTextStyle,
                color = style.paragraphColor,
                textAlign = style.paragraphTextAlign
            )
        }
    }
}
