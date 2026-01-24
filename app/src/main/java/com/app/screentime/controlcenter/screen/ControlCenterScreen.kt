package com.app.screentime.controlcenter.screen

import android.graphics.Color
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.common.component.AppLargeSectionTitle
import com.app.screentime.config.R
import com.app.screentime.config.data.Feature
import com.app.screentime.config.featureflag.FeatureFlagHelper
import com.app.screentime.controlcenter.viewmodel.ControlCenterViewModel
import com.app.screentime.navigation.ToastSnackbarManager
import com.app.screentime.network.model.AllowedUser
import com.app.screentime.ui.atom.PullToRefreshBox
import com.app.screentime.ui.theme.LocalThemeMode
import com.app.screentime.utils.DateUtils
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.link.ODSLink
import com.telekom.odsystem.atoms.link.ODSLinkAlignment
import com.telekom.odsystem.atoms.link.ODSLinkProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.atoms.radioicon.ODSRadioIcon
import com.telekom.odsystem.atoms.radioicon.ODSRadioIconProps
import com.telekom.odsystem.atoms.radioicon.ODSRadioIconSize
import com.telekom.odsystem.atoms.textfield.ODSTextField
import com.telekom.odsystem.atoms.textfield.ODSTextFieldProps
import com.telekom.odsystem.atoms.textfield.ODSTextFieldSize
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheetProps
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotification
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationMode
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationProps
import com.telekom.odsystem.organisms.pageheader.ODSPageHeader
import com.telekom.odsystem.organisms.pageheader.ODSPageHeaderProps
import com.telekom.odsystem.organisms.pageheader.ODSPageHeaderType
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlCenterScreen(
    viewModel: ControlCenterViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNavigateToRecordDetail: (String) -> Unit = {},
    scheme: ODSTheme = neutralScheme
) {

    val activity = LocalActivity.current
    val useDarkTheme = LocalThemeMode.current

    SideEffect {
        if (activity is AppCompatActivity) {
            activity.enableEdgeToEdge(
                statusBarStyle = if (useDarkTheme) {
                    SystemBarStyle.dark(scheme.basicBackground.getIntColor())
                } else {
                    SystemBarStyle.light(
                        scheme.basicBackground.getIntColor(),
                        darkScrim = scheme.basicBackground.getIntColor()
                    )
                }, navigationBarStyle = SystemBarStyle.auto(
                    Color.TRANSPARENT, Color.TRANSPARENT
                )
            )
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var usernameInput by remember { mutableStateOf("") }
    var selectedDuration by remember { mutableStateOf<Long?>(null) } // null = permanent
    var showManageDialog by remember { mutableStateOf<AllowedUser?>(null) }
    var selectedManageDuration by remember { mutableStateOf<Long?>(null) } // null = permanent
    var pendingRemove by remember { mutableStateOf<String?>(null) } // Track pending removal

    // Duration options in milliseconds for add dialog
    listOf(
        Pair(60 * 60 * 1000L, R.string.duration_1_hour), // 1 hour
        Pair(6 * 60 * 60 * 1000L, R.string.duration_6_hours), // 6 hours
        Pair(24 * 60 * 60 * 1000L, R.string.duration_1_day), // 1 day
        Pair(null, R.string.duration_permanent) // Permanent
    )

    // Duration options for manage dialog
    val manageDurationOptions = listOf(
        Pair(null, R.string.grant_permanent), // Permanent
        Pair(7 * 24 * 60 * 60 * 1000L, R.string.for_this_week), // 7 days (1 week)
        Pair(60 * 60 * 1000L, R.string.duration_1_hour), // 1 hour
        Pair(6 * 60 * 60 * 1000L, R.string.duration_6_hours), // 6 hours
        Pair(24 * 60 * 60 * 1000L, R.string.duration_24_hours) // 24 hours
    )
    val coroutineScope = rememberCoroutineScope()
    var previousIsAdding by remember { mutableStateOf(false) }
    var previousIsRemoving by remember { mutableStateOf<Set<String>>(emptySet()) }

    // Get string resources at composable level
    val usernameAddedMessage = stringResource(R.string.username_added)
    val usernameRemovedMessage = stringResource(R.string.username_removed)


    // Show error toast if there's an error
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            coroutineScope.launch {
                ToastSnackbarManager.showError(
                    message = error,
                    duration = 5000L
                )
            }
        }
    }

    // Show success message when user is added
    LaunchedEffect(uiState.isAdding) {
        if (previousIsAdding && !uiState.isAdding && uiState.error == null) {
            // User was successfully added
            coroutineScope.launch {
                ToastSnackbarManager.showSuccess(
                    message = usernameAddedMessage,
                    duration = 3000L
                )
            }
        }
        previousIsAdding = uiState.isAdding
    }

    // Show success message when user is removed
    LaunchedEffect(uiState.isRemoving) {
        val removedUsernames = previousIsRemoving - uiState.isRemoving
        if (removedUsernames.isNotEmpty() && uiState.error == null) {
            coroutineScope.launch {
                ToastSnackbarManager.showSuccess(
                    message = usernameRemovedMessage,
                    duration = 3000L
                )
            }
        }
        previousIsRemoving = uiState.isRemoving
    }

    // Load data on screen entry
    LaunchedEffect(Unit) {
        viewModel.loadControlPanel()
    }

    val isRefreshing = uiState.isRefreshing

    ODSColumn(
        modifier = Modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground))
    ) {
        // Status bar padding
        ODSBox(
            modifier = Modifier
                .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                .fillMaxWidth()
        ) {}

        ODSColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            ODSPageHeader(
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme,
                props = ODSPageHeaderProps(
                    type = ODSPageHeaderType.SUB_PAGE_HEADER
                ),
                subPageTitleSlot = {
                    ODSText(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = stringResource(R.string.control_center),
                        style = DSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )
                },
                onBackButtonClick = onBackClick,
                actionsSlot = {
                    ODSButton(
                        scheme = scheme,
                        props = ODSButtonProps(
                            buttonIcon = ODSIconModel(
                                imageVector = Icons.Default.Add,
                                tint = scheme.basicText,
                                contentDescription = stringResource(R.string.add_username)
                            ),
                            buttonType = ODSButtonButtonType.ICON_ONLY,
                            variant = ODSButtonVariant.GHOST,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = { showAddDialog = true }
                    )
                }
            )

            // Content with Pull to Refresh
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { viewModel.refresh() },
                modifier = Modifier.fillMaxSize()
            ) {
                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSLoadingSpinner(
                                scheme = scheme,
                                props = ODSLoadingSpinnerProps(
                                    size = ODSLoadingSpinnerSize.SMALL,
                                    variant = ODSLoadingSpinnerVariant.STANDARD,
                                    labelAlignment = ODSLoadingSpinnerLabelAlignment.NONE
                                )
                            )
                        }
                    }

                    uiState.error != null -> {
                        ODSColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    horizontal = DSVariables.spacingComponent4,
                                    vertical = DSVariables.spacingComponent7
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            ODSInlineNotification(
                                modifier = Modifier.fillMaxWidth(),
                                scheme = scheme,
                                props = ODSInlineNotificationProps(
                                    mode = ODSInlineNotificationMode.ERROR,
                                    title = stringResource(R.string.error),
                                    text = uiState.error ?: "Failed to load control panel",
                                    link1Props = ODSLinkProps(
                                        alignment = ODSLinkAlignment.LEFT,
                                        label = stringResource(R.string.retry)
                                    ),
                                    showCloseButton = false
                                ),
                                onFirstLinkClicked = { viewModel.loadControlPanel() }
                            )
                        }
                    }

                    uiState.allowedUsers.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(DSVariables.spacingComponent4),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSText(
                                text = stringResource(R.string.no_allowed_users),
                                style = DSTextStyles.bodyMRegular,
                                color = scheme.basicText,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    else -> {
                        ODSLazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            padding = ODSPadding(
                                horizontal = DSVariables.spacingComponent4,
                                vertical = DSVariables.spacingComponent3
                            ),
                            gap = DSVariables.spacingComponent3
                        ) {
                            item {
                                Spacer(modifier = Modifier.height(DSVariables.spacingComponent2))
                                ODSText(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(R.string.control_center_description),
                                    style = DSTextStyles.microcopyRegular,
                                    color = scheme.basicText
                                )
                            }

                            item {
                                AppLargeSectionTitle(title = stringResource(R.string.allowed_usernames))
                            }

                            items(uiState.allowedUsers) { user ->
                                AllowedUserItem(
                                    user = user,
                                    isRemoving = uiState.isRemoving.contains(user.username),
                                    onManageClick = { showManageDialog = user },
                                    onCardClick = { onNavigateToRecordDetail(user.username) },
                                    scheme = scheme
                                )
                            }

                            if (uiState.accessibleUsers.isNotEmpty()) {
                                item {
                                    AppLargeSectionTitle(title = stringResource(R.string.you_have_access_to_users))
                                }

                                items(uiState.accessibleUsers) { username ->
                                    AccessibleUserItem(
                                        username = username,
                                        onCardClick = { onNavigateToRecordDetail(username) },
                                        scheme = scheme
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Username Dialog
    if (showAddDialog) {
        ODSDialog(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            scheme = scheme,
            onDismissRequest = {
                usernameInput = ""
                selectedDuration = null
                showAddDialog = false
            },
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            props = ODSDialogProps(
                showCloseButton = true,
                showScrollbar = false,
                title = stringResource(R.string.add_username),
                bodyText = stringResource(R.string.control_center_add_person_description)
            ),
            contentSlot = {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = DSVariables.spacingComponent4,
                    padding = ODSPadding(top = DSVariables.spacingComponent3)
                ) {
                    ODSTextField(
                        scheme = scheme,
                        props = ODSTextFieldProps(
                            label = stringResource(R.string.username),
                            inputText = usernameInput,
                            placeholderText = stringResource(R.string.add_username),
                            size = ODSTextFieldSize.SMALL,
                            disabled = uiState.isAdding
                        ),
                        onValueChange = { usernameInput = it }
                    )
                }
            },
            actionSlot = {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    ODSButton(
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = stringResource(R.string.add_username),
                            variant = ODSButtonVariant.SECONDARY,
                            size = ODSButtonSize.SMALL,
                            disabled = usernameInput.isBlank() || uiState.isAdding
                        ),
                        onClick = {
                            val trimmed = usernameInput.trim()
                            if (trimmed.isNotBlank()) {
                                viewModel.grantAccess(trimmed)
                                usernameInput = ""
                                selectedDuration = null
                                showAddDialog = false
                            }
                        }
                    )
                }
            }
        )
    }

    // Manage Duration Bottom Sheet
    showManageDialog?.let { user ->
        // Initialize selected duration based on current user's duration
        LaunchedEffect(user.username) {
            selectedManageDuration = user.duration
            pendingRemove = null // Reset pending remove when dialog opens
        }

        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ODSBottomSheet(
            scheme = scheme,
            props = ODSBottomSheetProps(),
            showBottomSheet = true,
            bottomSheetState = sheetState,
            onDismissRequest = {
                showManageDialog = null
                selectedManageDuration = null
            },
            onCloseClicked = {
                showManageDialog = null
                selectedManageDuration = null
            },
            titleSlot = {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = DSVariables.spacingComponent2,
                    padding = ODSPadding(
                        horizontal = DSVariables.spacingComponent4,
                    )
                ) {
                    ODSText(
                        text = user.username,
                        style = DSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )
                    ODSText(
                        text = stringResource(R.string.control_center_extend_or_revoke_access),
                        style = DSTextStyles.bodySRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            },
            contentSlot = {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = 0.dp,
                    padding = ODSPadding(
                        horizontal = DSVariables.spacingComponent4,
                    )
                ) {
                    manageDurationOptions.forEachIndexed { index, (duration, stringRes) ->
                        DurationOptionRow(
                            text = stringResource(stringRes),
                            selected = pendingRemove == null && selectedManageDuration == duration,
                            onClick = {
                                // Clear pending remove when selecting a duration
                                pendingRemove = null
                                selectedManageDuration = duration
                            },
                            scheme = scheme
                        )

                        // Add divider after each option (except the last one before revoke)
                        if (index < manageDurationOptions.size - 1) {
                            ODSDivider(
                                modifier = Modifier.fillMaxWidth(),
                                scheme = scheme,
                                props = ODSDividerProps(
                                    variant = ODSDividerVariant.HORIZONTAL,
                                    spacing = false
                                )
                            )
                        }
                    }

                    ODSDivider(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSDividerProps(
                            variant = ODSDividerVariant.HORIZONTAL,
                            spacing = false
                        )
                    )

                    DurationOptionRow(
                        text = stringResource(R.string.remove),
                        selected = pendingRemove == user.username,
                        onClick = {
                            // Mark for removal instead of calling API immediately
                            if (pendingRemove == user.username) {
                                pendingRemove = null
                                selectedManageDuration = user.duration // Restore original duration
                            } else {
                                pendingRemove = user.username
                                selectedManageDuration = null // Clear duration selection
                            }
                        },
                        scheme = scheme,
                        isDestructive = true
                    )
                }
            },
            actionSlot = {
                ODSButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = stringResource(R.string.update_duration),
                        variant = ODSButtonVariant.SECONDARY,
                        size = ODSButtonSize.SMALL,
                        disabled = uiState.isRemoving.contains(user.username) ||
                                (selectedManageDuration == user.duration && pendingRemove == null)
                    ),
                    onClick = {
                        // Only call API when update button is clicked
                        if (pendingRemove == user.username) {
                            // Remove user
                            viewModel.revokeAccess(user.username)
                        } else if (selectedManageDuration != user.duration) {
                            // Update duration
                            viewModel.extendAccess(
                                user.username,
                                selectedManageDuration
                            )
                        }
                        showManageDialog = null
                        selectedManageDuration = null
                        pendingRemove = null
                    }
                )
            }
        )
    }

}

@Composable
private fun AllowedUserItem(
    user: AllowedUser,
    isRemoving: Boolean,
    onManageClick: () -> Unit,
    onCardClick: () -> Unit,
    scheme: ODSTheme
) {
    ODSCardBasic(
        contentPadding = ODSPadding(
            vertical = DSVariables.spacingComponent4,
            horizontal = DSVariables.spacingComponent4
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isRemoving, onClick = onCardClick),
        contentSlot = {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                gap = DSVariables.spacingComponent3
            ) {
                ODSColumn(
                    modifier = Modifier.weight(1f),
                    gap = DSVariables.spacingComponent2
                ) {

                    ODSText(
                        modifier = Modifier.fillMaxWidth(),
                        text = user.username,
                        style = DSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )

                    if (!user.addedAt.isNullOrEmpty()) {
                        ODSText(
                            text = user.addedAt,
                            style = DSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }

                    if (user.expiresAt != null) {
                        val expiryText =
                            "${stringResource(R.string.expires_at)} ${DateUtils.formatDateTime(user.expiresAt)}"

                        ODSText(
                            modifier = Modifier.fillMaxWidth(),
                            text = expiryText,
                            style = DSTextStyles.bodySRegular,
                            color = scheme.basicTextRecessive
                        )
                    } else if (user.duration == null) {
                        ODSText(
                            text = stringResource(R.string.permanent_access),
                            style = DSTextStyles.bodySRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }

                if (isRemoving) {
                    ODSLoadingSpinner(
                        scheme = scheme,
                        props = ODSLoadingSpinnerProps(
                            size = ODSLoadingSpinnerSize.X_SMALL,
                            variant = ODSLoadingSpinnerVariant.STANDARD,
                            labelAlignment = ODSLoadingSpinnerLabelAlignment.NONE
                        )
                    )
                } else {
                    ODSButton(
                        scheme = scheme,
                        modifier = Modifier.wrapContentWidth(),
                        props = ODSButtonProps(
                            buttonType = ODSButtonButtonType.STANDARD,
                            label = stringResource(R.string.manage),
                            leftIcon = false,
                            rightIcon = false,
                            size = ODSButtonSize.SMALL,
                            variant = ODSButtonVariant.OUTLINE
                        ), onClick = onManageClick
                    )
                }
            }
        }
    )
}

@Composable
private fun AccessibleUserItem(
    username: String,
    onCardClick: () -> Unit,
    scheme: ODSTheme
) {
    ODSCardBasic(
        onClick = onCardClick,
        contentPadding = ODSPadding(
            vertical = DSVariables.spacingComponent4,
            horizontal = DSVariables.spacingComponent4
        ),
        modifier = Modifier
            .fillMaxWidth(),
        contentSlot = {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    modifier = Modifier.weight(1f),
                    text = username,
                    style = DSTextStyles.bodyMBold,
                    color = scheme.basicText
                )
                ODSIcon(
                    iconModel = ODSIconModel(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                        tint = scheme.basicTextRecessive,
                        contentDescription = stringResource(R.string.tap_to_view_details)
                    ),
                    width = DSVariables.spacingComponent5,
                    height = DSVariables.spacingComponent5
                )
            }
        }
    )
}

@Composable
private fun DurationOptionRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    scheme: ODSTheme,
    isDestructive: Boolean = false,
    disabled: Boolean = false
) {
    ODSRow(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !disabled, onClick = onClick)
            .padding(vertical = DSVariables.spacingComponent4),
        gap = DSVariables.spacingComponent2,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ODSText(
            text = text,
            style = DSTextStyles.bodySRegular,
            color = when {
                disabled -> scheme.basicTextRecessive
                isDestructive -> scheme.functionalDestructiveStandard
                else -> scheme.basicText
            },
            modifier = Modifier.weight(1f)
        )
        ODSRadioIcon(
            props = ODSRadioIconProps(
                size = ODSRadioIconSize.SMALL,
                state = ODSActions.DEFAULT,
                selected = selected,
                disabled = disabled,
                readonly = false,
                error = false
            ),
            scheme = scheme
        )

    }
}

