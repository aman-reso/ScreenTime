package com.app.screentime.landing.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.R
import com.app.screentime.consent.screen.ConsentBottomSheetContent
import com.app.screentime.landing.component.GreetingUi
import com.app.screentime.landing.component.NetworkCard
import com.app.screentime.landing.component.CategoryUsageSection
import com.app.screentime.landing.component.UsageSummaryCard
import com.app.screentime.landing.component.JoinedChallengesCardStack
import com.app.screentime.landing.viewmodel.LandingViewModel
import com.app.screentime.navigation.Screen
import com.telekom.odsystem.molecules.searchbar.ODSSearchBarProps
import com.telekom.odsystem.organisms.searchview.ODSSearchView
import com.telekom.odsystem.organisms.searchview.ODSSearchViewProps
import com.app.screentime.ui.atom.appUsageListUi
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.link.ODSLinkProps
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.searchbar.ODSSearchBarButtonProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotification
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationMode
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationProps
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.aperitifSecondaryScheme
import com.telekom.odsystem.tokens.tokens.darkMode
import com.telekom.odsystem.tokens.tokens.guacamoleSecondaryScheme
import com.telekom.odsystem.tokens.tokens.kingfisherSecondaryScheme
import com.telekom.odsystem.tokens.tokens.lagoonSecondaryScheme
import com.telekom.odsystem.tokens.tokens.macawSecondaryScheme
import com.telekom.odsystem.tokens.tokens.orchidSecondaryScheme

@Composable
fun LandingScreenV2(
    modifier: Modifier = Modifier,
    onNavigateToReward: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToStatistics: () -> Unit = {},
    onNavigateToSingleAppUsageDetail: (String) -> Unit = {},
    onNavigateToChallengeDetail: (String) -> Unit = {},
    onNavigateToChallenges: () -> Unit = {},
    viewModel: LandingViewModel = hiltViewModel(),
    openSearchScreen: () -> Unit = {},
    scheme: ODSTheme = neutralScheme
) {
    val uiProps by viewModel.uiProps.collectAsState()

    // Consent bottom sheet state
    var showConsentSheet by remember { mutableStateOf(false) }

    // Update consent sheet state when uiProps changes
    LaunchedEffect(uiProps?.shouldShowConsent) {
        uiProps?.shouldShowConsent?.let {
            showConsentSheet = it
        }
    }

    // Show consent bottom sheet if not already displayed
    if (showConsentSheet) {
        ConsentBottomSheetContent(
            onDismiss = {
                viewModel.markConsentShown()
                showConsentSheet = false
            },
            onAccept = {
                viewModel.markConsentShown()
                showConsentSheet = false
            }
        )
    }

    ODSColumn(modifier = Modifier.fillMaxSize()) {
        ODSLazyColumn(
            modifier = modifier,
            gap = DSVariables.spacingComponent3,
            padding = ODSPadding(horizontal = DSVariables.spacingComponent4)
        ) {
            item {
                GreetingUi(
                    username = uiProps?.username, onLeaderboardClick = onNavigateToReward
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            stickyHeader {
                var searchQuery by remember { mutableStateOf("") }
                var hasNavigated by remember { mutableStateOf(false) }
                ODSSearchView(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSSearchViewProps(
                        showBackButton = false,
                        searchBarProps = ODSSearchBarProps(
                            input = searchQuery,
                            disabled = false,
                            placeholder = "@ Search by username...",
                            buttonProps = ODSSearchBarButtonProps(
                                buttonIcon = ODSIconModel(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = stringResource(R.string.search)
                                )
                            )
                        )
                    ),
                    onButtonClick = {
                        onNavigateToSearch()
                    },
                    onBackButtonClick = { },
                    onSearchValueChange = { newValue ->
                        if (!hasNavigated && newValue.isNotEmpty()) {
                            hasNavigated = true
                            onNavigateToSearch()
                        }
                    },
                    onFocusChange = { focusState ->
                        if (focusState.isFocused && !hasNavigated) {
                            hasNavigated = true
                            onNavigateToSearch()
                        }
                    },
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onNavigateToSearch()
                        }
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    )
                )
            }
            when {
                uiProps == null || uiProps!!.isLoading -> {
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                        ODSBox(
                            modifier = Modifier
                                .height(40.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSLoadingSpinner(
                                scheme = scheme, props = ODSLoadingSpinnerProps(
                                    labelText = stringResource(R.string.loading),
                                    size = ODSLoadingSpinnerSize.SMALL,
                                    variant = ODSLoadingSpinnerVariant.STANDARD,
                                    labelAlignment = ODSLoadingSpinnerLabelAlignment.HORIZONTAL
                                )
                            )
                        }
                    }
                }

                uiProps!!.error != null -> {
                    item {
                        ODSInlineNotification(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSInlineNotificationProps(
                                mode = ODSInlineNotificationMode.ERROR,
                                title = stringResource(R.string.error),
                                text = uiProps!!.error,
                                link1Props = ODSLinkProps(label = stringResource(R.string.retry)),
                                showCloseButton = false
                            ),
                            onFirstLinkClicked = {
                                viewModel.loadLandingData()
                            },
                            onDismiss = {
                                viewModel.clearError()
                            })
                    }
                }

                uiProps!!.topUsedApps.isEmpty() -> {
                    item {
                        ODSColumn(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(DSVariables.spacingComponent5))
                            ODSText(
                                text = stringResource(R.string.no_data_available),
                                style = DSTextStyles.bodyMRegular,
                                color = scheme.basicTextRecessive
                            )
                        }
                    }
                }

                else -> {
                    item {
                        Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                    }

                    // Show joined challenges CardStack notification if available
                    uiProps?.joinedChallenges?.takeIf { it.isNotEmpty() }?.let { challenges ->
                        item {
                            JoinedChallengesCardStack(
                                joinedChallenges = challenges,
                                modifier = Modifier.fillMaxWidth(),
                                onNavigateToChallengeDetail = onNavigateToChallengeDetail,
                                onNavigateToChallenges = onNavigateToChallenges,
                                scheme = scheme,
                                onDismiss = {
                                    // Dismiss handled by component
                                }
                            )
                            Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                        }
                    }

                    item {
                        uiProps?.usageDonutData?.let { donutData ->
                            UsageSummaryCard(
                                todayTotal = donutData.formattedTotalTime,
                                dailyGoal = "6h",
                                percentageChange = uiProps!!.percentageChangeFromYesterday,
                                onClick = onNavigateToStatistics,
                                scheme = macawSecondaryScheme
                            )
                            Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                        }
                    }

                    uiProps?.let { it ->
                        item {
                            NetworkCard(
                                modifier = Modifier.fillMaxWidth(),
                                wifiDataUsage = it.todayTotalWifiDataUsage,
                                wifiDataUsageDisplay = it.displayWifiDataUsage,
                                cellularDataUsage = it.todayTotalMobileDataUsage,
                                cellularDataUsageDisplay = it.displayMobileDataUsage,
                                totalDataDisplayName = it.displayTotalDataUsage
                            )
                            Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                        }
                    }

                    item {
                        CategoryUsageSection(
                            categoryUsage = uiProps!!.categoryUsage,
                            scheme = scheme
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                    }
                    item {
                        ODSText(
                            text = stringResource(R.string.usage_detail_insight),
                            style = DSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )
                    }
                    appUsageListUi(
                        uiProps!!.topUsedApps, scheme = scheme, onClick = { data ->
                            data.packageName?.let { packageName ->
                                onNavigateToSingleAppUsageDetail(packageName)
                            }
                        })
                }
            }
        }
    }
}