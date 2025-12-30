package com.app.screentime.statistics.screen

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues

import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.R
import com.app.screentime.data.uiModel.WeeklyDataReport
import com.app.screentime.navigation.Screen
import com.app.screentime.statistics.model.ChartFormatterProps
import com.app.screentime.statistics.viewmodel.StatisticsViewModel
import com.app.screentime.ui.atom.appUsageListUi
import com.app.screentime.ui.theme.LocalThemeMode
import com.app.screentime.ui.theme.headerTheme
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables

import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.barchart.ODSBarChart
import com.telekom.odsystem.organisms.barchart.ODSBarChartProps
import com.telekom.odsystem.organisms.barchart.ODSBarItemDirection
import com.telekom.odsystem.organisms.barchart.ODSBarItemProps
import com.telekom.odsystem.organisms.barchart.ODSBarItemShape
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import com.telekom.odsystem.tokens.tokens.lagoonSecondaryScheme

@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    onNavigateToSingleAppUsageDetail: (String) -> Unit = {},
    viewModel: StatisticsViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme
) {

    val activity = LocalActivity.current
    // Get theme mode for status bar styling
    val useDarkTheme = LocalThemeMode.current
    SideEffect {
        if (activity is ComponentActivity) {
            activity.enableEdgeToEdge(
                statusBarStyle = if (useDarkTheme) {
                    SystemBarStyle.dark(scheme.basicBackground.getIntColor())
                } else {
                    SystemBarStyle.light(
                        scheme.basicBackground.getIntColor(),
                        darkScrim = scheme.basicBackground.getIntColor()
                    )
                },
                navigationBarStyle = SystemBarStyle.auto(
                    Color.TRANSPARENT,
                    Color.TRANSPARENT
                )
            )
        }
    }

    val uiProps by viewModel.uiProps.collectAsState()
    val chartFormatterProps = viewModel.getChartFormatterProps()

    ODSColumn(modifier = Modifier.fillMaxSize()) {
        // Status bar padding
        ODSBox(
            modifier = Modifier
                .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                .fillMaxWidth()
        ) {}

        when {
            uiProps == null || uiProps!!.isLoading -> {
                ODSBox(
                    modifier = modifier.fillMaxSize(),
                    padding = ODSPadding(horizontal = DSVariables.spacingComponent3),
                    contentAlignment = Alignment.Center
                ) {
                    ODSLoadingSpinner(
                        modifier = Modifier.wrapContentHeight(),
                        scheme = scheme,
                        props = ODSLoadingSpinnerProps(
                            labelText = "Please wait...",
                            size = ODSLoadingSpinnerSize.SMALL,
                            variant = ODSLoadingSpinnerVariant.STANDARD,
                            labelAlignment = ODSLoadingSpinnerLabelAlignment.HORIZONTAL
                        )
                    )
                }
            }

            uiProps!!.error != null -> {
                ODSLazyColumn(
                    modifier = modifier.fillMaxSize(),
                    padding = ODSPadding(horizontal = 8.dp)
                ) {
                    item {
                        ODSBox(
                            modifier = Modifier.fillMaxWidth(),
                            background = listOf(ODSColorModel(scheme.functionalDestructiveSubtle)),
                            cornerRadius = ODSCorners(all = 12.dp)
                        ) {
                            ODSColumn(modifier = Modifier.padding(16.dp)) {
                                ODSText(
                                    text = stringResource(R.string.error),
                                    style = DSTextStyles.bodyL,
                                    color = scheme.functionalDestructiveStandard
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                ODSText(
                                    text = uiProps!!.error ?: "",
                                    style = DSTextStyles.bodyMRegular,
                                    color = scheme.functionalDestructiveStandard
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = { viewModel.clearError() }) {
                                    ODSText(
                                        text = stringResource(R.string.retry),
                                        style = DSTextStyles.bodyMBold,
                                        color = scheme.basicTextOnAccent
                                    )
                                }
                            }
                        }
                    }
                }
            }

            uiProps!!.barChartData.isEmpty() -> {
                ODSLazyColumn(
                    modifier = modifier.fillMaxSize(),
                    padding = ODSPadding(horizontal = DSVariables.spacingComponent3)
                ) {
                    item {
                        ODSBox(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSText(
                                text = stringResource(R.string.no_data_available),
                                style = DSTextStyles.bodyL,
                                color = scheme.basicText
                            )
                        }
                    }
                }
            }

            else -> {
                ODSLazyColumn(
                    modifier = modifier.fillMaxSize(),
                    background = listOf(ODSColorModel(scheme.basicBackground)),
                    padding = ODSPadding(all = DSVariables.spacingComponent3),
                    gap = DSVariables.spacingComponent3
                ) {
                    item {
                        ODSRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ODSText(
                                text = stringResource(R.string.activity),
                                style = DSTextStyles.subtitle,
                                color = scheme.basicText
                            )
                            ODSButton(
                                scheme = scheme,
                                props = ODSButtonProps(
                                    buttonIcon = ODSIconModel(
                                        imageVector = Icons.Default.SwapVert,
                                        contentDescription = "Toggle chart orientation"
                                    ),
                                    buttonType = ODSButtonButtonType.ICON_ONLY,
                                    variant = ODSButtonVariant.GHOST,
                                    size = ODSButtonSize.SMALL
                                ),
                                onClick = { viewModel.toggleChartOrientation() }
                            )
                        }
                    }
                    item {
                        SelectedDayInfoCard(
                            selectedDayReport = uiProps!!.selectedDayReport,
                            scheme = headerTheme.current
                        )
                    }

                    item {
                        WeeklyUsageChart(
                            barChartData = uiProps!!.barChartData,
                            weeklyReports = uiProps!!.weeklyReports,
                            chartFormatterProps = chartFormatterProps,
                            chartOrientation = uiProps!!.chartOrientation,
                            onBarClick = { dayIndex ->
                                viewModel.selectDay(dayIndex)
                            },
                            scheme = scheme
                        )
                    }

                    item {
                        ODSBox(height = DSVariables.spacingComponent5) {}
                    }

                    if (uiProps!!.selectedDayAppUsageList.isNotEmpty()) {
                        appUsageListUi(
                            appUsageList = uiProps!!.selectedDayAppUsageList,
                            onClick = { appUsage ->
                                appUsage.packageName?.let { packageName ->
                                    onNavigateToSingleAppUsageDetail(packageName)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Weekly Usage Chart using ODSBarChart
 */
@Composable
fun WeeklyUsageChart(
    barChartData: List<ODSBarItemProps>,
    weeklyReports: List<WeeklyDataReport>,
    chartFormatterProps: ChartFormatterProps,
    chartOrientation: ODSBarItemDirection,
    onBarClick: (Int) -> Unit = {},
    scheme: ODSTheme = neutralScheme
) {
    ODSBox(
        modifier = Modifier
            .fillMaxWidth()
            .height(
                if (chartOrientation == ODSBarItemDirection.HORIZONTAL) {
                    // Horizontal charts need more height
                    DSVariables.sizingComponent19 * 2
                } else {
                    DSVariables.sizingComponent19 + DSVariables.sizingMinimumTappableArea
                }
            ),
    ) {
        if (barChartData.isNotEmpty()) {
            ODSBarChart(
                modifier = Modifier.fillMaxSize(),
                scheme = scheme,
                props = ODSBarChartProps(
                    barItemsList = barChartData,
                    direction = chartOrientation,
                    shape = ODSBarItemShape.PILLED,
                    showTopLabels = chartOrientation == ODSBarItemDirection.VERTICAL,
                    showBottomLabels = true,
                    showLeftLabels = chartOrientation == ODSBarItemDirection.HORIZONTAL,
                    showRightLabels = chartOrientation == ODSBarItemDirection.VERTICAL,
                    stepCount = 2,
                    zoomEnabled = false,
                    scrollEnabled = false
                ),
                valueFormatter = chartFormatterProps.valueFormatter,
                onBarSelected = { index ->
                    if (index in weeklyReports.indices) {
                        onBarClick(index)
                    }
                },
                onBarDeSelected = {},
                verticalAxisFormatter = chartFormatterProps.verticalAxisFormatter
            )
        } else {
            ODSColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ODSText(
                    text = stringResource(R.string.no_data_available),
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive
                )
            }
        }
    }
}


@Composable
private fun SelectedDayInfoCard(
    selectedDayReport: WeeklyDataReport?,
    scheme: ODSTheme = neutralScheme
) {
    selectedDayReport?.let { selectedDay ->
        ODSCardBasic(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            contentPadding = ODSPadding(
                horizontal = DSVariables.spacingComponent5,
                vertical = DSVariables.spacingComponent5
            ),
            props = ODSCardBasicProps(),
            contentSlot = {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = DSVariables.spacingComponent4
                ) {
                    ODSText(
                        text = selectedDay.dayName ?: "",
                        style = DSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        gap = DSVariables.spacingComponent4
                    ) {
                        ODSColumn(
                            modifier = Modifier.weight(1f),
                            gap = DSVariables.spacingComponent1
                        ) {
                            ODSText(
                                text = stringResource(R.string.screen_time),
                                style = DSTextStyles.bodySRegular,
                                color = scheme.basicTextRecessive
                            )
                            ODSText(
                                text = selectedDay.displayScreenTime ?: "N/A",
                                style = DSTextStyles.linkSBold,
                                color = scheme.basicText
                            )
                        }
                        if (!selectedDay.displayTotalDataUsage.isNullOrEmpty()) {
                            ODSColumn(
                                modifier = Modifier.weight(1f),
                                gap = DSVariables.spacingComponent1
                            ) {
                                ODSText(
                                    text = stringResource(R.string.network_usage),
                                    style = DSTextStyles.bodySRegular,
                                    color = scheme.basicTextRecessive
                                )
                                ODSText(
                                    text = selectedDay.displayTotalDataUsage,
                                    style = DSTextStyles.linkSBold,
                                    color = scheme.basicText
                                )
                            }
                        }
                    }
                }
            },
            onClick = {}
        )
    }

}

