package com.app.screentime.landing.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Usage Summary Card component using ODS components.
 * Displays Today's Total and Daily Goal side by side in a card.
 * Matches the design shown in the image with large values and smaller labels.
 * Shows percentage change from yesterday if available.
 */
@Composable
fun UsageSummaryCard(
    modifier: Modifier = Modifier,
    todayTotal: String,
    dailyGoal: String = "6h",
    percentageChange: Float? = null,
    onClick: () -> Unit = {},
    scheme: ODSTheme = neutralScheme
) {
    ODSCardBasic(
        contentPadding = ODSPadding(
            horizontal = DSVariables.spacingComponent4,
            vertical = DSVariables.spacingComponent4
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        scheme = scheme,
        props = ODSCardBasicProps(),
        contentSlot = {
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSColumn(
                    modifier = Modifier.weight(1f),
                    gap = DSVariables.spacingComponent1
                ) {
                    ODSRow(
                        verticalAlignment = Alignment.CenterVertically,
                        gap = DSVariables.spacingComponent2
                    ) {
                        ODSText(
                            text = "Today's Total",
                            style = DSTextStyles.bodyMRegular,
                            color = scheme.basicTextRecessive
                        )
                        // Show percentage change if available
                        percentageChange?.let { change ->
                            val changeText = if (change > 0) {
                                "+${change.toInt()}%"
                            } else {
                                "${change.toInt()}%"
                            }
                            val changeColor = if (change > 0) {
                                scheme.functionalDestructiveStandard // Red for increase
                            } else {
                                scheme.functionalSuccessStandard // Green for decrease
                            }
                            ODSText(
                                text = changeText,
                                style = DSTextStyles.bodySRegular,
                                color = changeColor
                            )
                        }
                    }
                    ODSText(
                        text = todayTotal,
                        style = DSTextStyles.subtitle,
                        color = scheme.basicText
                    )
                }

                ODSColumn(
                    modifier = Modifier.weight(1f),
                    gap = DSVariables.spacingComponent1,
                    horizontalAlignment = Alignment.End
                ) {
                    ODSText(
                        text = "Daily Goal",
                        style = DSTextStyles.bodyMRegular,
                        color = scheme.basicTextRecessive
                    )
                    ODSText(
                        text = dailyGoal,
                        style = DSTextStyles.subtitle,
                        color = scheme.basicText
                    )
                }
            }
        }
    )
}

