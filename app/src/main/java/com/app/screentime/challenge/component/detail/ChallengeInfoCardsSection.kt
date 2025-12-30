package com.app.screentime.challenge.component.detail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandard
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardProps
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardVariant
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Info cards section for duration and prize pool.
 */
@Composable
fun ChallengeInfoCardsSection(
    dateRange: String, prize: String, duration: String?, scheme: ODSTheme = neutralScheme
) {
    ODSColumn(
        modifier = Modifier.fillMaxWidth(), gap = DSVariables.spacingComponent3
    ) {
        ODSListRowStandard(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSListRowStandardProps(
                variant = ODSListRowStandardVariant.ICON,
                label = "Duration",
                labelText = duration,
                descriptionText = dateRange,
                icon = ODSIconModel(
                    imageVector = Icons.Outlined.Timeline,
                    tint = scheme.basicAccent,
                    contentDescription = "Duration"
                )
            )
        )
    }
}

