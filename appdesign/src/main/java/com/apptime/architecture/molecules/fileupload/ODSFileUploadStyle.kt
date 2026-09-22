package com.telekom.odsystem.molecules.fileupload

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

/**
 * Code generated with ODS RADD Code Generator
 * 2025-08-29 (v1.32.3) - uid: 1dc2ba06
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=7904-10955
 */

class ODSFileUploadStyle {
    var background: List<ODSColorModel>? = null
    var padding: ODSPadding? = null
    var cornerRadius: ODSCorners? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var uploadContentGap: Dp? = null
    var uploadContentVerticalAlignment: Alignment.Vertical? = null
    var uploadContentHorizontalAlignment: Alignment.Horizontal? = null
    var uploadContentHorizontalArrangement: Arrangement.Horizontal? = null
    var iconProgressGap: Dp? = null
    var iconProgressVerticalAlignment: Alignment.Vertical? = null
    var iconProgressHorizontalAlignment: Alignment.Horizontal? = null
    var iconProgressHorizontalArrangement: Arrangement.Horizontal? = null
    var textLoadVerticalAlignment: Alignment.Vertical? = null
    var textLoadHorizontalArrangement: Arrangement.Horizontal? = null
    var filenameStyle: ODSTextStyle? = null
    var filenameColor: HexColor? = null
    var filenameTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSFileUploadProps
    ): ODSFileUploadStyle {
        val style = ODSFileUploadStyle()
        style.padding = DSFileUploadTokens.padding
        style.cornerRadius = DSFileUploadTokens.cornerRadius
        style.verticalAlignment = DSFileUploadTokens.verticalAlignment
        style.horizontalAlignment = DSFileUploadTokens.horizontalAlignment
        style.verticalArrangement = DSFileUploadTokens.verticalArrangement
        if (props.subtle) {
            style.background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle))
        }
        if (!props.subtle) {
            style.background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCardSubtle))
        }
        style.uploadContentGap = DSFileUploadTokens.uploadContentGap
        style.uploadContentVerticalAlignment = DSFileUploadTokens.uploadContentVerticalAlignment
        style.uploadContentHorizontalAlignment = DSFileUploadTokens.uploadContentHorizontalAlignment
        style.uploadContentHorizontalArrangement =
            DSFileUploadTokens.uploadContentHorizontalArrangement
        style.iconProgressGap = DSFileUploadTokens.iconProgressGap
        style.iconProgressVerticalAlignment = DSFileUploadTokens.iconProgressVerticalAlignment
        style.iconProgressHorizontalAlignment = DSFileUploadTokens.iconProgressHorizontalAlignment
        style.iconProgressHorizontalArrangement =
            DSFileUploadTokens.iconProgressHorizontalArrangement
        if (props.type == ODSFileUploadType.SIMPLE) {
            style.textLoadVerticalAlignment = DSFileUploadTokens.textLoadVerticalAlignmentTypeSimple
            style.textLoadHorizontalArrangement =
                DSFileUploadTokens.textLoadHorizontalArrangementTypeSimple
        }
        if (props.type == ODSFileUploadType.SIMPLE) {
            style.filenameStyle = DSFileUploadTokens.filenameStyleTypeSimple
            style.filenameColor = scheme.basicText
            style.filenameTextAlign = DSFileUploadTokens.filenameTextAlignTypeSimple
        }
        return style
    }
}
