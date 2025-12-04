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
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import com.telekom.odsystem.slots.cardcontentbasic.ODSCardContentBasic
import com.telekom.odsystem.slots.cardcontentbasic.ODSCardContentBasicProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Prize breakdown section.
 */
@Composable
fun PrizeBreakdownSection(
    prize: String,
    scheme: ODSTheme = neutralScheme
) {
    ODSCardBasic(
        modifier = Modifier.fillMaxWidth(),
        scheme = scheme,
        props = ODSCardBasicProps(),
        contentSlot = {
            ODSCardContentBasic(
                scheme = scheme,
                props = ODSCardContentBasicProps(
                    label = "Prize Information",
                    content = prize
                )
            )
        }
    )
}

