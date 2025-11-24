package com.app.screentime.search.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.R
import com.app.screentime.navigation.Screen
import com.app.screentime.profile.screen.VerifyTOTPBottomSheetContent
import com.app.screentime.search.component.GlassSearchBar
import com.app.screentime.search.component.UserSearchResultItem
import com.app.screentime.search.viewmodel.SearchViewModel
import com.app.screentime.ui.atom.AppLoader
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.LocalAppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
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

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val colors = LocalAppColors.current
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .padding(bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = colors?.tint ?: MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                )

                Spacer(modifier = Modifier.width(12.dp))

                GlassSearchBar(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    enabled = true,
                    query = searchQuery,
                    autoFocus = true,
                    onQueryChange = { query ->
                        searchQuery = query
                        if (query.length > 2) {
                            viewModel.searchUsers(query)
                        } else {
                            viewModel.clearSearch()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- Results Section ---
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AppLoader()
                    }
                }

                uiState.error != null -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            AppText(
                                text = "Error",
                                style = AppTextStyle.Body,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            AppText(
                                text = uiState.error ?: "",
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }

                searchQuery.length <= 2 -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.search_empty_state),
                            contentDescription = "empty screen"
                        )
                    }
                }

                uiState.searchResults.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AppText(
                            text = "No users found",
                            style = AppTextStyle.Body,
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                        contentPadding = PaddingValues(bottom = 32.dp)
                    ) {
                        items(
                            items = uiState.searchResults,
                            key = { user -> user.username ?: "" },
                            contentType = { "user_search_result_item" }
                        ) { user ->
                            UserSearchResultItem(
                                user = user,
                                onClick = {
                                    // Dismiss keyboard when user selects an item
                                    focusManager.clearFocus()
                                    keyboardController?.hide()

                                    user.username?.let { username ->
                                        selectedUsername = username
                                        showVerifyTOTP = true
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // --- TOTP Bottom Sheet ---
            if (showVerifyTOTP && selectedUsername != null) {
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
    }

}
