package com.app.screentime.landing.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.screentime.config.R
import com.app.screentime.customisation.model.ColorOption
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.link.ODSLink
import com.telekom.odsystem.atoms.link.ODSLinkAlignment
import com.telekom.odsystem.atoms.link.ODSLinkProps
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.jacuzziSecondaryScheme
import java.util.Calendar
import kotlin.Float

/**
 * Yearly Usage Stats Card
 * Shows a beautiful card with yearly and daily mobile usage statistics
 * Uses custom color from customisation settings
 */
@Composable
fun YearlyUsageStatsCard(
    modifier: Modifier = Modifier,
    year: Int = Calendar.getInstance().get(Calendar.YEAR),
    dailyScreenTime: String,
    yearlyScreenTime: String,
    customColor: ColorOption? = null,
    scheme: ODSTheme = jacuzziSecondaryScheme,
    onClick: (() -> Unit) = {},
    percentageChange: Float? = null,
    navigateToCustomisation: () -> Unit = {},
    dailyGoal: String? = "6h",
    onEditDailyGoal: (() -> Unit)? = null,
) {
    // Use custom scheme if provided, otherwise use default scheme
    val activeScheme = customColor?.scheme ?: scheme

    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick.invoke()
            },
        background = listOf(ODSColorModel(activeScheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(DSVariables.radiusMedium),
        padding = ODSPadding(all = DSVariables.spacingComponent5)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                gap = DSVariables.spacingComponent3,
                horizontalAlignment = Alignment.Start
            ) {

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
                            text = stringResource(R.string.todays_total),
                            style = DSTextStyles.bodyMRegular,
                            color = scheme.basicTextRecessive
                        )
                        ODSText(
                            text = dailyScreenTime,
                            style = DSTextStyles.subtitle,
                            color = scheme.basicText
                        )
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
                                            stringResource(
                                                com.app.screentime.config.R.string.more_than_yesterday,
                                                changeText
                                            )
                                        } else {
                                            stringResource(
                                                com.app.screentime.config.R.string.less_than_yesterday,
                                                changeText
                                            )
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

                    if (dailyGoal != null) {
                        ODSColumn(
                            gap = DSVariables.spacingComponent1,
                            horizontalAlignment = Alignment.End
                        ) {
                            ODSRow(
                                verticalAlignment = Alignment.CenterVertically,
                                gap = DSVariables.spacingComponent1
                            ) {
                                ODSText(
                                    text = stringResource(R.string.daily_goal),
                                    style = DSTextStyles.bodyMRegular,
                                    color = scheme.basicText
                                )
                                onEditDailyGoal?.let {
                                    ODSIcon(
                                        modifier = Modifier.clickable(
                                            onClick = it
                                        ),
                                        iconModel = ODSIconModel(
                                            drawableRes = com.telekom.odsystem.R.drawable.edit_type_standard,
                                            contentDescription = stringResource(com.app.screentime.config.R.string.edit_daily_goal),
                                            tint = scheme.basicText
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
                }

                Spacer(modifier = Modifier.height(DSVariables.spacingComponent2))

                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSColumn(
                        gap = DSVariables.spacingComponent1
                    ) {
                        ODSText(
                            text = stringResource(R.string.yearly_usage_stats_title, year),
                            style = DSTextStyles.bodySRegular,
                            color = activeScheme.basicText
                        )

                        ODSText(
                            text = yearlyScreenTime,
                            style = DSTextStyles.bodyMBold,
                            color = activeScheme.basicText
                        )
                    }

                    ODSLink(
                        onClick = navigateToCustomisation,
                        scheme = activeScheme,
                        props = ODSLinkProps(
                            alignment = ODSLinkAlignment.RIGHT,
                            label = stringResource(R.string.customize)
                        )
                    )
                }
            }

            // Dotted vertical line in center
            Canvas(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(2.dp)
                    .fillMaxHeight()
            ) {
                val pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(6f, 6f),
                    phase = 0f
                )
                drawLine(
                    color = activeScheme.basicStrokeSubtle.getColor(),
                    start = Offset(size.width / 2, 0f),
                    end = Offset(size.width / 2, size.height),
                    strokeWidth = 2f,
                    pathEffect = pathEffect
                )
            }
        }
    }
}

/**
 * Preview-friendly version with sample data
 */
@Preview(showBackground = true)
@Composable
fun YearlyUsageStatsCardPreview() {
    YearlyUsageStatsCard(
        year = 2026,
        dailyScreenTime = "5h 30m",
        yearlyScreenTime = "240h",
        customColor = ColorOption.DEFAULT_PALETTE[7], // Green color
        scheme = jacuzziSecondaryScheme
    )
}
