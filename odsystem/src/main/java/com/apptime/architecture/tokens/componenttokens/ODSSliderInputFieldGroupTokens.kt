package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSSliderInputFieldGroupTokens(
    var gapVariantSideBySide: Dp,
    var gapVariantSingle: Dp,
    var gapVariantStacked: Dp,
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var horizontalArrangementVariantSideBySide: Arrangement.Horizontal,
    var horizontalArrangementVariantSingle: Arrangement.Horizontal,
    var verticalArrangementVariantStacked: Arrangement.Vertical,
    var labelMinGap: Dp,
    var labelMinVerticalAlignment: Alignment.Vertical,
    var labelMinHorizontalAlignment: Alignment.Horizontal,
    var labelMinVerticalArrangement: Arrangement.Vertical,
    var labelVerticalAlignment: Alignment.Vertical,
    var labelHorizontalAlignment: Alignment.Horizontal,
    var labelHorizontalArrangement: Arrangement.Horizontal,
    var labelMaxGap: Dp,
    var labelMaxVerticalAlignmentVariantSideBySide: Alignment.Vertical,
    var labelMaxVerticalAlignmentVariantStacked: Alignment.Vertical,
    var labelMaxVerticalAlignmentVariantSingle: Alignment.Vertical,
    var labelMaxHorizontalAlignment: Alignment.Horizontal,
    var labelMaxVerticalArrangementVariantSideBySide: Arrangement.Vertical,
    var labelMaxVerticalArrangementVariantStacked: Arrangement.Vertical,
    var labelMaxHorizontalArrangementVariantSingle: Arrangement.Horizontal,
    var labelTextStyle: ODSTextStyle,
    var labelTextAlign: TextAlign,
    var labelTextOverflow: TextOverflow
)

var defaultODSSliderInputFieldGroupTokens = ODSSliderInputFieldGroupTokens(
    gapVariantSideBySide = DSVariables.spacingComponent3,
    gapVariantSingle = DSVariables.spacingComponent3,
    gapVariantStacked = DSVariables.spacingComponent7,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    horizontalArrangementVariantSideBySide = Arrangement.Start,
    horizontalArrangementVariantSingle = Arrangement.Start,
    verticalArrangementVariantStacked = Arrangement.Top,
    labelMinGap = DSVariables.spacingComponent3,
    labelMinVerticalAlignment = Alignment.Top,
    labelMinHorizontalAlignment = Alignment.Start,
    labelMinVerticalArrangement = Arrangement.Top,
    labelVerticalAlignment = Alignment.CenterVertically,
    labelHorizontalAlignment = Alignment.Start,
    labelHorizontalArrangement = Arrangement.Start,
    labelMaxGap = DSVariables.spacingComponent3,
    labelMaxVerticalAlignmentVariantSideBySide = Alignment.Top,
    labelMaxVerticalAlignmentVariantStacked = Alignment.Top,
    labelMaxVerticalAlignmentVariantSingle = Alignment.CenterVertically,
    labelMaxHorizontalAlignment = Alignment.Start,
    labelMaxVerticalArrangementVariantSideBySide = Arrangement.Top,
    labelMaxVerticalArrangementVariantStacked = Arrangement.Top,
    labelMaxHorizontalArrangementVariantSingle = Arrangement.Start,
    labelTextStyle = DSTextStyles.subtitle,
    labelTextAlign = TextAlign.Left,
    labelTextOverflow = TextOverflow.Ellipsis
)

var DSSliderInputFieldGroupTokens: ODSSliderInputFieldGroupTokens =
    defaultODSSliderInputFieldGroupTokens
