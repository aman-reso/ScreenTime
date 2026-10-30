package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSInputItemTokens(
    val borderRadius: ODSCorners,
    val borderModeStandard: Dp,
    val borderModeError: Dp,
    val width: Dp,
    val height: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val placeholderTextStyle: ODSTextStyle,
    val placeholderTextAlign: TextAlign,
    val inputValueTextStyleFilled: ODSTextStyle,
    val inputValueTextAlignFilled: TextAlign,
    val dotWidth: Dp, // Not exported from the plugin
    val dotHeight: Dp, // Not exported from the plugin
    val borderStateFocused: Dp, // Not exported from the plugin
)

val defaultODSInputItemTokens = ODSInputItemTokens(
    borderRadius = ODSCorners(all = 12.dp),
    borderModeStandard = DSVariables.strokes1,
    borderModeError = DSVariables.strokes3,
    width = 44.dp,
    height = 54.dp,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    horizontalArrangement = Arrangement.Center,
    placeholderTextStyle = DSTextStyles.paragraph,
    placeholderTextAlign = TextAlign.Center,
    inputValueTextStyleFilled = DSTextStyles.paragraph,
    inputValueTextAlignFilled = TextAlign.Center,
    dotWidth = 8.dp,
    dotHeight = 8.dp,
    borderStateFocused = DSVariables.strokes2,
)

var DSInputItemTokens: ODSInputItemTokens = defaultODSInputItemTokens
