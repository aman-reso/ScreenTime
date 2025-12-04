package com.telekom.odsystem.slots.bottomsheetheader

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-04 (v1.32.3) - uid: 5c52583b
 * Figma link: https://figma.com/design/RTdgj2EBwu8TwoaWWVEovL/ODS_OneID_Production_Library?node-id=17924-44
 */

class ODSBottomSheetHeaderStyle {
    var padding: ODSPadding? = null
    var minHeight: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var textContainerPadding: ODSPadding? = null
    var textContainerVerticalAlignment: Alignment.Vertical? = null
    var textContainerHorizontalAlignment: Alignment.Horizontal? = null
    var textContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var textContainerGap: Dp? = null
    var textContainerVerticalArrangement: Arrangement.Vertical? = null
    var titleLabelStyle: ODSTextStyle? = null
    var titleLabelColor: HexColor? = null
    var titleLabelTextAlign: TextAlign? = null
    var subtitleLabelStyle: ODSTextStyle? = null
    var subtitleLabelColor: HexColor? = null
    var subtitleLabelTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSBottomSheetHeaderProps
    ): ODSBottomSheetHeaderStyle {
        val style = ODSBottomSheetHeaderStyle()
        style.padding = DSBottomSheetHeaderTokens.padding
        style.minHeight = DSBottomSheetHeaderTokens.minHeight
        style.verticalAlignment = DSBottomSheetHeaderTokens.verticalAlignment
        style.horizontalAlignment = DSBottomSheetHeaderTokens.horizontalAlignment
        style.horizontalArrangement = DSBottomSheetHeaderTokens.horizontalArrangement
        style.textContainerVerticalAlignment =
            DSBottomSheetHeaderTokens.textContainerVerticalAlignment
        if (props.size == ODSBottomSheetHeaderSize.LARGE) {
            style.textContainerPadding = DSBottomSheetHeaderTokens.textContainerPaddingSizeLarge
            style.textContainerHorizontalAlignment =
                DSBottomSheetHeaderTokens.textContainerHorizontalAlignmentSizeLarge
            style.textContainerHorizontalArrangement =
                DSBottomSheetHeaderTokens.textContainerHorizontalArrangementSizeLarge
        }
        if (props.size == ODSBottomSheetHeaderSize.SMALL) {
            style.textContainerPadding = DSBottomSheetHeaderTokens.textContainerPaddingSizeSmall
            style.textContainerHorizontalAlignment =
                DSBottomSheetHeaderTokens.textContainerHorizontalAlignmentSizeSmall
            style.textContainerGap = DSBottomSheetHeaderTokens.textContainerGapSizeSmall
            style.textContainerVerticalArrangement =
                DSBottomSheetHeaderTokens.textContainerVerticalArrangementSizeSmall
        }
        style.titleLabelColor = scheme.basicText
        style.titleLabelTextAlign = DSBottomSheetHeaderTokens.titleLabelTextAlign
        if (props.size == ODSBottomSheetHeaderSize.LARGE) {
            style.titleLabelStyle = DSBottomSheetHeaderTokens.titleLabelStyleSizeLarge
        }
        if (props.size == ODSBottomSheetHeaderSize.SMALL) {
            style.titleLabelStyle = DSBottomSheetHeaderTokens.titleLabelStyleSizeSmall
        }
        if (props.size == ODSBottomSheetHeaderSize.SMALL) {
            style.subtitleLabelStyle = DSBottomSheetHeaderTokens.subtitleLabelStyleSizeSmall
            style.subtitleLabelColor = scheme.basicText
            style.subtitleLabelTextAlign = DSBottomSheetHeaderTokens.subtitleLabelTextAlignSizeSmall
        }
        return style
    }
}
