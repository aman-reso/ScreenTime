package com.app.screentime.permission.component.infocard

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * InfoCard component displaying title and description.
 *
 * @param modifier Modifier to be applied to the component.
 * @param scheme ODS theme scheme for styling.
 * @param props Configuration properties for the component.
 */
@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    scheme: ODSTheme,
    props: InfoCardProps
) {
    val style = remember(scheme) { InfoCardStyle().getStyle(scheme) }
    val tokens = defaultInfoCardTokens

    ODSBox(
        modifier = modifier.fillMaxWidth(),
        background = style.backgroundColor,
        cornerRadius = ODSCorners(all = tokens.cornerRadius),
        border = ODSBorder(
            width = tokens.borderWidth
        )
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            padding = ODSPadding(all = tokens.padding)
        ) {
            ODSText(
                text = props.title,
                style = DSTextStyles.bodyMBold,
                color = style.titleColor ?: scheme.basicText
            )
            ODSBox(height = tokens.titleSpacing) {}
            ODSText(
                text = props.description,
                style = DSTextStyles.bodyMRegular,
                color = style.descriptionColor ?: scheme.basicTextRecessive
            )
        }
    }
}

