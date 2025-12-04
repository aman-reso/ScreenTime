package com.telekom.odsystem.organisms.checkboxgroup

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

class ODSCheckboxGroupStyle {
    var gap: Dp? = null
    var padding: ODSPadding? = null
    var borderRadius: ODSCorners? = null
    var clipContent: Boolean? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var titleGap: Dp? = null
    var titlePadding: ODSPadding? = null
    var titleVerticalAlignment: Alignment.Vertical? = null
    var titleHorizontalAlignment: Alignment.Horizontal? = null
    var titleVerticalArrangement: Arrangement.Vertical? = null
    var supportMessagePadding: ODSPadding? = null
    var supportMessageVerticalAlignment: Alignment.Vertical? = null
    var supportMessageHorizontalAlignment: Alignment.Horizontal? = null
    var supportMessageVerticalArrangement: Arrangement.Vertical? = null
    var listContainerGap: Dp? = null
    var listContainerPadding: ODSPadding? = null
    var listContainerVerticalAlignment: Alignment.Vertical? = null
    var listContainerHorizontalAlignment: Alignment.Horizontal? = null
    var listContainerVerticalArrangement: Arrangement.Vertical? = null
    var secondLevelGap: Dp? = null
    var secondLevelPadding: ODSPadding? = null
    var secondLevelVerticalAlignment: Alignment.Vertical? = null
    var secondLevelHorizontalAlignment: Alignment.Horizontal? = null
    var secondLevelVerticalArrangement: Arrangement.Vertical? = null
    var secondLevelWidth: Dp? = null // Not used in mobile
    var titleTextStyle: ODSTextStyle? = null
    var titleColor: HexColor? = null
    var titleTextAlign: TextAlign? = null
    var titleTextOverflow: TextOverflow? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSCheckboxGroupProps
    ): ODSCheckboxGroupStyle {
        var style = ODSCheckboxGroupStyle()
        style.gap = DSCheckboxGroupTokens.gap
        style.padding = DSCheckboxGroupTokens.padding
        style.borderRadius = DSCheckboxGroupTokens.borderRadius
        style.clipContent = DSCheckboxGroupTokens.clipContent
        style.verticalAlignment = DSCheckboxGroupTokens.verticalAlignment
        style.horizontalAlignment = DSCheckboxGroupTokens.horizontalAlignment
        style.verticalArrangement = DSCheckboxGroupTokens.verticalArrangement
        style.titleGap = DSCheckboxGroupTokens.titleGap
        style.titlePadding = DSCheckboxGroupTokens.titlePadding
        style.titleVerticalAlignment = DSCheckboxGroupTokens.titleVerticalAlignment
        style.titleHorizontalAlignment = DSCheckboxGroupTokens.titleHorizontalAlignment
        style.titleVerticalArrangement = DSCheckboxGroupTokens.titleVerticalArrangement
        style.supportMessagePadding = DSCheckboxGroupTokens.supportMessagePadding
        style.supportMessageVerticalAlignment =
            DSCheckboxGroupTokens.supportMessageVerticalAlignment
        style.supportMessageHorizontalAlignment =
            DSCheckboxGroupTokens.supportMessageHorizontalAlignment
        style.supportMessageVerticalArrangement =
            DSCheckboxGroupTokens.supportMessageVerticalArrangement
        if (props.type == ODSCheckboxGroupType.NESTED) {
            style.listContainerGap = DSCheckboxGroupTokens.listContainerGapTypeNested
            style.listContainerPadding = DSCheckboxGroupTokens.listContainerPaddingTypeNested
            style.listContainerVerticalAlignment =
                DSCheckboxGroupTokens.listContainerVerticalAlignmentTypeNested
            style.listContainerHorizontalAlignment =
                DSCheckboxGroupTokens.listContainerHorizontalAlignmentTypeNested
            style.listContainerVerticalArrangement =
                DSCheckboxGroupTokens.listContainerVerticalArrangementTypeNested
        }
        if (props.type == ODSCheckboxGroupType.NESTED) {
            style.secondLevelGap = DSCheckboxGroupTokens.secondLevelGapTypeNested
            style.secondLevelPadding = DSCheckboxGroupTokens.secondLevelPaddingTypeNested
            style.secondLevelVerticalAlignment =
                DSCheckboxGroupTokens.secondLevelVerticalAlignmentTypeNested
            style.secondLevelHorizontalAlignment =
                DSCheckboxGroupTokens.secondLevelHorizontalAlignmentTypeNested
            style.secondLevelVerticalArrangement =
                DSCheckboxGroupTokens.secondLevelVerticalArrangementTypeNested
        }
        if (props.size == ODSCheckboxGroupSize.SMALL && props.type == ODSCheckboxGroupType.NESTED) {
            style.secondLevelWidth = DSCheckboxGroupTokens.secondLevelWidthSizeSmallTypeNested
        }
        style.titleTextStyle = DSCheckboxGroupTokens.titleTextStyle
        style.titleColor = scheme.basicText
        style.titleTextAlign = DSCheckboxGroupTokens.titleTextAlign
        style.titleTextOverflow = DSCheckboxGroupTokens.titleTextOverflow
        return style
    }
}
