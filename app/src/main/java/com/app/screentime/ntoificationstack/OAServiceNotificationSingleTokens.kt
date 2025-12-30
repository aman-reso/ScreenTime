package com.app.screentime.ntoificationstack

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

/**
 * Code generated with ODS RADD Code Generator
 * 2025-11-04 (v1.34.1) - uid: 5bee5665
 * Figma link: https://figma.com/design/3MbZ8LOrBNBjTZX9J3t8Lu/OneApp ODS Library?node-id=8940-4365
 */

data class OAServiceNotificationSingleTokens(
    val padding: ODSPadding,
    val cornerRadius: ODSCorners,
    val width: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalArrangement: Arrangement.Horizontal,
    val notificationGap: Dp,
    val notificationPadding: ODSPadding,
    val notificationVerticalAlignment: Alignment.Vertical,
    val notificationHorizontalAlignment: Alignment.Horizontal,
    val notificationHorizontalArrangement: Arrangement.Horizontal,
    val iconWidth: Dp,
    val iconHeight: Dp,
    val contentPadding: ODSPadding,
    val contentVerticalAlignment: Alignment.Vertical,
    val contentHorizontalAlignment: Alignment.Horizontal,
    val contentVerticalArrangement: Arrangement.Vertical,
    val titleStyle: ODSTextStyle,
    val titleTextAlign: TextAlign,
    val titleOverflow: TextOverflow,
    val spacerPadding: ODSPadding,
    val spacerVerticalAlignment: Alignment.Vertical,
    val spacerHorizontalAlignment: Alignment.Horizontal,
    val spacerHorizontalArrangement: Arrangement.Horizontal,
    val cardBgCornerRadius: ODSCorners,
)

val defaultOAServiceNotificationSingleTokens = OAServiceNotificationSingleTokens(
    padding = ODSPadding(left = DSVariables.spacingComponent7),
    cornerRadius = ODSCorners(all = DSVariables.radiusMedium),
    width = 374.dp,
    verticalAlignment = Alignment.Top,
    horizontalArrangement = Arrangement.SpaceBetween,
    notificationGap = DSVariables.spacingComponent4,
    notificationPadding = ODSPadding(
        top = DSVariables.spacingComponent6,
        bottom = DSVariables.spacingComponent3,
        right = DSVariables.spacingComponent5
    ),
    notificationVerticalAlignment = Alignment.Top,
    notificationHorizontalAlignment = Alignment.Start,
    notificationHorizontalArrangement = Arrangement.Start,
    iconWidth = 24.dp,
    iconHeight = 24.dp,
    contentPadding = ODSPadding(top = DSVariables.spacingComponent1),
    contentVerticalAlignment = Alignment.CenterVertically,
    contentHorizontalAlignment = Alignment.Start,
    contentVerticalArrangement = Arrangement.Center,
    titleStyle = DSTextStyles.bodyMBold,
    titleTextAlign = TextAlign.Left,
    titleOverflow = TextOverflow.Ellipsis,
    spacerPadding = ODSPadding(all = DSVariables.spacingComponent3),
    spacerVerticalAlignment = Alignment.CenterVertically,
    spacerHorizontalAlignment = Alignment.Start,
    spacerHorizontalArrangement = Arrangement.Start,
    cardBgCornerRadius = ODSCorners(all = DSVariables.radiusMedium),
)

var DSOAServiceNotificationSingleTokens: OAServiceNotificationSingleTokens =
    defaultOAServiceNotificationSingleTokens