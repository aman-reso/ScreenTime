package com.app.screentime.landing.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.screentime.config.R
import com.app.screentime.customisation.model.ColorOption
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.jacuzziSecondaryScheme
import java.util.Calendar

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
    onClick: (() -> Unit) = {}
) {
    // Use custom scheme if provided, otherwise use default scheme
    val activeScheme = customColor?.scheme ?: scheme

    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        background = listOf(ODSColorModel(activeScheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(DSVariables.radiusMedium),
        padding = ODSPadding(all = DSVariables.spacingComponent6)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DSVariables.spacingComponent2)
        ) {
            // Main content
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                gap = DSVariables.spacingComponent3,
                horizontalAlignment = Alignment.Start
            ) {
                // Title
                ODSText(
                    text = stringResource(R.string.yearly_usage_stats_title, year),
                    style = DSTextStyles.titleL,
                    color = activeScheme.basicText
                )

                Spacer(modifier = Modifier.height(DSVariables.spacingComponent2))

                // Daily Screen Time
                ODSColumn(
                    gap = DSVariables.spacingComponent1
                ) {
                    ODSText(
                        text = stringResource(R.string.daily_screen_time),
                        style = DSTextStyles.microcopyRegular,
                        color = activeScheme.basicTextRecessive
                    )

                    ODSText(
                        text = dailyScreenTime,
                        style = DSTextStyles.bodyMBold,
                        color = activeScheme.basicText
                    )
                }

                Spacer(modifier = Modifier.height(DSVariables.spacingComponent2))

                // Yearly Screen Time
                ODSColumn(
                    gap = DSVariables.spacingComponent1
                ) {
                    ODSText(
                        text = stringResource(R.string.yearly_screen_time),
                        style = DSTextStyles.microcopyRegular,
                        color = activeScheme.basicTextRecessive
                    )

                    ODSText(
                        text = yearlyScreenTime,
                        style = DSTextStyles.bodyMBold,
                        color = activeScheme.basicText
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = DSVariables.spacingComponent4)
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        imageVector = Icons.Default.PhoneAndroid,
                        tint = activeScheme.basicTextRecessive,
                        contentDescription = "Mobile phone"
                    ),
                    modifier = Modifier
                        .size(80.dp)
                        .graphicsLayer(
                            rotationZ = 15f,
                            translationX = 20f
                        ),
                    width = 80.dp,
                    height = 80.dp
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
