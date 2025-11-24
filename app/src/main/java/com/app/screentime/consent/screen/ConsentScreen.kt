package com.app.screentime.consent.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.consent.viewmodel.ConsentViewModel
import com.app.screentime.ui.atom.AppLoader
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.LocalAppColors

@Composable
fun ConsentScreen(
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    viewModel: ConsentViewModel = hiltViewModel()
) {
    val colors = LocalAppColors.current ?: return
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { /* Prevent dismiss on background click */ }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .align(Alignment.Center)
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFDFCFF) // Light off-white background
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp)
                ) {
                    // Header with close button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(40.dp)) // Balance for close button

                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            AppText(
                                text = "Privacy & Consent",
                                style = AppTextStyle.Title,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1C1B1F) // Dark grey/black
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color(0xFF49454F), // Dark grey
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Introductory text
                    AppText(
                        text = "We value your privacy and want to be transparent about how we use your data. Please review and accept the following:",
                        style = AppTextStyle.Body,
                        color = Color(0xFF1C1B1F) // Dark grey
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Loading state
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
                        // Error state
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            AppText(
                                text = uiState.error ?: "Failed to load consents",
                                style = AppTextStyle.Body,
                                color = colors.error
                            )
                        }
                    } else if (consentItems.isEmpty()) {
                        // Empty state
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            AppText(
                                text = "No consents available",
                                style = AppTextStyle.Body,
                                color = colors.textSecondary
                            )
                        }
                    } else {
                        // Display submission error if any
                        if (uiState.error != null && !uiState.isLoading) {
                            AppText(
                                text = uiState.error ?: "Failed to submit consents",
                                style = AppTextStyle.Label,
                                color = colors.error
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
                                    }
                                )
                                if (index < consentItems.size - 1) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Accept button
                    Button(
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
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !uiState.isLoading && !uiState.isSubmitting && consentItems.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6750A4), // Solid purple
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFFE0E0E0),
                            disabledContentColor = Color(0xFF9E9E9E)
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        if (uiState.isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        AppText(
                            text = if (uiState.isSubmitting) "Submitting..." else "Accept",
                            style = AppTextStyle.Body,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
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
    onCheckedChange: (Boolean) -> Unit
) {
    // Light lavender background for mandatory items when checked
    val lightLavender = Color(0xFFEADDFF) // Light lavender/purple tint
    val cardBackground = if (isMandatory && checked) {
        lightLavender
    } else {
        Color(0xFFFDFCFF) // White/very light off-white
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                AppText(
                    text = title,
                    style = AppTextStyle.Body,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C1B1F) // Dark grey/black
                )
                Spacer(modifier = Modifier.height(4.dp))
                AppText(
                    text = description,
                    style = AppTextStyle.Label,
                    color = Color(0xFF49454F) // Medium grey
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Custom Switch
            ConsentSwitch(
                checked = if (isMandatory) true else checked,
                onCheckedChange = onCheckedChange,
                enabled = !isMandatory
            )
        }
    }
}

@Composable
private fun ConsentSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean
) {
    val primaryPurple = Color(0xFF6750A4) // Solid purple
    val grayTrack = Color(0xFFE0E0E0) // Light grey
    val darkGrayHandle = Color(0xFF79747E) // Dark grey

    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = primaryPurple,
            uncheckedThumbColor = darkGrayHandle,
            uncheckedTrackColor = grayTrack
        ),
        thumbContent = {
            if (!checked) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Color.White
                )
            }
            // When checked, no icon (empty thumbContent)
        }
    )
}

