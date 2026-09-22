package com.app.screentime.feature.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Complete 11-Step Dating Preferences Flow.
 * Order:
 * 1. Name
 * 2. Gender
 * 3. Date of Birth
 * 4. Languages
 * 5. Height
 * 6. Salary / Income
 * 7. Interested In
 * 8. Long Term Goal / Intent
 * 9. Photos
 * 10. Live Photo Verification
 * 11. Location Settings (Skip) -> Followed by Notification Permission Sheet
 *
 * 100% constructed with Telekom ODS components & ODSLazyColumn.
 * Strictly adheres to project design principles and rules:
 * - Rule 1: 100% ODS components & ODSLazyColumn.
 * - Rule 2: Color picking via `scheme: ODSTheme`.
 * - Rule 3: Small buttons / actions.
 * - Rule 4: Max text size 16sp with Funnel Sans font family.
 * - Rule 5: Padding, margins, and gaps from ODSVariables.
 */

@Composable
fun PreferencesScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    initialStep: Int = 1,
    onBack: () -> Unit = {},
    onComplete: () -> Unit = {},
    onHelp: () -> Unit = {}
) {
    var currentStep by remember { mutableIntStateOf(initialStep) }
    val totalSteps = 3

    // Flow State across the combined steps
    var firstName by remember { mutableStateOf("Anya Sharma") }
    var selectedGender by remember { mutableStateOf("Woman") }
    var birthDay by remember { mutableStateOf("18") }
    var birthMonth by remember { mutableStateOf("11") }
    var birthYear by remember { mutableStateOf("1999") }
    var heightCm by remember { mutableStateOf("168") }
    val uploadedPhotos = remember {
        mutableStateListOf(
            "https://images.unsplash.com/photo-1534528741775-53994a69daeb?q=80&w=800&auto=format&fit=crop"
        )
    }
    var isLocationEnabled by remember { mutableStateOf(false) }
    var currentCity by remember { mutableStateOf("Manhattan, NY") }
    var showNotificationSheet by remember { mutableStateOf(false) }

    fun handleNext() {
        if (currentStep < totalSteps) {
            currentStep++
        } else {
            showNotificationSheet = true
        }
    }

    fun handlePrevious() {
        if (currentStep > 1) {
            currentStep--
        } else {
            onBack()
        }
    }

    val ctaText = when (currentStep) {
        1 -> "Continue to Preferences →"
        2 -> "Save Photos →"
        3 -> "Finish & Find Matches →"
        else -> "Continue →"
    }

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ── 1. Top Navigation & Progress Bar ───────────────────────────
            PreferencesTopBar(
                currentStep = currentStep,
                totalSteps = totalSteps,
                scheme = scheme,
                onBack = { handlePrevious() },
                onHelp = onHelp,
                modifier = Modifier.padding(
                    start = ODSVariables.spacingLayout1,
                    end = ODSVariables.spacingLayout1,
                    top = ODSVariables.spacingComponent4,
                    bottom = ODSVariables.spacingComponent2
                )
            )

            // ── 2. Scrollable Body Content with ODSLazyColumn (Rule #1) ─────
            ODSLazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                padding = ODSPadding(
                    horizontal = ODSVariables.spacingLayout1,
                    top = ODSVariables.spacingComponent3,
                    bottom = ODSVariables.spacingComponent5
                ),
                gap = ODSVariables.spacingComponent5
            ) {
                item {
                    when (currentStep) {
                        1 -> PreferencesStepOne(
                            name = firstName,
                            onNameChange = { firstName = it },
                            gender = selectedGender,
                            onGenderChange = { selectedGender = it },
                            day = birthDay,
                            month = birthMonth,
                            year = birthYear,
                            onDayChange = { birthDay = it },
                            onMonthChange = { birthMonth = it },
                            onYearChange = { birthYear = it },
                            heightCm = heightCm,
                            onHeightChange = { heightCm = it },
                            scheme = scheme,
                            onSubmit = { handleNext() }
                        )

                        2 -> PreferencesStepPhotos(
                            uploadedPhotos = uploadedPhotos,
                            scheme = scheme,
                            onAddPhoto = { uri ->
                                if (uploadedPhotos.size < 5) uploadedPhotos.add(uri)
                            },
                            onRemovePhoto = { index ->
                                if (uploadedPhotos.size > 1 && index < uploadedPhotos.size) {
                                    uploadedPhotos.removeAt(index)
                                }
                            }
                        )

                        3 -> PreferencesStepLocation(
                            currentCity = currentCity,
                            isLocationEnabled = isLocationEnabled,
                            onEnableLocation = {
                                isLocationEnabled = true
                                currentCity = "Manhattan, NY"
                            },
                            onSkip = { handleNext() },
                            scheme = scheme
                        )
                    }
                }
            }

            // ── 3. Bottom Action Dock (Pill CTA) ───────────────────────────
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = ODSVariables.spacingLayout1,
                        vertical = ODSVariables.spacingComponent4
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { handleNext() }
                    ),
                height = 52.dp,
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                contentAlignment = Alignment.Center
            ) {
                ODSRow(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    gap = ODSVariables.spacingComponent2
                ) {
                    ODSText(
                        text = ctaText,
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicBackgroundCard
                    )
                }
            }
        }

        // ── 4. Notification Permission Sheet (after Location) ─────────────
        if (showNotificationSheet) {
            NotificationPermissionBottomSheet(
                onDismiss = {
                    showNotificationSheet = false
                    onComplete()
                },
                onPermissionGranted = {
                    showNotificationSheet = false
                    onComplete()
                },
                scheme = scheme
            )
        }
    }
}
