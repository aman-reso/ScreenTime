package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSTagsTokens(
    var gap: Dp,
    var padding: ODSPadding,
    var borderRadius: ODSCorners,
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var horizontalArrangement: Arrangement.Horizontal,
    var checkmarkWidthTypeActiveOnBackground: Dp,
    var checkmarkWidthTypeActiveOnSubtle: Dp,
    var checkmarkHeightTypeActiveOnBackground: Dp,
    var checkmarkHeightTypeActiveOnSubtle: Dp,
    var labelTextStyle: ODSTextStyle,
    var labelTextAlign: TextAlign
)

var defaultODSTagsTokens = ODSTagsTokens(
    gap = DSVariables.spacingComponent3,
    padding = ODSPadding(
        top = DSVariables.spacingComponent1,
        bottom = DSVariables.spacingComponent1,
        left = DSVariables.spacingComponent3,
        right = DSVariables.spacingComponent3
    ),
    borderRadius = ODSCorners(all = DSVariables.radiusExtraSmall),
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    horizontalArrangement = Arrangement.Center,
    checkmarkWidthTypeActiveOnBackground = DSVariables.sizingComponent7,
    checkmarkWidthTypeActiveOnSubtle = DSVariables.sizingComponent7,
    checkmarkHeightTypeActiveOnBackground = DSVariables.sizingComponent7,
    checkmarkHeightTypeActiveOnSubtle = DSVariables.sizingComponent7,
    labelTextStyle = DSTextStyles.bodyMBold,
    labelTextAlign = TextAlign.Left
)

var DSTagsTokens: ODSTagsTokens = defaultODSTagsTokens
