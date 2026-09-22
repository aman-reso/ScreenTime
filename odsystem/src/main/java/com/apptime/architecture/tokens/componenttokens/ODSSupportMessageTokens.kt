package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSTextStyle

/**
 * Created by dmarinopoulos on 3/4/24
 */

data class ODSSupportMessageTokens(
    var gap: Dp,
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var horizontalArrangement: Arrangement.Horizontal,
    var informationWidthTypeInformative: Dp,
    var informationHeightTypeInformative: Dp,
    var errorWidthTypeError: Dp,
    var errorHeightTypeError: Dp,
    var successWidthTypeSuccess: Dp,
    var successHeightTypeSuccess: Dp,
    var labelTextStyle: ODSTextStyle,
    var labelTextAlign: TextAlign
)

var defaultODSSupportMessageTokens = ODSSupportMessageTokens(
    gap = DSVariables.spacingComponent2,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start,
    informationWidthTypeInformative = DSVariables.sizingComponent7,
    informationHeightTypeInformative = DSVariables.sizingComponent7,
    errorWidthTypeError = DSVariables.sizingComponent7,
    errorHeightTypeError = DSVariables.sizingComponent7,
    successWidthTypeSuccess = DSVariables.sizingComponent7,
    successHeightTypeSuccess = DSVariables.sizingComponent7,
    labelTextStyle = DSTextStyles.bodySBold,
    labelTextAlign = TextAlign.Left
)

var DSSupportMessageTokens: ODSSupportMessageTokens = defaultODSSupportMessageTokens
