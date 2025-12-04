package com.telekom.odsystem.slots.choicecardcontent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSTextStyle

/**
 * Code generated with ODS RADD Code Generator
 * 2025-11-07 (v1.33.1) - uid: 2bf64b81
 * Figma link: https://figma.com/design/Lv42UPNpBtiMLvZ33k8VHr/-ODS OneID Mobile Components?node-id=45848-7317
 */

data class ODSChoiceCardContentTokens(
    val gap: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val headingStyle: ODSTextStyle,
    val headingTextAlign: TextAlign,
    val bodyTextStyle: ODSTextStyle,
    val bodyTextTextAlign: TextAlign,
)

val defaultODSChoiceCardContentTokens = ODSChoiceCardContentTokens(
    gap = DSVariables.spacingComponent3,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    headingStyle = DSTextStyles.bodyL,
    headingTextAlign = TextAlign.Left,
    bodyTextStyle = DSTextStyles.bodyMRegular,
    bodyTextTextAlign = TextAlign.Left
)

var DSChoiceCardContentTokens: ODSChoiceCardContentTokens = defaultODSChoiceCardContentTokens
