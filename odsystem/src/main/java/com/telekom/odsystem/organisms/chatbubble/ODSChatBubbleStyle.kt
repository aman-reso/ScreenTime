package com.telekom.odsystem.organisms.chatbubble

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-19 (v1.33.1) - uid: 617274a4
 * Figma link: https://figma.com/design/RTdgj2EBwu8TwoaWWVEovL/ODS_OneID_Production_Library?node-id=18048-386
 */

class ODSChatBubbleStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var clipContent: Boolean? = null
    var avatarMinHeight: Dp? = null
    var avatarMinWidth: Dp? = null
    var avatarVerticalAlignment: Alignment.Vertical? = null
    var avatarHorizontalAlignment: Alignment.Horizontal? = null
    var avatarVerticalArrangement: Arrangement.Vertical? = null
    var contentContainerVerticalAlignment: Alignment.Vertical? = null
    var contentContainerHorizontalAlignment: Alignment.Horizontal? = null
    var contentContainerVerticalArrangement: Arrangement.Vertical? = null
    var bubbleBadgeGap: Dp? = null
    var bubbleBadgeVerticalAlignment: Alignment.Vertical? = null
    var bubbleBadgeHorizontalAlignment: Alignment.Horizontal? = null
    var bubbleBadgeHorizontalArrangement: Arrangement.Horizontal? = null
    var bubbleContainerZStackMaxWidth: Dp? = null
    var bubbleContainerZStackClipContent: Boolean? = null
    var bubbleContainerZStackContentAlignment: Alignment? = null
    var bubbleContainerCornerRadius: ODSCorners? = null
    var bubbleContainerMaxWidth: Dp? = null
    var bubbleContainerClipContent: Boolean? = null
    var bubbleContainerVerticalAlignment: Alignment.Vertical? = null
    var bubbleContainerHorizontalAlignment: Alignment.Horizontal? = null
    var bubbleContainerVerticalArrangement: Arrangement.Vertical? = null
    var bubbleContainerContentAlignment: Alignment? = null
    var bubbleBackgroundBackground: List<ODSColorModel>? = null
    var bubbleBackgroundCornerRadius: ODSCorners? = null
    var footerVerticalAlignment: Alignment.Vertical? = null
    var footerHorizontalAlignment: Alignment.Horizontal? = null
    var footerHorizontalArrangement: Arrangement.Horizontal? = null
    var helperTextGap: Dp? = null
    var helperTextPadding: ODSPadding? = null
    var helperTextHeight: Dp? = null
    var helperTextVerticalAlignment: Alignment.Vertical? = null
    var helperTextHorizontalAlignment: Alignment.Horizontal? = null
    var helperTextHorizontalArrangement: Arrangement.Horizontal? = null
    var textLabelStyle: ODSTextStyle? = null
    var textLabelColor: HexColor? = null
    var textLabelTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSChatBubbleProps,
    ): ODSChatBubbleStyle {
        val style = ODSChatBubbleStyle()
        style.gap = DSChatBubbleTokens.gap
        style.verticalAlignment = DSChatBubbleTokens.verticalAlignment
        style.horizontalAlignment = DSChatBubbleTokens.horizontalAlignment
        style.horizontalArrangement = DSChatBubbleTokens.horizontalArrangement
        if (props.variant == ODSChatBubbleVariant.INCOMING && !props.firstMessage) {
            style.clipContent = DSChatBubbleTokens.clipContentVariantIncoming
        }
        if (props.variant == ODSChatBubbleVariant.INCOMING) {
            style.avatarMinHeight = DSChatBubbleTokens.avatarMinHeightVariantIncoming
            style.avatarMinWidth = DSChatBubbleTokens.avatarMinWidthVariantIncoming
            style.avatarVerticalAlignment =
                DSChatBubbleTokens.avatarVerticalAlignmentVariantIncoming
            style.avatarHorizontalAlignment =
                DSChatBubbleTokens.avatarHorizontalAlignmentVariantIncoming
            style.avatarVerticalArrangement =
                DSChatBubbleTokens.avatarVerticalArrangementVariantIncoming
        }
        style.contentContainerVerticalAlignment =
            DSChatBubbleTokens.contentContainerVerticalAlignment
        style.contentContainerVerticalArrangement =
            DSChatBubbleTokens.contentContainerVerticalArrangement
        if (props.variant == ODSChatBubbleVariant.INCOMING) {
            style.contentContainerHorizontalAlignment =
                DSChatBubbleTokens.contentContainerHorizontalAlignmentVariantIncoming
        }
        if (props.variant == ODSChatBubbleVariant.OUTGOING) {
            style.contentContainerHorizontalAlignment =
                DSChatBubbleTokens.contentContainerHorizontalAlignmentVariantOutgoing
        }
        style.bubbleBadgeGap = DSChatBubbleTokens.bubbleBadgeGap
        style.bubbleBadgeVerticalAlignment = DSChatBubbleTokens.bubbleBadgeVerticalAlignment
        if (props.variant == ODSChatBubbleVariant.INCOMING) {
            style.bubbleBadgeHorizontalAlignment =
                DSChatBubbleTokens.bubbleBadgeHorizontalAlignmentVariantIncoming
            style.bubbleBadgeHorizontalArrangement =
                DSChatBubbleTokens.bubbleBadgeHorizontalArrangementVariantIncoming
        }
        if (props.variant == ODSChatBubbleVariant.OUTGOING) {
            style.bubbleBadgeHorizontalAlignment =
                DSChatBubbleTokens.bubbleBadgeHorizontalAlignmentVariantOutgoing
            style.bubbleBadgeHorizontalArrangement =
                DSChatBubbleTokens.bubbleBadgeHorizontalArrangementVariantOutgoing
        }
        style.bubbleContainerZStackMaxWidth = DSChatBubbleTokens.bubbleContainerZStackMaxWidth
        style.bubbleContainerZStackClipContent = DSChatBubbleTokens.bubbleContainerZStackClipContent
        style.bubbleContainerZStackContentAlignment =
            DSChatBubbleTokens.bubbleContainerZStackContentAlignment
        style.bubbleContainerMaxWidth = DSChatBubbleTokens.bubbleContainerMaxWidth
        style.bubbleContainerClipContent = DSChatBubbleTokens.bubbleContainerClipContent
        style.bubbleContainerVerticalAlignment = DSChatBubbleTokens.bubbleContainerVerticalAlignment
        style.bubbleContainerHorizontalAlignment =
            DSChatBubbleTokens.bubbleContainerHorizontalAlignment
        style.bubbleContainerVerticalArrangement =
            DSChatBubbleTokens.bubbleContainerVerticalArrangement
        style.bubbleContainerContentAlignment = DSChatBubbleTokens.bubbleContainerContentAlignment
        if (!props.firstMessage) {
            style.bubbleContainerCornerRadius = DSChatBubbleTokens.bubbleContainerCornerRadius
        }
        if (props.variant == ODSChatBubbleVariant.INCOMING && props.firstMessage) {
            style.bubbleContainerCornerRadius =
                DSChatBubbleTokens.bubbleContainerCornerRadiusVariantIncomingFirstMessage
        }
        if (props.variant == ODSChatBubbleVariant.OUTGOING && props.firstMessage) {
            style.bubbleContainerCornerRadius =
                DSChatBubbleTokens.bubbleContainerCornerRadiusVariantOutgoingFirstMessage
        }
        if (!props.firstMessage) {
            style.bubbleBackgroundCornerRadius = DSChatBubbleTokens.bubbleBackgroundCornerRadius
        }
        if (props.variant == ODSChatBubbleVariant.INCOMING) {
            style.bubbleBackgroundBackground =
                listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle))
        }
        if (props.variant == ODSChatBubbleVariant.OUTGOING) {
            style.bubbleBackgroundBackground =
                listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
        }
        if (props.variant == ODSChatBubbleVariant.INCOMING && props.firstMessage) {
            style.bubbleBackgroundCornerRadius =
                DSChatBubbleTokens.bubbleBackgroundCornerRadiusVariantIncomingFirstMessage
        }
        if (props.variant == ODSChatBubbleVariant.OUTGOING && props.firstMessage) {
            style.bubbleBackgroundCornerRadius =
                DSChatBubbleTokens.bubbleBackgroundCornerRadiusVariantOutgoingFirstMessage
        }
        style.footerVerticalAlignment = DSChatBubbleTokens.footerVerticalAlignment
        if (props.variant == ODSChatBubbleVariant.INCOMING) {
            style.footerHorizontalAlignment =
                DSChatBubbleTokens.footerHorizontalAlignmentVariantIncoming
            style.footerHorizontalArrangement =
                DSChatBubbleTokens.footerHorizontalArrangementVariantIncoming
        }
        if (props.variant == ODSChatBubbleVariant.OUTGOING) {
            style.footerHorizontalAlignment =
                DSChatBubbleTokens.footerHorizontalAlignmentVariantOutgoing
            style.footerHorizontalArrangement =
                DSChatBubbleTokens.footerHorizontalArrangementVariantOutgoing
        }
        style.helperTextGap = DSChatBubbleTokens.helperTextGap
        style.helperTextPadding = DSChatBubbleTokens.helperTextPadding
        style.helperTextHeight = DSChatBubbleTokens.helperTextHeight
        style.helperTextVerticalAlignment = DSChatBubbleTokens.helperTextVerticalAlignment
        style.helperTextHorizontalAlignment = DSChatBubbleTokens.helperTextHorizontalAlignment
        style.helperTextHorizontalArrangement = DSChatBubbleTokens.helperTextHorizontalArrangement
        style.textLabelStyle = DSChatBubbleTokens.textLabelStyle
        style.textLabelColor = scheme.basicTextRecessive
        style.textLabelTextAlign = DSChatBubbleTokens.textLabelTextAlign
        return style
    }
}
