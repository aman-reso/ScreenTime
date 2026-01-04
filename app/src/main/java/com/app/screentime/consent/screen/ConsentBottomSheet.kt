package com.app.screentime.consent.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.consent.viewmodel.ConsentViewModel
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.switch.ODSSwitch
import com.telekom.odsystem.atoms.switch.ODSSwitchProps
import com.telekom.odsystem.molecules.listrowcontrols.ODSListRowControls
import com.telekom.odsystem.molecules.listrowcontrols.ODSListRowControlsProps
import com.telekom.odsystem.molecules.listrowcontrols.ODSListRowControlsVariant
import com.telekom.odsystem.atoms.controls.ODSControlsType
import com.telekom.odsystem.slots.dialogbottomsheetpreferredactions.ODSDialogBottomSheetPreferredActions
import com.telekom.odsystem.slots.dialogbottomsheetpreferredactions.ODSDialogBottomSheetPreferredActionsProps
import com.telekom.odsystem.slots.dialogbottomsheetpreferredactions.ODSDialogBottomSheetPreferredActionsVariant
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import androidx.compose.ui.res.stringResource
import com.app.screentime.R
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet

import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeader
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeaderProps
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeaderSize
import com.telekom.odsystem.slots.bottomsheettitlelabel.ODSBottomSheetTitleLabel
import com.telekom.odsystem.slots.bottomsheettitlelabel.ODSBottomSheetTitleLabelProps
import com.telekom.odsystem.slots.dialogbottomsheetpreferredactions.ODSDialogBottomSheetPreferredActions
import com.telekom.odsystem.tokens.tokens.ODSTheme
import javax.xml.validation.Schema

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsentBottomSheetContent(
    onDismiss: () -> Unit, onAccept: () -> Unit, viewModel: ConsentViewModel = hiltViewModel()
) {
    val scheme = neutralScheme
    val uiState by viewModel.uiState.collectAsState()

    // Get string resources
    val dataCollectionName = stringResource(R.string.consent_data_collection)
    val dataCollectionDesc = stringResource(R.string.consent_data_collection_description)
    val dataSharingName = stringResource(R.string.consent_data_sharing)
    val dataSharingDesc = stringResource(R.string.consent_data_sharing_description)
    val analyticsName = stringResource(R.string.consent_analytics)
    val analyticsDesc = stringResource(R.string.consent_analytics_description)

    // Hardcoded consent items
    val hardcodedConsentItems = remember(
        dataCollectionName,
        dataCollectionDesc,
        dataSharingName,
        dataSharingDesc,
        analyticsName,
        analyticsDesc
    ) {
        listOf(
            ConsentItemData(
                id = "data_collection",
                name = dataCollectionName,
                description = dataCollectionDesc,
                isMandatory = true
            ), ConsentItemData(
                id = "data_sharing",
                name = dataSharingName,
                description = dataSharingDesc,
                isMandatory = false
            ), ConsentItemData(
                id = "analytics",
                name = analyticsName,
                description = analyticsDesc,
                isMandatory = false
            )
        )
    }

    // Track consent values
    val consentValueStates = remember {
        hardcodedConsentItems.mapIndexed { index, item ->
            index to mutableStateOf(item.isMandatory)
        }.toMap()
    }

    LaunchedEffect(uiState.isSubmitted) {
        if (uiState.isSubmitted) {
            onAccept()
        }
    }

    ODSBottomSheet(showBottomSheet = true, onDismissRequest = onDismiss, titleSlot = {
        ODSBottomSheetHeader(
            props = ODSBottomSheetHeaderProps(
                smallHeading = stringResource(R.string.privacy_consent),
                size = ODSBottomSheetHeaderSize.SMALL
            )
        )
    }, contentSlot = {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = DSVariables.spacingComponent3
        ) {
            hardcodedConsentItems.forEachIndexed { index, consentItem ->
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
                        })
                }
            }

            if (uiState.error != null && !uiState.isSubmitting) {
                ODSText(
                    text = uiState.error ?: "Failed to submit consents",
                    style = DSTextStyles.bodySRegular,
                    color = scheme.functionalDestructiveStandard
                )
            }
        }
    }, actionSlot = {
        ODSDialogBottomSheetPreferredActions(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSDialogBottomSheetPreferredActionsProps(
                variant = ODSDialogBottomSheetPreferredActionsVariant.STACKED,
                secondaryActionProps = ODSButtonProps(
                    size = ODSButtonSize.SMALL,
                    label = if (uiState.isSubmitting) stringResource(R.string.submitting) else stringResource(
                        R.string.action_allow
                    ),
                    disabled = uiState.isSubmitting, variant = ODSButtonVariant.SECONDARY,
                ),
                mainActionProps = ODSButtonProps(
                    label = stringResource(R.string.deny),
                    variant = ODSButtonVariant.OUTLINE,
                    size = ODSButtonSize.SMALL,
                    disabled = uiState.isSubmitting
                ),
            ),
            onMainButtonClick = onDismiss,
            onSecondaryButtonClick = {
                val consentItemsWithValues = hardcodedConsentItems.map { item ->
                    val itemIndex = hardcodedConsentItems.indexOf(item)
                    val value = if (item.isMandatory) {
                        true
                    } else {
                        consentValueStates[itemIndex]?.value ?: false
                    }
                    item.id to value
                }
                viewModel.submitHardcodedConsents(consentItemsWithValues)
            })
    }, onCloseClicked = onDismiss)
}

@Composable
private fun ConsentSectionCard(
    scheme: ODSTheme = neutralScheme,
    title: String,
    description: String,
    checked: Boolean,
    isMandatory: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ODSListRowControls(
        modifier = Modifier.fillMaxWidth(), scheme = scheme, props = ODSListRowControlsProps(
            variant = ODSListRowControlsVariant.STANDARD,
            type = ODSControlsType.SWITCH_ICON,
            labelTitle = title,
            labelText = description,
            readOnly = true,
            selected = if (isMandatory) true else checked,
        ), onSwitchClick = { newValue ->
            if (!isMandatory) {
                onCheckedChange(newValue)
            }
        })
}

// Data class for hardcoded consent items
private data class ConsentItemData(
    val id: String, val name: String, val description: String, val isMandatory: Boolean
)
