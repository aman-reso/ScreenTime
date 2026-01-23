package com.app.screentime.challenge.screen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.config.R
import com.app.screentime.challenge.component.util.parseChallengeInstant
import com.app.screentime.challenge.screen.variant.ChallengeVariantCardLoader
import com.app.screentime.challenge.viewmodel.ChallengeViewModel
import com.app.screentime.ui.atom.PullToRefreshBox
import com.app.screentime.ui.atom.AppScreenShimmer
import com.app.screentime.ads.NativeAdvancedAd
import com.app.screentime.ads.AdConfig
import com.app.screentime.ads.rememberNativeAd
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
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengesTab(
    onNavigateToChallengeDetail: (String) -> Unit = {},
    viewModel: ChallengeViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme
) {
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }
    val uiState by viewModel.uiState.collectAsState()
    val challenges = uiState.challenges
    val nativeAdState = rememberNativeAd(AdConfig.getNativeAdvancedAdUnitId())

    var selectedFilter by remember { mutableIntStateOf(0) }
    val filters = listOf(
        stringResource(R.string.all),
        stringResource(R.string.active),
        stringResource(R.string.past),
    )

    val now = Instant.now()
    val filteredChallenges = remember(challenges, selectedFilter) {
        when (selectedFilter) {
            0 -> challenges
            1 -> challenges.filter { c ->
                val s = parseChallengeInstant(c.startTime) ?: return@filter false
                val e = parseChallengeInstant(c.endTime) ?: return@filter false
                now >= s && now < e
            }
            2 -> challenges.filter { c ->
                val e = parseChallengeInstant(c.endTime) ?: return@filter false
                now >= e
            }
            else -> challenges
        }
    }

    val emptyMessage = when (selectedFilter) {
        0 -> stringResource(R.string.no_challenges)
        1 -> stringResource(R.string.no_current_challenges)
        2 -> stringResource(R.string.no_expired_challenges)
        else -> stringResource(R.string.no_challenges)
    }

    val isRefreshing = uiState.isLoading

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            viewModel.refresh()
        },
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            uiState.isLoading -> {
                ODSLazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
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
                    padding = ODSPadding(
                        horizontal = DSVariables.spacingComponent4,
                        vertical = DSVariables.spacingComponent7
                    )
                ) {
                    ODSInlineNotification(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSInlineNotificationProps(
                            mode = ODSInlineNotificationMode.ERROR,
                            title = stringResource(R.string.error),
                            text = uiState.error,
                            link1Props = ODSLinkProps(
                                label = stringResource(R.string.retry),
                                alignment = ODSLinkAlignment.LEFT
                            ),
                            showCloseButton = false
                        ),
                        onFirstLinkClicked = {
                            viewModel.refresh()
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
                    item {
                        ODSRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            gap = DSVariables.spacingComponent2
                        ) {
                            filters.forEachIndexed { index, filter ->
                                ODSToggleChip(
                                    modifier = Modifier.wrapContentWidth(),
                                    scheme = scheme,
                                    props = ODSToggleChipProps(
                                        label = filter,
                                        selected = selectedFilter == index
                                    ),
                                    onToggle = { if (it) selectedFilter = index }
                                )
                            }
                        }
                    }

                    nativeAdState?.let {
                        item("ad_key_challenge_list") {
                            NativeAdvancedAd(adState = it)
                        }
                    }

                    if (filteredChallenges.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = DSVariables.spacingComponent7),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSText(
                                    text = emptyMessage,
                                    style = DSTextStyles.bodySRegular,
                                    color = scheme.basicTextRecessive
                                )
                            }
                        }
                    } else {
                        items(
                            items = filteredChallenges,
                            key = { it.id }
                        ) { challenge ->
                            ChallengeVariantCardLoader(
                                challenge = challenge,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { onNavigateToChallengeDetail(challenge.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
