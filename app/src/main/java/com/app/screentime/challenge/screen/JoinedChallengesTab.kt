package com.app.screentime.challenge.screen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.config.R
import com.app.screentime.challenge.component.QuickJoinCard
import com.app.screentime.challenge.viewmodel.ChallengeFilter
import com.app.screentime.challenge.viewmodel.JoinedChallengeViewModel
import com.app.screentime.ui.atom.PullToRefreshBox
import com.app.screentime.ui.atom.AppScreenShimmer
import com.app.screentime.ads.AdConfig
import com.app.screentime.ads.BannerAd
import com.app.screentime.ads.rememberBannerAd
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.link.ODSLinkAlignment
import com.telekom.odsystem.atoms.link.ODSLinkProps
import com.telekom.odsystem.atoms.togglechip.ODSToggleChip
import com.telekom.odsystem.atoms.togglechip.ODSToggleChipProps
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotification
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationMode
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationProps
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.neutralScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinedChallengesTab(
    onNavigateToChallengeDetail: (String) -> Unit = {},
    viewModel: JoinedChallengeViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme
) {
    LaunchedEffect(Unit) {
        viewModel.loadJoinedChallenges()
    }

    val uiState by viewModel.uiState.collectAsState()
    val filters = listOf(
        stringResource(R.string.all),
        stringResource(R.string.current),
        stringResource(R.string.expired)
    )

    val isRefreshing = uiState.isLoading

    // Banner ad for joined challenges
    val bannerAd = rememberBannerAd(
        adUnitId = AdConfig.getBannerAdUnitId(),
    )

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            viewModel.loadJoinedChallenges()
        },
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            uiState.isLoading -> {
                ODSLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    padding = ODSPadding(horizontal = DSVariables.spacingComponent4),
                    gap = DSVariables.spacingComponent3
                ) {
                    item {
                        Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                    }
                    item {
                        AppScreenShimmer(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme
                        )
                    }
                }
            }

            uiState.error != null -> {
                ODSColumn(
                    modifier = Modifier.fillMaxSize(),
                    padding = ODSPadding(all = DSVariables.spacingComponent4)
                ) {
                    ODSInlineNotification(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSInlineNotificationProps(
                            mode = ODSInlineNotificationMode.ERROR,
                            title = stringResource(R.string.error),
                            text = uiState.error,
                            link1Props = ODSLinkProps(
                                alignment = ODSLinkAlignment.LEFT,
                                label = stringResource(R.string.retry)
                            ),
                            showCloseButton = false
                        ),
                        onFirstLinkClicked = {
                            viewModel.loadJoinedChallenges()
                        }
                    )
                }
            }

            else -> {
                ODSLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    gap = DSVariables.spacingComponent4,
                    padding = ODSPadding(
                        horizontal = DSVariables.spacingComponent2,
                        vertical = DSVariables.spacingComponent3
                    ),
                ) {
                    // Always show filter section
                    item {
                        ODSRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            gap = DSVariables.spacingComponent2
                        ) {
                            filters.forEachIndexed { index, filter ->
                                val isSelected = when (index) {
                                    0 -> uiState.selectedFilter == ChallengeFilter.ALL
                                    1 -> uiState.selectedFilter == ChallengeFilter.CURRENT
                                    2 -> uiState.selectedFilter == ChallengeFilter.EXPIRED
                                    else -> false
                                }
                                ODSToggleChip(
                                    scheme = scheme,
                                    props = ODSToggleChipProps(
                                        label = filter,
                                        selected = isSelected
                                    ),
                                    onToggle = {
                                        if (it) {
                                            val newFilter = when (index) {
                                                0 -> ChallengeFilter.ALL
                                                1 -> ChallengeFilter.CURRENT
                                                2 -> ChallengeFilter.EXPIRED
                                                else -> ChallengeFilter.ALL
                                            }
                                            viewModel.setFilter(newFilter)
                                        }
                                    }
                                )
                            }
                        }
                    }

                    // Show empty state if no challenges
                    if (uiState.filteredChallenges.isEmpty()) {
                        item {
                            ODSColumn(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                padding = ODSPadding(vertical = DSVariables.spacingComponent8)
                            ) {
                                ODSText(
                                    text = when (uiState.selectedFilter) {
                                        ChallengeFilter.ALL -> stringResource(R.string.no_challenges)
                                        ChallengeFilter.CURRENT -> stringResource(R.string.no_current_challenges)
                                        ChallengeFilter.EXPIRED -> stringResource(R.string.no_expired_challenges)
                                    },
                                    style = DSTextStyles.subtitle,
                                    color = scheme.basicText
                                )
                                Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                                ODSText(
                                    text = when (uiState.selectedFilter) {
                                        ChallengeFilter.ALL -> stringResource(R.string.no_challenges)
                                        ChallengeFilter.CURRENT -> stringResource(R.string.no_active_challenges_message)
                                        ChallengeFilter.EXPIRED -> stringResource(R.string.no_expired_challenges_message)
                                    },
                                    style = DSTextStyles.bodyMRegular,
                                    color = scheme.basicTextRecessive
                                )
                            }
                        }
                    } else {
                        // Show first challenge
                        item(key = uiState.filteredChallenges[0].id) {
                            QuickJoinCard(
                                challenge = uiState.filteredChallenges[0],
                                modifier = Modifier.fillMaxWidth(),
                                onJoin = {
                                    onNavigateToChallengeDetail(uiState.filteredChallenges[0].id)
                                },
                                scheme = uiState.filteredChallenges[0].getTheme()
                            )
                        }

                        // Show banner ad after first challenge (2nd item)
                        if (AdConfig.areAdsEnabled() && bannerAd != null) {
                            val (adView, adState) = bannerAd
                            item(key = "banner_ad") {
                                BannerAd(
                                    adView = adView,
                                    adState = adState,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        // Show remaining challenges after the ad
                        if (uiState.filteredChallenges.size > 1) {
                            items(
                                items = uiState.filteredChallenges.drop(1),
                                key = { it.id }
                            ) { challenge ->
                                QuickJoinCard(
                                    challenge = challenge,
                                    modifier = Modifier.fillMaxWidth(),
                                    onJoin = {
                                        onNavigateToChallengeDetail(challenge.id)
                                    },
                                    scheme = challenge.getTheme()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
