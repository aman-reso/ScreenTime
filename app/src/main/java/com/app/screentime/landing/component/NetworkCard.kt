package com.app.screentime.landing.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalCellular4Bar
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.screentime.R
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.R.*
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.dataprogresstrack.ODSDataProgressTrack
import com.telekom.odsystem.atoms.dataprogresstrack.ODSDataProgressTrackMode
import com.telekom.odsystem.atoms.dataprogresstrack.ODSDataProgressTrackProps
import com.telekom.odsystem.atoms.dataprogresstrack.ODSDataProgressTrackSize
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.basketballSecondaryScheme
import com.telekom.odsystem.tokens.tokens.frogSecondaryScheme
import com.telekom.odsystem.tokens.tokens.hummingbirdSecondaryScheme
import com.telekom.odsystem.tokens.tokens.jacuzziSecondaryScheme
import com.telekom.odsystem.tokens.tokens.lagoonSecondaryScheme
import com.telekom.odsystem.tokens.tokens.macawSecondaryScheme
import com.telekom.odsystem.tokens.tokens.orchidSecondaryScheme
import kotlin.math.ceil

/**
 * Network data usage card component using ODS design system
 * Displays WiFi and Cellular data usage with percentages and progress bars
 *
 * @param modifier Modifier for the card
 * @param wifiDataUsage Total WiFi data usage in bytes
 * @param wifiDataUsageDisplay Formatted WiFi data usage string
 * @param cellularDataUsage Total Cellular data usage in bytes
 * @param cellularDataUsageDisplay Formatted Cellular data usage string
 * @param totalDataDisplayName Formatted total data usage string
 * @param scheme ODS theme scheme
 */
@Composable
fun NetworkCard(
    modifier: Modifier = Modifier,
    wifiDataUsage: Long = 0L,
    wifiDataUsageDisplay: String? = null,
    cellularDataUsage: Long = 0L,
    cellularDataUsageDisplay: String? = null,
    totalDataDisplayName: String? = null,
    scheme: ODSTheme = neutralScheme
) {
    val totalDataUsage = ceil((wifiDataUsage + cellularDataUsage).toDouble()).toInt()
    val wifiPercentage = if (totalDataUsage > 0) {
        (wifiDataUsage.toFloat() / totalDataUsage.toFloat() * 100f).toInt()
    } else 0
    val cellularPercentage = if (totalDataUsage > 0) {
        (cellularDataUsage.toFloat() / totalDataUsage.toFloat() * 100f).toInt()
    } else 0

    ODSCardBasic(
        modifier = modifier.fillMaxWidth(),
        scheme = scheme,
        props = ODSCardBasicProps(),
        contentPadding = ODSPadding(
            horizontal = DSVariables.spacingComponent5,
            vertical = DSVariables.spacingComponent5
        ),
        contentSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent5,
            ) {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSText(
                        text = stringResource(R.string.network_card_title),
                        style = DSTextStyles.bodyMRegular,
                        color = scheme.basicText
                    )

                    totalDataDisplayName?.let {
                        ODSText(
                            text = it,
                            style = DSTextStyles.bodyMBold,
                            color = scheme.basicTextRecessive
                        )
                    }
                }

                NetworkUsageSection(
                    label = buildString {
                        append(stringResource(R.string.network_wifi_label))
                        wifiDataUsageDisplay?.let {
                            append(" (")
                            append(it)
                            append(")")
                        }
                    },
                    percentage = wifiPercentage,
                    progress = wifiPercentage / 100f,
                    icon = ODSIconModel(
                        drawableRes = drawable.wifi_type_standard,
                        tint = scheme.basicText
                    ),
                    isWifi = true,
                    scheme = scheme
                )

                // Cellular section
                NetworkUsageSection(
                    label = buildString {
                        append(stringResource(R.string.network_cellular_label))
                        cellularDataUsageDisplay?.let {
                            append(" (")
                            append(it)
                            append(")")
                        }
                    },
                    percentage = cellularPercentage,
                    progress = cellularPercentage / 100f,
                    icon = ODSIconModel(
                        drawableRes = drawable.network_signal_type_standard,
                        tint = scheme.basicText
                    ),
                    isWifi = false,
                    scheme = scheme
                )
            }
        })
}

@Composable
private fun NetworkUsageSection(
    label: String,
    percentage: Int,
    progress: Float,
    icon: ODSIconModel,
    isWifi: Boolean,
    scheme: ODSTheme
) {
    // Use ODS DataProgressTrack mode
    // WiFi uses STANDARD mode, Cellular uses SUCCESS mode (green)
    val progressMode = if (isWifi) {
        ODSDataProgressTrackMode.STANDARD
    } else {
        ODSDataProgressTrackMode.SUCCESS
    }

    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        gap = DSVariables.spacingComponent3
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            gap = DSVariables.spacingComponent3
        ) {
            ODSIcon(
                iconModel = icon, width = 20.dp, height = 20.dp
            )
            ODSText(
                text = label,
                style = DSTextStyles.bodySRegular,
                color = scheme.basicText,
                modifier = Modifier.weight(1f)
            )
            ODSText(
                text = "$percentage%", style = DSTextStyles.bodySBold,
                color = scheme.basicText
            )
        }
        // Progress bar using ODS DataProgressTrack
        ODSDataProgressTrack(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme, props = ODSDataProgressTrackProps(
                progress = progress.coerceIn(0f, 1f),
                size = ODSDataProgressTrackSize.SMALL,
                mode = progressMode
            )
        )
    }
}
