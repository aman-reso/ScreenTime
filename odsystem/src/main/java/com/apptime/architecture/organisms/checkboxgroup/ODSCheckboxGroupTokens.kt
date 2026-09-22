package com.telekom.odsystem.organisms.checkboxgroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSCheckboxGroupTokens(
    var gap: Dp,
    var padding: ODSPadding,
    var borderRadius: ODSCorners,
    var clipContent: Boolean,
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var verticalArrangement: Arrangement.Vertical,
    var titleGap: Dp,
    var titlePadding: ODSPadding,
    var titleVerticalAlignment: Alignment.Vertical,
    var titleHorizontalAlignment: Alignment.Horizontal,
    var titleVerticalArrangement: Arrangement.Vertical,
    var supportMessagePadding: ODSPadding,
    var supportMessageVerticalAlignment: Alignment.Vertical,
    var supportMessageHorizontalAlignment: Alignment.Horizontal,
    var supportMessageVerticalArrangement: Arrangement.Vertical,
    var listContainerGapTypeNested: Dp,
    var listContainerPaddingTypeNested: ODSPadding,
    var listContainerVerticalAlignmentTypeNested: Alignment.Vertical,
    var listContainerHorizontalAlignmentTypeNested: Alignment.Horizontal,
    var listContainerVerticalArrangementTypeNested: Arrangement.Vertical,
    var secondLevelGapTypeNested: Dp,
    var secondLevelPaddingTypeNested: ODSPadding,
    var secondLevelVerticalAlignmentTypeNested: Alignment.Vertical,
    var secondLevelHorizontalAlignmentTypeNested: Alignment.Horizontal,
    var secondLevelVerticalArrangementTypeNested: Arrangement.Vertical,
    var secondLevelWidthSizeSmallTypeNested: Dp,
    var titleTextStyle: ODSTextStyle,
    var titleTextAlign: TextAlign,
    var titleTextOverflow: TextOverflow
)

var defaultODSCheckboxGroupTokens = ODSCheckboxGroupTokens(
    gap = DSVariables.spacingComponent1,
    padding = ODSPadding(
        top = DSVariables.spacingComponent3,
        bottom = DSVariables.spacingComponent3,
        left = DSVariables.spacingComponent0,
        right = DSVariables.spacingComponent0
    ),
    borderRadius = ODSCorners(all = DSVariables.radiusExtraSmall),
    clipContent = true,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    titleGap = DSVariables.spacingComponent0,
    titlePadding = ODSPadding(
        top = DSVariables.spacingComponent2,
        bottom = DSVariables.spacingComponent2,
        left = DSVariables.spacingComponent1,
        right = DSVariables.spacingComponent1
    ),
    titleVerticalAlignment = Alignment.Top,
    titleHorizontalAlignment = Alignment.Start,
    titleVerticalArrangement = Arrangement.Top,
    supportMessagePadding = ODSPadding(
        left = DSVariables.spacingComponent1,
        right = DSVariables.spacingComponent1
    ),
    supportMessageVerticalAlignment = Alignment.Top,
    supportMessageHorizontalAlignment = Alignment.Start,
    supportMessageVerticalArrangement = Arrangement.Top,
    listContainerGapTypeNested = DSVariables.spacingComponent0,
    listContainerPaddingTypeNested = ODSPadding(
        top = DSVariables.spacingComponent0,
        bottom = DSVariables.spacingComponent0,
        left = DSVariables.spacingComponent0,
        right = DSVariables.spacingComponent0
    ),
    listContainerVerticalAlignmentTypeNested = Alignment.Top,
    listContainerHorizontalAlignmentTypeNested = Alignment.Start,
    listContainerVerticalArrangementTypeNested = Arrangement.Top,
    secondLevelGapTypeNested = DSVariables.spacingComponent1,
    secondLevelPaddingTypeNested = ODSPadding(
        top = DSVariables.spacingComponent0,
        bottom = DSVariables.spacingComponent0,
        left = DSVariables.spacingComponent10,
        right = DSVariables.spacingComponent0
    ),
    secondLevelVerticalAlignmentTypeNested = Alignment.Top,
    secondLevelHorizontalAlignmentTypeNested = Alignment.Start,
    secondLevelVerticalArrangementTypeNested = Arrangement.Top,
    secondLevelWidthSizeSmallTypeNested = 453.dp,
    titleTextStyle = DSTextStyles.subtitle,
    titleTextAlign = TextAlign.Left,
    titleTextOverflow = TextOverflow.Ellipsis
)

var DSCheckboxGroupTokens: ODSCheckboxGroupTokens = defaultODSCheckboxGroupTokens
