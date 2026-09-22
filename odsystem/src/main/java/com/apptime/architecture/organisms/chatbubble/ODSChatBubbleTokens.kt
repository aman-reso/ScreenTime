package com.telekom.odsystem.organisms.chatbubble

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

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-19 (v1.33.1) - uid: 617274a4
 * Figma link: https://figma.com/design/RTdgj2EBwu8TwoaWWVEovL/ODS_OneID_Production_Library?node-id=18048-386
 */

data class ODSChatBubbleTokens(
    val gap: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val clipContentVariantIncoming: Boolean,
    val avatarMinHeightVariantIncoming: Dp,
    val avatarMinWidthVariantIncoming: Dp,
    val avatarVerticalAlignmentVariantIncoming: Alignment.Vertical,
    val avatarHorizontalAlignmentVariantIncoming: Alignment.Horizontal,
    val avatarVerticalArrangementVariantIncoming: Arrangement.Vertical,
    val contentContainerVerticalAlignment: Alignment.Vertical,
    val contentContainerHorizontalAlignmentVariantIncoming: Alignment.Horizontal,
    val contentContainerHorizontalAlignmentVariantOutgoing: Alignment.Horizontal,
    val contentContainerVerticalArrangement: Arrangement.Vertical,
    val bubbleBadgeGap: Dp,
    val bubbleBadgeVerticalAlignment: Alignment.Vertical,
    val bubbleBadgeHorizontalAlignmentVariantIncoming: Alignment.Horizontal,
    val bubbleBadgeHorizontalAlignmentVariantOutgoing: Alignment.Horizontal,
    val bubbleBadgeHorizontalArrangementVariantIncoming: Arrangement.Horizontal,
    val bubbleBadgeHorizontalArrangementVariantOutgoing: Arrangement.Horizontal,
    val bubbleContainerZStackMaxWidth: Dp,
    val bubbleContainerZStackClipContent: Boolean,
    val bubbleContainerZStackContentAlignment: Alignment,
    val bubbleContainerCornerRadiusVariantIncomingFirstMessage: ODSCorners,
    val bubbleContainerCornerRadius: ODSCorners,
    val bubbleContainerCornerRadiusVariantOutgoingFirstMessage: ODSCorners,
    val bubbleContainerMaxWidth: Dp,
    val bubbleContainerClipContent: Boolean,
    val bubbleContainerVerticalAlignment: Alignment.Vertical,
    val bubbleContainerHorizontalAlignment: Alignment.Horizontal,
    val bubbleContainerVerticalArrangement: Arrangement.Vertical,
    val bubbleContainerContentAlignment: Alignment,
    val bubbleBackgroundCornerRadiusVariantIncomingFirstMessage: ODSCorners,
    val bubbleBackgroundCornerRadius: ODSCorners,
    val bubbleBackgroundCornerRadiusVariantOutgoingFirstMessage: ODSCorners,
    val footerVerticalAlignment: Alignment.Vertical,
    val footerHorizontalAlignmentVariantIncoming: Alignment.Horizontal,
    val footerHorizontalAlignmentVariantOutgoing: Alignment.Horizontal,
    val footerHorizontalArrangementVariantIncoming: Arrangement.Horizontal,
    val footerHorizontalArrangementVariantOutgoing: Arrangement.Horizontal,
    val helperTextGap: Dp,
    val helperTextPadding: ODSPadding,
    val helperTextHeight: Dp,
    val helperTextVerticalAlignment: Alignment.Vertical,
    val helperTextHorizontalAlignment: Alignment.Horizontal,
    val helperTextHorizontalArrangement: Arrangement.Horizontal,
    val textLabelStyle: ODSTextStyle,
    val textLabelTextAlign: TextAlign,
)

val defaultODSChatBubbleTokens = ODSChatBubbleTokens(
    gap = DSVariables.spacingComponent2,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start,
    clipContentVariantIncoming = true,
    avatarMinHeightVariantIncoming = 24.dp,
    avatarMinWidthVariantIncoming = 24.dp,
    avatarVerticalAlignmentVariantIncoming = Alignment.Top,
    avatarHorizontalAlignmentVariantIncoming = Alignment.Start,
    avatarVerticalArrangementVariantIncoming = Arrangement.Top,
    contentContainerVerticalAlignment = Alignment.CenterVertically,
    contentContainerHorizontalAlignmentVariantIncoming = Alignment.Start,
    contentContainerHorizontalAlignmentVariantOutgoing = Alignment.End,
    contentContainerVerticalArrangement = Arrangement.Center,
    bubbleBadgeGap = DSVariables.spacingComponent3,
    bubbleBadgeVerticalAlignment = Alignment.CenterVertically,
    bubbleBadgeHorizontalAlignmentVariantIncoming = Alignment.Start,
    bubbleBadgeHorizontalAlignmentVariantOutgoing = Alignment.End,
    bubbleBadgeHorizontalArrangementVariantIncoming = Arrangement.Start,
    bubbleBadgeHorizontalArrangementVariantOutgoing = Arrangement.End,
    bubbleContainerZStackMaxWidth = 260.dp,
    bubbleContainerZStackClipContent = true,
    bubbleContainerZStackContentAlignment = Alignment.TopStart,
    bubbleContainerCornerRadiusVariantIncomingFirstMessage = ODSCorners(
        topLeft = 0.dp,
        topRight = DSVariables.radiusSmall,
        bottomLeft = DSVariables.radiusSmall,
        bottomRight = DSVariables.radiusSmall
    ),
    bubbleContainerCornerRadius = ODSCorners(all = DSVariables.radiusSmall),
    bubbleContainerCornerRadiusVariantOutgoingFirstMessage = ODSCorners(
        topLeft = DSVariables.radiusSmall,
        topRight = 0.dp,
        bottomLeft = DSVariables.radiusSmall,
        bottomRight = DSVariables.radiusSmall
    ),
    bubbleContainerMaxWidth = 260.dp,
    bubbleContainerClipContent = true,
    bubbleContainerVerticalAlignment = Alignment.Top,
    bubbleContainerHorizontalAlignment = Alignment.Start,
    bubbleContainerVerticalArrangement = Arrangement.Top,
    bubbleContainerContentAlignment = Alignment.TopStart,
    bubbleBackgroundCornerRadiusVariantIncomingFirstMessage = ODSCorners(
        topLeft = 0.dp,
        topRight = DSVariables.radiusSmall,
        bottomLeft = DSVariables.radiusSmall,
        bottomRight = DSVariables.radiusSmall
    ),
    bubbleBackgroundCornerRadius = ODSCorners(all = DSVariables.radiusSmall),
    bubbleBackgroundCornerRadiusVariantOutgoingFirstMessage = ODSCorners(
        topLeft = DSVariables.radiusSmall,
        topRight = 0.dp,
        bottomLeft = DSVariables.radiusSmall,
        bottomRight = DSVariables.radiusSmall
    ),
    footerVerticalAlignment = Alignment.CenterVertically,
    footerHorizontalAlignmentVariantIncoming = Alignment.Start,
    footerHorizontalAlignmentVariantOutgoing = Alignment.End,
    footerHorizontalArrangementVariantIncoming = Arrangement.Start,
    footerHorizontalArrangementVariantOutgoing = Arrangement.End,
    helperTextGap = DSVariables.spacingComponent2,
    helperTextPadding = ODSPadding(
        top = DSVariables.spacingComponent4,
        bottom = DSVariables.spacingComponent4,
        right = DSVariables.spacingComponent3
    ),
    helperTextHeight = DSVariables.sizingMinimumTappableArea,
    helperTextVerticalAlignment = Alignment.CenterVertically,
    helperTextHorizontalAlignment = Alignment.Start,
    helperTextHorizontalArrangement = Arrangement.Start,
    textLabelStyle = DSTextStyles.bodySRegular,
    textLabelTextAlign = TextAlign.Left
)

var DSChatBubbleTokens: ODSChatBubbleTokens = defaultODSChatBubbleTokens
