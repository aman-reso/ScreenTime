package com.app.screentime.registrations.screen

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.app.screentime.navigation.ScreenTimeNavigation
import com.app.screentime.permission.RegistrationStepsContent
import com.app.screentime.permission.checkUsageStatsPermission
import com.app.screentime.permission.component.bottombar.BottomBar
import com.app.screentime.permission.component.bottombar.BottomBarProps
import com.app.screentime.profile.dialog.LanguageSelectionDialog
import com.app.screentime.permission.viewmodel.RegistrationUiState
import com.app.screentime.permission.viewmodel.RegistrationViewModel
import com.app.screentime.ui.language.LanguageViewModel
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.link.ODSLinkAlignment
import com.telekom.odsystem.atoms.link.ODSLinkProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotification
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationMode
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationProps
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RegistrationsScreen(
    scheme: ODSTheme = neutralScheme,
    registrationViewModel: RegistrationViewModel,
    deeplinkUri: android.net.Uri? = null,
    isUserInIndia: Boolean = false,
    languageViewModel: LanguageViewModel = hiltViewModel()
) {
    val activity = LocalActivity.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val currentLanguage by languageViewModel.language.collectAsState()
    var showLanguageDialog by remember { mutableStateOf(false) }
    val uiState by registrationViewModel.uiState.collectAsState()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(com.app.screentime.config.R.raw.cat))
    val usageStatsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        if (checkUsageStatsPermission(context)) {
            registrationViewModel.markRegistrationComplete()
        }
    }

    // Handle Allow button click
    val handleAllowClick = {
        if (!checkUsageStatsPermission(context)) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            if (intent.resolveActivity(context.packageManager) != null) {
                usageStatsPermissionLauncher.launch(intent)
            } else {
                val fallback = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    "package:${context.packageName}".toUri()
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                usageStatsPermissionLauncher.launch(fallback)
            }
        } else {
            registrationViewModel.markRegistrationComplete()
        }
    }


    val screenState = when {
        uiState.isRegistrationComplete && uiState.registrationError == null && checkUsageStatsPermission(
            context
        ) -> RegistrationScreenState.SUCCESS

        uiState.registrationError != null -> RegistrationScreenState.ERROR

        uiState.isRegisteringDevice -> RegistrationScreenState.LOADING

        else -> RegistrationScreenState.LOADING
    }

    AnimatedContent(
        targetState = screenState, transitionSpec = {
            fadeIn(tween(300)) + scaleIn(initialScale = 0.98f) with fadeOut(tween(200)) + scaleOut(
                targetScale = 1f
            )
        }, label = "registration_transition"
    ) { state ->
        when (state) {
            RegistrationScreenState.SUCCESS -> {
                ScreenTimeNavigation(
                    scheme = scheme, deeplinkUri = deeplinkUri, isUserInIndia = isUserInIndia
                )
            }

            RegistrationScreenState.LOADING, RegistrationScreenState.ERROR -> {
                RegistrationContent(
                    scheme = scheme,
                    uiState = uiState,
                    onRetry = {
                        registrationViewModel.registerDevice()
                    },
                    onPermissionRequested = {
                        handleAllowClick.invoke()
                    },
                    showLanguageDialog = showLanguageDialog,
                    currentLanguage = currentLanguage,
                    onLanguageDialogDismiss = {
                        showLanguageDialog = false
                    },
                    onLanguageSelected = {
                        coroutineScope.launch {
                            delay(100)
                            if (activity is Activity) {
                                activity.recreate()
                            }
                        }
                    }, showLanguageDialogClick = {
                        showLanguageDialog = true
                    }, composition = composition
                )
            }
        }
    }
}

@Composable
private fun RegistrationContent(
    currentLanguage: String,
    showLanguageDialog: Boolean = false,
    scheme: ODSTheme,
    uiState: RegistrationUiState,
    onRetry: () -> Unit,
    onPermissionRequested: () -> Unit,
    onLanguageDialogDismiss: () -> Unit = {},
    onLanguageSelected: () -> Unit,
    showLanguageDialogClick: () -> Unit, composition: LottieComposition?
) {
    val context = LocalContext.current
    ODSColumn(
        modifier = Modifier.fillMaxSize(), padding = ODSPadding(
            horizontal = DSVariables.spacingComponent4, vertical = DSVariables.spacingComponent7
        ), background = listOf(ODSColorModel(scheme.basicBackground))
    ) {
        ODSBox(
            modifier = Modifier
                .height(
                    WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                )
                .fillMaxWidth()
        ) {}

        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
        ) {
            ODSColumn(
                modifier = Modifier
                    .wrapContentHeight()
                    .verticalScroll(rememberScrollState())
                    .align(Alignment.TopStart)
            ) {
                // Header with Language Icon
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    ODSColumn(modifier = Modifier.weight(1f)) {
                        ODSText(
                            text = stringResource(id = com.app.screentime.config.R.string.hola), color = scheme.basicText, style = DSTextStyles.titleM
                        )
                        ODSText(
                            text = stringResource(id = com.app.screentime.config.R.string.welcome), color = scheme.basicText, style = DSTextStyles.titleM
                        )
                    }

                    // Language Icon
                    ODSIcon(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                showLanguageDialogClick.invoke()
                            }, iconModel = ODSIconModel(
                            imageVector = Icons.Outlined.Language, tint = scheme.basicText
                        ), width = 24.dp, height = 24.dp
                    )
                }

                RegistrationStepsContent(
                    modifier = Modifier.wrapContentHeight(),
                    scheme = scheme,
                    steps = uiState.registrationStep
                )
                CatLottieOnBoarding(composition)
            }
            if (uiState.isRegisteringDevice) {
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    padding = ODSPadding(vertical = DSVariables.spacingComponent6)
                ) {
                    ODSLoadingSpinner(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        scheme = scheme,
                        props = ODSLoadingSpinnerProps(
                            labelText = stringResource(id = com.app.screentime.config.R.string.preparing_your_app_experience),
                            labelAlignment = ODSLoadingSpinnerLabelAlignment.VERTICAL,
                            size = ODSLoadingSpinnerSize.SMALL
                        )
                    )
                }
            }
            if (uiState.isRegistrationComplete) {
                BottomBar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    scheme = scheme,
                    props = BottomBarProps.default(context),
                    onAllowClick = onPermissionRequested
                )
            }
            if (!uiState.registrationError.isNullOrEmpty()) {
                ODSInlineNotification(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    scheme = scheme,
                    props = ODSInlineNotificationProps(
                        text = uiState.registrationError,
                        mode = ODSInlineNotificationMode.ERROR,
                        showCloseButton = false,
                        link1Props = ODSLinkProps(
                            label = stringResource(id = com.app.screentime.config.R.string.retry),
                            alignment = ODSLinkAlignment.LEFT
                        )
                    ),
                    onFirstLinkClicked = onRetry
                )
            }
        }
    }

    // Language selection dialog
    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = currentLanguage, onDismiss = {
                onLanguageDialogDismiss.invoke()
            }, onLanguageSelected = { language ->
                onLanguageSelected.invoke()
            }, scheme = scheme
        )
    }
}

@Composable
fun CatLottieOnBoarding(composition: LottieComposition?) {
    val progress by animateLottieCompositionAsState(composition)
    LottieAnimation(
        composition = composition,
        progress = { progress },
    )
}

enum class RegistrationScreenState {
    LOADING, ERROR, SUCCESS
}
