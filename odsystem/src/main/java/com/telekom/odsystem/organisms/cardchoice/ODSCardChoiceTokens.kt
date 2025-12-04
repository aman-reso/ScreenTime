package com.telekom.odsystem.organisms.cardchoice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.SCALE_FACTOR

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-10 (v1.33.1) - uid: 22378211
 * Figma link: https://figma.com/design/hre4oCiCoYfStczE6GmCaA/Untitled?node-id=84-11470
 */

data class ODSCardChoiceTokens(
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
    val selectorContainerLeftVerticalAlignmentSelectorAlignmentTop: Alignment.Vertical,
    val selectorContainerLeftVerticalAlignmentSelectorAlignmentMiddle: Alignment.Vertical,
    val selectorContainerLeftHorizontalAlignment: Alignment.Horizontal,
    val selectorContainerLeftHorizontalArrangement: Arrangement.Horizontal,
    val contentContainerVerticalAlignment: Alignment.Vertical,
    val contentContainerHorizontalAlignment: Alignment.Horizontal,
    val contentContainerVerticalArrangement: Arrangement.Vertical,
    val selectorContainerRightVerticalAlignmentSelectorAlignmentTop: Alignment.Vertical,
    val selectorContainerRightVerticalAlignmentSelectorAlignmentMiddle: Alignment.Vertical,
    val selectorContainerRightHorizontalAlignment: Alignment.Horizontal,
    val selectorContainerRightHorizontalArrangement: Arrangement.Horizontal,
    val scaleFactor: Float // Not exported by the plugin
)

val defaultODSCardChoiceTokens = ODSCardChoiceTokens(
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
    selectorContainerLeftVerticalAlignmentSelectorAlignmentTop = Alignment.Top,
    selectorContainerLeftVerticalAlignmentSelectorAlignmentMiddle = Alignment.CenterVertically,
    selectorContainerLeftHorizontalAlignment = Alignment.CenterHorizontally,
    selectorContainerLeftHorizontalArrangement = Arrangement.Center,
    contentContainerVerticalAlignment = Alignment.Top,
    contentContainerHorizontalAlignment = Alignment.Start,
    contentContainerVerticalArrangement = Arrangement.Top,
    selectorContainerRightVerticalAlignmentSelectorAlignmentTop = Alignment.Top,
    selectorContainerRightVerticalAlignmentSelectorAlignmentMiddle = Alignment.CenterVertically,
    selectorContainerRightHorizontalAlignment = Alignment.CenterHorizontally,
    selectorContainerRightHorizontalArrangement = Arrangement.Center,
    scaleFactor = SCALE_FACTOR,
)

var DSCardChoiceTokens: ODSCardChoiceTokens = defaultODSCardChoiceTokens
