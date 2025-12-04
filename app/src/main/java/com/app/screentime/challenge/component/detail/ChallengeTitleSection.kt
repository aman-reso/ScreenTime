package com.app.screentime.challenge.component.detail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Title and description section.
 */
@Composable
fun ChallengeTitleSection(
    title: String,
    description: String,
    scheme: ODSTheme = neutralScheme
) {
    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        gap = DSVariables.spacingComponent2
    ) {
        ODSText(
            text = title,
            style = DSTextStyles.bodyMBold,
            color = scheme.basicText
        )
        ODSText(
            text = description,
            style = DSTextStyles.bodySRegular,
            color = scheme.basicTextRecessive
        )
    }
}

