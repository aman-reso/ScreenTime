package com.app.screentime.landing.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R
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
    dailyGoal: String? = "6h",
    notificationCount: Int? = null,
    percentageChange: Float? = null,
    onClick: () -> Unit = {},
    onEditDailyGoal: (() -> Unit)? = null,
    scheme: ODSTheme = neutralScheme,
    dateLabel: String? = null // NEW: Custom date label (e.g., "21 Jan" or null for "Today's Total")
) {
    val editIconInteractionSource = remember { MutableInteractionSource() }

    ODSCardBasic(
        onClick = onClick,
        contentPadding = ODSPadding(
            horizontal = DSVariables.spacingComponent4,
            vertical = DSVariables.spacingComponent4
        ),
        modifier = modifier
            .fillMaxWidth(),
        scheme = scheme,
        props = ODSCardBasicProps(),
        contentSlot = {
            ODSColumn(modifier = Modifier.fillMaxWidth()) {
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
                        ODSText(
                            text = dateLabel ?: stringResource(R.string.todays_total),
                            style = DSTextStyles.bodyMRegular,
                            color = scheme.basicTextRecessive
                        )
                        ODSText(
                            text = todayTotal,
                            style = DSTextStyles.subtitle,
                            color = scheme.basicText
                        )
                    }

                    // Only show daily goal if not null
                    if (dailyGoal != null) {
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
                                    text = stringResource(R.string.daily_goal),
                                    style = DSTextStyles.bodyMRegular,
                                    color = scheme.basicTextRecessive
                                )
                                onEditDailyGoal?.let {
                                ODSIcon(
                                    modifier = Modifier.clickable(
                                        interactionSource = editIconInteractionSource,
                                        indication = null,
                                        onClick = it
                                    ),
                                    iconModel = ODSIconModel(
                                        drawableRes = com.telekom.odsystem.R.drawable.edit_type_standard,
                                        contentDescription = stringResource(com.app.screentime.config.R.string.edit_daily_goal),
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
                    }

                    notificationCount?.let { count ->
                        ODSColumn(
                            modifier = Modifier.weight(1f),
                            gap = DSVariables.spacingComponent1,
                            horizontalAlignment = Alignment.End
                        ) {
                            ODSText(
                                text = stringResource(R.string.notifications),
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
                percentageChange?.let { change ->
                    if (change != 0f) {
                        val isIncrease = change > 0
                        val changeText = "${kotlin.math.abs(change.toInt())}%"
                        val arrowIcon = if (isIncrease) {
                            com.telekom.odsystem.R.drawable.collapse_up_type_standard
                        } else {
                            com.telekom.odsystem.R.drawable.collapse_down_type_standard_size_standard
                        }
                        ODSRow(
                            verticalAlignment = Alignment.CenterVertically,
                            gap = 2.dp
                        ) {
                            ODSText(
                                text = if (isIncrease) {
                                    stringResource(com.app.screentime.config.R.string.more_than_yesterday, changeText)
                                } else {
                                    stringResource(com.app.screentime.config.R.string.less_than_yesterday, changeText)
                                },
                                style = DSTextStyles.bodySRegular,
                                color = scheme.basicText
                            )
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    drawableRes = arrowIcon,
                                    contentDescription = if (isIncrease) {
                                        stringResource(com.app.screentime.config.R.string.increased)
                                    } else {
                                        stringResource(com.app.screentime.config.R.string.decreased)
                                    },
                                    tint = scheme.basicText
                                ),
                                width = 14.dp,
                                height = 14.dp
                            )
                        }
                    }
                }
            }
        }
    )
}