package com.app.screentime.landing.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.app.screentime.R
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Usage Summary Card component using ODS components.
 * Displays Today's Total, Daily Goal, and Notification Count in a card.
 * Matches the design shown in the image with large values and smaller labels.
 * Shows percentage change from yesterday if available.
 */
@Composable
fun UsageSummaryCard(
    modifier: Modifier = Modifier,
    todayTotal: String,
    dailyGoal: String = "6h",
    notificationCount: Int? = null,
    percentageChange: Float? = null,
    onClick: () -> Unit = {},
    onEditDailyGoal: (() -> Unit)? = null,
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
                        percentageChange?.let { change ->
                            val changeText = if (change > 0) {
                                "+${change.toInt()}%"
                            } else {
                                "${change.toInt()}%"
                            }
                            val changeColor = if (change > 0) {
                                scheme.basicTextDominant // Red for increase
                            } else {
                                scheme.basicTextDominant // Green for decrease
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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ODSRow(
                        verticalAlignment = Alignment.CenterVertically,
                        gap = DSVariables.spacingComponent1
                    ) {
                        ODSText(
                            text = "Daily Goal",
                            style = DSTextStyles.bodyMRegular,
                            color = scheme.basicTextRecessive
                        )
                        onEditDailyGoal?.let {
                            ODSIcon(
                                modifier = Modifier.clickable { it() },
                                iconModel = ODSIconModel(
                                    drawableRes = com.telekom.odsystem.R.drawable.edit_type_standard,
                                    contentDescription = "Edit daily goal",
                                    tint = scheme.basicTextRecessive
                                ),
                                width = 20.dp,
                                height = 20.dp
                            )
                        }
                    }
                    ODSText(
                        text = dailyGoal,
                        style = DSTextStyles.subtitle,
                        color = scheme.basicText
                    )
                }

                notificationCount?.let { count ->
                    ODSColumn(
                        modifier = Modifier.weight(1f),
                        gap = DSVariables.spacingComponent1,
                        horizontalAlignment = Alignment.End
                    ) {
                        ODSText(
                            text = "Notifications",
                            style = DSTextStyles.bodyMRegular,
                            color = scheme.basicTextRecessive
                        )
                        ODSText(
                            text = count.toString(),
                            style = DSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )
                    }
                }
            }
        }
    )
}

