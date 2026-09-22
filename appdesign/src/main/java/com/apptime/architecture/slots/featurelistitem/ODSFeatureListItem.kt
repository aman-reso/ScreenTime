package com.telekom.odsystem.slots.featurelistitem

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ODSFeatureListItem(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSFeatureListItemProps = ODSFeatureListItemProps()
) {

    val style = ODSFeatureListItemStyle().getStyle(scheme = scheme)

    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) { },
        gap = style.gap,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        horizontalArrangement = style.horizontalArrangement
    ) {
        ODSIcon(
            iconModel = props.icon,
            tint = style.iconColor?.getColor(),
            width = style.iconWidth,
            height = style.iconHeight
        )
        if (!props.label.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.weight(1f),
                text = props.label,
                style = style.labelStyle,
                color = style.labelColor,
                textAlign = style.labelTextAlign
            )
        }
    }
}
