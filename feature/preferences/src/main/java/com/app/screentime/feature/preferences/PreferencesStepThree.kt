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
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

private data class IntentOption(
    val title: String,
    val subtitle: String,
    val emoji: String
)

/**
 * Step 3: Relationship Intent Selection.
 */
@Composable
fun PreferencesStepThree(
    selectedIntent: String,
    scheme: ODSTheme,
    onIntentSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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
        gap = ODSVariables.spacingComponent5
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent2
        ) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "What's your relationship intent?",
                style = ODSTextStyles.bodyMBold,
                color = scheme.basicTextDominant
            )
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "Be upfront about what you're looking for right now.",
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicTextRecessive
            )
        }

        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent3
        ) {
            intentOptions.forEach { opt ->
                val isSelected = selectedIntent == opt.title
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onIntentSelect(opt.title) }
                        ),
                    background = listOf(
                        ODSColorModel(
                            hexColor = if (isSelected) scheme.basicAccentSecondary else scheme.basicBackgroundCard
                        )
                    ),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                    border = ODSBorder(
                        width = ODSVariables.strokes2,
                        colorList = listOf(
                            ODSColorModel(
                                hexColor = if (isSelected) scheme.basicAccent else scheme.basicStroke
                            )
                        )
                    ),
                    padding = ODSPadding(
                        horizontal = ODSVariables.spacingLayout1,
                        vertical = ODSVariables.spacingComponent4
                    )
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
                            gap = ODSVariables.spacingComponent4
                        ) {
                            // Emoji Container
                            ODSBox(
                                modifier = Modifier.size(44.dp),
                                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSText(
                                    text = opt.emoji,
                                    style = ODSTextStyles.bodyMBold
                                )
                            }

                            // Text Column
                            ODSColumn(
                                modifier = Modifier.weight(1f),
                                gap = ODSVariables.spacingComponent1
                            ) {
                                ODSText(
                                    text = opt.title,
                                    style = ODSTextStyles.bodyMBold,
                                    color = scheme.basicTextDominant
                                )
                                ODSText(
                                    text = opt.subtitle,
                                    style = ODSTextStyles.microcopyRegular,
                                    color = scheme.basicTextRecessive
                                )
                            }
                        }

                        // Selected Accent Checkmark Badge
                        if (isSelected) {
                            ODSBox(
                                modifier = Modifier.size(24.dp),
                                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                                cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSIcon(
                                    iconModel = ODSIconModel(
                                        drawableRes = R.drawable.ic_check,
                                        contentDescription = "Selected"
                                    ),
                                    tint = scheme.basicBackgroundCard.getColor(),
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
