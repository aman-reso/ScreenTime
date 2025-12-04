package com.telekom.odsystem.atoms.timersegment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSTimerSegmentTokens
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSTimerSegmentStyle {
    var backgroundColor: List<ODSColorModel>? = null
    var borderRadius: ODSCorners? = null
    var clipContent: Boolean? = null // Not used in mobile
    var verticalAlignment: Alignment.Vertical? = null // Not used in mobile
    var horizontalAlignment: Alignment.Horizontal? = null // Not used in mobile
    var horizontalArrangement: Arrangement.Horizontal? = null // Not used in mobile
    var contentAlignment: Alignment? = null // Not used in mobile
    var progressBackgroundColor: List<ODSColorModel>? = null
    var progressBorderRadius: ODSCorners? = null // Not used in mobile
    var progressHeight: Dp? = null
    var progressClipContent: Boolean? = null // Not used in mobile
    var progressContentAlignment: Alignment? = null // Not used in mobile
    var indicatorBackgroundColor: List<ODSColorModel>? = null
    var indicatorBorderRadius: ODSCorners? = null // Not used in mobile
    var indicatorWidth: Dp? = null
    var indicatorHeight: Dp? = null // Not used in mobile
    var indicatorClipContent: Boolean? = null // Not used in mobile
    fun getStyle(
        scheme: ODSTheme
    ): ODSTimerSegmentStyle {
        val style = ODSTimerSegmentStyle()
        style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        style.borderRadius = DSTimerSegmentTokens.borderRadius
        style.clipContent = DSTimerSegmentTokens.clipContent
        style.verticalAlignment = DSTimerSegmentTokens.verticalAlignment
        style.horizontalAlignment = DSTimerSegmentTokens.horizontalAlignment
        style.horizontalArrangement = DSTimerSegmentTokens.horizontalArrangement
        style.contentAlignment = DSTimerSegmentTokens.contentAlignment
        style.progressBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
        style.progressBorderRadius = DSTimerSegmentTokens.progressBorderRadius
        style.progressHeight = DSTimerSegmentTokens.progressHeight
        style.progressClipContent = DSTimerSegmentTokens.progressClipContent
        style.progressContentAlignment = DSTimerSegmentTokens.progressContentAlignment
        style.indicatorBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
        style.indicatorBorderRadius = DSTimerSegmentTokens.indicatorBorderRadius
        style.indicatorWidth = DSTimerSegmentTokens.indicatorWidth
        style.indicatorHeight = DSTimerSegmentTokens.indicatorHeight
        style.indicatorClipContent = DSTimerSegmentTokens.indicatorClipContent
        return style
    }
}
