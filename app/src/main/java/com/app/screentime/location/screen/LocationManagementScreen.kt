package com.app.screentime.location.screen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.common.component.AppLargeSectionTitle
import com.app.screentime.config.R
import com.app.screentime.location.viewmodel.LocationState
import com.app.screentime.location.viewmodel.LocationViewModel
import com.app.screentime.navigation.ToastSnackbarManager
import com.app.screentime.permission.createPermissionManager
import com.app.screentime.utils.DateUtils
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.skeleton.ODSSkeleton
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonProps
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonVariant
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.organisms.cardnotification.ODSCardNotification
import com.telekom.odsystem.organisms.cardnotification.ODSCardNotificationProps
import com.telekom.odsystem.organisms.cardswitch.ODSCardSwitch
import com.telekom.odsystem.organisms.cardswitch.ODSCardSwitchProps
import com.telekom.odsystem.organisms.cardswitch.ODSCardSwitchVariant
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.atoms.link.ODSLinkProps
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotification
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationMode
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationProps
import com.telekom.odsystem.organisms.pageheader.ODSPageHeader
import com.telekom.odsystem.organisms.pageheader.ODSPageHeaderProps
import com.telekom.odsystem.organisms.pageheader.ODSPageHeaderType
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationManagementScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: LocationViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme
) {
    val uiState by viewModel.uiState.collectAsState()
    val activity = LocalActivity.current ?: return
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val locationDialogLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.fetchCurrentLocation(useHighAccuracy = true)
        } else {
            // Location dialog denied - show warning notification
            viewModel.setLocationDialogDenied()
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val hasPermission = permissions.values.any { it }
        if (hasPermission) {
            // Permission granted - request location dialog
            requestLocationDialog(activity, viewModel, locationDialogLauncher)
        } else {
            // Permission denied - update state to show error message
            viewModel.setPermissionDenied()
        }
    }

    // Check permissions on initial load
    LaunchedEffect(Unit) {
        if (!hasLocationPermission(context)) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            requestLocationDialog(
                activity = activity, viewModel = viewModel, launcher = locationDialogLauncher
            )
        }
    }

    // Re-check permissions when screen resumes (e.g., returning from settings)
    // This handles the case when user grants permission in settings and comes back
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // Re-check permissions when screen resumes
                if (hasLocationPermission(context)) {
                    // Permission is now granted - request location dialog
                    if (uiState.state == LocationState.PERMISSION_DENIED || uiState.state == LocationState.IDLE) {
                        requestLocationDialog(
                            activity = activity, viewModel = viewModel, launcher = locationDialogLauncher
                        )
                    }
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    // Show toast messages
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            ToastSnackbarManager.showToast(error)
            viewModel.clearError()
        }
    }

    ODSColumn(
        background = listOf(ODSColorModel(scheme.basicBackground)),
        modifier = modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        ODSPageHeader(
            modifier = Modifier.fillMaxWidth(), scheme = scheme, props = ODSPageHeaderProps(
                type = ODSPageHeaderType.SUB_PAGE_HEADER
            ), subPageTitleSlot = {
                ODSText(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.manage_location),
                    style = DSTextStyles.bodyMBold,
                    color = scheme.basicText
                )
            }, onBackButtonClick = onBackClick
        )

        ODSLazyColumn(
            modifier = Modifier.fillMaxSize(), padding = ODSPadding(
                all = DSVariables.spacingComponent4
            ), gap = DSVariables.spacingComponent4
        ) {

            item {
                Spacer(modifier = Modifier.height(DSVariables.spacingComponent4))
                ODSText(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.control_center_description),
                    style = DSTextStyles.bodySRegular,
                    color = scheme.basicText
                )
            }

            item {
                AppLargeSectionTitle(title = stringResource(R.string.current_location))
            }

            item {
                when (uiState.state) {
                    LocationState.LOADING -> {
                        LocationShimmer(scheme = scheme)
                    }

                    LocationState.SUCCESS -> {
                        uiState.location?.let { location ->
                            CurrentLocationNotification(
                                location = location, scheme = scheme
                            )
                        } ?: run {
                            ODSInlineNotification(
                                modifier = Modifier.fillMaxWidth(),
                                scheme = scheme,
                                props = ODSInlineNotificationProps(
                                    mode = ODSInlineNotificationMode.INFORMATIVE,
                                    title = stringResource(R.string.location_not_available),
                                    text = stringResource(R.string.location_not_available),
                                    showCloseButton = false
                                ),
                                onDismiss = {})
                        }
                    }

                    LocationState.ERROR -> {
                        // Show error notification
                        ODSInlineNotification(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSInlineNotificationProps(
                                mode = ODSInlineNotificationMode.ERROR,
                                title = stringResource(R.string.error),
                                text = uiState.error
                                    ?: stringResource(R.string.location_not_available),
                                showCloseButton = false
                            ),
                            onDismiss = {})
                    }

                    LocationState.PERMISSION_DENIED -> {
                        // Show permission denied notification with action button
                        PermissionDeniedNotification(
                            onOpenSettings = {
                                context.startActivity(
                                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                        data = android.net.Uri.fromParts(
                                            "package", context.packageName, null
                                        )
                                    })
                            }, onRetry = {
                                if (!hasLocationPermission(context)) {
                                    permissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                } else {
                                    requestLocationDialog(
                                        activity, viewModel, locationDialogLauncher
                                    )
                                }
                            }, scheme = scheme
                        )
                    }

                    LocationState.LOCATION_DIALOG_DENIED -> {
                        // Show warning notification when location dialog is denied
                        ODSInlineNotification(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSInlineNotificationProps(
                                mode = ODSInlineNotificationMode.WARNING,
                                title = stringResource(R.string.location_not_enabled),
                                text = uiState.error
                                    ?: stringResource(R.string.location_permission_required),
                                link1Props = ODSLinkProps(
                                    label = stringResource(R.string.open_settings)
                                ),
                                showCloseButton = false
                            ),
                            onFirstLinkClicked = {
                                context.startActivity(
                                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                )
                            },
                            onDismiss = {}
                        )
                    }

                    LocationState.IDLE -> {
                        if (!hasLocationPermission(context)) {
                            PermissionDeniedNotification(
                                onOpenSettings = {
                                    context.startActivity(
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                            data = android.net.Uri.fromParts(
                                                "package", context.packageName, null
                                            )
                                        })
                                }, onRetry = {
                                    permissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                }, scheme = scheme
                            )
                        }
                    }
                }
            }

            // Show last location card
            uiState.userLastLocation?.let { lastLocation ->
                item {
                    LastLocationCard(
                        location = lastLocation,
                        scheme = scheme
                    )
                }
            }
        }
    }
}

@Composable
private fun LocationShimmer(
    scheme: ODSTheme
) {
    ODSSkeleton(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        scheme = scheme,
        props = ODSSkeletonProps(
            variant = ODSSkeletonVariant.MEDIUM
        )
    )
}

@Composable
private fun CurrentLocationNotification(
    location: com.app.screentime.network.model.LocationData, scheme: ODSTheme
) {
    // Build location text
    val locationText = buildString {
        val parts = mutableListOf<String>()

        if (location.latitude != null && location.longitude != null) {
            parts.add("Lat: ${location.latitude}, Lon: ${location.longitude}")
        }
        if (!location.address.isNullOrEmpty()) {
            parts.add("Address: ${location.address}")
        }
        if (!location.lastUpdated.isNullOrEmpty()) {
            val formattedDate = try {
                DateUtils.formatDateTime(location.lastUpdated)
            } catch (e: Exception) {
                location.lastUpdated
            }
            parts.add("Updated: $formattedDate")
        }

        append(parts.joinToString("\n"))
    }

    ODSInlineNotification(
        modifier = Modifier.fillMaxWidth(), scheme = scheme, props = ODSInlineNotificationProps(
            mode = ODSInlineNotificationMode.INFORMATIVE,
            title = stringResource(R.string.current_location),
            text = locationText.ifEmpty { stringResource(R.string.location_not_available) },
            showCloseButton = false
        ), onDismiss = {})
}

@Composable
private fun LastLocationCard(
    location: com.app.screentime.network.model.UserLastLocationData,
    scheme: ODSTheme
) {
    val locationText = buildString {
        if (!location.address.isNullOrBlank()) {
            append(location.address)
        } else {
            append("${location.latitude}, ${location.longitude}")
        }
        location.timestamp?.let { timestamp ->
            try {
                val timeAgo = DateUtils.getTimeAgo(DateUtils.toMillis(timestamp))
                append("\n$timeAgo")
            } catch (e: Exception) {
                // Ignore parsing errors
            }
        }
    }

    ODSCardNotification(
        modifier = Modifier.fillMaxWidth(),
        scheme = scheme,
        props = ODSCardNotificationProps(
            showCloseButton = false,
            title = stringResource(R.string.last_location),
            text = locationText
        )
    )
}

@Composable
private fun ShareLocationCard(
    shareLocation: Boolean, onToggle: (Boolean) -> Unit, isUpdating: Boolean, scheme: ODSTheme
) {
    ODSCardSwitch(
        modifier = Modifier.fillMaxWidth(), scheme = scheme, props = ODSCardSwitchProps(
            title = stringResource(R.string.share_location),
            subtitle = stringResource(R.string.enable_location_sharing),
            selected = shareLocation,
            variant = ODSCardSwitchVariant.TITLE
        ), onClick = { enabled ->
            if (!isUpdating) {
                onToggle(enabled)
            }
        })
}

@Composable
private fun PermissionDeniedNotification(
    onOpenSettings: () -> Unit, onRetry: () -> Unit, scheme: ODSTheme
) {
    ODSColumn(
        modifier = Modifier.fillMaxWidth(), gap = DSVariables.spacingComponent3
    ) {
        ODSInlineNotification(
            modifier = Modifier.fillMaxWidth(), scheme = scheme, props = ODSInlineNotificationProps(
                mode = ODSInlineNotificationMode.ERROR,
                title = stringResource(R.string.location_permission_denied),
                text = stringResource(R.string.location_permission_required),
                showCloseButton = false
            ), onDismiss = {})

        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End,
            horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3)
        ) {
            ODSButton(
                modifier = Modifier.weight(1f), scheme = scheme, props = ODSButtonProps(
                    label = stringResource(R.string.open_settings),
                    variant = ODSButtonVariant.SECONDARY,
                    size = ODSButtonSize.SMALL
                ), onClick = onOpenSettings
            )
        }
    }
}


fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}


fun requestLocationDialog(
    activity: Activity?,
    viewModel: LocationViewModel,
    launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>? = null
) {
    if (activity == null) return
    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, 10_000
    ).build()

    val settingsRequest = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        .setAlwaysShow(true) // 🔥 forces dialog
        .build()

    val settingsClient = LocationServices.getSettingsClient(activity)

    settingsClient.checkLocationSettings(settingsRequest).addOnSuccessListener {
        viewModel.fetchCurrentLocation(useHighAccuracy = true)
    }.addOnFailureListener { exception ->
        if (exception is ResolvableApiException && launcher != null) {
            val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
            launcher.launch(intentSenderRequest)
        } else {
            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        }
    }
}
