package com.telekom.odsystem.organisms.cardcheckmark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.SCALE_FACTOR

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-10 (v1.33.1) - uid: 5ac49cde
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=8756-22767
 */

data class ODSCardCheckmarkTokens(
    val zStackWidth: Dp,
    val zStackContentAlignment: Alignment,
    val gap: Dp,
    val padding: ODSPadding,
    val width: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val contentAlignment: Alignment,
    val cardBgCornerRadius: ODSCorners,
    val cardBgVerticalAlignment: Alignment.Vertical,
    val cardBgHorizontalAlignment: Alignment.Horizontal,
    val cardBgVerticalArrangement: Arrangement.Vertical,
    val cardBgBorder: Dp,
    val cardBgBorderSelected: Dp,
    val contentGap: Dp,
    val contentVerticalAlignment: Alignment.Vertical,
    val contentHorizontalAlignment: Alignment.Horizontal,
    val contentHorizontalArrangement: Arrangement.Horizontal,
    val contentPaddingSelected: ODSPadding,
    val contentContainerVerticalAlignment: Alignment.Vertical,
    val contentContainerHorizontalAlignment: Alignment.Horizontal,
    val contentContainerVerticalArrangement: Arrangement.Vertical,
    val selectorContainerRightWidth: Dp,
    val selectorContainerRightVerticalAlignmentSelectorAlignmentTop: Alignment.Vertical,
    val selectorContainerRightVerticalAlignmentSelectorAlignmentMiddle: Alignment.Vertical,
    val selectorContainerRightHorizontalAlignment: Alignment.Horizontal,
    val selectorContainerRightHorizontalArrangement: Arrangement.Horizontal,
    val checkmarkRightWidth: Dp,
    val checkmarkRightHeight: Dp,
    val scaleFactor: Float // Not exported by the plugin
)

val defaultODSCardCheckmarkTokens = ODSCardCheckmarkTokens(
    zStackWidth = DSVariables.columns4Columns,
    zStackContentAlignment = Alignment.TopCenter,
    gap = DSVariables.spacingComponent10,
    padding = ODSPadding(all = DSVariables.spacingComponent7),
    width = DSVariables.columns4Columns,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
    contentAlignment = Alignment.TopCenter,
    cardBgCornerRadius = ODSCorners(all = DSVariables.radiusMedium),
    cardBgVerticalAlignment = Alignment.Top,
    cardBgHorizontalAlignment = Alignment.CenterHorizontally,
    cardBgVerticalArrangement = Arrangement.Top,
    cardBgBorder = DSVariables.strokes1,
    cardBgBorderSelected = DSVariables.strokes2,
    contentGap = DSVariables.spacingComponent5,
    contentVerticalAlignment = Alignment.Top,
    contentHorizontalAlignment = Alignment.Start,
    contentHorizontalArrangement = Arrangement.Start,
    contentPaddingSelected = ODSPadding(right = DSVariables.spacingComponent0),
    contentContainerVerticalAlignment = Alignment.Top,
    contentContainerHorizontalAlignment = Alignment.Start,
    contentContainerVerticalArrangement = Arrangement.Top,
    selectorContainerRightWidth = DSVariables.sizingComponent10,
    selectorContainerRightVerticalAlignmentSelectorAlignmentTop = Alignment.Top,
    selectorContainerRightVerticalAlignmentSelectorAlignmentMiddle = Alignment.CenterVertically,
    selectorContainerRightHorizontalAlignment = Alignment.CenterHorizontally,
    selectorContainerRightHorizontalArrangement = Arrangement.Center,
    checkmarkRightWidth = 24.dp,
    checkmarkRightHeight = 24.dp,
    scaleFactor = SCALE_FACTOR
)

var DSCardCheckmarkTokens: ODSCardCheckmarkTokens = defaultODSCardCheckmarkTokens
