package com.app.screentime.profile.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.R
import com.app.screentime.profile.component.SettingsItemCard
import com.app.screentime.profile.component.UserProfileCard
import com.app.screentime.profile.dialog.LanguageSelectionDialog
import com.app.screentime.profile.dialog.ThemeSelectionDialog
import com.app.screentime.profile.model.ProfileSettingsUi
import com.app.screentime.profile.viewmodel.ProfileViewModel
import com.app.screentime.security.TOTP
import com.app.screentime.ui.atom.AppLoader
import com.app.screentime.ui.atom.AppLoaderType
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.language.LanguageViewModel
import com.app.screentime.ui.theme.ThemeViewModel
import com.app.screentime.widget.WidgetSetupHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant

private const val TIME_STEP_SECONDS = 60L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(),
    languageViewModel: LanguageViewModel = hiltViewModel()
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val internalState = viewModel.getInternalState()
    val currentTheme by themeViewModel.theme.collectAsState()
    val currentLanguage by languageViewModel.language.collectAsState()
    var otp by remember { mutableStateOf("") }
    var progress by remember { mutableFloatStateOf(1.0f) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "OTP Countdown"
    )

    LaunchedEffect(Unit) {
        while (true) {
            val epochSeconds = Instant.now().epochSecond
            val remainingSeconds = TIME_STEP_SECONDS - (epochSeconds % TIME_STEP_SECONDS)
            otp = TOTP.generateTOTP()
            for (i in remainingSeconds downTo 0) {
                progress = i.toFloat() / TIME_STEP_SECONDS
                delay(1000)
            }
        }
    }

    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = currentTheme,
            onDismiss = { showThemeDialog = false },
            onThemeSelected = { theme ->
                themeViewModel.setTheme(theme)
                showThemeDialog = false
            }
        )
    }

    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = currentLanguage,
            onDismiss = { showLanguageDialog = false },
            onLanguageSelected = { language ->
                languageViewModel.setLanguage(language)
                showLanguageDialog = false
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            internalState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AppLoader(type = AppLoaderType.CIRCULAR)
                }
            }

            internalState.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.error),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = internalState.error ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.clearError() }) {
                        Text(stringResource(R.string.dismiss))
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    item("Key") {
                        Column(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(R.string.one_time_password),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(24.dp))

                            Box(contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(
                                    progress = { 1f },
                                    modifier = Modifier.size(160.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    strokeWidth = 12.dp,
                                    strokeCap = StrokeCap.Round
                                )
                                CircularProgressIndicator(
                                    progress = { animatedProgress },
                                    modifier = Modifier.size(160.dp),
                                    strokeWidth = 12.dp,
                                    strokeCap = StrokeCap.Round
                                )
                                Text(
                                    text = otp,
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 4.sp,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = stringResource(R.string.otp_regenerate_message),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                        }
                    }
                    uiState.data?.let { settingsList ->
                        items(
                            items = settingsList,
                            key = { item -> item.key ?: item.text },
                            contentType = { item -> 
                                when (item) {
                                    is ProfileSettingsUi.ProfileData -> "profile_data"
                                    is ProfileSettingsUi.AccountDetails -> "account_details"
                                    is ProfileSettingsUi.Other -> "other"
                                    is ProfileSettingsUi.SectionTitle -> "section_title"
                                    is ProfileSettingsUi.Restriction -> "restriction"
                                }
                            }
                        ) { data ->
                            when (data) {
                                is ProfileSettingsUi.ProfileData -> {
                                    UserProfileCard(
                                        username = internalState.profile?.username,
                                        userId = internalState.profile?.userId
                                    )
                                }

                                is ProfileSettingsUi.Other -> {
                                    SettingsItemCard(data) {
                                        when (data.key) {
                                            "theme" -> showThemeDialog = true
                                            "language" -> showLanguageDialog = true
                                            "widget" -> {
                                                coroutineScope.launch {
                                                    WidgetSetupHelper.requestWidgetSetup(context)
                                                }
                                            }
                                        }
                                    }
                                }

                                is ProfileSettingsUi.AccountDetails -> {
                                    SettingsItemCard(data)
                                }

                                is ProfileSettingsUi.Restriction -> {
                                    SettingsItemCard(data)
                                }

                                is ProfileSettingsUi.SectionTitle -> {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    AppText(
                                        text = data.text, fontWeight = FontWeight.Bold,
                                        style = AppTextStyle.Label
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

