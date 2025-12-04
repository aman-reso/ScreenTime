package com.telekom.odsystem.slots.quickactioncardpreferredcontent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSTextStyle

/**
 * Code generated with ODS RADD Code Generator
 * 2025-11-07 (v1.33.1) - uid: 1cd7f45c
 * Figma link: https://figma.com/design/Lv42UPNpBtiMLvZ33k8VHr/-ODS OneID Mobile Components?node-id=45713-2590
 */

data class ODSQuickActionCardPreferredContentTokens(
    val gap: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val copyAndSparklineGap: Dp,
    val copyAndSparklineVerticalAlignment: Alignment.Vertical,
    val copyAndSparklineHorizontalAlignment: Alignment.Horizontal,
    val copyAndSparklineVerticalArrangement: Arrangement.Vertical,
    val titleStyle: ODSTextStyle,
    val titleTextAlign: TextAlign,
    val subtitleStyle: ODSTextStyle,
    val subtitleTextAlign: TextAlign,
    val tagsContainerGap: Dp,
    val tagsContainerVerticalAlignment: Alignment.Vertical,
    val tagsContainerHorizontalAlignment: Alignment.Horizontal,
    val tagsContainerHorizontalArrangement: Arrangement.Horizontal,
    val logoHeight: Dp, // Not exported from plugin
)

val defaultODSQuickActionCardPreferredContentTokens = ODSQuickActionCardPreferredContentTokens(
    gap = DSVariables.spacingComponent6,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    copyAndSparklineGap = DSVariables.spacingComponent3,
    copyAndSparklineVerticalAlignment = Alignment.CenterVertically,
    copyAndSparklineHorizontalAlignment = Alignment.Start,
    copyAndSparklineVerticalArrangement = Arrangement.Center,
    titleStyle = DSTextStyles.bodyL,
    titleTextAlign = TextAlign.Left,
    subtitleStyle = DSTextStyles.bodyMBold,
    subtitleTextAlign = TextAlign.Left,
    tagsContainerGap = DSVariables.spacingComponent3,
    tagsContainerVerticalAlignment = Alignment.Top,
    tagsContainerHorizontalAlignment = Alignment.Start,
    tagsContainerHorizontalArrangement = Arrangement.Start,
    logoHeight = 24.dp
)

var DSQuickActionCardPreferredContentTokens: ODSQuickActionCardPreferredContentTokens =
    defaultODSQuickActionCardPreferredContentTokens
