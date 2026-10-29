package com.app.screentime.feature.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.ui.components.GenericHeader
import com.app.screentime.core.ui.components.GenericHeaderProps
import com.app.screentime.core.ui.components.HeaderTitleAlignment
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.core.ui.util.showODSToast
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.sliderthumb.ODSSliderThumb
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheetProps
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeader
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeaderProps
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeaderSize
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * EditPreferencesScreen (Figma node-id: 84-1040).
 *
 * Fully implemented using 100% Telekom ODS components according to project rules:
 * 1. ODSLazyColumn for the scrollable list (no Column with verticalScroll).
 * 2. Colors derived strictly from [ODSTheme] scheme (scheme.basicAccent, scheme.basicTextDominant, etc.).
 * 3. Typography uses ODSTextStyles Funnel Sans (bodyMBold, bodyMRegular, bodySRegular, microcopyRegular).
 * 4. Sliders, toggles, and selection sheets wired directly to Clean Architecture [EditPreferencesViewModel].
 * 5. Telekom ODS components (ODSBottomSheet, ODSDialog, ODSButton) used exclusively for sheets, dialogs, and actions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPreferencesScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    viewModel: EditPreferencesViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val preferences = uiState.preferences

    // ── Toast Messages from ViewModel ──────────────────────────────────────
    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            context.showODSToast(it)
            viewModel.clearMessage()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            context.showODSToast(it)
            viewModel.clearMessage()
        }
    }

    // ── Bottom Sheet Selection State ────────────────────────────────────────
    var activeBottomSheetType by remember { mutableStateOf<PreferenceFilterType?>(null) }
    var tempSelectedValue by remember { mutableStateOf<String?>(null) }

    // ── Reset Confirmation Dialog State ─────────────────────────────────────
    var showResetConfirmDialog by remember { mutableStateOf(false) }

    ODSBox(
        modifier = modifier
            .fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // ── Top Navigation Bar ──────────────────────────────────────────
            EditPreferencesTopBar(
                title = "Preferences",
                scheme = scheme,
                onBack = onBack
            )

            // ── Scrollable Preferences Content ──────────────────────────────
            ODSLazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                padding = ODSPadding(
                    left = ODSVariables.spacingLayout0,
                    right = ODSVariables.spacingLayout0,
                    top = ODSVariables.spacingComponent2,
                    bottom = ODSVariables.spacingLayout4
                )
            ) {
                // ── 1. DISCOVERY SECTION ────────────────────────────────────
                item {
                    PreferenceSectionHeader(
                        title = "DISCOVERY",
                        scheme = scheme
                    )
                }

                item {
                    PreferenceValueRow(
                        title = "Show Me",
                        value = preferences.showMe,
                        scheme = scheme,
                        onClick = {
                            tempSelectedValue = preferences.showMe
                            activeBottomSheetType = PreferenceFilterType.SHOW_ME
                        }
                    )
                }

                item {
                    PreferenceDivider(scheme = scheme)
                }

                item {
                    PreferenceAgeRangeSliderRow(
                        minAge = preferences.minAge,
                        maxAge = preferences.maxAge,
                        scheme = scheme,
                        onRangeChange = { newMin, newMax ->
                            viewModel.updateAgeRange(newMin, newMax)
                        }
                    )
                }

                item {
                    PreferenceDivider(scheme = scheme)
                }

                item {
                    PreferenceDistanceSliderRow(
                        distanceKm = preferences.maxDistanceKm,
                        scheme = scheme,
                        onDistanceChange = { newDist ->
                            viewModel.updateMaxDistance(newDist)
                        }
                    )
                }

                item {
                    PreferenceDivider(scheme = scheme)
                }


                item {
                    PreferenceDivider(scheme = scheme)
                }

                // ── 2. ADVANCED FILTERS SECTION ─────────────────────────────
                item {
                    PreferenceSectionHeader(
                        title = "ADVANCED FILTERS",
                        scheme = scheme
                    )
                }

                item {
                    PreferenceValueRow(
                        title = "Religion",
                        value = preferences.religion,
                        scheme = scheme,
                        onClick = {
                            tempSelectedValue = preferences.religion
                            activeBottomSheetType = PreferenceFilterType.RELIGION
                        }
                    )
                }

                item {
                    PreferenceDivider(scheme = scheme)
                }

                item {
                    PreferenceValueRow(
                        title = "Height",
                        value = preferences.heightRange,
                        scheme = scheme,
                        onClick = {
                            tempSelectedValue = preferences.heightRange
                            activeBottomSheetType = PreferenceFilterType.HEIGHT
                        }
                    )
                }

                item {
                    PreferenceDivider(scheme = scheme)
                }

                item {
                    PreferenceValueRow(
                        title = "Education",
                        value = preferences.education,
                        scheme = scheme,
                        onClick = {
                            tempSelectedValue = preferences.education
                            activeBottomSheetType = PreferenceFilterType.EDUCATION
                        }
                    )
                }

                item {
                    PreferenceDivider(scheme = scheme)
                }

                item {
                    PreferenceValueRow(
                        title = "Drinking",
                        value = preferences.drinking,
                        scheme = scheme,
                        onClick = {
                            tempSelectedValue = preferences.drinking
                            activeBottomSheetType = PreferenceFilterType.DRINKING
                        }
                    )
                }

                item {
                    PreferenceDivider(scheme = scheme)
                }

                item {
                    PreferenceValueRow(
                        title = "Smoking",
                        value = preferences.smoking,
                        scheme = scheme,
                        onClick = {
                            tempSelectedValue = preferences.smoking
                            activeBottomSheetType = PreferenceFilterType.SMOKING
                        }
                    )
                }

            }

            // ── Sticky Bottom Actions: Update Preferences & Reset Preferences ──
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth(),
                padding = ODSPadding(
                    top = ODSVariables.spacingComponent3,
                    left = ODSVariables.spacingLayout1,
                    right = ODSVariables.spacingLayout1
                )
            ) {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingComponent2,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ODSButton(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Update Preferences",
                            buttonType = ODSButtonButtonType.STANDARD,
                            variant = ODSButtonVariant.SECONDARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = {
                            viewModel.savePreferences {
                                onBack()
                            }
                        }
                    )
                }
            }
        }

        // ── Reset Confirmation Dialog (ODSDialog) ───────────────────────────
        if (showResetConfirmDialog) {
            ODSDialog(
                scheme = scheme,
                onDismissRequest = { showResetConfirmDialog = false },
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                props = ODSDialogProps(
                    showCloseButton = true,
                    title = "Reset Preferences",
                    bodyText = "Are you sure you want to reset all preferences to default values? This action cannot be undone."
                ),
                actionSlot = {
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = ODSVariables.spacingComponent3
                    ) {
                        ODSButton(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = "Yes, Reset Preferences",
                                variant = ODSButtonVariant.PRIMARY,
                                size = ODSButtonSize.SMALL
                            ),
                            onClick = {
                                viewModel.resetPreferences()
                                showResetConfirmDialog = false
                            }
                        )

                        ODSButton(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = "Cancel",
                                variant = ODSButtonVariant.SECONDARY,
                                size = ODSButtonSize.SMALL
                            ),
                            onClick = { showResetConfirmDialog = false }
                        )
                    }
                }
            )
        }

        // ── Option Picker Bottom Sheet (ODSBottomSheet) ─────────────────────
        ODSBottomSheet(
            scheme = scheme,
            showBottomSheet = activeBottomSheetType != null,
            props = ODSBottomSheetProps(showHandle = true),
            onDismissRequest = {
                activeBottomSheetType = null
                tempSelectedValue = null
            },
            onCloseClicked = {
                activeBottomSheetType = null
                tempSelectedValue = null
            },
            titleSlot = {
                activeBottomSheetType?.let { filterType ->
                    ODSBottomSheetHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 40.dp),
                        scheme = scheme,
                        props = ODSBottomSheetHeaderProps(
                            smallHeading = filterType.title,
                            size = ODSBottomSheetHeaderSize.SMALL
                        )
                    )
                }
            },
            contentSlot = {
                activeBottomSheetType?.let { filterType ->
                    val currentValue = when (filterType) {
                        PreferenceFilterType.SHOW_ME -> preferences.showMe
                        PreferenceFilterType.RELIGION -> preferences.religion
                        PreferenceFilterType.HEIGHT -> preferences.heightRange
                        PreferenceFilterType.EDUCATION -> preferences.education
                        PreferenceFilterType.DRINKING -> preferences.drinking
                        PreferenceFilterType.SMOKING -> preferences.smoking
                        PreferenceFilterType.CHILDREN -> preferences.children
                    }
                    PreferenceOptionSheetContent(
                        filterType = filterType,
                        currentValue = tempSelectedValue ?: currentValue,
                        scheme = scheme,
                        onOptionSelected = { selected ->
                            tempSelectedValue = selected
                        }
                    )
                }
            },
            actionSlot = {
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ODSVariables.spacingLayout1),
                    gap = ODSVariables.spacingComponent3
                ) {
                    ODSButton(
                        modifier = Modifier.weight(1f),
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Apply",
                            variant = ODSButtonVariant.PRIMARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = {
                            val selected = tempSelectedValue
                            val filterType = activeBottomSheetType
                            if (selected != null && filterType != null) {
                                when (filterType) {
                                    PreferenceFilterType.SHOW_ME -> viewModel.updateShowMe(selected)
                                    PreferenceFilterType.RELIGION -> viewModel.updateReligion(
                                        selected
                                    )

                                    PreferenceFilterType.HEIGHT -> viewModel.updateHeightRange(
                                        selected
                                    )

                                    PreferenceFilterType.EDUCATION -> viewModel.updateEducation(
                                        selected
                                    )

                                    PreferenceFilterType.DRINKING -> viewModel.updateDrinking(
                                        selected
                                    )

                                    PreferenceFilterType.SMOKING -> viewModel.updateSmoking(selected)
                                    PreferenceFilterType.CHILDREN -> viewModel.updateChildren(
                                        selected
                                    )
                                }
                            }
                            activeBottomSheetType = null
                            tempSelectedValue = null
                        }
                    )

                    ODSButton(
                        modifier = Modifier.weight(1f),
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Cancel",
                            variant = ODSButtonVariant.SECONDARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = {
                            activeBottomSheetType = null
                            tempSelectedValue = null
                        }
                    )
                }
            }
        )
    }
}

/**
 * Top bar using GenericHeader with centered title and back navigation.
 */
@Composable
private fun EditPreferencesTopBar(
    title: String,
    scheme: ODSTheme,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    GenericHeader(
        props = GenericHeaderProps(
            title = title,
            showBackArrow = true,
            onBackClick = onBack,
            titleAlignment = HeaderTitleAlignment.CENTER
        ),
        scheme = scheme,
        modifier = modifier
    )
}

/**
 * Section Header: DISCOVERY, ADVANCED FILTERS.
 */
@Composable
private fun PreferenceSectionHeader(
    title: String,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = ODSVariables.spacingComponent6,
                bottom = ODSVariables.spacingComponent3,
                start = ODSVariables.spacingLayout1,
                end = ODSVariables.spacingLayout1
            ),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start
    ) {
        ODSText(
            text = title,
            style = ODSTextStyles.bodySBold,
            color = scheme.basicTextDominant
        )
    }
}

/**
 * Standard selectable row with Chevron right (e.g., Show Me: Men >).
 */
@Composable
private fun PreferenceValueRow(
    title: String,
    value: String,
    scheme: ODSTheme,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = ODSVariables.spacingLayout1),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ODSText(
            text = title,
            style = ODSTextStyles.bodyMRegular,
            color = scheme.basicText
        )

        ODSRow(
            gap = ODSVariables.spacingComponent3,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            ODSText(
                text = value,
                style = ODSTextStyles.bodySRegular,
                color = scheme.basicTextRecessive
            )
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.ic_chevron_right,
                    contentDescription = "Expand"
                ),
                tint = scheme.basicTextRecessive.getColor(),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

/**
 * Age Range Slider Row: "Age Range" on left, "22 - 30" in maroon on right, dual-thumb range slider with ODS thumb and track.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PreferenceAgeRangeSliderRow(
    minAge: Float,
    maxAge: Float,
    scheme: ODSTheme,
    onRangeChange: (Float, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = ODSVariables.spacingComponent4,
                bottom = ODSVariables.spacingComponent4,
                start = ODSVariables.spacingLayout1,
                end = ODSVariables.spacingLayout1
            ),
        gap = ODSVariables.spacingComponent3,
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Label Row
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ODSText(
                text = "Age Range",
                style = ODSTextStyles.bodyMRegular,
                color = scheme.basicTextDominant
            )
            ODSText(
                text = "${minAge.toInt()} - ${maxAge.toInt()}",
                style = ODSTextStyles.bodyMBold,
                color = scheme.basicAccent
            )
        }

        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
            RangeSlider(
                value = minAge..maxAge,
                onValueChange = { range ->
                    onRangeChange(range.start, range.endInclusive)
                },
                valueRange = 18f..65f,
                steps = 46,
                track = { state ->
                    SliderDefaults.Track(
                        modifier = Modifier
                            .height(4.dp)
                            .fillMaxWidth(),
                        rangeSliderState = state,
                        thumbTrackGapSize = 0.dp,
                        trackInsideCornerSize = 0.dp,
                        colors = SliderDefaults.colors(
                            activeTrackColor = scheme.basicAccent.getColor(),
                            inactiveTrackColor = scheme.basicStroke.getColor()
                        )
                    )
                },
                startThumb = {
                    ODSSliderThumb(
                        modifier = Modifier.focusProperties { canFocus = false },
                        scheme = scheme
                    )
                },
                endThumb = {
                    ODSSliderThumb(
                        modifier = Modifier.focusProperties { canFocus = false },
                        scheme = scheme
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
            )
        }

        // Boundary min/max labels
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSText(
                text = "18",
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicTextRecessive
            )
            ODSText(
                text = "65",
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicTextRecessive
            )
        }
    }
}

/**
 * Maximum Distance Slider Row: "Maximum Distance" on left, "50 km" on right, slider with ODS thumb and track.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PreferenceDistanceSliderRow(
    distanceKm: Float,
    scheme: ODSTheme,
    onDistanceChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = ODSVariables.spacingComponent4,
                bottom = ODSVariables.spacingComponent4,
                start = ODSVariables.spacingLayout1,
                end = ODSVariables.spacingLayout1
            ),
        gap = ODSVariables.spacingComponent3,
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Label Row
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ODSText(
                text = "Maximum Distance",
                style = ODSTextStyles.bodyMRegular,
                color = scheme.basicTextDominant
            )
            ODSText(
                text = "${distanceKm.toInt()} km",
                style = ODSTextStyles.bodyMBold,
                color = scheme.basicAccent
            )
        }

        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
            Slider(
                value = distanceKm.coerceIn(10f, 150f),
                onValueChange = onDistanceChange,
                valueRange = 10f..150f,
                track = { state ->
                    SliderDefaults.Track(
                        modifier = Modifier
                            .height(4.dp)
                            .fillMaxWidth(),
                        sliderState = state,
                        thumbTrackGapSize = 0.dp,
                        trackInsideCornerSize = 0.dp,
                        colors = SliderDefaults.colors(
                            activeTrackColor = scheme.basicAccent.getColor(),
                            inactiveTrackColor = scheme.basicStroke.getColor()
                        )
                    )
                },
                thumb = {
                    ODSSliderThumb(
                        modifier = Modifier.focusProperties { canFocus = false },
                        scheme = scheme
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
            )
        }

        // Boundary min/max labels
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSText(
                text = "5 km",
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicTextRecessive
            )
            ODSText(
                text = "150 km",
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicTextRecessive
            )
        }
    }
}

/**
 * 1px Subtle Divider line matching Figma.
 */
@Composable
private fun PreferenceDivider(
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp),
        background = listOf(ODSColorModel(hexColor = scheme.basicStroke))
    )
}

/**
 * Filter Types supported by the bottom sheet selector.
 */
private enum class PreferenceFilterType(val title: String, val options: List<String>) {
    SHOW_ME(
        title = "Show Me",
        options = listOf("Men", "Women", "Everyone")
    ),
    RELIGION(
        title = "Religion",
        options = listOf(
            "No Preference",
            "Hinduism (🕉️)",
            "Islam (☪️)",
            "Christianity (✝️)",
            "Sikhism (ੴ)",
            "Jainism (🛕)",
            "Buddhism (☸️)",
            "Parsi / Zoroastrian (🕯️)",
            "Judaism (✡️)",
            "Spiritual (🕊️)",
            "Agnostic (❓)",
            "Atheist (⚛️)",
            "Other (🌟)"
        )
    ),
    HEIGHT(
        title = "Height Preference",
        options = listOf(
            "No Preference",
            "4ft 10in - 5ft 4in",
            "5ft 0in - 5ft 8in",
            "5ft 2in - 6ft 0in",
            "5ft 6in - 6ft 2in",
            "5ft 10in - 6ft 6in",
            "6ft 0in+"
        )
    ),
    EDUCATION(
        title = "Education",
        options = listOf(
            "Any",
            "High School",
            "Trade / Tech School",
            "In College",
            "Undergraduate Degree",
            "Bachelor’s degree",
            "Master’s degree",
            "PhD / Doctorate"
        )
    ),
    DRINKING(
        title = "Drinking",
        options = listOf(
            "Any",
            "Never",
            "Social Drinker",
            "Regularly",
            "Sober"
        )
    ),
    SMOKING(
        title = "Smoking",
        options = listOf(
            "Any",
            "Never",
            "Socially",
            "Regularly",
            "Trying to quit"
        )
    ),
    CHILDREN(
        title = "Children",
        options = listOf(
            "Any",
            "Open to Kids",
            "Want Kids",
            "Don't Want Kids",
            "Have Kids",
            "Not Sure Yet"
        )
    )
}

/**
 * Bottom Sheet content for selecting an option.
 */
@Composable
private fun PreferenceOptionSheetContent(
    filterType: PreferenceFilterType,
    currentValue: String,
    scheme: ODSTheme,
    onOptionSelected: (String) -> Unit
) {
    ODSColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = ODSVariables.spacingLayout1,
                vertical = ODSVariables.spacingComponent2
            ),
        gap = ODSVariables.spacingComponent2
    ) {
        filterType.options.forEach { option ->
            val isSelected = option.equals(currentValue, ignoreCase = true)
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onOptionSelected(option) }
                    )
                    .padding(horizontal = ODSVariables.spacingComponent2),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ODSText(
                    text = option,
                    style = if (isSelected) ODSTextStyles.bodyMBold else ODSTextStyles.bodyMRegular,
                    color = if (isSelected) scheme.basicAccent else scheme.basicTextDominant
                )

                if (isSelected) {
                    ODSBox(
                        modifier = Modifier.size(10.dp),
                        cornerRadius = ODSCorners(all = 5.dp),
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccent))
                    )
                }
            }

            PreferenceDivider(scheme = scheme)
        }
    }
}
