package com.app.screentime.network.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.config.R
import android.net.TrafficStats
import kotlinx.coroutines.delay
import com.app.screentime.network.model.ConnectionType
import com.app.screentime.network.model.NetworkHealth
import com.app.screentime.network.utils.NetworkUtils
import com.app.screentime.network.utils.TrafficUtils
import com.app.screentime.network.viewmodel.NetworkViewModel
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.accordion.ODSAccordion
import com.telekom.odsystem.molecules.accordion.ODSAccordionProps
import com.telekom.odsystem.molecules.accordion.ODSAccordionSize
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import com.telekom.odsystem.tokens.tokens.ODSTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalWifi4Bar
import androidx.compose.material.icons.filled.SignalCellular4Bar
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.platform.LocalContext

/**
 * Entry point for Hilt injection in composables
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface NetworkUtilsEntryPoint {
    fun networkUtils(): NetworkUtils
}

/**
 * Network Health Section component
 * Displays network information, connection status, signal strength, and network details
 */
@Composable
fun NetworkHealthSection(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    viewModel: NetworkViewModel = hiltViewModel()
) {
    val networkInfo by viewModel.networkInfo.collectAsState()
    val context = LocalContext.current
    val networkUtils = remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            NetworkUtilsEntryPoint::class.java
        ).networkUtils()
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
                headerText = stringResource(R.string.network_health),
                expanded = isExpanded,
                size = ODSAccordionSize.SMALL
            ),
            onClick = { expanded ->
                isExpanded = expanded
            },
            contentSlot = {
                networkInfo?.let { info ->
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = DSVariables.spacingComponent3
                    ) {
                        // Network Information Card
                        NetworkInfoCard(
                            networkInfo = info,
                            scheme = scheme,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Network Status Card
                        NetworkStatusCard(
                            networkInfo = info,
                            scheme = scheme,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Connection Details Card
                        if (info.isConnected) {
                            ConnectionDetailsCard(
                                networkInfo = info,
                                scheme = scheme,
                                modifier = Modifier.fillMaxWidth()
                            )

                            NetworkSpeedCard(
                                scheme = scheme,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // eSIM Compatibility Card
                        EsimCompatibilityCard(
                            networkUtils = networkUtils,
                            scheme = scheme,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } ?: run {
//                    ODSText(
//                        text = "No network information available",
//                        style = DSTextStyles.bodyMRegular,
//                        color = scheme.basicTextRecessive
//                    )

                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = DSVariables.spacingComponent3
                    ) {
                        NetworkSpeedCard(
                            scheme = scheme,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // eSIM Compatibility Card
                        EsimCompatibilityCard(
                            networkUtils = networkUtils,
                            scheme = scheme,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        )
    }
}

/**
 * Network Information Card
 * Shows connection type and IP address
 */
@Composable
private fun NetworkInfoCard(
    networkInfo: com.app.screentime.network.model.NetworkInfo,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    val connectionTypeText = when (networkInfo.connectionType) {
        ConnectionType.WIFI -> stringResource(R.string.wifi)
        ConnectionType.MOBILE -> stringResource(R.string.mobile_data)
        ConnectionType.ETHERNET -> stringResource(R.string.ethernet)
        ConnectionType.VPN -> stringResource(R.string.vpn)
        ConnectionType.NONE -> stringResource(R.string.no_connection)
    }

    val connectionIcon = when (networkInfo.connectionType) {
        ConnectionType.WIFI -> Icons.Default.SignalWifi4Bar
        ConnectionType.MOBILE -> Icons.Default.SignalCellular4Bar
        ConnectionType.ETHERNET -> Icons.Default.Info
        ConnectionType.VPN -> Icons.Default.Info
        ConnectionType.NONE -> Icons.Default.WifiOff
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
                    text = stringResource(R.string.network_information),
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicText
                )

                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSRow(
                        horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent2),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                imageVector = connectionIcon,
                                tint = if (networkInfo.isConnected) scheme.functionalSuccessStandard else scheme.basicTextRecessive,
                                contentDescription = connectionTypeText
                            ),
                            width = 24.dp,
                            height = 24.dp
                        )
                        ODSColumn {
                            ODSText(
                                text = connectionTypeText,
                                style = DSTextStyles.bodySRegular,
                                color = scheme.basicText
                            )
                            ODSText(
                                text = stringResource(R.string.connection_type),
                                style = DSTextStyles.microcopyRegular,
                                color = scheme.basicTextRecessive
                            )
                        }
                    }

                    ODSColumn(horizontalAlignment = Alignment.End) {
                        ODSText(
                            text = networkInfo.ipAddress ?: stringResource(R.string.na),
                            style = DSTextStyles.bodySRegular,
                            color = scheme.basicText
                        )
                        ODSText(
                            text = stringResource(R.string.ip_address),
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
 * Network Status Card
 * Shows signal strength and network operator/SSID
 */
@Composable
private fun NetworkStatusCard(
    networkInfo: com.app.screentime.network.model.NetworkInfo,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    val statusText = when {
        !networkInfo.isConnected -> stringResource(R.string.not_connected)
        networkInfo.signalStrength == null -> stringResource(R.string.connected)
        networkInfo.signalStrength >= -70 -> stringResource(R.string.excellent)
        networkInfo.signalStrength >= -85 -> stringResource(R.string.good)
        networkInfo.signalStrength >= -100 -> stringResource(R.string.fair)
        else -> stringResource(R.string.poor)
    }

    val statusColor = when {
        !networkInfo.isConnected -> scheme.functionalDestructiveStandard
        networkInfo.signalStrength == null -> scheme.basicText
        networkInfo.signalStrength >= -70 -> scheme.functionalSuccessStandard
        networkInfo.signalStrength >= -85 -> scheme.functionalSuccessStandard
        networkInfo.signalStrength >= -100 -> scheme.functionalWarningStandard
        else -> scheme.functionalDestructiveStandard
    }

    val networkName = when (networkInfo.connectionType) {
        ConnectionType.WIFI -> networkInfo.wifiSSID ?: stringResource(R.string.unknown_wifi)
        ConnectionType.MOBILE -> networkInfo.networkOperator ?: stringResource(R.string.unknown_operator)
        else -> stringResource(R.string.na)
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
                    text = stringResource(R.string.network_status),
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
                            text = stringResource(R.string.signal_strength),
                            style = DSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }

                    ODSColumn(horizontalAlignment = Alignment.End) {
                        ODSText(
                            text = networkName,
                            style = DSTextStyles.bodySRegular,
                            color = scheme.basicText
                        )
                        ODSText(
                            text = if (networkInfo.connectionType == ConnectionType.WIFI) stringResource(R.string.wifi_network) else stringResource(R.string.network_operator),
                            style = DSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }

                // Show signal strength in dBm if available
                networkInfo.signalStrength?.let { signal ->
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ODSText(
                            text = "${signal} dBm",
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
 * Connection Details Card
 * Shows additional connection details like roaming status
 */
@Composable
private fun ConnectionDetailsCard(
    networkInfo: com.app.screentime.network.model.NetworkInfo,
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
                    text = stringResource(R.string.connection_details),
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
                            text = if (networkInfo.isRoaming) stringResource(R.string.yes) else stringResource(R.string.no),
                            style = DSTextStyles.bodySRegular,
                            color = if (networkInfo.isRoaming) scheme.functionalWarningStandard else scheme.basicText
                        )
                        ODSText(
                            text = stringResource(R.string.roaming),
                            style = DSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }

                    ODSColumn(horizontalAlignment = Alignment.End) {
                        ODSText(
                            text = if (networkInfo.isConnected) stringResource(R.string.active) else stringResource(R.string.inactive),
                            style = DSTextStyles.bodySRegular,
                            color = if (networkInfo.isConnected) scheme.functionalSuccessStandard else scheme.basicTextRecessive
                        )
                        ODSText(
                            text = stringResource(R.string.connection),
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
 * Network Speed Card
 * Shows real-time download and upload speeds for total, WiFi, and mobile
 * Uses TrafficUtils approach with mobile/total separation
 */
@Composable
private fun NetworkSpeedCard(
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    var totalDownloadSpeed by remember { mutableStateOf("0 KB/s") }
    var totalUploadSpeed by remember { mutableStateOf("0 KB/s") }
    var wifiDownloadSpeed by remember { mutableStateOf("0 KB/s") }
    var wifiUploadSpeed by remember { mutableStateOf("0 KB/s") }
    var mobileDownloadSpeed by remember { mutableStateOf("0 KB/s") }
    var mobileUploadSpeed by remember { mutableStateOf("0 KB/s") }

    // Track previous values for speed calculation
    var lastTotalRxBytes by remember { mutableStateOf(0L) }
    var lastTotalTxBytes by remember { mutableStateOf(0L) }
    var lastMobileRxBytes by remember { mutableStateOf(0L) }
    var lastMobileTxBytes by remember { mutableStateOf(0L) }
    var isInitialized by remember { mutableStateOf(false) }

    // Monitor network speed using TrafficUtils approach
    LaunchedEffect(Unit) {
        // Initialize first reading
        lastTotalRxBytes = TrafficStats.getTotalRxBytes()
        lastTotalTxBytes = TrafficStats.getTotalTxBytes()
        lastMobileRxBytes = TrafficStats.getMobileRxBytes()
        lastMobileTxBytes = TrafficStats.getMobileTxBytes()

        while (true) {
            delay(1000) // Wait 1 second (non-blocking)

            // Get current values
            val currentTotalRxBytes = TrafficStats.getTotalRxBytes()
            val currentTotalTxBytes = TrafficStats.getTotalTxBytes()
            val currentMobileRxBytes = TrafficStats.getMobileRxBytes()
            val currentMobileTxBytes = TrafficStats.getMobileTxBytes()

            if (isInitialized) {
                // Calculate speeds in bytes per second
                val totalDownloadBytes = currentTotalRxBytes - lastTotalRxBytes
                val totalUploadBytes = currentTotalTxBytes - lastTotalTxBytes
                val mobileDownloadBytes = currentMobileRxBytes - lastMobileRxBytes
                val mobileUploadBytes = currentMobileTxBytes - lastMobileTxBytes

                // WiFi = Total - Mobile
                val wifiDownloadBytes = (totalDownloadBytes - mobileDownloadBytes).coerceAtLeast(0L)
                val wifiUploadBytes = (totalUploadBytes - mobileUploadBytes).coerceAtLeast(0L)

                // Format using TrafficUtils
                totalDownloadSpeed =
                    TrafficUtils.getMetricData(if (totalDownloadBytes > 0) totalDownloadBytes else 0L) + "/s"
                totalUploadSpeed =
                    TrafficUtils.getMetricData(if (totalUploadBytes > 0) totalUploadBytes else 0L) + "/s"
                wifiDownloadSpeed =
                    TrafficUtils.getMetricData(if (wifiDownloadBytes > 0) wifiDownloadBytes else 0L) + "/s"
                wifiUploadSpeed =
                    TrafficUtils.getMetricData(if (wifiUploadBytes > 0) wifiUploadBytes else 0L) + "/s"
                mobileDownloadSpeed =
                    TrafficUtils.getMetricData(if (mobileDownloadBytes > 0) mobileDownloadBytes else 0L) + "/s"
                mobileUploadSpeed =
                    TrafficUtils.getMetricData(if (mobileUploadBytes > 0) mobileUploadBytes else 0L) + "/s"
            }

            // Update last values for next iteration
            lastTotalRxBytes = currentTotalRxBytes
            lastTotalTxBytes = currentTotalTxBytes
            lastMobileRxBytes = currentMobileRxBytes
            lastMobileTxBytes = currentMobileTxBytes
            isInitialized = true // Mark as initialized after first delay
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
                    text = stringResource(R.string.network_speed),
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicText
                )

                // Total Speed
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSColumn {
                        ODSText(
                            text = totalDownloadSpeed,
                            style = DSTextStyles.bodySRegular,
                            color = scheme.functionalSuccessStandard
                        )
                        ODSText(
                            text = stringResource(R.string.total_download),
                            style = DSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }

                    ODSColumn(horizontalAlignment = Alignment.End) {
                        ODSText(
                            text = totalUploadSpeed,
                            style = DSTextStyles.bodySRegular,
                            color = scheme.functionalWarningStandard
                        )
                        ODSText(
                            text = stringResource(R.string.total_upload),
                            style = DSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }

                // WiFi Speed
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSColumn {
                        ODSText(
                            text = wifiDownloadSpeed,
                            style = DSTextStyles.bodySRegular,
                            color = scheme.functionalSuccessStandard
                        )
                        ODSText(
                            text = stringResource(R.string.wifi_download),
                            style = DSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }

                    ODSColumn(horizontalAlignment = Alignment.End) {
                        ODSText(
                            text = wifiUploadSpeed,
                            style = DSTextStyles.bodySRegular,
                            color = scheme.functionalWarningStandard
                        )
                        ODSText(
                            text = stringResource(R.string.wifi_upload),
                            style = DSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }

                // Mobile Speed
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSColumn {
                        ODSText(
                            text = mobileDownloadSpeed,
                            style = DSTextStyles.bodySRegular,
                            color = scheme.functionalSuccessStandard
                        )
                        ODSText(
                            text = stringResource(R.string.mobile_download),
                            style = DSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }

                    ODSColumn(horizontalAlignment = Alignment.End) {
                        ODSText(
                            text = mobileUploadSpeed,
                            style = DSTextStyles.bodySRegular,
                            color = scheme.functionalWarningStandard
                        )
                        ODSText(
                            text = stringResource(R.string.mobile_upload),
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
 * eSIM Compatibility Card
 * Shows whether the device supports eSIM or not
 */
@Composable
private fun EsimCompatibilityCard(
    networkUtils: NetworkUtils,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    val isEsimCompatible = remember { networkUtils.isEsimCompatible() }
    val compatibilityText = if (isEsimCompatible) stringResource(R.string.yes) else stringResource(R.string.no)
    val compatibilityColor = if (isEsimCompatible) scheme.functionalSuccessStandard else scheme.basicTextRecessive

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
                    text = stringResource(R.string.esim_compatibility),
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
                            text = compatibilityText,
                            style = DSTextStyles.bodySRegular,
                            color = compatibilityColor
                        )
                        ODSText(
                            text = stringResource(R.string.device_support),
                            style = DSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }

                    ODSColumn(horizontalAlignment = Alignment.End) {
                        ODSText(
                            text = if (isEsimCompatible) stringResource(R.string.supported) else stringResource(R.string.not_supported),
                            style = DSTextStyles.bodySRegular,
                            color = compatibilityColor
                        )
                        ODSText(
                            text = stringResource(R.string.status),
                            style = DSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }
            }
        }
    )
}

