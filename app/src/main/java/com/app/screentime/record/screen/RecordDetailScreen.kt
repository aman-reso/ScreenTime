package com.app.screentime.record.screen

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues

import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.record.component.DateSpinner
import com.app.screentime.record.component.SummaryTab
import com.app.screentime.record.component.TimelineTab
import com.app.screentime.record.viewmodel.RecordDetailViewModel
import com.app.screentime.ui.theme.LocalThemeMode

import com.app.screentime.utils.DateUtils
import kotlinx.coroutines.launch
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.tabs.ODSTabs
import com.telekom.odsystem.molecules.tabs.ODSTabsProps
import com.telekom.odsystem.molecules.tabs.ODSTabsSize
import com.telekom.odsystem.molecules.tabs.ODSTabsVariant
import com.telekom.odsystem.molecules.tabs.ODSTabItemModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordDetailScreen(
    modifier: Modifier = Modifier,
    username: String,
    onBackClick: () -> Unit = {},
    onNavigateToAppDetails: (String) -> Unit = {},
    scheme: ODSTheme = neutralScheme,
    viewModel: RecordDetailViewModel = hiltViewModel()
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
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val tabs = listOf("Summary", "Timeline")
    val pagerState = rememberPagerState(pageCount = { tabs.size }, initialPage = 0)

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // Track selected date for display
    val today = com.app.screentime.utils.DateUtils.today()
    var selectedDateDisplay by remember { mutableStateOf(today.toString("d MMM yyyy")) }

    // Sync tab selection with pager state
    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }

    // Sync pager with tab selection
    LaunchedEffect(selectedTabIndex) {
        if (pagerState.currentPage != selectedTabIndex) {
            coroutineScope.launch {
                pagerState.animateScrollToPage(selectedTabIndex)
            }
        }
    }

    // Tab elements
    val tabElements = listOf(
        ODSTabItemModel(label = "Summary"),
        ODSTabItemModel(label = "Timeline")
    )

    LaunchedEffect(Unit) {
        // After TOTP verification, use the new daily stats API
        // Use username as targetUserId (API may accept username or userId)
        viewModel.getDailyUsageStats(targetUserId = username)
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
        
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            padding = ODSPadding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSButton(
                scheme = scheme,
                props = ODSButtonProps(
                    buttonIcon = ODSIconModel(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        tint = scheme.basicText,
                        contentDescription = "Back"
                    ),
                    buttonType = ODSButtonButtonType.ICON_ONLY,
                    variant = ODSButtonVariant.GHOST,
                    size = com.telekom.odsystem.atoms.button.ODSButtonSize.SMALL
                ),
                onClick = onBackClick
            )

            ODSText(
                text = username,
                style = DSTextStyles.bodyL,
                color = scheme.basicText,
                modifier = Modifier.weight(1f)
            )

            ODSBox(modifier = Modifier.width(160.dp)) {
                DateSpinner(
                    onDateSelected = { selectedDate ->
                        val localDate = LocalDate.parse(
                            selectedDate,
                            DateTimeFormat.forPattern("yyyy-MM-dd")
                        )
                        selectedDateDisplay = localDate.toString("d MMM yyyy")
                        viewModel.getDailyUsageStats(targetUserId = username, date = selectedDate)
                    }
                )
            }
        }

        ODSTabs(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSTabsProps(
                tabElements = tabElements,
                size = ODSTabsSize.SMALL,
                variant = ODSTabsVariant.FILL,
                showDividerFrame = true
            ),
            selectedTabIndex = selectedTabIndex,
            onSelectedTabChange = { index ->
                selectedTabIndex = index
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }
        )

        // Tab Content with Pager
        HorizontalPager(
            state = pagerState, modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) { page ->
            when (page) {
                0 -> {
                    // Summary Tab
                    SummaryTab(
                        viewModel = viewModel,
                        uiState = uiState,
                        onNavigateToAppDetails = onNavigateToAppDetails,
                        selectedDateDisplay = selectedDateDisplay, scheme = scheme
                    )
                }

                1 -> {
                    // Timeline Tab
                    TimelineTab(
                        uiState = uiState, viewModel = viewModel, scheme = scheme
                    )
                }
            }
        }
    }
}

