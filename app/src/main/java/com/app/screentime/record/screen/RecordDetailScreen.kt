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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.config.data.Feature
import com.app.screentime.config.featureflag.FeatureFlagHelper
import com.app.screentime.record.component.DateSpinner
import com.app.screentime.record.component.SummaryTab
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

    val today = DateUtils.today()
    var selectedDateDisplay by remember { mutableStateOf("Today") }

    LaunchedEffect(Unit) {
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
                        // Format without year
                        val today = DateUtils.today()
                        selectedDateDisplay = when (localDate) {
                            today -> "Today"
                            today.minusDays(1) -> "Yesterday"
                            today.minusDays(2) -> "Day before yesterday"
                            else -> localDate.toString("d MMM")
                        }
                        viewModel.getDailyUsageStats(targetUserId = username, date = selectedDate)
                    }
                )
            }
        }

        // Summary Tab Content
        SummaryTab(
            uiState = uiState,
            onNavigateToAppDetails = onNavigateToAppDetails,
            selectedDateDisplay = selectedDateDisplay,
            scheme = scheme,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            onClearError = {
                viewModel.clearError()
            }
        )
    }
}

