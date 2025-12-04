package com.app.screentime.challenge.component.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Sponsor section.
 */
@Composable
fun SponsorSection(
    sponsor: String,
    scheme: ODSTheme = neutralScheme
) {
    ODSRow(
        modifier = Modifier.fillMaxWidth(),
        padding = ODSPadding(horizontal = DSVariables.spacingComponent5),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ODSText(
            text = "Sponsored by ",
            style = DSTextStyles.bodyMRegular,
            color = scheme.basicTextRecessive
        )
        ODSText(
            text = sponsor,
            style = DSTextStyles.bodyMRegular,
            color = scheme.basicAccent
        )
    }
}

