package com.telekom.odsystem.slots.choicecardcontent

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-10 (v1.33.1) - uid: 2330973d
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=8790-6599
 */

@Composable
fun ODSChoiceCardContent(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSChoiceCardContentProps = ODSChoiceCardContentProps()
) {
    val style = ODSChoiceCardContentStyle().getStyle(scheme = scheme)

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = style.gap,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        verticalArrangement = style.verticalArrangement
    ) {
        if (!props.heading.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.heading,
                style = style.headingStyle,
                color = style.headingColor,
                textAlign = style.headingTextAlign
            )
        }
        if (!props.content.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.content,
                style = style.bodyTextStyle,
                color = style.bodyTextColor,
                textAlign = style.bodyTextTextAlign
            )
        }
    }
}
