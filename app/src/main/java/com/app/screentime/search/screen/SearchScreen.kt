package com.app.screentime.search.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.R
import com.app.screentime.navigation.Screen
import com.app.screentime.profile.screen.VerifyTOTPBottomSheetContent
import com.app.screentime.search.viewmodel.SearchViewModel

import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.atoms.resultitem.ODSResultItemProps
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.searchbar.ODSSearchBarButtonProps
import com.telekom.odsystem.molecules.searchbar.ODSSearchBarProps
import com.telekom.odsystem.molecules.searchresultlist.ODSSearchResultList
import com.telekom.odsystem.molecules.searchresultlist.ODSSearchResultListProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.searchview.ODSSearchView
import com.telekom.odsystem.organisms.searchview.ODSSearchViewProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showVerifyTOTP by remember { mutableStateOf(false) }
    var selectedUsername by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Dismiss keyboard when screen is disposed
    DisposableEffect(Unit) {
        onDispose {
            focusManager.clearFocus()
            keyboardController?.hide()
        }
    }

    ODSBox(
        modifier = modifier.fillMaxSize(),
        padding = ODSPadding(horizontal = DSVariables.spacingComponent3),
        background = listOf(ODSColorModel(scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            ODSSearchView(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSSearchViewProps(
                    showBackButton = true, searchBarProps = ODSSearchBarProps(
                        input = searchQuery,
                        placeholder = "@ Search User name ...",
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
                onBackButtonClick = {
                    navController.popBackStack()
                },
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
                                    labelText = "Please wait...",
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
                                        text = "No users found",
                                        style = DSTextStyles.bodyMRegular,
                                        color = scheme.basicText
                                    )
                                }
                            } else {
                                ODSSearchResultList(
                                    modifier = Modifier.fillMaxWidth(),
                                    scheme = neutralScheme,
                                    props = ODSSearchResultListProps(
                                        label = "Search Results",
                                        resultList = uiState.searchResults.map {
                                            ODSResultItemProps(labelText = it.username)
                                        }),
                                    onItemClick = { index ->
                                        focusManager.clearFocus()
                                        keyboardController?.hide()
                                        uiState.searchResults[index].also { result ->
                                            selectedUsername = result.username
                                            showVerifyTOTP = true
                                        }
                                    })
                            }
                        }
                    }
                })
        }
    }
    if (showVerifyTOTP && !selectedUsername.isNullOrBlank()) {
        VerifyTOTPBottomSheetContent(
            username = selectedUsername,
            onDismiss = {
                showVerifyTOTP = false
                selectedUsername = null
            },
            onVerifySuccess = {
                showVerifyTOTP = false
                selectedUsername?.let { username ->
                    navController.navigate(
                        Screen.RecordDetail.createRoute(username)
                    )
                }
                selectedUsername = null
            }
        )
    }
}
