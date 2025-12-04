package com.telekom.odsystem.slots.productcarddescriptivetext

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.slots.featurelistitem.ODSFeatureListItem
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ODSProductCardDescriptiveText(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSProductCardDescriptiveTextProps = ODSProductCardDescriptiveTextProps()
) {

    val style = ODSProductCardDescriptiveTextStyle().getStyle(scheme = scheme)

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
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = style.listContainerGap,
            verticalAlignment = style.listContainerVerticalAlignment,
            horizontalAlignment = style.listContainerHorizontalAlignment,
            verticalArrangement = style.listContainerVerticalArrangement
        ) {
            props.featureListItemProps?.forEachIndexed { index, item ->
                ODSFeatureListItem(scheme = scheme, props = item)
            }
        }
    }
}
