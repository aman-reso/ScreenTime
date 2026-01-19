package com.app.screentime.search.screen

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.ads.AdConfig
import com.app.screentime.ads.BannerAd
import com.app.screentime.ads.rememberBannerAd
import com.app.screentime.config.R
import com.app.screentime.profile.screen.VerifyTOTPBottomSheetContent
import com.app.screentime.search.viewmodel.SearchViewModel
import com.app.screentime.ui.theme.LocalThemeMode
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.atoms.resultitem.ODSResultItem
import com.telekom.odsystem.atoms.resultitem.ODSResultItemProps
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.searchbar.ODSSearchBarButtonProps
import com.telekom.odsystem.molecules.searchbar.ODSSearchBarProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.searchview.ODSSearchView
import com.telekom.odsystem.organisms.searchview.ODSSearchViewProps
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNavigateToRecordDetail: (String) -> Unit = {},
    viewModel: SearchViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showVerifyTOTP by remember { mutableStateOf(false) }
    var selectedUsername by remember { mutableStateOf<String?>(null) }
    var isCheckingTOTPStatus by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    
    // Banner ad - remember once to prevent reload when typing
    val bannerAd = rememberBannerAd(adUnitId = AdConfig.getBannerAdUnitId())

    // Dismiss keyboard when screen is disposed
    DisposableEffect(Unit) {
        onDispose {
            focusManager.clearFocus()
            keyboardController?.hide()
        }
    }
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

    ODSColumn(
        modifier = modifier.fillMaxSize(),
        padding = ODSPadding(horizontal = DSVariables.spacingComponent3),
        background = listOf(ODSColorModel(scheme.basicBackground))
    ) {
        ODSBox(
            modifier = Modifier
                .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                .fillMaxWidth()
        ) {}

        ODSSearchView(
            modifier = Modifier.fillMaxWidth(),
            scheme = neutralScheme,
            props = ODSSearchViewProps(
                showBackButton = true, searchBarProps = ODSSearchBarProps(
                    input = searchQuery,
                    placeholder = stringResource(R.string.search_user_placeholder),
                    buttonProps = ODSSearchBarButtonProps(
                        buttonIcon = ODSIconModel(
                            imageVector = Icons.Default.Clear, contentDescription = "Clear"
                        )
                    )
                )
            ),
            onSearchValueChange = {
                searchQuery = it
                if (it.length > 2) {
                    viewModel.searchUsers(it)
                } else {
                    viewModel.clearSearch()
                }
            },
            onButtonClick = {
                searchQuery = ""
                focusManager.clearFocus()
                keyboardController?.hide()
            },
            onBackButtonClick = onBackClick,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                }),
            resultListSlot = {
                if (uiState.isLoading) {
                    ODSBox(
                        modifier = Modifier
                            .height(60.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSLoadingSpinner(
                            modifier = Modifier.wrapContentHeight(),
                            scheme = scheme,
                            props = ODSLoadingSpinnerProps(
                                labelText = stringResource(R.string.please_wait),
                                size = ODSLoadingSpinnerSize.SMALL,
                                variant = ODSLoadingSpinnerVariant.STANDARD,
                                labelAlignment = ODSLoadingSpinnerLabelAlignment.HORIZONTAL
                            )
                        )
                    }
                } else {
                    if (searchQuery.length <= 2) {
                        ODSBox(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.search_empty_state),
                                contentDescription = "empty screen"
                            )
                        }
                    } else {
                        if (uiState.searchResults.isEmpty()) {
                            ODSBox(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSText(
                                    text = stringResource(R.string.no_users_found),
                                    style = DSTextStyles.bodyMRegular,
                                    color = scheme.basicText
                                )
                            }
                        } else {
                            ODSColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                // Search Results Label
                                ODSBox(
                                    modifier = Modifier.fillMaxWidth(),
                                    padding = ODSPadding(
                                        horizontal = DSVariables.spacingComponent4,
                                        vertical = DSVariables.spacingComponent3
                                    )
                                ) {
                                    ODSText(
                                        text = stringResource(R.string.search_results),
                                        style = DSTextStyles.bodyMBold,
                                        color = scheme.basicText
                                    )
                                }
                                
                                // Search Results List with Banner Ad at the end
                                ODSLazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    gap = 0.dp,
                                    padding = ODSPadding(horizontal = DSVariables.spacingComponent4)
                                ) {
                                    itemsIndexed(
                                        items = uiState.searchResults,
                                        key = { index, _ -> index }
                                    ) { index, result ->
                                        ODSResultItem(
                                            scheme = neutralScheme,
                                            props = ODSResultItemProps(labelText = result.username),
                                            onItemClick = {
                                                focusManager.clearFocus()
                                                keyboardController?.hide()
                                                coroutineScope.launch {
                                                    isCheckingTOTPStatus = true
                                                    val hasAccess = viewModel.checkTOTPStatus(result.username)
                                                    isCheckingTOTPStatus = false

                                                    if (hasAccess && !result.username.isNullOrBlank()) {
                                                        onNavigateToRecordDetail(result.username)
                                                    } else {
                                                        // No access, show TOTP verification bottom sheet
                                                        selectedUsername = result.username
                                                        showVerifyTOTP = true
                                                    }
                                                }
                                            }
                                        )
                                        if (index < uiState.searchResults.size - 1) {
                                            ODSDivider(
                                                modifier = Modifier.fillMaxWidth(),
                                                scheme = neutralScheme,
                                                props = ODSDividerProps(
                                                    variant = ODSDividerVariant.HORIZONTAL,
                                                    inset = false
                                                )
                                            )
                                        }
                                    }
                                    
                                    // Banner ad at the end
                                    if (bannerAd != null) {
                                        val (adView, adState) = bannerAd
                                        item(key = "banner_ad") {
                                            BannerAd(
                                                adView = adView,
                                                adState = adState,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            })
    }
    if (showVerifyTOTP && !selectedUsername.isNullOrBlank()) {
        VerifyTOTPBottomSheetContent(
            username = selectedUsername,
            onDismiss = {
                showVerifyTOTP = false
                selectedUsername = null
            },
            onVerifySuccess = {
                viewModel.trackTOTPVerify()
                showVerifyTOTP = false
                selectedUsername?.let { username ->
                    onNavigateToRecordDetail(username)
                }
                selectedUsername = null
            }
        )
    }
}
