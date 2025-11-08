package com.app.screentime.consent.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.consent.component.ConsentingSection
import com.app.screentime.consent.viewmodel.ConsentViewModel
import com.app.screentime.ui.atom.AppLoader
import com.app.screentime.ui.atom.AppPrimaryButton
import com.app.screentime.ui.atom.AppSecondaryButton
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.atom.glassBottomSheetBackground
import com.app.screentime.ui.theme.NeutralBlackDark
import com.app.screentime.ui.theme.hintTextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsentBottomSheetContent(
    username: String,
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    viewModel: ConsentViewModel = hiltViewModel()
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    val uiState by viewModel.uiState.collectAsState()

    // Get consent items from API response
    val consentItems = uiState.consentItems

    // Call onAccept when submission is successful
    LaunchedEffect(uiState.isSubmitted) {
        if (uiState.isSubmitted) {
            onAccept()
        }
    }

    // Track consent values - use individual state for each item
    // Recreate states when consentItems change
    val consentValueStates = remember(consentItems) {
        consentItems.mapIndexed { index, item ->
            index to mutableStateOf(item.isMandatory) // Mandatory items default to true
        }.toMap()
    }
    ModalBottomSheet(
        modifier = Modifier,
        sheetGesturesEnabled = false,
        sheetState = sheetState,
        properties = ModalBottomSheetProperties(
            shouldDismissOnClickOutside = false
        ),
        onDismissRequest = {
        },
        containerColor = NeutralBlackDark,
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(NeutralBlackDark)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    text = "Privacy & Consent",
                    style = AppTextStyle.SubTitle,
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close, contentDescription = "Close"
                    )
                }
            }
            AppText(
                text = "We value your privacy and want to be transparent about how we use your data. Please review and accept the following:",
                style = AppTextStyle.Label
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(Modifier, DividerDefaults.Thickness, color = hintTextColor)

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AppLoader()
                }
            } else if (uiState.error != null && consentItems.isEmpty()) {
                // Show error state when loading fails
                AppText(
                    text = uiState.error ?: "Failed to load consents",
                    style = AppTextStyle.Label,
                    color = Color.Red
                )
                Spacer(modifier = Modifier.height(8.dp))
            } else if (consentItems.isEmpty()) {
                // Show empty state
                AppText(
                    text = "No consents available",
                    style = AppTextStyle.Label
                )
                Spacer(modifier = Modifier.height(8.dp))
            } else {
                // Display submission error if any
                if (uiState.error != null && !uiState.isLoading) {
                    AppText(
                        text = uiState.error ?: "Failed to submit consents",
                        style = AppTextStyle.Label,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                // Display consent items from API
                consentItems.forEachIndexed { index, consentItem ->
                    val state = consentValueStates[index]
                    if (state != null) {
                        val currentValue by state
                        ConsentingSection(
                            title = consentItem.name,
                            description = consentItem.description,
                            checked = currentValue,
                            isMandatory = consentItem.isMandatory,
                            onCheckedChange = { newValue ->
                                state.value = newValue
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppPrimaryButton(
                    modifier = Modifier.weight(1f),
                    text = if (uiState.isSubmitting) "Submitting..." else "Accept",
                    enabled = !uiState.isLoading && !uiState.isSubmitting && consentItems.isNotEmpty()
                ) {
                    // Get final consent values (mandatory items will always be true)
                    val finalValues = consentItems.mapIndexed { index, item ->
                        index to if (item.isMandatory) {
                            true
                        } else {
                            consentValueStates[index]?.value ?: false
                        }
                    }.toMap()
                    viewModel.submitConsents(finalValues)
                    // onAccept will be called after successful submission
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
