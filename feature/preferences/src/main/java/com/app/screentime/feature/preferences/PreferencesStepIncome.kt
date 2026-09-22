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
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSEffect
import com.telekom.odsystem.foundations.ODSElevation
import com.telekom.odsystem.foundations.ODSElevationType
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Step: Yearly Income Range Preferences.
 * Recreated with 100% ODS components per Figma node-id 10-376.
 * Strictly adheres to project rules:
 * - 100% ODS components.
 * - Colors picked from `scheme: ODSTheme`.
 * - Max text size 16sp with Funnel Sans.
 * - Padding, margins, and gaps from ODSVariables.
 * - Broken down into modular, maintainable sub-components.
 */

val DEFAULT_INCOME_BRACKETS = listOf(
    "Under $50,000",
    "$50,000 – $100,000",
    "$100,000 – $200,000",
    "$200,000+",
    "Prefer not to say / Keep private 🔒"
)

/**
 * Header component for Yearly Income Range.
 */
@Composable
fun IncomeHeader(
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent2
    ) {
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "What's your yearly income range?",
            style = ODSTextStyles.bodyMBold,
            color = scheme.basicTextDominant
        )
    }
}

/**
 * Single selectable income option card.
 */
@Composable
fun IncomeOptionCard(
    incomeRange: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    val dropShadowEffect = if (isSelected) {
        ODSEffect(
            elevations = listOf(
                ODSElevation(
                    x = 0,
                    y = 4,
                    blur = 12,
                    spread = 0,
                    color = HexColor(0x1A000000),
                    type = ODSElevationType.DROP_SHADOW
                )
            )
        )
    } else null

    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onSelect
            ),
        padding = ODSPadding(all = ODSVariables.spacingLayout1),
        cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
        border = ODSBorder(
            width = ODSVariables.strokes2,
            colorList = listOf(
                ODSColorModel(
                    hexColor = if (isSelected) scheme.basicAccent else scheme.basicStrokeSubtle
                )
            )
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        background = listOf(
            ODSColorModel(
                hexColor = if (isSelected) scheme.basicBackgroundCard else scheme.basicBackgroundCardSubtle
            )
        ),
        effect = dropShadowEffect
    ) {
        ODSText(
            text = incomeRange,
            style = ODSTextStyles.bodyMBold,
            color = scheme.basicTextDominant
        )

        if (isSelected) {
            // Checkmark indicator badge per Figma
            ODSBox(
                modifier = Modifier.size(ODSVariables.spacingLayout2),
                cornerRadius = ODSCorners(all = ODSVariables.radiusSmall),
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
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

/**
 * List of income option cards.
 */
@Composable
fun IncomeOptionsList(
    brackets: List<String>,
    selectedIncome: String,
    onSelectIncome: (String) -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent4
    ) {
        brackets.forEach { bracket ->
            IncomeOptionCard(
                incomeRange = bracket,
                isSelected = selectedIncome == bracket,
                onSelect = { onSelectIncome(bracket) },
                scheme = scheme
            )
        }
    }
}

/**
 * Privacy disclaimer footer note per Figma node 10-376.
 */
@Composable
fun IncomePrivacyDisclaimer(
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSText(
        modifier = modifier.fillMaxWidth(),
        text = "We use this to verify economic alignment. You can choose to show or hide this on your public card.",
        style = ODSTextStyles.bodySRegular,
        color = scheme.basicTextRecessive
    )
}

/**
 * Complete Step: Yearly Income Range.
 */
@Composable
fun PreferencesStepIncome(
    selectedIncome: String,
    scheme: ODSTheme,
    onIncomeSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    brackets: List<String> = DEFAULT_INCOME_BRACKETS
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent6
    ) {
        // ── 1. Heading ─────────────────────────────────────────────────────
        IncomeHeader(scheme = scheme)

        // ── 2. Income Option Cards ─────────────────────────────────────────
        IncomeOptionsList(
            brackets = brackets,
            selectedIncome = selectedIncome,
            onSelectIncome = onIncomeSelect,
            scheme = scheme
        )

        // ── 3. Privacy Disclaimer ──────────────────────────────────────────
        IncomePrivacyDisclaimer(scheme = scheme)
    }
}
