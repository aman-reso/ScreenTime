package com.app.screentime.feature.preferences

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

private data class CategoryOneCompletedStep(
    val subStepIndex: Int,
    val label: String,
    val value: String
)

/**
 * Category 1 (Essential details) One-by-One Sub-step Flow.
 * Enum-driven step flow with active question & completed summary cards.
 */
@Composable
fun PreferencesCategoryOneFlow(
    uiState: PreferencesUiState,
    scheme: ODSTheme,
    onNameChange: (String) -> Unit,
    onGenderSelect: (String) -> Unit,
    onAgeYearsChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onToggleLanguage: (String) -> Unit,
    onEnableLocation: () -> Unit,
    onStepSelect: (Int) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryTextColor = scheme.basicText
    val secondaryTextColor = scheme.basicTextRecessive
    val accentColor = scheme.basicAccent
    val trackColor = scheme.basicStrokeSubtle

    val currentSubStep = uiState.preferenceSubStep
    val progressFraction by animateFloatAsState(
        targetValue = currentSubStep / 6f,
        label = "categoryOneProgressAnim"
    )

    val completedSteps = remember(uiState) {
        val list = mutableListOf<CategoryOneCompletedStep>()
        if (currentSubStep > 1 && uiState.name.isNotBlank()) {
            list.add(CategoryOneCompletedStep(1, "Name", uiState.name))
        }
        if (currentSubStep > 2 && uiState.gender.isNotBlank()) {
            list.add(CategoryOneCompletedStep(2, "Gender", uiState.gender))
        }
        if (currentSubStep > 3) {
            list.add(CategoryOneCompletedStep(3, "Age", "${uiState.ageYears} yrs"))
        }
        if (currentSubStep > 4 && uiState.heightCm.isNotBlank()) {
            val cm = uiState.heightCm.toIntOrNull() ?: 175
            val totalInches = kotlin.math.round(cm / 2.54).toInt()
            val feet = totalInches / 12
            val inches = totalInches % 12
            val ftInStr = if (inches > 0) "$feet ft $inches in" else "$feet ft"
            list.add(CategoryOneCompletedStep(4, "Height", "$cm cm ($ftInStr)"))
        }
        if (currentSubStep > 5 && uiState.languages.isNotEmpty()) {
            list.add(CategoryOneCompletedStep(5, "Languages", uiState.languages.joinToString(", ")))
        }
        list
    }

    ODSBox(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Bar & Progress Bar
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 24.dp, end = 24.dp, bottom = 8.dp),
                gap = 10.dp
            ) {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSText(
                        text = "ESSENTIAL DETAILS · STEP $currentSubStep OF 6",
                        style = ODSTextStyles.microcopyBold,
                        color = secondaryTextColor
                    )

                    ODSText(
                        modifier = Modifier.clickable { onBack() },
                        text = if (currentSubStep > 1) "Back" else "Exit",
                        style = ODSTextStyles.microcopyBold,
                        color = if (currentSubStep > 1) accentColor else secondaryTextColor
                    )
                }

                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                ) {
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp),
                        cornerRadius = ODSCorners(all = 3.dp),
                        background = listOf(ODSColorModel(hexColor = trackColor))
                    )
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth(fraction = progressFraction)
                            .height(6.dp),
                        cornerRadius = ODSCorners(all = 3.dp),
                        background = listOf(ODSColorModel(hexColor = accentColor))
                    )
                }
            }

            // Scrollable Active Question & Completed Summary Cards
            ODSLazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Top
            ) {
                // Active Question
                item {
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 28.dp, bottom = 20.dp)
                    ) {
                        AnimatedContent(
                            targetState = currentSubStep,
                            transitionSpec = {
                                if (targetState > initialState) {
                                    (slideInVertically { height -> height / 3 } + fadeIn()).togetherWith(
                                        slideOutVertically { height -> -height / 3 } + fadeOut()
                                    )
                                } else {
                                    (slideInVertically { height -> -height / 3 } + fadeIn()).togetherWith(
                                        slideOutVertically { height -> height / 3 } + fadeOut()
                                    )
                                }
                            },
                            label = "categoryOneTransition"
                        ) { targetStep ->
                            when (targetStep) {
                                1 -> PreferencesStepName(
                                    name = uiState.name,
                                    onNameChange = onNameChange,
                                    onSubmit = onContinue,
                                    scheme = scheme,
                                    error = uiState.nameError
                                )

                                2 -> PreferencesStepGender(
                                    selectedGender = uiState.gender,
                                    onGenderSelect = onGenderSelect,
                                    scheme = scheme,
                                    error = uiState.genderError
                                )

                                3 -> PreferencesStepDob(
                                    ageYears = uiState.ageYears,
                                    onAgeYearsChange = onAgeYearsChange,
                                    scheme = scheme,
                                    error = uiState.dobError
                                )

                                4 -> PreferencesStepHeight(
                                    heightCm = uiState.heightCm,
                                    onHeightChange = onHeightChange,
                                    scheme = scheme,
                                    error = uiState.heightError
                                )

                                5 -> PreferencesStepFour(
                                    selectedLanguages = uiState.languages,
                                    scheme = scheme,
                                    onToggleLanguage = onToggleLanguage
                                )

                                6 -> PreferencesStepLocation(
                                    currentCity = uiState.currentCity,
                                    isLocationEnabled = uiState.isLocationEnabled,
                                    onEnableLocation = onEnableLocation,
                                    scheme = scheme,
                                    error = uiState.locationError
                                )
                            }
                        }
                    }
                }

                if (completedSteps.isNotEmpty()) {
                    item {
                        ODSColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 24.dp),
                            gap = 10.dp
                        ) {
                            completedSteps.forEach { stepItem ->
                                CompletedStepSummaryCard(
                                    label = stepItem.label,
                                    value = stepItem.value,
                                    onEdit = { onStepSelect(stepItem.subStepIndex) },
                                    scheme = scheme
                                )
                            }
                        }
                    }
                }
            }

            // Bottom CTA
            val isLocationStep = currentSubStep == 6
            val hasValidLocation = uiState.isLocationEnabled &&
                    uiState.latitude != null && uiState.longitude != null &&
                    (uiState.latitude != 0.0 || uiState.longitude != 0.0) &&
                    uiState.currentCity.isNotBlank() &&
                    !uiState.currentCity.equals("Detecting location...", ignoreCase = true) &&
                    !uiState.currentCity.equals("Location Off", ignoreCase = true) &&
                    !uiState.currentCity.equals("Location detection failed", ignoreCase = true)

            val buttonLabel = when {
                isLocationStep && !hasValidLocation -> "Enable Location to Continue"
                isLocationStep -> "Continue to Photos →"
                else -> "Continue →"
            }
            val buttonVariant = if (isLocationStep && !hasValidLocation) ODSButtonVariant.SECONDARY else ODSButtonVariant.PRIMARY

            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                ODSButton(
                    props = ODSButtonProps(
                        label = buttonLabel,
                        variant = buttonVariant
                    ),
                    onClick = {
                        if (isLocationStep && !hasValidLocation) {
                            onEnableLocation()
                        } else {
                            onContinue()
                        }
                    },
                    scheme = scheme,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
