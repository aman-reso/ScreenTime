package com.app.screentime.battery.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.BatteryStd
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.screentime.config.R
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.battery.model.BatteryHealth
import com.app.screentime.battery.model.ChargingStatus
import com.app.screentime.battery.viewmodel.BatteryViewModel
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.accordion.ODSAccordion
import com.telekom.odsystem.molecules.accordion.ODSAccordionProps
import com.telekom.odsystem.molecules.accordion.ODSAccordionSize
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandard
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardProps
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardVariant
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

/**
 * Battery Health Section component
 * Displays battery information, status, live current graph, temperature, and discharging rate
 */
@Composable
fun BatteryHealthSection(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    viewModel: BatteryViewModel = hiltViewModel()
) {
    val batteryInfo by viewModel.batteryInfo.collectAsState()

    // Store current readings for live graph (last 20 readings)
    var currentReadings by remember { mutableStateOf<List<Double>>(emptyList()) }

    // Update current readings periodically for live graph
    LaunchedEffect(batteryInfo) {
        while (true) {
            batteryInfo?.let { info ->
                val current = if (info.currentNow != 0) {
                    // Convert microamperes to milliamperes
                    abs(info.currentNow / 1000.0)
                } else {
                    0.0
                }
                currentReadings = (currentReadings + current).takeLast(20)
            }
            delay(2000) // Update every 2 seconds
        }
    }

    var isExpanded by remember { mutableStateOf(false) }

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = DSVariables.spacingComponent3
    ) {
        ODSAccordion(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSAccordionProps(
                headerText = stringResource(R.string.battery_health),
                expanded = isExpanded,
                size = ODSAccordionSize.SMALL
            ),
            onClick = { expanded ->
                isExpanded = expanded
                if (expanded) {
                    viewModel.trackBatteryHealth()
                }
            },
            contentSlot = {
                batteryInfo?.let { info ->
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = DSVariables.spacingComponent3
                    ) {
                        // Create list of cards
                        val cards = mutableListOf<@Composable () -> Unit>()

                        cards.add {
                            BatteryInfoCard(
                                batteryInfo = info,
                                scheme = scheme,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        cards.add {
                            BatteryStatusCard(
                                batteryInfo = info,
                                scheme = scheme,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (currentReadings.isNotEmpty()) {
                            cards.add {
                                BatteryCurrentGraphCard(
                                    currentReadings = currentReadings,
                                    scheme = scheme,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        cards.add {
                            BatteryMetricsCard(
                                batteryInfo = info,
                                scheme = scheme,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        cards.chunked(2).forEach { pair ->
                            ODSRow(
                                modifier = Modifier.fillMaxWidth(),
                                gap = DSVariables.spacingComponent3
                            ) {
                                pair.forEach { card ->
                                    card()
                                }
                                // Add spacer if odd number of items
                                if (pair.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                } ?: run {
                    ODSBox(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                        padding = ODSPadding(vertical = DSVariables.spacingComponent4)
                    ) {
                        ODSText(
                            text = stringResource(R.string.battery_information_unavailable),
                            style = DSTextStyles.bodyMRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }
            }
        )
    }
}

/**
 * Battery Information Card
 * Shows estimated time and charging percentage
 */
@Composable
private fun BatteryInfoCard(
    batteryInfo: com.app.screentime.battery.model.BatteryInfo,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSCardBasic(
        modifier = modifier.fillMaxWidth(),
        scheme = scheme,
        props = ODSCardBasicProps(),
        contentPadding = ODSPadding(
            horizontal = DSVariables.spacingComponent4,
            vertical = DSVariables.spacingComponent4
        ),
        contentSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = stringResource(R.string.battery_information),
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicText
                )

                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSColumn {
                        ODSText(
                            text = "${batteryInfo.level}%",
                            style = DSTextStyles.bodySRegular,
                            color = scheme.basicText
                        )
                        ODSText(
                            text = stringResource(R.string.battery_level),
                            style = DSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }

                    ODSColumn(horizontalAlignment = Alignment.End) {
                        val timeText = when {
                            batteryInfo.isCharging && batteryInfo.estimatedTimeToFull != null -> {
                                formatTime(batteryInfo.estimatedTimeToFull)
                            }

                            !batteryInfo.isCharging && batteryInfo.estimatedTimeRemaining != null -> {
                                formatTime(batteryInfo.estimatedTimeRemaining)
                            }

                            batteryInfo.isCharging -> stringResource(R.string.charging)
                            else -> stringResource(R.string.calculating)
                        }

                        val labelText = if (batteryInfo.isCharging) {
                            stringResource(R.string.time_to_full)
                        } else {
                            stringResource(R.string.time_remaining)
                        }

                        ODSText(
                            text = timeText,
                            style = DSTextStyles.bodySRegular,
                            color = scheme.basicText
                        )
                        ODSText(
                            text = labelText,
                            style = DSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }
            }
        }
    )
}

/**
 * Battery Status Card
 * Shows battery health status (Good, Bad, etc.)
 */
@Composable
private fun BatteryStatusCard(
    batteryInfo: com.app.screentime.battery.model.BatteryInfo,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    val statusText = when (batteryInfo.health) {
        BatteryHealth.GOOD -> stringResource(R.string.battery_health_good)
        BatteryHealth.OVERHEAT -> stringResource(R.string.battery_health_overheating)
        BatteryHealth.DEAD -> stringResource(R.string.battery_health_dead)
        BatteryHealth.OVER_VOLTAGE -> stringResource(R.string.battery_health_over_voltage)
        BatteryHealth.UNSPECIFIED_FAILURE -> stringResource(R.string.battery_health_failure)
        BatteryHealth.COLD -> stringResource(R.string.battery_health_cold)
        BatteryHealth.UNKNOWN -> stringResource(R.string.battery_health_unknown)
    }

    val statusColor = when (batteryInfo.health) {
        BatteryHealth.GOOD -> scheme.functionalSuccessStandard
        BatteryHealth.OVERHEAT, BatteryHealth.DEAD, BatteryHealth.OVER_VOLTAGE,
        BatteryHealth.UNSPECIFIED_FAILURE -> scheme.functionalDestructiveStandard

        BatteryHealth.COLD -> scheme.functionalWarningStandard
        BatteryHealth.UNKNOWN -> scheme.basicTextRecessive
    }

    val chargingStatusText = when (batteryInfo.chargingStatus) {
        ChargingStatus.NOT_CHARGING -> stringResource(R.string.not_charging)
        ChargingStatus.CHARGING -> stringResource(R.string.charging)
        ChargingStatus.CHARGING_VIA_USB -> stringResource(R.string.charging_via_usb)
        ChargingStatus.CHARGING_VIA_AC -> stringResource(R.string.charging_via_ac)
        ChargingStatus.CHARGING_VIA_WIRELESS -> stringResource(R.string.charging_wirelessly)
        ChargingStatus.FULL -> stringResource(R.string.fully_charged)
    }

    ODSCardBasic(
        modifier = modifier.fillMaxWidth(),
        scheme = scheme,
        props = ODSCardBasicProps(),
        contentPadding = ODSPadding(
            horizontal = DSVariables.spacingComponent4,
            vertical = DSVariables.spacingComponent4
        ),
        contentSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = stringResource(R.string.battery_status),
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicText
                )

                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSColumn {
                        ODSText(
                            text = statusText,
                            style = DSTextStyles.bodySRegular,
                            color = statusColor
                        )
                        ODSText(
                            text = stringResource(R.string.health),
                            style = DSTextStyles.bodyMRegular,
                            color = scheme.basicTextRecessive
                        )
                    }

                    ODSColumn(horizontalAlignment = Alignment.End) {
                        ODSText(
                            text = chargingStatusText,
                            style = DSTextStyles.bodySRegular,
                            color = scheme.basicText
                        )
                        ODSText(
                            text = stringResource(R.string.status),
                            style = DSTextStyles.bodyMRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }
            }
        }
    )
}

/**
 * Live Current Sparkline Card
 * Shows a live sparkline of electric current
 */
@Composable
private fun BatteryCurrentGraphCard(
    currentReadings: List<Double>,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    val maxCurrent = remember(currentReadings) {
        if (currentReadings.isNotEmpty()) {
            currentReadings.maxOrNull() ?: 1.0
        } else {
            1.0
        }
    }

    val minCurrent = remember(currentReadings) {
        if (currentReadings.isNotEmpty()) {
            currentReadings.minOrNull() ?: 0.0
        } else {
            0.0
        }
    }

    val currentRange = remember(maxCurrent, minCurrent) {
        if (maxCurrent > minCurrent) {
            maxCurrent - minCurrent
        } else {
            1.0
        }
    }

    ODSCardBasic(
        modifier = modifier.fillMaxWidth(),
        scheme = scheme,
        props = ODSCardBasicProps(),
        contentPadding = ODSPadding(
            horizontal = DSVariables.spacingComponent4,
            vertical = DSVariables.spacingComponent4
        ),
        contentSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = stringResource(R.string.electric_current),
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicText
                )

                if (currentReadings.isNotEmpty()) {
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val width = size.width
                            val height = size.height
                            val padding = 8.dp.toPx()
                            val chartWidth = width - (padding * 2)
                            val chartHeight = height - (padding * 2)

                            // Draw sparkline path
                            val path = Path()
                            val pointCount = currentReadings.size

                            if (pointCount > 1) {
                                currentReadings.forEachIndexed { index, current ->
                                    val x =
                                        padding + (index.toFloat() / (pointCount - 1).coerceAtLeast(
                                            1
                                        )) * chartWidth
                                    val normalizedValue = if (currentRange > 0) {
                                        (current - minCurrent) / currentRange
                                    } else {
                                        0.5f
                                    }
                                    val y =
                                        padding + chartHeight - (normalizedValue.toFloat() * chartHeight)

                                    if (index == 0) {
                                        path.moveTo(x, y)
                                    } else {
                                        path.lineTo(x, y)
                                    }
                                }

                                // Draw the line
                                drawPath(
                                    path = path,
                                    color = scheme.functionalSuccessStandard.getColor(),
                                    style = Stroke(
                                        width = 2.dp.toPx(),
                                        cap = StrokeCap.Round,
                                        join = StrokeJoin.Round
                                    )
                                )

                                // Draw points
                                currentReadings.forEachIndexed { index, current ->
                                    val x =
                                        padding + (index.toFloat() / (pointCount - 1).coerceAtLeast(
                                            1
                                        )) * chartWidth
                                    val normalizedValue = if (currentRange > 0) {
                                        (current - minCurrent) / currentRange
                                    } else {
                                        0.5f
                                    }
                                    val y =
                                        padding + chartHeight - (normalizedValue.toFloat() * chartHeight)

                                    drawCircle(
                                        color = scheme.functionalSuccessStandard.getColor(),
                                        radius = 3.dp.toPx(),
                                        center = Offset(x, y)
                                    )
                                }
                            }
                        }
                    }

                    // Show current value
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ODSText(
                            text = stringResource(R.string.current_ma, currentReadings.lastOrNull()?.toInt() ?: 0),
                            style = DSTextStyles.bodySRegular,
                            color = scheme.basicTextRecessive
                        )
                        ODSText(
                            text = stringResource(R.string.max_ma, maxCurrent.toInt()),
                            style = DSTextStyles.bodySRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                } else {
                    ODSText(
                        text = stringResource(R.string.no_data_available_battery),
                        style = DSTextStyles.bodySRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            }
        }
    )
}

/**
 * Battery Metrics Card
 * Shows temperature and discharging rate
 */
@Composable
private fun BatteryMetricsCard(
    batteryInfo: com.app.screentime.battery.model.BatteryInfo,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSCardBasic(
        modifier = modifier.fillMaxWidth(),
        scheme = scheme,
        props = ODSCardBasicProps(),
        contentPadding = ODSPadding(
            horizontal = DSVariables.spacingComponent4,
            vertical = DSVariables.spacingComponent4
        ),
        contentSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = stringResource(R.string.battery_metrics),
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicText
                )

                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Temperature
                    ODSRow(
                        horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                imageVector = Icons.Default.Thermostat,
                                tint = scheme.basicText,
                                contentDescription = "Temperature"
                            ),
                            modifier = Modifier.size(24.dp)
                        )
                        ODSColumn {
                            ODSText(
                                text = String.format("%.1f°C", batteryInfo.temperature),
                                style = DSTextStyles.bodySRegular,
                                color = scheme.basicText
                            )
                            ODSText(
                                text = stringResource(R.string.temperature),
                                style = DSTextStyles.microcopyRegular,
                                color = scheme.basicTextRecessive
                            )
                        }
                    }

                    // Discharging Rate
                    ODSRow(
                        horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val batteryIcon = when {
                            batteryInfo.isCharging -> Icons.Default.BatteryChargingFull
                            batteryInfo.level > 50 -> Icons.Default.BatteryFull
                            else -> Icons.Default.BatteryStd
                        }
                        ODSIcon(
                            iconModel = ODSIconModel(
                                imageVector = batteryIcon,
                                tint = scheme.basicText,
                                contentDescription = "Discharge Rate"
                            ),
                            modifier = Modifier.size(24.dp)
                        )
                        ODSColumn(horizontalAlignment = Alignment.End) {
                            ODSText(
                                text = if (batteryInfo.dischargingRate > 0) {
                                    String.format("%.1f mA/h", batteryInfo.dischargingRate)
                                } else {
                                    "N/A"
                                },
                                style = DSTextStyles.bodySRegular,
                                color = scheme.basicText
                            )
                            ODSText(
                                text = stringResource(R.string.discharge_rate),
                                style = DSTextStyles.microcopyRegular,
                                color = scheme.basicTextRecessive
                            )
                        }
                    }
                }
            }
        }
    )
}

/**
 * App Battery Usage Card
 * Shows battery consumption by app
 */
@Composable
private fun AppBatteryUsageCard(
    appBatteryUsage: List<com.app.screentime.battery.model.AppBatteryUsage>,
    scheme: ODSTheme
) {
    ODSCardBasic(
        modifier = Modifier.fillMaxWidth(),
        scheme = scheme,
        props = ODSCardBasicProps(),
        contentPadding = ODSPadding(
            horizontal = DSVariables.spacingComponent4,
            vertical = DSVariables.spacingComponent4
        ),
        contentSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = stringResource(R.string.battery_usage_by_app),
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicText
                )

                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = DSVariables.spacingComponent2
                ) {
                    appBatteryUsage.forEach { appUsage ->
                        ODSBox(
                            modifier = Modifier.fillMaxWidth(),
                            background = listOf(ODSColorModel(scheme.basicBackgroundCard)),
                            cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
                            padding = ODSPadding(horizontal = DSVariables.spacingComponent4)
                        ) {
                            ODSListRowStandard(
                                modifier = Modifier,
                                scheme = scheme,
                                props = ODSListRowStandardProps(
                                    variant = ODSListRowStandardVariant.STANDARD,
                                    labelText = appUsage.appName,
                                    descriptionTitle = String.format(
                                        "%.1f%%",
                                        appUsage.estimatedBatteryPercent
                                    ),
                                    descriptionText = formatTime(appUsage.usageTimeMs)
                                )
                            )
                        }
                    }
                }
            }
        }
    )
}

/**
 * Format time in milliseconds to readable string
 */
private fun formatTime(timeMs: Long): String {
    val hours = timeMs / (1000 * 60 * 60)
    val minutes = (timeMs % (1000 * 60 * 60)) / (1000 * 60)

    return when {
        hours > 0 -> "${hours}h ${minutes}m"
        minutes > 0 -> "${minutes}m"
        else -> "< 1m"
    }
}

