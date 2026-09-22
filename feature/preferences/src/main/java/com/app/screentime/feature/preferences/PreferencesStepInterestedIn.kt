package com.app.screentime.feature.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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

/**
 * Step 7: Interested In Preference.
 * Recreated with 100% ODS components strictly following GEMINI.md rules.
 */

data class InterestedOptionItem(
    val title: String,
    val description: String,
    val emoji: String
)

val DEFAULT_INTERESTED_OPTIONS = listOf(
    InterestedOptionItem(
        title = "Women",
        description = "Show me women searching for connections",
        emoji = "✨"
    ),
    InterestedOptionItem(
        title = "Men",
        description = "Show me men searching for connections",
        emoji = "⚡"
    ),
    InterestedOptionItem(
        title = "Everyone",
        description = "Open to discovering all wonderful people",
        emoji = "🌈"
    )
)

@Composable
fun InterestedInHeader(
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent2
    ) {
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "Who are you interested in?",
            style = ODSTextStyles.bodyMBold,
            color = scheme.basicTextDominant
        )
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "Select who you would like to explore connections and matches with.",
            style = ODSTextStyles.bodySRegular,
            color = scheme.basicTextRecessive
        )
    }
}

@Composable
fun InterestedInCard(
    option: InterestedOptionItem,
    isSelected: Boolean,
    onSelect: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onSelect
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
                    hexColor = if (isSelected) scheme.basicAccent else scheme.basicStrokeSubtle
                )
            )
        ),
        padding = ODSPadding(all = ODSVariables.spacingLayout1)
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSRow(
                modifier = Modifier.weight(1f),
                gap = ODSVariables.spacingComponent4,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSText(
                    text = option.emoji,
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicTextDominant
                )

                ODSColumn(
                    modifier = Modifier.weight(1f),
                    gap = ODSVariables.spacingComponent1
                ) {
                    ODSText(
                        text = option.title,
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicTextDominant
                    )
                    ODSText(
                        text = option.description,
                        style = ODSTextStyles.microcopyRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            }

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

@Composable
fun PreferencesStepInterestedIn(
    selectedInterestedIn: String,
    onInterestedInChange: (String) -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier,
    options: List<InterestedOptionItem> = DEFAULT_INTERESTED_OPTIONS
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent6
    ) {
        InterestedInHeader(scheme = scheme)

        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent3
        ) {
            options.forEach { opt ->
                InterestedInCard(
                    option = opt,
                    isSelected = selectedInterestedIn == opt.title,
                    onSelect = { onInterestedInChange(opt.title) },
                    scheme = scheme
                )
            }
        }
    }
}
