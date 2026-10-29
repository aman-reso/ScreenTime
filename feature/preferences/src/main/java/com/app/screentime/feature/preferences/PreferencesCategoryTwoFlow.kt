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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.screentime.feature.preferences.domain.model.PreferencesMetadata
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.radiobutton.ODSRadioButton
import com.telekom.odsystem.atoms.radiobutton.ODSRadioButtonProps
import com.telekom.odsystem.atoms.switch.ODSSwitch
import com.telekom.odsystem.atoms.switch.ODSSwitchProps
import com.telekom.odsystem.atoms.switch.ODSSwitchSize
import com.telekom.odsystem.atoms.togglechip.ODSToggleChip
import com.telekom.odsystem.atoms.togglechip.ODSToggleChipProps
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelect
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelectOptions
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelectProps
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

private data class CategoryTwoCompletedStep(
    val subStepIndex: Int, val label: String, val value: String
)

private val EDUCATION_OPTIONS = listOf(
    "High School",
    "Bachelor’s degree",
    "Master’s degree",
    "Doctorate",
    "Trade School",
    "Prefer not to say"
)

/**
 * Category 2 (Other details / Personalization) One-by-One Sub-step Flow.
 * 5 sub-steps: Education, Interests, Visibility (ODSSwitch), Bio, Relationship Intent.
 * 100% Telekom ODS components.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PreferencesCategoryTwoFlow(
    uiState: PreferencesUiState,
    scheme: ODSTheme,
    onEducationChange: (String) -> Unit,
    onToggleInterest: (String) -> Unit,
    onVisibilityChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onDatingIntentChange: (String) -> Unit,
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
        targetValue = currentSubStep / 5f, label = "categoryTwoProgressAnim"
    )

    var showEducationSheet by remember { mutableStateOf(false) }

    val completedSteps = remember(uiState) {
        val list = mutableListOf<CategoryTwoCompletedStep>()
        if (currentSubStep > 1 && uiState.education.isNotBlank()) {
            list.add(CategoryTwoCompletedStep(1, "Education", uiState.education))
        }
        if (currentSubStep > 2 && uiState.interests.isNotEmpty()) {
            list.add(CategoryTwoCompletedStep(2, "Interests", uiState.interests.joinToString(", ")))
        }
        if (currentSubStep > 3 && uiState.visibility.isNotBlank()) {
            list.add(
                CategoryTwoCompletedStep(
                    3, "Visibility", uiState.visibility.replaceFirstChar { it.uppercase() })
            )
        }
        if (currentSubStep > 4 && uiState.bio.isNotBlank()) {
            list.add(CategoryTwoCompletedStep(4, "Bio", uiState.bio))
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
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Bar & Progress Bar
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 24.dp, end = 24.dp, bottom = 8.dp), gap = 10.dp
            ) {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSText(
                        text = "PERSONALIZATION · STEP $currentSubStep OF 5",
                        style = ODSTextStyles.microcopyBold,
                        color = secondaryTextColor
                    )

                    ODSText(
                        modifier = Modifier.clickable { onBack() },
                        text = "Back",
                        style = ODSTextStyles.microcopyBold,
                        color = accentColor
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
                            targetState = currentSubStep, transitionSpec = {
                                if (targetState > initialState) {
                                    (slideInVertically { height -> height / 3 } + fadeIn()).togetherWith(
                                        slideOutVertically { height -> -height / 3 } + fadeOut())
                                } else {
                                    (slideInVertically { height -> -height / 3 } + fadeIn()).togetherWith(
                                        slideOutVertically { height -> height / 3 } + fadeOut())
                                }
                            }, label = "categoryTwoTransition"
                        ) { targetStep ->
                            when (targetStep) {
                                1 -> ODSColumn(modifier = Modifier.fillMaxWidth(), gap = 12.dp) {
                                    ODSText(
                                        text = "What is your education level?",
                                        style = ODSTextStyles.bodyL,
                                        color = primaryTextColor
                                    )
                                    ODSDropdownSelect(
                                        props = ODSDropdownSelectProps(
                                            label = "Education",
                                            selectedValue = if (uiState.education.isNotBlank()) ODSDropdownSelectOptions(
                                                label = uiState.education
                                            ) else null
                                        ),
                                        onClick = { showEducationSheet = true },
                                        scheme = scheme,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                2 -> ODSColumn(modifier = Modifier.fillMaxWidth(), gap = 20.dp) {
                                    ODSText(
                                        text = "Select your favorite interests",
                                        style = ODSTextStyles.bodyL,
                                        color = primaryTextColor
                                    )
                                    PreferencesMetadata.DEFAULT.interests.forEach { category ->
                                        ODSColumn(
                                            modifier = Modifier.fillMaxWidth(), gap = 10.dp
                                        ) {
                                            ODSText(
                                                text = "${category.emoji} ${category.category}",
                                                style = ODSTextStyles.bodyMBold,
                                                color = primaryTextColor
                                            )
                                            FlowRow(
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                category.items.forEach { item ->
                                                    val chipText = "${item.emoji} ${item.label}"
                                                    val isSelected =
                                                        uiState.interests.contains(item.label) || uiState.interests.contains(
                                                            chipText
                                                        )
                                                    ODSToggleChip(
                                                        props = ODSToggleChipProps(
                                                            label = chipText, selected = isSelected
                                                        ),
                                                        onToggle = { onToggleInterest(item.label) },
                                                        scheme = scheme
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                3 -> ODSColumn(modifier = Modifier.fillMaxWidth(), gap = 16.dp) {
                                    ODSText(
                                        text = "Set your profile visibility",
                                        style = ODSTextStyles.bodyL,
                                        color = primaryTextColor
                                    )
                                    ODSText(
                                        text = "Toggle whether your profile is public or private to other members.",
                                        style = ODSTextStyles.bodyMRegular,
                                        color = secondaryTextColor
                                    )
                                    val isPublic =
                                        !uiState.visibility.equals("private", ignoreCase = true)
                                    ODSSwitch(
                                        modifier = Modifier.fillMaxWidth(),
                                        scheme = scheme,
                                        props = ODSSwitchProps(
                                            label = if (isPublic) "Public (Everyone can see your profile)" else "Private (Only matches can see your profile)",
                                            selected = isPublic,
                                            size = ODSSwitchSize.SMALL
                                        ),
                                        onCheckedChange = { isChecked ->
                                            onVisibilityChange(if (isChecked) "public" else "private")
                                        })
                                }

                                4 -> PreferencesStepBio(
                                    bio = uiState.bio,
                                    onBioChange = onBioChange,
                                    scheme = scheme,
                                    error = uiState.bioError
                                )

                                5 -> PreferencesStepThree(
                                    selectedIntent = uiState.relationshipIntent,
                                    scheme = scheme,
                                    onIntentSelect = onDatingIntentChange
                                )
                            }
                        }
                    }
                }

                // Completed Cards
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
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                ODSButton(
                    props = ODSButtonProps(
                        label = if (currentSubStep == 5) "Continue to Photos →" else "Continue →",
                        variant = ODSButtonVariant.PRIMARY
                    ), onClick = onContinue, scheme = scheme, modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    // Education Sheet (With Item Dividers)
    if (showEducationSheet) {
        ODSBottomSheet(
            showBottomSheet = true,
            onDismissRequest = { showEducationSheet = false },
            onCloseClicked = { showEducationSheet = false },
            contentSlot = {
                ODSColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp), gap = 12.dp
                ) {
                    ODSText(
                        text = "Select Education",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )
                    EDUCATION_OPTIONS.forEachIndexed { idx, ed ->
                        ODSColumn(modifier = Modifier.fillMaxWidth()) {
                            ODSRow(modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onEducationChange(ed)
                                    showEducationSheet = false
                                }
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically) {
                                ODSText(
                                    text = ed,
                                    style = if (ed == uiState.education) ODSTextStyles.bodySBold else ODSTextStyles.bodySRegular,
                                    color = if (ed == uiState.education) scheme.basicText else scheme.basicTextRecessive
                                )
                                if (ed == uiState.education) {
                                    ODSIcon(
                                        iconModel = ODSIconModel(
                                            drawableRes = R.drawable.ic_check,
                                            contentDescription = "Selected"
                                        ),
                                        tint = scheme.basicText.getColor(),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            if (idx < EDUCATION_OPTIONS.size - 1) {
                                ODSBox(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp),
                                    background = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                                )
                            }
                        }
                    }
                }
            })
    }
}
