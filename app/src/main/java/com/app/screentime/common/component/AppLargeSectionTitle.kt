package com.app.screentime.common.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun AppLargeSectionTitle(scheme: ODSTheme = neutralScheme, title: String = "") {
    ODSBox(
        modifier = Modifier.fillMaxWidth(),
        padding = ODSPadding(
            top = DSVariables.spacingComponent7,
            bottom = DSVariables.spacingComponent3
        )
    ) {
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            style = DSTextStyles.bodyMRegular,
            color = scheme.basicText,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
    }
}