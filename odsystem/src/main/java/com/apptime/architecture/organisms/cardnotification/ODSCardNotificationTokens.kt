package com.telekom.odsystem.organisms.cardnotification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.foundations.SCALE_FACTOR

data class ODSCardNotificationTokens(
    val padding: ODSPadding,
    val minHeight: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val contentAlignment: Alignment,
    val cardBgBorderRadius: ODSCorners,
    val cardBgClipContent: Boolean,
    val cardBgVerticalAlignment: Alignment.Vertical,
    val cardBgHorizontalAlignment: Alignment.Horizontal,
    val cardBgVerticalArrangement: Arrangement.Vertical,
    val headerContainerGapShowImageSlot: Dp,
    val headerContainerGap: Dp,
    val headerContainerPadding: ODSPadding,
    val headerContainerMinHeight: Dp,
    val headerContainerVerticalAlignment: Alignment.Vertical,
    val headerContainerHorizontalAlignment: Alignment.Horizontal,
    val headerContainerHorizontalArrangement: Arrangement.Horizontal,
    val headerContentGap: Dp,
    val headerContentPaddingShowImageSlot: ODSPadding,
    val headerContentPadding: ODSPadding,
    val headerContentVerticalAlignment: Alignment.Vertical,
    val headerContentHorizontalAlignment: Alignment.Horizontal,
    val headerContentVerticalArrangement: Arrangement.Vertical,
    val headerTextStyle: ODSTextStyle,
    val headerTextAlign: TextAlign,
    val textTextStyle: ODSTextStyle,
    val textTextAlign: TextAlign,
    val closeButtonContainerPadding: ODSPadding,
    val closeButtonContainerVerticalAlignment: Alignment.Vertical,
    val closeButtonContainerHorizontalAlignment: Alignment.Horizontal,
    val closeButtonContainerHorizontalArrangement: Arrangement.Horizontal,
    val actionContainerGap: Dp,
    val actionContainerPaddingShowImageSlot: ODSPadding,
    val actionContainerPadding: ODSPadding,
    val actionContainerVerticalAlignment: Alignment.Vertical,
    val actionContainerHorizontalAlignment: Alignment.Horizontal,
    val actionContainerHorizontalArrangement: Arrangement.Horizontal,
    var scaleFactor: Float? = null, // Not exported from the plugin
)

val defaultODSCardNotificationTokens = ODSCardNotificationTokens(
    padding = ODSPadding(bottom = DSVariables.spacingComponent7),
    minHeight = DSVariables.sizingComponent19 - DSVariables.sizingComponent10,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    contentAlignment = Alignment.TopStart,
    cardBgBorderRadius = ODSCorners(all = DSVariables.radiusMedium),
    cardBgClipContent = true,
    cardBgVerticalAlignment = Alignment.Top,
    cardBgHorizontalAlignment = Alignment.CenterHorizontally,
    cardBgVerticalArrangement = Arrangement.Top,
    headerContainerGapShowImageSlot = DSVariables.spacingLayout6,
    headerContainerGap = DSVariables.spacingComponent2,
    headerContainerPadding = ODSPadding(left = DSVariables.spacingComponent7),
    headerContainerMinHeight = 62.dp,
    headerContainerVerticalAlignment = Alignment.Top,
    headerContainerHorizontalAlignment = Alignment.Start,
    headerContainerHorizontalArrangement = Arrangement.Start,
    headerContentGap = DSVariables.spacingComponent2,
    headerContentPaddingShowImageSlot = ODSPadding(top = DSVariables.spacingComponent7),
    headerContentPadding = ODSPadding(
        top = DSVariables.spacingComponent7,
        right = DSVariables.spacingComponent7
    ),
    headerContentVerticalAlignment = Alignment.Top,
    headerContentHorizontalAlignment = Alignment.Start,
    headerContentVerticalArrangement = Arrangement.Top,
    headerTextStyle = DSTextStyles.bodyMBold,
    headerTextAlign = TextAlign.Left,
    textTextStyle = DSTextStyles.bodySRegular,
    textTextAlign = TextAlign.Left,
    closeButtonContainerPadding = ODSPadding(all = DSVariables.spacingComponent3),
    closeButtonContainerVerticalAlignment = Alignment.Top,
    closeButtonContainerHorizontalAlignment = Alignment.Start,
    closeButtonContainerHorizontalArrangement = Arrangement.Start,
    actionContainerGap = DSVariables.spacingComponent2,
    actionContainerPaddingShowImageSlot = ODSPadding(
        top = DSVariables.spacingComponent4,
        left = DSVariables.spacingComponent7,
        right = 144.dp
    ),
    actionContainerPadding = ODSPadding(
        top = DSVariables.spacingComponent4,
        left = DSVariables.spacingComponent7,
        right = DSVariables.spacingComponent7
    ),
    actionContainerVerticalAlignment = Alignment.Bottom,
    actionContainerHorizontalAlignment = Alignment.Start,
    actionContainerHorizontalArrangement = Arrangement.Start,
    scaleFactor = SCALE_FACTOR,
)

var DSCardNotificationTokens: ODSCardNotificationTokens = defaultODSCardNotificationTokens
