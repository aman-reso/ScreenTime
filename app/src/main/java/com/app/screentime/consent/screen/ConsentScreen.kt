package com.app.screentime.consent.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.R
import com.app.screentime.consent.viewmodel.ConsentViewModel
import com.telekom.odsystem.atoms.ODSText

import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.switch.ODSSwitch
import com.telekom.odsystem.atoms.switch.ODSSwitchProps
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ConsentScreen(
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    viewModel: ConsentViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme

) {
    val uiState by viewModel.uiState.collectAsState()

    // Get consent items from API response
    val consentItems = uiState.consentItems

    // Call onAccept when submission completes (success or failure)
    LaunchedEffect(uiState.isSubmitted) {
        if (uiState.isSubmitted) {
            onAccept()
        }
    }

    // Track consent values - use individual state for each item
    val consentValueStates = remember(consentItems) {
        consentItems.mapIndexed { index, item ->
            index to mutableStateOf(item.isMandatory) // Mandatory items default to true
        }.toMap()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        ODSBox(
            modifier = Modifier
                .fillMaxSize()
                .clickable { /* Prevent dismiss on background click */ },
            background = listOf(ODSColorModel(HexColor("#000000", alpha = 0.5f)))
        ) {
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                background = listOf(ODSColorModel(scheme.basicBackgroundCard)),
                cornerRadius = ODSCorners(all = 28.dp),
                contentAlignment = Alignment.Center
            ) {
                ODSColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp)
                ) {
                    // Header with close button
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(40.dp)) // Balance for close button

                        ODSBox(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSText(
                                text = stringResource(R.string.privacy_consent),
                                style = DSTextStyles.bodyL,
                                color = scheme.basicText
                            )
                        }

                        ODSButton(
                            scheme = scheme,
                            props = ODSButtonProps(
                                buttonIcon = ODSIconModel(
                                    imageVector = Icons.Default.Close,
                                    tint = scheme.basicTextRecessive,
                                    contentDescription = "Close"
                                ),
                                buttonType = ODSButtonButtonType.ICON_ONLY,
                                variant = ODSButtonVariant.GHOST,
                                size = ODSButtonSize.SMALL
                            ),
                            onClick = onDismiss
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Introductory text
                    ODSText(
                        text = stringResource(R.string.privacy_consent_description),
                        style = DSTextStyles.bodyMRegular,
                        color = scheme.basicText
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Loading state
                    if (uiState.isLoading) {
                        ODSBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // ODSLoader if available, otherwise show text
                            ODSText(
                                text = stringResource(R.string.loading),
                                style = DSTextStyles.bodyMRegular,
                                color = scheme.basicTextRecessive
                            )
                        }
                    } else if (uiState.error != null && consentItems.isEmpty()) {
                        // Error state
                        ODSBox(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSText(
                                text = uiState.error
                                    ?: stringResource(R.string.failed_to_load_consents),
                                style = DSTextStyles.bodyMRegular,
                                color = scheme.functionalDestructiveStandard
                            )
                        }
                    } else if (consentItems.isEmpty()) {
                        // Empty state
                        ODSBox(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSText(
                                text = stringResource(R.string.no_consents_available),
                                style = DSTextStyles.bodyMRegular,
                                color = scheme.basicTextRecessive
                            )
                        }
                    } else {
                        // Display submission error if any
                        if (uiState.error != null && !uiState.isLoading) {
                            ODSText(
                                text = uiState.error
                                    ?: stringResource(R.string.failed_to_submit_consents),
                                style = DSTextStyles.bodyMBold,
                                color = scheme.functionalDestructiveStandard
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Display consent items from API
                        consentItems.forEachIndexed { index, consentItem ->
                            val state = consentValueStates[index]
                            if (state != null) {
                                val currentValue by state
                                ConsentSectionCard(
                                    title = consentItem.name,
                                    description = consentItem.description,
                                    checked = currentValue,
                                    isMandatory = consentItem.isMandatory,
                                    onCheckedChange = { newValue ->
                                        state.value = newValue
                                    },
                                    scheme = scheme
                                )
                                if (index < consentItems.size - 1) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Accept button
                    ODSButton(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = if (uiState.isSubmitting) stringResource(R.string.submitting) else stringResource(
                                R.string.accept
                            ),
                            disabled = uiState.isLoading || uiState.isSubmitting || consentItems.isEmpty()
                        ),
                        onClick = {
                            // Get final consent values (mandatory items will always be true)
                            val finalValues = consentItems.mapIndexed { index, item ->
                                index to if (item.isMandatory) {
                                    true
                                } else {
                                    consentValueStates[index]?.value ?: false
                                }
                            }.toMap()
                            viewModel.submitConsents(finalValues)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ConsentSectionCard(
    title: String,
    description: String,
    checked: Boolean,
    isMandatory: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    scheme: ODSTheme = neutralScheme
) {
    ODSBox(
        modifier = Modifier.fillMaxWidth(),
        background = if (isMandatory && checked) {
            listOf(ODSColorModel(scheme.basicAccent))
        } else {
            listOf(ODSColorModel(scheme.basicBackgroundCard))
        },
        cornerRadius = ODSCorners(all = 20.dp),
        padding = ODSPadding(all = 16.dp)
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSColumn(
                modifier = Modifier.weight(1f)
            ) {
                ODSText(
                    text = title,
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicText
                )
                Spacer(modifier = Modifier.height(4.dp))
                ODSText(
                    text = description,
                    style = DSTextStyles.bodyMBold,
                    color = scheme.basicTextRecessive
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // ODS Switch
            ODSSwitch(
                scheme = scheme,
                props = ODSSwitchProps(
                    selected = if (isMandatory) true else checked,
                    disabled = isMandatory,
                    readOnly = isMandatory
                ),
                onCheckedChange = onCheckedChange
            )
        }
    }
}


