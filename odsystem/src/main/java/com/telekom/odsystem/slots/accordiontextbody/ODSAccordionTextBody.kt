package com.telekom.odsystem.slots.accordiontextbody

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator Plugin
 * 2025-06-23 (v1.31.6) - uid: 427e26ec
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=4627-4269
 */

/**
 * ODSAccordionTextBody composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
@Composable
fun ODSAccordionTextBody(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSAccordionTextBodyProps = ODSAccordionTextBodyProps()
) {

    val style = ODSAccordionTextBodyStyle().getStyle(scheme = scheme)

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        padding = style.padding,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        verticalArrangement = style.verticalArrangement
    ) {
        if (!props.text.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = AnnotatedString.fromHtml(props.text!!),
                style = style.paragraphStyle,
                color = style.paragraphColor,
                textAlign = style.paragraphTextAlign
            )
        }
    }
}
