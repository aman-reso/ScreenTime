package com.app.screentime.record.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.screentime.config.R
import com.app.screentime.landing.component.CircularDonutText
import com.app.screentime.record.util.formatUsageTime
import com.app.screentime.record.viewmodel.RecordDetailUiState
import com.app.screentime.ui.atom.AppScreenShimmer
import com.app.screentime.ui.atom.appUsageListUi
import com.app.screentime.utils.DateUtils
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.link.ODSLinkProps
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardnotification.ODSCardNotification
import com.telekom.odsystem.organisms.cardnotification.ODSCardNotificationProps
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotification
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationMode
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun SummaryTab(
    modifier: Modifier = Modifier,
    uiState: RecordDetailUiState,
    onNavigateToAppDetails: (String) -> Unit = {},
    selectedDateDisplay: String,
    scheme: ODSTheme = neutralScheme,
    onClearError: () -> Unit = {}
) {

    when {
        uiState.isLoading -> {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                padding = ODSPadding(horizontal = DSVariables.spacingComponent4)
            ) {
                AppScreenShimmer(
                    modifier = modifier.fillMaxSize(),
                    scheme = scheme
                )
            }
        }

        uiState.error != null -> {
            ODSColumn(
                modifier = modifier.fillMaxSize(),
                padding = ODSPadding(all = DSVariables.spacingComponent5)
            ) {
                ODSInlineNotification(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSInlineNotificationProps(
                        mode = ODSInlineNotificationMode.ERROR,
                        title = stringResource(R.string.error),
                        text = uiState.error,
                        link1Props = ODSLinkProps(
                            label = stringResource(R.string.retry)
                        ),
                        showCloseButton = true
                    ),
                    onDismiss = { onClearError.invoke() },
                    onFirstLinkClicked = {
                        onClearError.invoke()
                    }
                )
            }
        }

        else -> {
            ODSLazyColumn(
                modifier = modifier.fillMaxSize(),
                padding = ODSPadding(horizontal = DSVariables.spacingComponent3),
                gap = DSVariables.spacingComponent3
            ) {
                item {
                    Spacer(modifier = Modifier.height(DSVariables.spacingComponent4))
                }

                uiState.userLocation?.let { location ->
                    item {
                        val locationText = buildString {
                            if (!location.address.isNullOrBlank()) {
                                append(location.address)
                            } else {
                                append("${location.latitude}, ${location.longitude}")
                            }
                            location.timestamp?.let { timestamp ->
                                try {
                                    val timeAgo =
                                        DateUtils.getTimeAgo(DateUtils.toMillis(timestamp))
                                    append("\n$timeAgo")
                                } catch (e: Exception) {
                                    // Ignore parsing errors
                                }
                            }
                        }

                        ODSCardNotification(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSCardNotificationProps(
                                showCloseButton = false,
                                title = stringResource(R.string.last_location),
                                text = locationText
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                uiState.summaryScreenTime?.let { screenTime ->
                    item {
                        val formattedTime = formatUsageTime(screenTime)
                        CircularDonutText(
                            time = formattedTime,
                            scheme = scheme
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                if (!uiState.stats.isNullOrEmpty()) {
                    item {
                        ODSText(
                            text = stringResource(R.string.usage_summary),
                            style = DSTextStyles.bodyMRegular,
                            color = scheme.basicText
                        )
                    }

                    appUsageListUi(
                        appUsageList = uiState.stats,
                        scheme = scheme,
                        onClick = { appUsage ->
                            if (!appUsage.packageName.isNullOrEmpty()) {
                                onNavigateToAppDetails(appUsage.packageName)
                            }
                        }, onExpandCollapseToggle = {

                        }
                    )
                }

            }
        }
    }
}

