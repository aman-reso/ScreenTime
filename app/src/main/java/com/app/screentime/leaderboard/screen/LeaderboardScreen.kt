package com.app.screentime.leaderboard.screen

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.R
import com.app.screentime.leaderboard.viewmodel.LeaderboardViewModel
import com.app.screentime.network.model.LeaderboardEntry
import com.app.screentime.record.repository.formatDuration
import com.app.screentime.ui.theme.ColorPalette
import com.app.screentime.ui.theme.LocalThemeMode
import com.app.screentime.ui.theme.headerTheme
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.atoms.tagstatic.ODSTagStatic
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticProps
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticType
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.tabs.ODSTabItemModel
import com.telekom.odsystem.molecules.tabs.ODSTabs
import com.telekom.odsystem.molecules.tabs.ODSTabsProps
import com.telekom.odsystem.molecules.tabs.ODSTabsSize
import com.telekom.odsystem.molecules.tabs.ODSTabsVariant
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotification
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationMode
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationProps
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.launch

/**
 * Leaderboard Screen
 * Displays daily and weekly leaderboards with top 3 players prominently displayed
 */
@Composable
fun LeaderboardScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    scheme: ODSTheme = neutralScheme,
    headerScheme: ODSTheme = headerTheme.current,
    viewModel: LeaderboardViewModel = hiltViewModel()
) {
    val activity = LocalActivity.current
    // Get theme mode for status bar styling
    val useDarkTheme = LocalThemeMode.current
    val uiState by viewModel.uiState.collectAsState()

    // Calculate ColorPalette schemes for top 3 (rank 1, 2, 3)
    val rank1Scheme = ColorPalette.schemeGet(headerScheme)
    val rank2Scheme = ColorPalette.schemeGet(rank1Scheme)
    val rank3Scheme = ColorPalette.schemeGet(rank2Scheme)

    SideEffect {
        if (activity is ComponentActivity) {
            activity.enableEdgeToEdge(
                statusBarStyle = if (useDarkTheme) {
                    SystemBarStyle.dark(headerScheme.basicBackgroundCard.getIntColor())
                } else {
                    SystemBarStyle.light(
                        headerScheme.basicBackgroundCard.getIntColor(),
                        darkScrim = headerScheme.basicBackgroundCard.getIntColor()
                    )
                },
                navigationBarStyle = SystemBarStyle.auto(
                    Color.TRANSPARENT,
                    Color.TRANSPARENT
                )
            )
        }
    }

    ODSColumn(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground))
    ) {
        // Status bar padding
        ODSBox(
            modifier = Modifier
                .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                .fillMaxWidth()
        ) {}

        // Header section with scheme background
        ODSBox(
            modifier = Modifier.fillMaxWidth(),
            background = listOf(ODSColorModel(headerScheme.basicBackgroundCard)),
            padding = ODSPadding(bottom = DSVariables.spacingComponent3)
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent2
            ) {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    padding = ODSPadding(
                        horizontal = DSVariables.spacingComponent3
                    ),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSButton(
                        scheme = headerScheme, props = ODSButtonProps(
                            buttonIcon = ODSIconModel(
                                drawableRes = com.telekom.odsystem.R.drawable.left_condensed_type_standard_size_standard,
                                tint = headerScheme.basicText,
                                contentDescription = "Back"
                            ),
                            buttonType = ODSButtonButtonType.ICON_ONLY,
                            variant = ODSButtonVariant.GHOST,
                            size = ODSButtonSize.SMALL
                        ), onClick = onBackClick
                    )

                    ODSText(
                        text = "Leaderboard",
                        style = DSTextStyles.bodyL,
                        color = headerScheme.basicText,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = DSVariables.spacingComponent2),
                        textAlign = TextAlign.Center
                    )

                    ODSButton(
                        scheme = headerScheme, props = ODSButtonProps(
                            buttonIcon = ODSIconModel(
                                imageVector = Icons.Default.Refresh,
                                tint = headerScheme.basicText,
                                contentDescription = stringResource(R.string.refresh)
                            ),
                            buttonType = ODSButtonButtonType.ICON_ONLY,
                            variant = ODSButtonVariant.GHOST,
                            size = ODSButtonSize.SMALL
                        ), onClick = { viewModel.refresh() })
                }
            }
        }

        LeaderboardPage(
            modifier = Modifier.weight(1f),
            scheme = scheme,
            headerScheme = headerScheme,
            viewModel = viewModel
        )
    }
}

@Composable
fun LeaderboardPage(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    headerScheme: ODSTheme = headerTheme.current,
    viewModel: LeaderboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = 0
    ) { 2 }

    // Calculate ColorPalette schemes for top 3 (rank 1, 2, 3)
    val rank1Scheme = ColorPalette.schemeGet(headerScheme)
    val rank2Scheme = ColorPalette.schemeGet(rank1Scheme)
    val rank3Scheme = ColorPalette.schemeGet(rank2Scheme)

    ODSColumn(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground))
    ) {
        ODSBox(
            modifier = Modifier.fillMaxWidth(),
            background = listOf(ODSColorModel(headerScheme.basicBackgroundCard)),
            cornerRadius = ODSCorners(
                bottomLeft = DSVariables.spacingComponent4,
                bottomRight = DSVariables.spacingComponent4
            ),
            padding = ODSPadding(bottom = DSVariables.spacingComponent3)
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent2
            ) {
                ODSTabs(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DSVariables.spacingComponent3),
                    scheme = headerScheme,
                    props = ODSTabsProps(
                        tabElements = listOf(
                            ODSTabItemModel(label = "Daily"),
                            ODSTabItemModel(label = "Weekly")
                        ),
                        variant = ODSTabsVariant.FILL,
                        size = ODSTabsSize.SMALL,
                        showDividerFrame = true
                    ),
                    selectedTabIndex = pagerState.currentPage,
                    onSelectedTabChange = { index ->
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )

                // Add spacing between tabs and top 3 section
                Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))

                if (!uiState.isLoading && uiState.error == null) {
                    val entries =
                        if (pagerState.currentPage == 0) uiState.dailyEntries else uiState.weeklyEntries
                    if (entries.size >= 3) {
                        TopThreePlayersSection(
                            firstPlace = entries[0],
                            secondPlace = entries[1],
                            thirdPlace = entries[2],
                            rank1Scheme = rank1Scheme,
                            rank2Scheme = rank2Scheme,
                            rank3Scheme = rank3Scheme,
                            modifier = Modifier.padding(
                                horizontal = DSVariables.spacingComponent3,
                                vertical = DSVariables.spacingComponent3
                            )
                        )
                    } else if (entries.isNotEmpty()) {
                        TopPlayersSection(
                            entries = entries.take(3),
                            rank1Scheme = rank1Scheme,
                            rank2Scheme = rank2Scheme,
                            rank3Scheme = rank3Scheme,
                            modifier = Modifier.padding(
                                horizontal = DSVariables.spacingComponent3,
                                vertical = DSVariables.spacingComponent3
                            )
                        )
                    }
                }
            }
        }

        when {
            uiState.isLoading -> {
                ODSBox(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    ODSLoadingSpinner(
                        modifier = Modifier.wrapContentHeight(),
                        scheme = scheme,
                        props = ODSLoadingSpinnerProps(
                            labelText = stringResource(R.string.loading),
                            size = ODSLoadingSpinnerSize.SMALL,
                            variant = ODSLoadingSpinnerVariant.STANDARD,
                            labelAlignment = ODSLoadingSpinnerLabelAlignment.HORIZONTAL
                        )
                    )
                }
            }

            uiState.error != null -> {
                ODSColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    gap = DSVariables.spacingComponent3
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            imageVector = Icons.Default.EmojiEvents,
                            tint = scheme.functionalDestructiveStandard,
                            contentDescription = null
                        ), modifier = Modifier.size(48.dp)
                    )
                    ODSText(
                        text = uiState.error ?: "Error loading leaderboard",
                        style = DSTextStyles.bodyMRegular,
                        color = scheme.functionalDestructiveStandard,
                        textAlign = TextAlign.Center
                    )
                    ODSButton(
                        scheme = scheme, props = ODSButtonProps(
                            label = "Retry",
                            variant = ODSButtonVariant.PRIMARY,
                            size = ODSButtonSize.SMALL
                        ), onClick = { viewModel.refresh() })
                }
            }

            else -> {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.weight(1f),
                    userScrollEnabled = true
                ) { pageIndex ->
                    val entries =
                        if (pageIndex == 0) uiState.dailyEntries else uiState.weeklyEntries
                    LeaderboardContent(
                        entries = entries,
                        currentUserId = uiState.currentUserId,
                        scheme = scheme,
                        rank1Scheme = rank1Scheme,
                        rank2Scheme = rank2Scheme,
                        rank3Scheme = rank3Scheme
                    )
                }
            }
        }
    }
}

@Composable
fun LeaderboardContent(
    entries: List<LeaderboardEntry>,
    currentUserId: String?,
    scheme: ODSTheme,
    rank1Scheme: ODSTheme,
    rank2Scheme: ODSTheme,
    rank3Scheme: ODSTheme,
) {
    val scrollState = rememberScrollState()

    ODSColumn(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        padding = ODSPadding(
            top = DSVariables.spacingComponent4,
            horizontal = DSVariables.spacingComponent4
        )
    ) {
        if (entries.isNotEmpty()) {
            entries.forEachIndexed { index, entry ->
                val itemScheme = when (entry.rank) {
                    1 -> rank1Scheme
                    2 -> rank2Scheme
                    3 -> rank3Scheme
                    else -> scheme
                }
                LeaderboardItem(
                    entry = entry,
                    isCurrentUser = entry.userId == currentUserId,
                    scheme = itemScheme
                )
                if (index < entries.size - 1) {
                    Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                }
            }
        } else {
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3),
                gap = DSVariables.spacingComponent3
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        imageVector = Icons.Default.EmojiEvents,
                        tint = scheme.basicTextRecessive,
                        contentDescription = null
                    ), modifier = Modifier.size(64.dp)
                )
                ODSText(
                    text = stringResource(R.string.no_leaderboard_data_available),
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun TopThreePlayersSection(
    firstPlace: LeaderboardEntry,
    secondPlace: LeaderboardEntry,
    thirdPlace: LeaderboardEntry,
    rank1Scheme: ODSTheme,
    rank2Scheme: ODSTheme,
    rank3Scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {

        TopPlayerCard(
            entry = secondPlace,
            rank = 2,
            modifier = Modifier.weight(1f),
            scheme = rank2Scheme
        )

        Spacer(modifier = Modifier.width(DSVariables.spacingComponent2))

        // First Place (Center) - Larger with crown
        TopPlayerCard(
            entry = firstPlace,
            rank = 1,
            modifier = Modifier.weight(1.2f),
            scheme = rank1Scheme
        )

        Spacer(modifier = Modifier.width(DSVariables.spacingComponent2))

        // Third Place (Right)
        TopPlayerCard(
            entry = thirdPlace,
            rank = 3,
            modifier = Modifier.weight(1f),
            scheme = rank3Scheme
        )
    }
}

@Composable
private fun TopPlayersSection(
    entries: List<LeaderboardEntry>,
    rank1Scheme: ODSTheme,
    rank2Scheme: ODSTheme,
    rank3Scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier.fillMaxWidth(),
        padding = ODSPadding(vertical = DSVariables.spacingComponent3),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        entries.forEachIndexed { index, entry ->
            if (index > 0) {
                Spacer(modifier = Modifier.width(DSVariables.spacingComponent2))
            }
            // Use ColorPalette scheme based on rank
            val cardScheme = when (entry.rank) {
                1 -> rank1Scheme
                2 -> rank2Scheme
                3 -> rank3Scheme
                else -> neutralScheme
            }
            TopPlayerCard(
                entry = entry,
                rank = entry.rank,
                modifier = Modifier.weight(1f),
                scheme = cardScheme
            )
        }
    }
}

@Composable
private fun TopPlayerCard(
    entry: LeaderboardEntry,
    rank: Int,
    modifier: Modifier = Modifier,
    scheme: ODSTheme
) {
    ODSColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        gap = DSVariables.spacingComponent1
    ) {
        val ringSize = if (rank == 1) 100.dp else 85.dp
        Box(
            modifier = Modifier.size(ringSize),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .zIndex(2f)
                    .align(Alignment.TopCenter)
                    .offset(y = (-15).dp)
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(scheme.basicAccentSecondary.getColor()),
                contentAlignment = Alignment.Center
            ) {
                ODSText(
                    text = "$rank",
                    style = DSTextStyles.bodySBold,
                    color = scheme.basicText
                )
            }

            Box(
                modifier = Modifier
                    .size(ringSize)
                    .padding(DSVariables.spacingComponent1)
                    .clip(CircleShape)
                    .background(scheme.basicBackground.getColor())
                    .border(
                        width = DSVariables.spacingComponent1,
                        color = scheme.basicAccentSecondary.getColor(), shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                ODSText(
                    text = getInitials(entry.name, entry.username ?: ""),
                    style = if (rank == 1)
                        DSTextStyles.bodyL
                    else
                        DSTextStyles.bodyL,
                    color = scheme.basicText
                )
            }
        }

        Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))

        ODSText(
            text = entry.name ?: entry.username,
            style = if (rank == 1) DSTextStyles.bodyMBold else DSTextStyles.bodyMRegular,
            color = scheme.basicText,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Composable
private fun LeaderboardItem(
    entry: LeaderboardEntry,
    isCurrentUser: Boolean,
    scheme: ODSTheme,
) {
    ODSCardBasic(
        modifier = Modifier.fillMaxWidth(),
        scheme = scheme,
        props = ODSCardBasicProps(isHorizontal = true),
        contentSlot = {
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSBox(
                    modifier = Modifier
                        .defaultMinSize(minWidth = 24.dp, minHeight = 24.dp)
                        .clip(CircleShape)
                        .background(
                            color = neutralScheme.basicBackground.getColor()
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = "${entry.rank}",
                        style = DSTextStyles.bodyMRegular,
                        color = neutralScheme.basicText
                    )
                }

                ODSColumn(
                    modifier = Modifier.weight(1f), gap = DSVariables.spacingComponent0
                ) {
                    ODSText(
                        text = if (isCurrentUser) "You" else (entry.name ?: entry.username
                        ?: entry.userId),
                        style = DSTextStyles.oxBodySBold,
                        color = scheme.basicText
                    )
                }
                ODSTagStatic(
                    props = ODSTagStaticProps(
                        label = formatDuration(entry.totalScreenTime ?: 0),
                        type = ODSTagStaticType.PROMOTION
                    )
                )
            }
        })
}

/**
 * User rank notification card
 */
@Composable
private fun UserRankNotificationCard(
    rank: Int, duration: String, scheme: ODSTheme
) {
    ODSInlineNotification(
        modifier = Modifier.fillMaxWidth(),
        scheme = scheme,
        props = ODSInlineNotificationProps(
            title = "Your Rank",
            text = "You are ranked #$rank with $duration screen time",
            mode = ODSInlineNotificationMode.INFORMATIVE,
            showCloseButton = false
        ),
        onDismiss = {})
}

/**
 * Get initials from a name or username
 */
private fun getInitials(name: String?, username: String): String {
    val text = name?.trim() ?: username.trim()
    if (text.isEmpty()) return "?"

    val parts = text.split(" ", ".", "_", "-").filter { it.isNotEmpty() }
    return when {
        parts.size >= 2 -> "${parts[0].first().uppercaseChar()}${
            parts[1].first().uppercaseChar()
        }"

        text.length >= 2 -> text.take(2).uppercase()
        else -> text.first().uppercaseChar().toString()
    }
}
