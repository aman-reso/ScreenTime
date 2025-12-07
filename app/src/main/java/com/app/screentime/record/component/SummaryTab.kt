package com.app.screentime.record.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.screentime.R
import com.app.screentime.landing.component.UsageDonutComponent
import com.app.screentime.record.viewmodel.RecordDetailUiState
import com.app.screentime.record.viewmodel.RecordDetailViewModel
import com.app.screentime.ui.atom.appUsageListUi

import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.link.ODSLinkProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotification
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationMode
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun SummaryTab(
    viewModel: RecordDetailViewModel,
    uiState: RecordDetailUiState,
    onNavigateToAppDetails: (String) -> Unit = {},
    selectedDateDisplay: String,
    scheme: ODSTheme = neutralScheme
) {
    val summaryTabUiProps by viewModel.summaryTabUiProps.collectAsState()

    when {
        summaryTabUiProps == null || summaryTabUiProps!!.isLoading -> {
            ODSBox(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ODSLoadingSpinner(
                    scheme = scheme,
                    props = ODSLoadingSpinnerProps(
                        size = ODSLoadingSpinnerSize.SMALL,
                        variant = ODSLoadingSpinnerVariant.STANDARD,
                        labelAlignment = ODSLoadingSpinnerLabelAlignment.HORIZONTAL,
                        labelText = stringResource(R.string.loading)
                    )
                )
            }
        }

        summaryTabUiProps!!.error != null -> {
            ODSColumn(
                modifier = Modifier.fillMaxSize(),
                padding = ODSPadding(all = DSVariables.spacingComponent5)
            ) {
                ODSInlineNotification(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSInlineNotificationProps(
                        mode = ODSInlineNotificationMode.ERROR,
                        title = stringResource(R.string.error),
                        text = summaryTabUiProps!!.error,
                        link1Props = ODSLinkProps(
                            label = stringResource(R.string.retry)
                        ),
                        showCloseButton = true
                    ),
                    onDismiss = { viewModel.clearError() },
                    onFirstLinkClicked = {
                        viewModel.clearError()
                    }
                )
            }
        }

        summaryTabUiProps!!.appUsageList.isEmpty() -> {
            ODSBox(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ODSText(
                    text = "No usage records found",
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive
                )
            }
        }

        else -> {
            ODSLazyColumn(
                modifier = Modifier.fillMaxSize(),
                padding = ODSPadding(horizontal = DSVariables.spacingComponent3),
                gap = DSVariables.spacingComponent3
            ) {
                summaryTabUiProps!!.usageDonutData?.let { donutData ->
                    item {
                        UsageDonutComponent(
                            usageDonutData = donutData,
                            title = selectedDateDisplay
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                if (summaryTabUiProps!!.appUsageList.isNotEmpty()) {
                    item {
                        ODSText(
                            text = "Usage Summary",
                            style = DSTextStyles.bodyMRegular,
                            color = scheme.basicText
                        )
                    }
                }

                appUsageListUi(
                    appUsageList = summaryTabUiProps!!.appUsageList, scheme = scheme
                ) { appUsage ->
                    if (!appUsage.packageName.isNullOrEmpty()) {
                        onNavigateToAppDetails(appUsage.packageName)
                    }
                }
            }
        }
    }
}

