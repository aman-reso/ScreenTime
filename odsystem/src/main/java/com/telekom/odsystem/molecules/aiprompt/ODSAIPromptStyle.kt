package com.telekom.odsystem.molecules.aiprompt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-07-23 (v1.32.2) - uid: 4a5353d2
 * Figma link: https://figma.com/design/RTdgj2EBwu8TwoaWWVEovL/ODS_OneID_Production_Library?node-id=16705-24289
 */

@Suppress("LongMethod")
class ODSAIPromptStyle {
    var minHeight: Dp? = null
    var minWidth: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var contentZStackMinHeight: Dp? = null
    var contentZStackContentAlignment: Alignment? = null
    var contentGap: Dp? = null
    var contentPadding: ODSPadding? = null
    var contentMinHeight: Dp? = null
    var contentVerticalAlignment: Alignment.Vertical? = null
    var contentHorizontalAlignment: Alignment.Horizontal? = null
    var contentHorizontalArrangement: Arrangement.Horizontal? = null
    var contentContentAlignment: Alignment? = null
    var promptBgBackground: List<ODSColorModel>? = null
    var promptBgCornerRadius: ODSCorners? = null
    var promptBgBorder: Dp? = null
    var promptBgBorderColor: List<ODSColorModel>? = null
    var containerGap: Dp? = null
    var containerVerticalAlignment: Alignment.Vertical? = null
    var containerHorizontalAlignment: Alignment.Horizontal? = null
    var containerVerticalArrangement: Arrangement.Vertical? = null
    var containerHorizontalArrangement: Arrangement.Horizontal? = null
    var iconColor: HexColor? = null
    var iconWidth: Dp? = null
    var iconHeight: Dp? = null
    var textContainerGap: Dp? = null
    var textContainerVerticalAlignment: Alignment.Vertical? = null
    var textContainerHorizontalAlignment: Alignment.Horizontal? = null
    var textContainerVerticalArrangement: Arrangement.Vertical? = null
    var titleStyle: ODSTextStyle? = null
    var titleColor: HexColor? = null
    var titleTextAlign: TextAlign? = null
    var descriptionTextStyle: ODSTextStyle? = null
    var descriptionTextColor: HexColor? = null
    var descriptionTextTextAlign: TextAlign? = null
    var title2Style: ODSTextStyle? = null
    var title2Color: HexColor? = null
    var title2TextAlign: TextAlign? = null
    var descriptionText2Style: ODSTextStyle? = null
    var descriptionText2Color: HexColor? = null
    var descriptionText2TextAlign: TextAlign? = null
    var rightIconColor: HexColor? = null
    var rightIconWidth: Dp? = null
    var rightIconHeight: Dp? = null
    var scaleFactor: Float? = null // Not exported by plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSAIPromptProps,
        state: ODSActions
    ): ODSAIPromptStyle {
        val style = ODSAIPromptStyle()
        style.minHeight = DSAIPromptTokens.minHeight
        style.minWidth = DSAIPromptTokens.minWidth
        style.verticalAlignment = DSAIPromptTokens.verticalAlignment
        style.horizontalAlignment = DSAIPromptTokens.horizontalAlignment
        style.horizontalArrangement = DSAIPromptTokens.horizontalArrangement
        style.contentZStackMinHeight = DSAIPromptTokens.contentZStackMinHeight
        style.contentZStackContentAlignment = DSAIPromptTokens.contentZStackContentAlignment
        style.contentGap = DSAIPromptTokens.contentGap
        style.contentPadding = DSAIPromptTokens.contentPadding
        style.contentMinHeight = DSAIPromptTokens.contentMinHeight
        style.contentVerticalAlignment = DSAIPromptTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSAIPromptTokens.contentHorizontalAlignment
        style.contentHorizontalArrangement = DSAIPromptTokens.contentHorizontalArrangement
        style.contentContentAlignment = DSAIPromptTokens.contentContentAlignment
        style.promptBgCornerRadius = DSAIPromptTokens.promptBgCornerRadius
        if (props.variant == ODSAIPromptVariant.FILLED) {
            style.promptBgBackground =
                listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle))
        }
        if (props.variant == ODSAIPromptVariant.OUTLINE) {
            style.promptBgBorder = DSAIPromptTokens.promptBgBorderVariantOutline
            style.promptBgBorderColor = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        }
        if (state == ODSActions.HOVERED) {
            style.promptBgBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverBackgroundSubtleHover))
        }
        if (state == ODSActions.PRESSED) {
            style.promptBgBackground =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedBackgroundSubtlePressed))
        }
        style.containerVerticalAlignment = DSAIPromptTokens.containerVerticalAlignment
        style.containerHorizontalAlignment = DSAIPromptTokens.containerHorizontalAlignment
        if (props.type == ODSAIPromptType.TEXT_ONLY) {
            style.containerVerticalArrangement =
                DSAIPromptTokens.containerVerticalArrangementTypeTextOnly
        }
        if (props.type == ODSAIPromptType.TOP_ICON_TEXT) {
            style.containerGap = DSAIPromptTokens.containerGapTypeTopIconText
            style.containerVerticalArrangement =
                DSAIPromptTokens.containerVerticalArrangementTypeTopIconText
        }
        if (props.type == ODSAIPromptType.LEFT_ICON_TEXT) {
            style.containerGap = DSAIPromptTokens.containerGapTypeLeftIconText
            style.containerHorizontalArrangement =
                DSAIPromptTokens.containerHorizontalArrangementTypeLeftIconText
        }
        if (props.type == ODSAIPromptType.TOP_ICON_TEXT) {
            style.iconColor = scheme.basicText
            style.iconWidth = DSAIPromptTokens.iconWidthTypeTopIconText
            style.iconHeight = DSAIPromptTokens.iconHeightTypeTopIconText
        }
        if (props.type == ODSAIPromptType.LEFT_ICON_TEXT) {
            style.iconColor = scheme.basicText
            style.iconWidth = DSAIPromptTokens.iconWidthTypeLeftIconText
            style.iconHeight = DSAIPromptTokens.iconHeightTypeLeftIconText
        }
        if (props.type == ODSAIPromptType.TOP_ICON_TEXT) {
            style.textContainerGap = DSAIPromptTokens.textContainerGapTypeTopIconText
            style.textContainerVerticalAlignment =
                DSAIPromptTokens.textContainerVerticalAlignmentTypeTopIconText
            style.textContainerHorizontalAlignment =
                DSAIPromptTokens.textContainerHorizontalAlignmentTypeTopIconText
            style.textContainerVerticalArrangement =
                DSAIPromptTokens.textContainerVerticalArrangementTypeTopIconText
        }
        if (props.type == ODSAIPromptType.LEFT_ICON_TEXT) {
            style.textContainerGap = DSAIPromptTokens.textContainerGapTypeLeftIconText
            style.textContainerVerticalAlignment =
                DSAIPromptTokens.textContainerVerticalAlignmentTypeLeftIconText
            style.textContainerHorizontalAlignment =
                DSAIPromptTokens.textContainerHorizontalAlignmentTypeLeftIconText
            style.textContainerVerticalArrangement =
                DSAIPromptTokens.textContainerVerticalArrangementTypeLeftIconText
        }
        if (props.type == ODSAIPromptType.TOP_ICON_TEXT) {
            style.titleStyle = DSAIPromptTokens.titleStyleTypeTopIconText
            style.titleColor = scheme.basicText
            style.titleTextAlign = DSAIPromptTokens.titleTextAlignTypeTopIconText
        }
        if (props.type == ODSAIPromptType.LEFT_ICON_TEXT) {
            style.titleStyle = DSAIPromptTokens.titleStyleTypeLeftIconText
            style.titleColor = scheme.basicText
            style.titleTextAlign = DSAIPromptTokens.titleTextAlignTypeLeftIconText
        }
        if (props.type == ODSAIPromptType.TOP_ICON_TEXT) {
            style.descriptionTextStyle = DSAIPromptTokens.descriptionTextStyleTypeTopIconText
            style.descriptionTextColor = scheme.basicText
            style.descriptionTextTextAlign =
                DSAIPromptTokens.descriptionTextTextAlignTypeTopIconText
        }
        if (props.type == ODSAIPromptType.LEFT_ICON_TEXT) {
            style.descriptionTextStyle = DSAIPromptTokens.descriptionTextStyleTypeLeftIconText
            style.descriptionTextColor = scheme.basicText
            style.descriptionTextTextAlign =
                DSAIPromptTokens.descriptionTextTextAlignTypeLeftIconText
        }
        if (props.type == ODSAIPromptType.TEXT_ONLY) {
            style.title2Style = DSAIPromptTokens.title2StyleTypeTextOnly
            style.title2Color = scheme.basicText
            style.title2TextAlign = DSAIPromptTokens.title2TextAlignTypeTextOnly
        }
        if (props.type == ODSAIPromptType.TEXT_ONLY) {
            style.descriptionText2Style = DSAIPromptTokens.descriptionText2StyleTypeTextOnly
            style.descriptionText2Color = scheme.basicText
            style.descriptionText2TextAlign = DSAIPromptTokens.descriptionText2TextAlignTypeTextOnly
        }
        style.rightIconColor = scheme.basicText
        style.rightIconWidth = DSAIPromptTokens.rightIconWidth
        style.rightIconHeight = DSAIPromptTokens.rightIconHeight

        // Custom Addition
        style.scaleFactor = DSAIPromptTokens.scaleFactor
        return style
    }
}
