package com.app.screentime.feature.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

private data class IntentOption(
    val title: String,
    val subtitle: String,
    val emoji: String
)

/**
 * Step 6: Relationship Intent Selection.
 * Matches unified Figma design system layout:
 * - "What's your relationship intent?"
 * - "Be upfront about what you're looking for right now."
 * - Options: Long-term partner, Casual & fun, New friends, Open to anything
 */
@Composable
fun PreferencesStepThree(
    selectedIntent: String,
    scheme: ODSTheme,
    onIntentSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryTextColor = scheme.basicTextDominant
    val secondaryTextColor = scheme.basicTextRecessive
    val accentColor = scheme.basicAccent
    val inactiveBorderColor = scheme.basicStrokeSubtle

    val intentOptions = remember {
        listOf(
            IntentOption(
                title = "Long-term partner",
                subtitle = "Deep connections, building a future together.",
                emoji = "💖"
            ),
            IntentOption(
                title = "Casual & fun",
                subtitle = "No pressure, going with the flow & dating.",
                emoji = "🍷"
            ),
            IntentOption(
                title = "New friends",
                subtitle = "Expanding your circle, trying new activities.",
                emoji = "👋"
            ),
            IntentOption(
                title = "Open to anything",
                subtitle = "Letting chemistry decide the connection.",
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
                style = ODSTextStyles.titleL,
                color = primaryTextColor
            )
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "Be upfront about what you're looking for right now.",
                style = ODSTextStyles.bodyMRegular,
                color = secondaryTextColor
            )
        }

        // ── 2. Intent Options List ─────────────────────────────────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 12.dp,
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            intentOptions.forEach { opt ->
                val isSelected = selectedIntent.equals(opt.title, ignoreCase = true)
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onIntentSelect(opt.title) }
                        ),
                    cornerRadius = ODSCorners(all = 16.dp),
                    border = ODSBorder(
                        width = if (isSelected) 2.dp else 1.dp,
                        colorList = listOf(
                            ODSColorModel(
                                hexColor = if (isSelected) accentColor else inactiveBorderColor
                            )
                        )
                    ),
                    background = listOf(
                        ODSColorModel(
                            hexColor = if (isSelected) scheme.basicAccentSecondary else scheme.basicBackgroundCard
                        )
                    ),
                    padding = ODSPadding(all = 16.dp)
                ) {
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ODSRow(
                            modifier = Modifier.weight(1f),
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
                                    color = if (isSelected) accentColor else primaryTextColor
                                )
                                ODSText(
                                    text = opt.subtitle,
                                    style = ODSTextStyles.microcopyRegular,
                                    color = secondaryTextColor
                                )
                            }
                        }

                        if (isSelected) {
                            ODSBox(
                                modifier = Modifier.size(24.dp),
                                background = listOf(ODSColorModel(hexColor = accentColor)),
                                cornerRadius = ODSCorners(all = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSIcon(
                                    iconModel = ODSIconModel(
                                        drawableRes = R.drawable.ic_check,
                                        contentDescription = "Selected"
                                    ),
                                    tint = scheme.basicTextOnAccent.getColor(),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
