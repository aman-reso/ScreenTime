package com.telekom.odsystem.molecules.aiprompt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.foundations.SCALE_FACTOR

/**
 * Code generated with ODS RADD Code Generator
 * 2025-07-23 (v1.32.2) - uid: 4a5353d2
 * Figma link: https://figma.com/design/RTdgj2EBwu8TwoaWWVEovL/ODS_OneID_Production_Library?node-id=16705-24289
 */

data class ODSAIPromptTokens(
    val minHeight: Dp,
    val minWidth: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val contentZStackMinHeight: Dp,
    val contentZStackContentAlignment: Alignment,
    val contentGap: Dp,
    val contentPadding: ODSPadding,
    val contentMinHeight: Dp,
    val contentVerticalAlignment: Alignment.Vertical,
    val contentHorizontalAlignment: Alignment.Horizontal,
    val contentHorizontalArrangement: Arrangement.Horizontal,
    val contentContentAlignment: Alignment,
    val promptBgCornerRadius: ODSCorners,
    val promptBgBorderVariantOutline: Dp,
    val containerGapTypeTopIconText: Dp,
    val containerGapTypeLeftIconText: Dp,
    val containerVerticalAlignment: Alignment.Vertical,
    val containerHorizontalAlignment: Alignment.Horizontal,
    val containerVerticalArrangementTypeTopIconText: Arrangement.Vertical,
    val containerVerticalArrangementTypeTextOnly: Arrangement.Vertical,
    val containerHorizontalArrangementTypeLeftIconText: Arrangement.Horizontal,
    val iconWidthTypeTopIconText: Dp,
    val iconWidthTypeLeftIconText: Dp,
    val iconHeightTypeTopIconText: Dp,
    val iconHeightTypeLeftIconText: Dp,
    val textContainerGapTypeTopIconText: Dp,
    val textContainerGapTypeLeftIconText: Dp,
    val textContainerVerticalAlignmentTypeTopIconText: Alignment.Vertical,
    val textContainerVerticalAlignmentTypeLeftIconText: Alignment.Vertical,
    val textContainerHorizontalAlignmentTypeTopIconText: Alignment.Horizontal,
    val textContainerHorizontalAlignmentTypeLeftIconText: Alignment.Horizontal,
    val textContainerVerticalArrangementTypeTopIconText: Arrangement.Vertical,
    val textContainerVerticalArrangementTypeLeftIconText: Arrangement.Vertical,
    val titleStyleTypeTopIconText: ODSTextStyle,
    val titleStyleTypeLeftIconText: ODSTextStyle,
    val titleTextAlignTypeTopIconText: TextAlign,
    val titleTextAlignTypeLeftIconText: TextAlign,
    val descriptionTextStyleTypeTopIconText: ODSTextStyle,
    val descriptionTextStyleTypeLeftIconText: ODSTextStyle,
    val descriptionTextTextAlignTypeTopIconText: TextAlign,
    val descriptionTextTextAlignTypeLeftIconText: TextAlign,
    val title2StyleTypeTextOnly: ODSTextStyle,
    val title2TextAlignTypeTextOnly: TextAlign,
    val descriptionText2StyleTypeTextOnly: ODSTextStyle,
    val descriptionText2TextAlignTypeTextOnly: TextAlign,
    val rightIconWidth: Dp,
    val rightIconHeight: Dp,
    val scaleFactor: Float // Not exported by plugin
)

val defaultODSAIPromptTokens = ODSAIPromptTokens(
    minHeight = DSVariables.sizingComponent14,
    minWidth = DSVariables.sizingComponent14,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start,
    contentZStackMinHeight = DSVariables.sizingComponent13,
    contentZStackContentAlignment = Alignment.CenterStart,
    contentGap = DSVariables.spacingComponent4,
    contentPadding = ODSPadding(
        top = DSVariables.spacingComponent4,
        bottom = DSVariables.spacingComponent4,
        left = DSVariables.spacingComponent5,
        right = DSVariables.spacingComponent5
    ),
    contentMinHeight = DSVariables.sizingComponent13,
    contentVerticalAlignment = Alignment.CenterVertically,
    contentHorizontalAlignment = Alignment.Start,
    contentHorizontalArrangement = Arrangement.Start,
    contentContentAlignment = Alignment.CenterStart,
    promptBgCornerRadius = ODSCorners(all = DSVariables.radiusSmall),
    promptBgBorderVariantOutline = DSVariables.strokes1,
    containerGapTypeTopIconText = DSVariables.spacingComponent3,
    containerGapTypeLeftIconText = DSVariables.spacingComponent3,
    containerVerticalAlignment = Alignment.CenterVertically,
    containerHorizontalAlignment = Alignment.Start,
    containerVerticalArrangementTypeTopIconText = Arrangement.Center,
    containerVerticalArrangementTypeTextOnly = Arrangement.Center,
    containerHorizontalArrangementTypeLeftIconText = Arrangement.Start,
    iconWidthTypeTopIconText = DSVariables.sizingComponent7,
    iconWidthTypeLeftIconText = DSVariables.sizingComponent7,
    iconHeightTypeTopIconText = DSVariables.sizingComponent7,
    iconHeightTypeLeftIconText = DSVariables.sizingComponent7,
    textContainerGapTypeTopIconText = DSVariables.spacingComponent1,
    textContainerGapTypeLeftIconText = DSVariables.spacingComponent1,
    textContainerVerticalAlignmentTypeTopIconText = Alignment.CenterVertically,
    textContainerVerticalAlignmentTypeLeftIconText = Alignment.CenterVertically,
    textContainerHorizontalAlignmentTypeTopIconText = Alignment.Start,
    textContainerHorizontalAlignmentTypeLeftIconText = Alignment.Start,
    textContainerVerticalArrangementTypeTopIconText = Arrangement.Center,
    textContainerVerticalArrangementTypeLeftIconText = Arrangement.Center,
    titleStyleTypeTopIconText = DSTextStyles.bodyMBold,
    titleStyleTypeLeftIconText = DSTextStyles.bodyMBold,
    titleTextAlignTypeTopIconText = TextAlign.Left,
    titleTextAlignTypeLeftIconText = TextAlign.Left,
    descriptionTextStyleTypeTopIconText = DSTextStyles.microcopyRegular,
    descriptionTextStyleTypeLeftIconText = DSTextStyles.microcopyRegular,
    descriptionTextTextAlignTypeTopIconText = TextAlign.Left,
    descriptionTextTextAlignTypeLeftIconText = TextAlign.Left,
    title2StyleTypeTextOnly = DSTextStyles.bodyMBold,
    title2TextAlignTypeTextOnly = TextAlign.Left,
    descriptionText2StyleTypeTextOnly = DSTextStyles.microcopyRegular,
    descriptionText2TextAlignTypeTextOnly = TextAlign.Left,
    rightIconWidth = DSVariables.sizingComponent7,
    rightIconHeight = DSVariables.sizingComponent7,
    scaleFactor = SCALE_FACTOR
)

var DSAIPromptTokens: ODSAIPromptTokens = defaultODSAIPromptTokens
