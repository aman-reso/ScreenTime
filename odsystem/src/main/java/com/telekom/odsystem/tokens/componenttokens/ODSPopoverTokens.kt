package com.telekom.odsystem.componenttokens

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

data class ODSPopoverTokens(
    val padding: ODSPadding,
    val borderRadius: ODSCorners,
    val maxWidth: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val contentAlignment: Alignment,
    val titleCloseGap: Dp,
    val titleCloseVerticalAlignment: Alignment.Vertical,
    val titleCloseHorizontalAlignment: Alignment.Horizontal,
    val titleCloseHorizontalArrangement: Arrangement.Horizontal,
    val titleVerticalAlignment: Alignment.Vertical,
    val titleHorizontalAlignment: Alignment.Horizontal,
    val titleHorizontalArrangement: Arrangement.Horizontal,
    val labelTextStyle: ODSTextStyle,
    val labelTextAlign: TextAlign,
    val labelMaxWidth: Dp,
    val contentActionsGap: Dp,
    val contentActionsVerticalAlignment: Alignment.Vertical,
    val contentActionsHorizontalAlignment: Alignment.Horizontal,
    val contentActionsVerticalArrangement: Arrangement.Vertical,
    val contentGap: Dp,
    val contentVerticalAlignment: Alignment.Vertical,
    val contentHorizontalAlignment: Alignment.Horizontal,
    val contentVerticalArrangement: Arrangement.Vertical,
    val textTextStyle: ODSTextStyle,
    val textTextAlign: TextAlign,
    val textMaxWidth: Dp,
    val contentSlotContainerMaxHeight: Dp,
    val contentSlotContainerMaxWidth: Dp,
    val contentSlotContainerClipContent: Boolean,
    val contentSlotContainerVerticalAlignment: Alignment.Vertical,
    val contentSlotContainerHorizontalAlignment: Alignment.Horizontal,
    val contentSlotContainerVerticalArrangement: Arrangement.Vertical,
    val actionSlotContainerGap: Dp,
    val actionSlotContainerVerticalAlignment: Alignment.Vertical,
    val actionSlotContainerHorizontalAlignment: Alignment.Horizontal,
    val actionSlotContainerHorizontalArrangement: Arrangement.Horizontal,
    val containerVerticalAlignmentTop: Alignment.Vertical, // Not exported from the plugin
    val containerVerticalAlignmentBottom: Alignment.Vertical, // Not exported from the plugin
    val containerHorizontalAlignmentStart: Alignment.Horizontal, // Not exported from the plugin
    val containerHorizontalAlignmentCenter: Alignment.Horizontal, // Not exported from the plugin
    val containerHorizontalAlignmentEnd: Alignment.Horizontal, // Not exported from the plugin
    val containerVerticalArrangementTop: Arrangement.Vertical, // Not exported from the plugin
    val containerVerticalArrangementBottom: Arrangement.Vertical, // Not exported from the plugin
    val containerHorizontalArrangementStart: Arrangement.Horizontal, // Not exported from the plugin
    val containerHorizontalArrangementCenter: Arrangement.Horizontal, // Not exported from the plugin
    val containerHorizontalArrangementEnd: Arrangement.Horizontal, // Not exported from the plugin
    val caretVerticalHeight: Dp, // Not exported from the plugin
    val caretVerticalWidth: Dp, // Not exported from the plugin
    val caretHorizontalHeight: Dp, // Not exported from the plugin
    val caretHorizontalWidth: Dp, // Not exported from the plugin
    val caretPaddingLeftAlignment: ODSPadding, // Not exported from the plugin
    val caretPaddingRightAlignment: ODSPadding, // Not exported from the plugin
    val caretPaddingTopAlignment: ODSPadding, // Not exported from the plugin
    val caretPaddingBottomAlignment: ODSPadding, // Not exported from the plugin
)

val defaultODSPopoverTokens = ODSPopoverTokens(
    padding = ODSPadding(all = DSVariables.spacingComponent4),
    borderRadius = ODSCorners(all = 4.dp),
    maxWidth = 336.dp,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.End,
    verticalArrangement = Arrangement.Top,
    contentAlignment = Alignment.BottomStart,
    titleCloseGap = DSVariables.spacingComponent3,
    titleCloseVerticalAlignment = Alignment.CenterVertically,
    titleCloseHorizontalAlignment = Alignment.Start,
    titleCloseHorizontalArrangement = Arrangement.Start,
    titleVerticalAlignment = Alignment.CenterVertically,
    titleHorizontalAlignment = Alignment.CenterHorizontally,
    titleHorizontalArrangement = Arrangement.Center,
    labelTextStyle = DSTextStyles.bodySBold,
    labelTextAlign = TextAlign.Left,
    labelMaxWidth = 256.dp,
    contentActionsGap = DSVariables.spacingComponent3,
    contentActionsVerticalAlignment = Alignment.Top,
    contentActionsHorizontalAlignment = Alignment.Start,
    contentActionsVerticalArrangement = Arrangement.Top,
    contentGap = DSVariables.spacingComponent3,
    contentVerticalAlignment = Alignment.Top,
    contentHorizontalAlignment = Alignment.Start,
    contentVerticalArrangement = Arrangement.Top,
    textTextStyle = DSTextStyles.bodySRegular,
    textTextAlign = TextAlign.Left,
    textMaxWidth = 312.dp,
    contentSlotContainerMaxHeight = 180.dp,
    contentSlotContainerMaxWidth = 312.dp,
    contentSlotContainerClipContent = true,
    contentSlotContainerVerticalAlignment = Alignment.Top,
    contentSlotContainerHorizontalAlignment = Alignment.Start,
    contentSlotContainerVerticalArrangement = Arrangement.Top,
    actionSlotContainerGap = DSVariables.spacingComponent3,
    actionSlotContainerVerticalAlignment = Alignment.Top,
    actionSlotContainerHorizontalAlignment = Alignment.Start,
    actionSlotContainerHorizontalArrangement = Arrangement.Start,
    containerVerticalAlignmentTop = Alignment.Top,
    containerVerticalAlignmentBottom = Alignment.Bottom,
    containerHorizontalAlignmentStart = Alignment.Start,
    containerHorizontalAlignmentCenter = Alignment.CenterHorizontally,
    containerHorizontalAlignmentEnd = Alignment.End,
    containerVerticalArrangementTop = Arrangement.Top,
    containerVerticalArrangementBottom = Arrangement.Bottom,
    containerHorizontalArrangementStart = Arrangement.Start,
    containerHorizontalArrangementCenter = Arrangement.Center,
    containerHorizontalArrangementEnd = Arrangement.End,
    caretVerticalHeight = 6.dp,
    caretVerticalWidth = 12.dp,
    caretHorizontalHeight = 12.dp,
    caretHorizontalWidth = 6.dp,
    caretPaddingLeftAlignment = ODSPadding(
        top = 0.dp,
        bottom = 0.dp,
        left = 12.dp,
        right = 0.dp
    ),
    caretPaddingRightAlignment = ODSPadding(
        top = 0.dp,
        bottom = 0.dp,
        left = 0.dp,
        right = 12.dp
    ),
    caretPaddingTopAlignment = ODSPadding(
        top = 12.dp,
        bottom = 0.dp,
        left = 0.dp,
        right = 0.dp
    ),
    caretPaddingBottomAlignment = ODSPadding(
        top = 0.dp,
        bottom = 12.dp,
        left = 0.dp,
        right = 0.dp
    ),
)

var DSPopoverTokens: ODSPopoverTokens = defaultODSPopoverTokens
