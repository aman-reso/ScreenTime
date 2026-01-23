package com.app.screentime.wallpaper.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import com.app.screentime.config.R
import com.app.screentime.wallpaper.api.model.ImageItem
import com.app.screentime.wallpaper.component.WallpaperCard
import com.app.screentime.wallpaper.component.WallpaperGridShimmer
import com.app.screentime.wallpaper.model.Wallpaper
import com.app.screentime.wallpaper.viewmodel.WallpaperViewModel
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.molecules.searchbar.ODSSearchBar
import com.telekom.odsystem.molecules.searchbar.ODSSearchBarButtonProps
import com.telekom.odsystem.molecules.searchbar.ODSSearchBarProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.pageheader.ODSPageHeader
import com.telekom.odsystem.organisms.pageheader.ODSPageHeaderProps
import com.telekom.odsystem.organisms.pageheader.ODSPageHeaderType
import com.telekom.odsystem.organisms.searchview.ODSSearchView
import com.telekom.odsystem.organisms.searchview.ODSSearchViewProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun WallpaperSearchScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onNavigateToFullScreen: (String, ImageItem?) -> Unit = { _, _ -> },
    viewModel: WallpaperViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var searchResults by remember { mutableStateOf<List<Wallpaper>>(emptyList()) }
    var searchImageItems by remember { mutableStateOf<Map<String, ImageItem>>(emptyMap()) }
    var isLoadingSearch by remember { mutableStateOf(false) }
    var searchError by remember { mutableStateOf<String?>(null) }

    ODSColumn(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground))
    ) {
        ODSBox(
            modifier = Modifier
                .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                .fillMaxWidth(),
            background = listOf(ODSColorModel(scheme.basicBackgroundCard))
        ) {}


        // Search Bar
        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = DSVariables.spacingComponent4,
                    vertical = DSVariables.spacingComponent3
                )
        ) {
            ODSSearchView(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                scheme = neutralScheme,
                props = ODSSearchViewProps(
                    showBackButton = true, searchBarProps = ODSSearchBarProps(
                        input = searchQuery,
                        placeholder = stringResource(R.string.search_placeholder),
                        buttonProps = ODSSearchBarButtonProps(
                            buttonIcon = ODSIconModel(
                                imageVector = Icons.Default.Clear, contentDescription = "Clear"
                            )
                        )
                    )
                ),
                onSearchValueChange = {
                    searchQuery = it
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
                    })
            )
        }

        LaunchedEffect(searchQuery) {
            isLoadingSearch = true
            searchError = null
            try {
                // Call search API
                val result = viewModel.searchWallpapers(searchQuery)
                result.fold(
                    onSuccess = { (wallpapers, imageItems) ->
                        searchResults = wallpapers
                        searchImageItems = imageItems
                        isLoadingSearch = false
                    },
                    onFailure = { exception ->
                        searchError =
                            exception.message ?: context.getString(R.string.search_failed)
                        isLoadingSearch = false
                    }
                )
            } catch (e: Exception) {
                searchError = e.message ?: context.getString(R.string.search_failed)
                isLoadingSearch = false
            }
        }


        when {
            isLoadingSearch -> {
                WallpaperGridShimmer(scheme = scheme)
            }

            searchError != null -> {
                ODSBox(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = searchError ?: "An error occurred",
                        style = DSTextStyles.bodyMRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            }

            isSearching && searchResults.isEmpty() && !isLoadingSearch -> {
                ODSBox(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ODSColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        gap = DSVariables.spacingComponent2
                    ) {
                        ODSText(
                            text = stringResource(R.string.no_results_found),
                            style = DSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )
                        ODSText(
                            text = stringResource(R.string.try_different_search),
                            style = DSTextStyles.bodySRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }
            }

            searchResults.isNotEmpty() -> {
                SearchResultsGrid(
                    wallpapers = searchResults,
                    imageItems = searchImageItems,
                    currentHomeWallpaper = uiState.currentHomeWallpaper,
                    currentLockWallpaper = uiState.currentLockWallpaper,
                    scheme = scheme,
                    onWallpaperClick = { wallpaper ->
                        val imageItem = searchImageItems[wallpaper.id]
                        onNavigateToFullScreen(wallpaper.id, imageItem)
                    }
                )
            }

            else -> {
                // Initial state - show prompt
                ODSBox(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ODSColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        gap = DSVariables.spacingComponent2
                    ) {
                        ODSText(
                            text = stringResource(R.string.search_wallpapers_prompt),
                            style = DSTextStyles.bodyMRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }
            }
        }
    }

// Show error toast
    uiState.error?.let { error ->
        LaunchedEffect(error) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }
}

@Composable
private fun SearchResultsGrid(
    wallpapers: List<Wallpaper>,
    imageItems: Map<String, ImageItem>,
    currentHomeWallpaper: Wallpaper?,
    currentLockWallpaper: Wallpaper?,
    scheme: ODSTheme,
    onWallpaperClick: (Wallpaper) -> Unit
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isExpandedScreen = windowSizeClass.isWidthAtLeastBreakpoint(840)

    LazyVerticalGrid(
        columns = GridCells.Fixed(
            if (isExpandedScreen) 3 else 2
        ),
        contentPadding = PaddingValues(DSVariables.spacingComponent4),
        horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3),
        verticalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3)
    ) {
        items(wallpapers) { wallpaper ->
            WallpaperCard(
                wallpaper = wallpaper,
                scheme = scheme,
                onClick = {
                    onWallpaperClick(wallpaper)
                },
                isCurrentHome = wallpaper.id == currentHomeWallpaper?.id,
                isCurrentLock = wallpaper.id == currentLockWallpaper?.id
            )
        }
    }
}
