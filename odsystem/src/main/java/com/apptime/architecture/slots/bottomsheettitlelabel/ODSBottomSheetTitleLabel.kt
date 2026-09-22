package com.telekom.odsystem.slots.bottomsheettitlelabel

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
/**
 * ODSBottomSheetTitleLabel composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
fun ODSBottomSheetTitleLabel(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSBottomSheetTitleLabelProps = ODSBottomSheetTitleLabelProps()
) {

    val style = ODSBottomSheetTitleLabelStyle().getStyle(scheme = scheme)

    ODSRow(
        modifier = modifier,
        padding = style.padding,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
    ) {
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = props.title,
            style = style.titleLabelTextStyle,
            color = style.titleLabelColor,
            textAlign = style.titleLabelTextAlign
        )
    }
}
