package com.telekom.odsystem.molecules.radiobuttongroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSRadioButtonGroupStyle {
    var gap: Dp? = null
    var padding: ODSPadding? = null
    var borderRadius: ODSCorners? = null
    var minWidth: Dp? = null
    var clipContent: Boolean? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var titleGap: Dp? = null
    var titlePadding: ODSPadding? = null
    var titleVerticalAlignment: Alignment.Vertical? = null
    var titleHorizontalAlignment: Alignment.Horizontal? = null
    var titleVerticalArrangement: Arrangement.Vertical? = null
    var listContainerGap: Dp? = null
    var listContainerVerticalAlignment: Alignment.Vertical? = null
    var listContainerHorizontalAlignment: Alignment.Horizontal? = null
    var listContainerVerticalArrangement: Arrangement.Vertical? = null
    var supportMessagePadding: ODSPadding? = null
    var supportMessageVerticalAlignment: Alignment.Vertical? = null
    var supportMessageHorizontalAlignment: Alignment.Horizontal? = null
    var supportMessageVerticalArrangement: Arrangement.Vertical? = null
    var titleTextStyle: ODSTextStyle? = null
    var titleColor: HexColor? = null
    var titleTextAlign: TextAlign? = null
    var titleTextOverflow: TextOverflow? = null
    fun getStyle(
        scheme: ODSTheme
    ): ODSRadioButtonGroupStyle {
        var style = ODSRadioButtonGroupStyle()
        style.gap = DSRadioButtonGroupTokens.gap
        style.padding = DSRadioButtonGroupTokens.padding
        style.borderRadius = DSRadioButtonGroupTokens.borderRadius
        style.minWidth = DSRadioButtonGroupTokens.minWidth
        style.clipContent = DSRadioButtonGroupTokens.clipContent
        style.verticalAlignment = DSRadioButtonGroupTokens.verticalAlignment
        style.horizontalAlignment = DSRadioButtonGroupTokens.horizontalAlignment
        style.verticalArrangement = DSRadioButtonGroupTokens.verticalArrangement
        style.titleGap = DSRadioButtonGroupTokens.titleGap
        style.titlePadding = DSRadioButtonGroupTokens.titlePadding
        style.titleVerticalAlignment = DSRadioButtonGroupTokens.titleVerticalAlignment
        style.titleHorizontalAlignment = DSRadioButtonGroupTokens.titleHorizontalAlignment
        style.titleVerticalArrangement = DSRadioButtonGroupTokens.titleVerticalArrangement
        style.listContainerGap = DSRadioButtonGroupTokens.listContainerGap
        style.listContainerVerticalAlignment =
            DSRadioButtonGroupTokens.listContainerVerticalAlignment
        style.listContainerHorizontalAlignment =
            DSRadioButtonGroupTokens.listContainerHorizontalAlignment
        style.listContainerVerticalArrangement =
            DSRadioButtonGroupTokens.listContainerVerticalArrangement
        style.supportMessagePadding = DSRadioButtonGroupTokens.supportMessagePadding
        style.supportMessageVerticalAlignment =
            DSRadioButtonGroupTokens.supportMessageVerticalAlignment
        style.supportMessageHorizontalAlignment =
            DSRadioButtonGroupTokens.supportMessageHorizontalAlignment
        style.supportMessageVerticalArrangement =
            DSRadioButtonGroupTokens.supportMessageVerticalArrangement
        style.titleTextStyle = DSRadioButtonGroupTokens.titleTextStyle
        style.titleColor = scheme.basicText
        style.titleTextAlign = DSRadioButtonGroupTokens.titleTextAlign
        style.titleTextOverflow = DSRadioButtonGroupTokens.titleTextOverflow
        return style
    }
}
