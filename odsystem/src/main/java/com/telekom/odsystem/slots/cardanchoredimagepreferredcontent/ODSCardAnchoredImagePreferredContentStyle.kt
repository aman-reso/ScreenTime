package com.telekom.odsystem.slots.cardanchoredimagepreferredcontent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSCardAnchoredImagePreferredContentStyle {
    var padding: ODSPadding? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var gap: Dp? = null
    var dataGap: Dp? = null
    var dataVerticalAlignment: Alignment.Vertical? = null
    var dataHorizontalAlignment: Alignment.Horizontal? = null
    var dataVerticalArrangement: Arrangement.Vertical? = null
    var dataPadding: ODSPadding? = null
    var usageGap: Dp? = null
    var usageVerticalAlignment: Alignment.Vertical? = null
    var usageHorizontalAlignment: Alignment.Horizontal? = null
    var usageVerticalArrangement: Arrangement.Vertical? = null
    var progressLabelTextStyle: ODSTextStyle? = null
    var progressLabelColor: HexColor? = null
    var progressLabelTextAlign: TextAlign? = null
    var progressLabelMaxWidth: Dp? = null
    var progressLabelMinWidth: Dp? = null
    var barsLabelTextStyle: ODSTextStyle? = null
    var barsLabelColor: HexColor? = null
    var barsLabelTextAlign: TextAlign? = null
    var barsLabelMaxWidth: Dp? = null
    var barsLabelMinWidth: Dp? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSCardAnchoredImagePreferredContentProps
    ): ODSCardAnchoredImagePreferredContentStyle {
        var style = ODSCardAnchoredImagePreferredContentStyle()
        style.horizontalAlignment = DSCardAnchoredImagePreferredContentTokens.horizontalAlignment
        style.horizontalArrangement =
            DSCardAnchoredImagePreferredContentTokens.horizontalArrangement
        if (props.content == ODSCardAnchoredImagePreferredContentContent.BARS) {
            style.padding = DSCardAnchoredImagePreferredContentTokens.paddingContentBars
            style.verticalAlignment =
                DSCardAnchoredImagePreferredContentTokens.verticalAlignmentContentBars
        }
        if (props.content == ODSCardAnchoredImagePreferredContentContent.OVERVIEW) {
            style.padding = DSCardAnchoredImagePreferredContentTokens.paddingContentOverview
            style.verticalAlignment =
                DSCardAnchoredImagePreferredContentTokens.verticalAlignmentContentOverview
            style.gap = DSCardAnchoredImagePreferredContentTokens.gapContentOverview
        }
        if (props.content == ODSCardAnchoredImagePreferredContentContent.PROGRESS_BAR) {
            style.padding = DSCardAnchoredImagePreferredContentTokens.paddingContentProgressBar
            style.verticalAlignment =
                DSCardAnchoredImagePreferredContentTokens.verticalAlignmentContentProgressBar
        }
        if (props.content == ODSCardAnchoredImagePreferredContentContent.OVERVIEW) {
            style.dataGap = DSCardAnchoredImagePreferredContentTokens.dataGapContentOverview
            style.dataVerticalAlignment =
                DSCardAnchoredImagePreferredContentTokens.dataVerticalAlignmentContentOverview
            style.dataHorizontalAlignment =
                DSCardAnchoredImagePreferredContentTokens.dataHorizontalAlignmentContentOverview
            style.dataVerticalArrangement =
                DSCardAnchoredImagePreferredContentTokens.dataVerticalArrangementContentOverview
            style.dataPadding = DSCardAnchoredImagePreferredContentTokens.dataPaddingContentOverview
        }
        if (props.content == ODSCardAnchoredImagePreferredContentContent.PROGRESS_BAR) {
            style.dataGap = DSCardAnchoredImagePreferredContentTokens.dataGapContentProgressBar
            style.dataVerticalAlignment =
                DSCardAnchoredImagePreferredContentTokens.dataVerticalAlignmentContentProgressBar
            style.dataHorizontalAlignment =
                DSCardAnchoredImagePreferredContentTokens.dataHorizontalAlignmentContentProgressBar
            style.dataVerticalArrangement =
                DSCardAnchoredImagePreferredContentTokens.dataVerticalArrangementContentProgressBar
        }
        if (props.content == ODSCardAnchoredImagePreferredContentContent.BARS) {
            style.usageGap = DSCardAnchoredImagePreferredContentTokens.usageGapContentBars
            style.usageVerticalAlignment =
                DSCardAnchoredImagePreferredContentTokens.usageVerticalAlignmentContentBars
            style.usageHorizontalAlignment =
                DSCardAnchoredImagePreferredContentTokens.usageHorizontalAlignmentContentBars
            style.usageVerticalArrangement =
                DSCardAnchoredImagePreferredContentTokens.usageVerticalArrangementContentBars
        }
        if (props.content == ODSCardAnchoredImagePreferredContentContent.OVERVIEW) {
            style.usageGap = DSCardAnchoredImagePreferredContentTokens.usageGapContentOverview
            style.usageVerticalAlignment =
                DSCardAnchoredImagePreferredContentTokens.usageVerticalAlignmentContentOverview
            style.usageHorizontalAlignment =
                DSCardAnchoredImagePreferredContentTokens.usageHorizontalAlignmentContentOverview
            style.usageVerticalArrangement =
                DSCardAnchoredImagePreferredContentTokens.usageVerticalArrangementContentOverview
        }
        if (props.content == ODSCardAnchoredImagePreferredContentContent.OVERVIEW) {
            style.progressLabelTextStyle =
                DSCardAnchoredImagePreferredContentTokens.progressLabelTextStyleContentOverview
            style.progressLabelColor = scheme.basicText
            style.progressLabelTextAlign =
                DSCardAnchoredImagePreferredContentTokens.progressLabelTextAlignContentOverview
            style.progressLabelMaxWidth =
                DSCardAnchoredImagePreferredContentTokens.progressLabelMaxWidthContentOverview
            style.progressLabelMinWidth =
                DSCardAnchoredImagePreferredContentTokens.progressLabelMinWidthContentOverview
        }
        if (props.content == ODSCardAnchoredImagePreferredContentContent.BARS) {
            style.barsLabelTextStyle =
                DSCardAnchoredImagePreferredContentTokens.barsLabelTextStyleContentBars
            style.barsLabelColor = scheme.basicText
            style.barsLabelTextAlign =
                DSCardAnchoredImagePreferredContentTokens.barsLabelTextAlignContentBars
            style.barsLabelMinWidth =
                DSCardAnchoredImagePreferredContentTokens.barsLabelMinWidthContentBars
        }
        if (props.content == ODSCardAnchoredImagePreferredContentContent.OVERVIEW) {
            style.barsLabelTextStyle =
                DSCardAnchoredImagePreferredContentTokens.barsLabelTextStyleContentOverview
            style.barsLabelColor = scheme.basicText
            style.barsLabelTextAlign =
                DSCardAnchoredImagePreferredContentTokens.barsLabelTextAlignContentOverview
            style.barsLabelMaxWidth =
                DSCardAnchoredImagePreferredContentTokens.barsLabelMaxWidthContentOverview
            style.barsLabelMinWidth =
                DSCardAnchoredImagePreferredContentTokens.barsLabelMinWidthContentOverview
        }
        return style
    }
}
