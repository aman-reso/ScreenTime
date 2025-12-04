package com.telekom.odsystem.componenttokens

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

data class ODSDialogTokens(
    val borderRadius: ODSCorners,
    val minHeight: Dp,
    val width: Dp,
    val clipContent: Boolean,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val headerContainerVerticalAlignment: Alignment.Vertical,
    val headerContainerHorizontalAlignment: Alignment.Horizontal,
    val headerContainerVerticalArrangement: Arrangement.Vertical,
    val titleContainerVerticalAlignment: Alignment.Vertical,
    val titleContainerHorizontalAlignment: Alignment.Horizontal,
    val titleContainerHorizontalArrangement: Arrangement.Horizontal,
    val textContainerPadding: ODSPadding,
    val textContainerVerticalAlignment: Alignment.Vertical,
    val textContainerHorizontalAlignment: Alignment.Horizontal,
    val textContainerHorizontalArrangement: Arrangement.Horizontal,
    val headerTextStyle: ODSTextStyle,
    val headerTextAlign: TextAlign,
    val buttonContainerPadding: ODSPadding,
    val buttonContainerVerticalAlignment: Alignment.Vertical,
    val buttonContainerHorizontalAlignment: Alignment.Horizontal,
    val buttonContainerHorizontalArrangement: Arrangement.Horizontal,
    val dividerContainerVerticalAlignment: Alignment.Vertical,
    val dividerContainerHorizontalAlignment: Alignment.Horizontal,
    val dividerContainerVerticalArrangement: Arrangement.Vertical,
    val scrollContainerGap: Dp,
    val scrollContainerClipContent: Boolean,
    val scrollContainerVerticalAlignment: Alignment.Vertical,
    val scrollContainerHorizontalAlignment: Alignment.Horizontal,
    val scrollContainerVerticalArrangement: Arrangement.Vertical,
    val textSectionPadding: ODSPadding,
    val textSectionClipContent: Boolean,
    val textSectionVerticalAlignment: Alignment.Vertical,
    val textSectionHorizontalAlignment: Alignment.Horizontal,
    val textSectionVerticalArrangement: Arrangement.Vertical,
    val textTextStyle: ODSTextStyle,
    val textTextAlign: TextAlign,
    val textTextOverflow: TextOverflow,
    val slotContainerPadding: ODSPadding,
    val slotContainerVerticalAlignment: Alignment.Vertical,
    val slotContainerHorizontalAlignment: Alignment.Horizontal,
    val slotContainerHorizontalArrangement: Arrangement.Horizontal,
    val actionSlotContainerPadding: ODSPadding,
    val actionSlotContainerVerticalAlignment: Alignment.Vertical,
    val actionSlotContainerHorizontalAlignment: Alignment.Horizontal,
    val actionSlotContainerVerticalArrangement: Arrangement.Vertical,
    val padding: ODSPadding, // Not exported from the plugin
)

val defaultODSDialogTokens = ODSDialogTokens(
    borderRadius = ODSCorners(all = DSVariables.radiusMedium),
    minHeight = 280.dp,
    width = DSVariables.columns6Columns,
    clipContent = true,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    headerContainerVerticalAlignment = Alignment.Top,
    headerContainerHorizontalAlignment = Alignment.End,
    headerContainerVerticalArrangement = Arrangement.Top,
    titleContainerVerticalAlignment = Alignment.Top,
    titleContainerHorizontalAlignment = Alignment.End,
    titleContainerHorizontalArrangement = Arrangement.End,
    textContainerPadding = ODSPadding(all = DSVariables.spacingComponent7),
    textContainerVerticalAlignment = Alignment.Top,
    textContainerHorizontalAlignment = Alignment.Start,
    textContainerHorizontalArrangement = Arrangement.Start,
    headerTextStyle = DSTextStyles.subtitle,
    headerTextAlign = TextAlign.Left,
    buttonContainerPadding = ODSPadding(
        top = DSVariables.spacingComponent3,
        right = DSVariables.spacingComponent3
    ),
    buttonContainerVerticalAlignment = Alignment.Top,
    buttonContainerHorizontalAlignment = Alignment.Start,
    buttonContainerHorizontalArrangement = Arrangement.Start,
    dividerContainerVerticalAlignment = Alignment.Bottom,
    dividerContainerHorizontalAlignment = Alignment.Start,
    dividerContainerVerticalArrangement = Arrangement.Bottom,
    scrollContainerGap = DSVariables.spacingComponent7,
    scrollContainerClipContent = true,
    scrollContainerVerticalAlignment = Alignment.Top,
    scrollContainerHorizontalAlignment = Alignment.Start,
    scrollContainerVerticalArrangement = Arrangement.Top,
    textSectionPadding = ODSPadding(
        left = DSVariables.spacingComponent7,
        right = DSVariables.spacingComponent7
    ),
    textSectionClipContent = true,
    textSectionVerticalAlignment = Alignment.Top,
    textSectionHorizontalAlignment = Alignment.Start,
    textSectionVerticalArrangement = Arrangement.Top,
    textTextStyle = DSTextStyles.bodyMBold,
    textTextAlign = TextAlign.Left,
    textTextOverflow = TextOverflow.Ellipsis,
    slotContainerPadding = ODSPadding(
        left = DSVariables.spacingComponent7,
        right = DSVariables.spacingComponent7
    ),
    slotContainerVerticalAlignment = Alignment.Top,
    slotContainerHorizontalAlignment = Alignment.Start,
    slotContainerHorizontalArrangement = Arrangement.Start,
    actionSlotContainerPadding = ODSPadding(all = DSVariables.spacingComponent7),
    actionSlotContainerVerticalAlignment = Alignment.Top,
    actionSlotContainerHorizontalAlignment = Alignment.Start,
    actionSlotContainerVerticalArrangement = Arrangement.Top,
    padding = ODSPadding(
        left = DSVariables.spacingComponent5,
        right = DSVariables.spacingComponent5,
    )
)

var DSDialogTokens: ODSDialogTokens = defaultODSDialogTokens
