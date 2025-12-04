package com.telekom.odsystem.slots.dialogpreferredcontent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSDialogPreferredContentTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSDialogPreferredContentStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var imageContainerMaxHeight: Dp? = null
    var imageContainerClipContent: Boolean? = null
    var imageContainerVerticalAlignment: Alignment.Vertical? = null
    var imageContainerHorizontalAlignment: Alignment.Horizontal? = null
    var imageContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var imageWidth: Dp? = null // Not used in mobile
    var imageHeight: Dp? = null // Not used in mobile
    var imageObjectFit: ContentScale? = null
    var labelHeadingGap: Dp? = null
    var labelHeadingVerticalAlignment: Alignment.Vertical? = null
    var labelHeadingHorizontalAlignment: Alignment.Horizontal? = null
    var labelHeadingVerticalArrangement: Arrangement.Vertical? = null
    var bodyContentGap: Dp? = null
    var bodyContentVerticalAlignment: Alignment.Vertical? = null
    var bodyContentHorizontalAlignment: Alignment.Horizontal? = null
    var bodyContentVerticalArrangement: Arrangement.Vertical? = null
    var headerTextStyle: ODSTextStyle? = null
    var headerColor: HexColor? = null
    var headerTextAlign: TextAlign? = null
    var subtitleTextStyle: ODSTextStyle? = null
    var subtitleColor: HexColor? = null
    var subtitleTextAlign: TextAlign? = null
    var bodyTextTextStyle: ODSTextStyle? = null
    var bodyTextColor: HexColor? = null
    var bodyTextTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSDialogPreferredContentProps
    ): ODSDialogPreferredContentStyle {
        var style = ODSDialogPreferredContentStyle()
        style.gap = DSDialogPreferredContentTokens.gap
        style.verticalAlignment = DSDialogPreferredContentTokens.verticalAlignment
        style.horizontalAlignment = DSDialogPreferredContentTokens.horizontalAlignment
        style.verticalArrangement = DSDialogPreferredContentTokens.verticalArrangement
        if (props.variant == ODSDialogPreferredContentVariant.GENERAL_WITH_IMAGE) {
            style.imageContainerMaxHeight =
                DSDialogPreferredContentTokens.imageContainerMaxHeightTypeGeneralWithImage
            style.imageContainerClipContent =
                DSDialogPreferredContentTokens.imageContainerClipContentTypeGeneralWithImage
            style.imageContainerVerticalAlignment =
                DSDialogPreferredContentTokens.imageContainerVerticalAlignmentTypeGeneralWithImage
            style.imageContainerHorizontalAlignment =
                DSDialogPreferredContentTokens.imageContainerHorizontalAlignmentTypeGeneralWithImage
            style.imageContainerHorizontalArrangement =
                DSDialogPreferredContentTokens.imageContainerHorizontalArrangementTypeGeneralWithImage
        }
        if (props.variant == ODSDialogPreferredContentVariant.GENERAL_WITH_IMAGE) {
            style.imageWidth = DSDialogPreferredContentTokens.imageWidthTypeGeneralWithImage
            style.imageHeight = DSDialogPreferredContentTokens.imageHeightTypeGeneralWithImage
            style.imageObjectFit = DSDialogPreferredContentTokens.imageObjectFitTypeGeneralWithImage
        }
        if (props.variant == ODSDialogPreferredContentVariant.GENERAL_WITH_IMAGE) {
            style.labelHeadingGap =
                DSDialogPreferredContentTokens.labelHeadingGapTypeGeneralWithImage
            style.labelHeadingVerticalAlignment =
                DSDialogPreferredContentTokens.labelHeadingVerticalAlignmentTypeGeneralWithImage
            style.labelHeadingHorizontalAlignment =
                DSDialogPreferredContentTokens.labelHeadingHorizontalAlignmentTypeGeneralWithImage
            style.labelHeadingVerticalArrangement =
                DSDialogPreferredContentTokens.labelHeadingVerticalArrangementTypeGeneralWithImage
        }
        style.bodyContentGap = DSDialogPreferredContentTokens.bodyContentGap
        style.bodyContentVerticalAlignment =
            DSDialogPreferredContentTokens.bodyContentVerticalAlignment
        style.bodyContentHorizontalAlignment =
            DSDialogPreferredContentTokens.bodyContentHorizontalAlignment
        style.bodyContentVerticalArrangement =
            DSDialogPreferredContentTokens.bodyContentVerticalArrangement
        if (props.variant == ODSDialogPreferredContentVariant.GENERAL_WITH_IMAGE) {
            style.headerTextStyle =
                DSDialogPreferredContentTokens.headerTextStyleTypeGeneralWithImage
            style.headerColor = scheme.basicText
            style.headerTextAlign =
                DSDialogPreferredContentTokens.headerTextAlignTypeGeneralWithImage
        }
        if (props.variant == ODSDialogPreferredContentVariant.GENERAL_WITH_IMAGE) {
            style.subtitleTextStyle =
                DSDialogPreferredContentTokens.subtitleTextStyleTypeGeneralWithImage
            style.subtitleColor = scheme.basicText
            style.subtitleTextAlign =
                DSDialogPreferredContentTokens.subtitleTextAlignTypeGeneralWithImage
        }
        style.bodyTextTextStyle = DSDialogPreferredContentTokens.bodyTextTextStyle
        style.bodyTextColor = scheme.basicText
        style.bodyTextTextAlign = DSDialogPreferredContentTokens.bodyTextTextAlign
        return style
    }
}
