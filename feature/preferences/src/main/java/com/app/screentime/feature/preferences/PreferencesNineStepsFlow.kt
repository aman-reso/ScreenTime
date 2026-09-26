package com.app.screentime.feature.preferences

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

private data class CompletedStepItem(
    val stepIndex: Int,
    val label: String,
    val value: String
)

/**
 * 9-step single-screen onboarding preferences flow.
 * Matches Figma mockups & requirements:
 * - "STEP X OF 9" with animated progress bar
 * - Active question section
 * - Completed questions slide down into compact summary cards with edit pencil icons
 * - Pill "Continue" button at the bottom
 */
@Composable
fun PreferencesNineStepsFlow(
    uiState: PreferencesUiState,
    scheme: ODSTheme,
    onNameChange: (String) -> Unit,
    onGenderSelect: (String) -> Unit,
    onBirthDayChange: (String) -> Unit,
    onBirthMonthChange: (String) -> Unit,
    onBirthYearChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onInterestedInChange: (String) -> Unit,
    onRelationshipIntentChange: (String) -> Unit,
    onToggleLanguage: (String) -> Unit,
    onIncomeSelect: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onStepSelect: (Int) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryTextColor = scheme.basicTextDominant
    val secondaryTextColor = scheme.basicTextRecessive
    val accentColor = scheme.basicAccent
    val trackColor = scheme.basicStrokeSubtle

    val currentStep = uiState.preferenceSubStep
    val progressFraction by animateFloatAsState(
        targetValue = currentStep / 9f,
        label = "progressAnim"
    )

    // Collect all steps that are currently completed (i.e. strictly before current active step)
    val completedSteps = remember(uiState) {
        val list = mutableListOf<CompletedStepItem>()
        if (currentStep > 1 && uiState.name.isNotBlank()) {
            list.add(CompletedStepItem(1, "Name", uiState.name))
        }
        if (currentStep > 2 && uiState.gender.isNotBlank()) {
            list.add(CompletedStepItem(2, "Gender", uiState.gender))
        }
        if (currentStep > 3) {
            val formattedDate = "${uiState.birthDay} ${uiState.birthMonth} ${uiState.birthYear}"
            list.add(CompletedStepItem(3, "Birthday", formattedDate))
        }
        if (currentStep > 4 && uiState.heightCm.isNotBlank()) {
            list.add(CompletedStepItem(4, "Height", "${uiState.heightCm} cm"))
        }
        if (currentStep > 5 && uiState.interestedIn.isNotBlank()) {
            list.add(CompletedStepItem(5, "Interested in", uiState.interestedIn))
        }
        if (currentStep > 6 && uiState.relationshipIntent.isNotBlank()) {
            list.add(CompletedStepItem(6, "Looking for", uiState.relationshipIntent))
        }
        if (currentStep > 7 && uiState.languages.isNotEmpty()) {
            list.add(CompletedStepItem(7, "Languages", uiState.languages.joinToString(", ")))
        }
        if (currentStep > 8 && uiState.income.isNotBlank()) {
            list.add(CompletedStepItem(8, "Income", uiState.income))
        }
        list
    }

    ODSBox(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ── 1. Top Bar & Progress Track ────────────────────────────────
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
                        text = "STEP $currentStep OF 9",
                        style = ODSTextStyles.microcopyBold,
                        color = secondaryTextColor
                    )

                    if (currentStep > 1) {
                        ODSText(
                            modifier = Modifier.clickable { onBack() },
                            text = "Back",
                            style = ODSTextStyles.microcopyBold,
                            color = accentColor
                        )
                    }
                }

                // Progress Bar (Track & Active Fill)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                ) {
                    // Inactive Gray Track
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp),
                        cornerRadius = ODSCorners(all = 3.dp),
                        background = listOf(ODSColorModel(hexColor = trackColor))
                    )
                    // Active Maroon Fill
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth(fraction = progressFraction)
                            .height(6.dp),
                        cornerRadius = ODSCorners(all = 3.dp),
                        background = listOf(ODSColorModel(hexColor = accentColor))
                    )
                }
            }

            // ── 2. Scrollable Body: Active Step + Completed Cards ──────────
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Top
            ) {
                // Active Step Content
                item {
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 28.dp, bottom = 20.dp)
                    ) {
                        AnimatedContent(
                            targetState = currentStep,
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
                            label = "stepContentTransition"
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
                                    onGenderSelect = {
                                        onGenderSelect(it)
                                    },
                                    scheme = scheme,
                                    error = uiState.genderError
                                )

                                3 -> PreferencesStepDob(
                                    day = uiState.birthDay,
                                    month = uiState.birthMonth,
                                    year = uiState.birthYear,
                                    onDayChange = onBirthDayChange,
                                    onMonthChange = onBirthMonthChange,
                                    onYearChange = onBirthYearChange,
                                    scheme = scheme,
                                    error = uiState.dobError
                                )

                                4 -> PreferencesStepHeight(
                                    heightCm = uiState.heightCm,
                                    onHeightChange = onHeightChange,
                                    scheme = scheme,
                                    error = uiState.heightError
                                )

                                5 -> PreferencesStepInterestedIn(
                                    selectedInterestedIn = uiState.interestedIn,
                                    onInterestedInChange = onInterestedInChange,
                                    scheme = scheme
                                )

                                6 -> PreferencesStepThree(
                                    selectedIntent = uiState.relationshipIntent,
                                    scheme = scheme,
                                    onIntentSelect = onRelationshipIntentChange
                                )

                                7 -> PreferencesStepFour(
                                    selectedLanguages = uiState.languages,
                                    scheme = scheme,
                                    onToggleLanguage = onToggleLanguage
                                )

                                8 -> PreferencesStepIncome(
                                    selectedIncome = uiState.income,
                                    scheme = scheme,
                                    onIncomeSelect = onIncomeSelect
                                )

                                9 -> PreferencesStepBio(
                                    bio = uiState.bio,
                                    onBioChange = onBioChange,
                                    scheme = scheme,
                                    error = uiState.bioError
                                )
                            }
                        }
                    }
                }

                // Completed Steps Summary Cards (Animated slide-down)
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
                                    onEdit = { onStepSelect(stepItem.stepIndex) },
                                    scheme = scheme
                                )
                            }
                        }
                    }
                }
            }

            // ── 3. Bottom Action Dock (Continue Button) ────────────────────
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp, start = 24.dp, end = 24.dp, top = 8.dp),
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.Start
            ) {
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onContinue
                        ),
                    cornerRadius = ODSCorners(all = 27.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    background = listOf(ODSColorModel(hexColor = accentColor))
                ) {
                    ODSText(
                        text = if (currentStep == 9) "Continue to Photos" else "Continue",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicTextOnAccent
                    )
                }
            }
        }
    }
}
