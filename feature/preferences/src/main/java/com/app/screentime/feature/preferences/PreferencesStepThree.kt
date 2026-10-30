package com.app.screentime.feature.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.organisms.cardchoice.ODSCardChoice
import com.telekom.odsystem.organisms.cardchoice.ODSCardChoiceProps
import com.telekom.odsystem.organisms.cardchoice.ODSCardChoiceSelectorPosition
import com.telekom.odsystem.organisms.cardchoice.ODSCardChoiceType
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

private data class IntentOption(
    val title: String,
    val emoji: String
)

/**
 * Step 6: Relationship Intent Selection using Telekom ODS ODSCardChoice.
 * 100% Telekom ODS UI components.
 */
@Composable
fun PreferencesStepThree(
    selectedIntent: String,
    scheme: ODSTheme,
    onIntentSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryTextColor = scheme.basicText
    val secondaryTextColor = scheme.basicTextRecessive

    val intentOptions = remember {
        listOf(
            IntentOption(
                title = "Long-term partner",
                emoji = "💖"
            ),
            IntentOption(
                title = "Casual & fun",
                emoji = "🍷"
            ),
            IntentOption(
                title = "New friends",
                emoji = "👋"
            ),
            IntentOption(
                title = "Open to anything",
                emoji = "🔮"
            )
        )
    }

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = 28.dp,
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        // ── 1. Heading & Subtitle ──────────────────────────────────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 8.dp,
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "What's your relationship intent?",
                style = ODSTextStyles.bodyL,
                color = scheme.basicText
            )
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "Be upfront about what you're looking for right now.",
                style = ODSTextStyles.bodyMRegular,
                color = scheme.basicTextRecessive
            )
        }

        // ── 2. Intent Options List (ODSCardChoice) ───────────────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 12.dp,
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            intentOptions.forEach { opt ->
                val isSelected = selectedIntent.equals(opt.title, ignoreCase = true)
                ODSCardChoice(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSCardChoiceProps(
                        selected = isSelected,
                        type = ODSCardChoiceType.RADIO_CHOICE,
                        selectorPosition = ODSCardChoiceSelectorPosition.RIGHT
                    ),
                    onClick = { onIntentSelect(opt.title) },
                    contentSlot = {
                        ODSRow(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            gap = 12.dp
                        ) {
                            ODSBox(
                                modifier = Modifier.size(40.dp),
                                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                                cornerRadius = ODSCorners(all = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSText(
                                    text = opt.emoji,
                                    style = ODSTextStyles.bodyL
                                )
                            }

                            ODSColumn(
                                modifier = Modifier.weight(1f),
                                gap = 4.dp
                            ) {
                                ODSText(
                                    text = opt.title,
                                    style = ODSTextStyles.bodyMBold,
                                    color = primaryTextColor
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}
