package com.app.screentime.challenge.component.detail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EmojiEvents
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
        ODSCardBasic(
            contentPadding = ODSPadding(
                horizontal = DSVariables.spacingComponent3,
                vertical = DSVariables.spacingComponent3
            ),
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSCardBasicProps(),
            contentSlot = {
                ODSListRowStandard(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSListRowStandardProps(
                        variant = ODSListRowStandardVariant.ICON,
                        label = "Duration",
                        labelText = duration,
                        descriptionTitle = dateRange,
                        icon = ODSIconModel(
                            imageVector = Icons.Default.CalendarToday,
                            tint = scheme.basicAccent,
                            contentDescription = "Duration"
                        )
                    )
                )
            })

        ODSCardBasic(
            contentPadding = ODSPadding(
                horizontal = DSVariables.spacingComponent3, vertical = DSVariables.spacingComponent3
            ),
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSCardBasicProps(),
            contentSlot = {
                ODSListRowStandard(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSListRowStandardProps(
                        variant = ODSListRowStandardVariant.ICON,
                        label = "Total Prize Pool",
                        descriptionTitle = prize,
                        icon = ODSIconModel(
                            imageVector = Icons.Default.EmojiEvents,
                            tint = scheme.functionalWarningStandard,
                            contentDescription = "Prize"
                        )
                    )
                )
            })
    }
}

