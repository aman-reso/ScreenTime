package com.telekom.odsystem.slots.cardfeaturepreferredcontent

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-25 (v1.33.1) - uid: 506748f4
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=9079-18873
 */

/**
 * Displays preferred content within a feature card, typically including product name and price.
 * This composable arranges the product name and price vertically, styled according to the provided theme.
 *
 * @param modifier [Modifier] to be applied to the layout.
 * @param scheme [ODSTheme] used to style the content. Defaults to [neutralScheme].
 * @param props [ODSCardFeaturePreferredContentProps] containing the data to display, such as product name and price.
 */
@Composable
fun ODSCardFeaturePreferredContent(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardFeaturePreferredContentProps = ODSCardFeaturePreferredContentProps(),
) {
    val style = ODSCardFeaturePreferredContentStyle().getStyle(scheme = scheme)

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = style.gap,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        verticalArrangement = style.verticalArrangement
    ) {
        if (!props.productName.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.productName,
                style = style.productNameStyle,
                color = style.productNameColor,
                textAlign = style.productNameTextAlign
            )
        }
        if (!props.productPrice.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.productPrice,
                style = style.productPriceStyle,
                color = style.productPriceColor,
                textAlign = style.productPriceTextAlign
            )
        }
    }
}
