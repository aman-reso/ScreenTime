package com.app.screentime.profile.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.R
import com.app.screentime.profile.viewmodel.BlockedSitesViewModel
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotification
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationMode
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationProps
import com.telekom.odsystem.tokens.tokens.ODSTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockedSitesBottomSheetContent(
    onDismiss: () -> Unit,
    viewModel: BlockedSitesViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme

) {
    val uiProps by viewModel.uiProps.collectAsState()

    ODSBottomSheet(
        showBottomSheet = true,
        onDismissRequest = onDismiss,
        titleSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent2
            ) {
                ODSText(
                    text = stringResource(R.string.blocked_sites),
                    style = DSTextStyles.titleM,
                    color = scheme.basicText
                )
                if (uiProps != null && !uiProps!!.isLoading && uiProps!!.error == null) {
                    val blockedSites = uiProps!!.blockedSites
                    if (blockedSites.isNotEmpty()) {
                        ODSText(
                            text = stringResource(R.string.blocked_sites_count, blockedSites.size),
                            style = DSTextStyles.bodyMRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }
                if (uiProps?.isLoading == true) {
                    ODSText(
                        text = stringResource(R.string.loading),
                        style = DSTextStyles.bodyMRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            }
        },
        contentSlot = {
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                gap = DSVariables.spacingComponent3
            ) {
                when {
                    uiProps == null || uiProps!!.isLoading -> {
                        ODSBox(
                            modifier = Modifier.fillMaxWidth(),
                            padding = ODSPadding(vertical = DSVariables.spacingComponent8),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSText(
                                text = stringResource(R.string.loading),
                                style = DSTextStyles.bodyMRegular,
                                color = scheme.basicTextRecessive
                            )
                        }
                    }

                    uiProps!!.error != null -> {
                        ODSInlineNotification(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSInlineNotificationProps(
                                mode = ODSInlineNotificationMode.ERROR,
                                text = uiProps!!.error,
                                showCloseButton = false
                            ),
                            onDismiss = { viewModel.clearError() }
                        )
                    }

                    uiProps!!.blockedSites.isEmpty() -> {
                        ODSBox(
                            modifier = Modifier.fillMaxWidth(),
                            padding = ODSPadding(vertical = DSVariables.spacingComponent8),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSText(
                                text = stringResource(R.string.no_blocked_sites),
                                style = DSTextStyles.bodyMRegular,
                                color = scheme.basicTextRecessive
                            )
                        }
                    }

                    else -> {
                        uiProps!!.blockedSites.forEach { site ->
                            ODSBox(
                                modifier = Modifier.fillMaxWidth(),
                                background = listOf(ODSColorModel(scheme.basicBackgroundCard)),
                                cornerRadius = ODSCorners(all = DSVariables.radiusMedium),
                                padding = ODSPadding(all = DSVariables.spacingComponent5)
                            ) {
                                ODSText(
                                    text = site,
                                    style = DSTextStyles.bodyMRegular,
                                    color = scheme.basicText
                                )
                            }
                        }
                    }
                }
            }
        },
        onCloseClicked = onDismiss
    )
}
