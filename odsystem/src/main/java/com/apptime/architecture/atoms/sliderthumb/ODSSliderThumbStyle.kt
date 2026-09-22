package com.telekom.odsystem.atoms.sliderthumb

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.componenttokens.DSSliderThumbTokens
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSSliderThumbStyle {
    var gap: Dp? = null
    var width: Dp? = null
    var height: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var thumbBackgroundColor: List<ODSColorModel>? = null
    var thumbBorderRadius: ODSCorners? = null
    var thumbBorder: Dp? = null // Not used in mobile
    var thumbBorderColor: List<ODSColorModel>? = null
    var thumbWidth: Dp? = null
    var thumbHeight: Dp? = null
    var thumbClipContent: Boolean? = null
    var thumbVerticalAlignment: Alignment.Vertical? = null
    var thumbHorizontalAlignment: Alignment.Horizontal? = null
    var thumbVerticalArrangement: Arrangement.Vertical? = null
    var innerThumbWidth: Dp? = null // Not exported by plugin.
    var innerThumbHeight: Dp? = null // Not exported by plugin.
    fun getStyle(
        scheme: ODSTheme,
        state: ODSActions
    ): ODSSliderThumbStyle {
        var style = ODSSliderThumbStyle()
        style.gap = DSSliderThumbTokens.gap
        style.width = DSSliderThumbTokens.width
        style.height = DSSliderThumbTokens.height
        style.verticalAlignment = DSSliderThumbTokens.verticalAlignment
        style.horizontalAlignment = DSSliderThumbTokens.horizontalAlignment
        style.verticalArrangement = DSSliderThumbTokens.verticalArrangement
        style.thumbBackgroundColor =
            listOf(ODSColorModel(hexColor = scheme.shadesSecondaryAccentShadesSecondaryAccentExtraDominant))
        style.thumbBorderRadius = DSSliderThumbTokens.thumbBorderRadius
        style.thumbBorder = DSSliderThumbTokens.thumbBorder
        style.thumbBorderColor = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        style.thumbWidth = DSSliderThumbTokens.thumbWidth
        style.thumbHeight = DSSliderThumbTokens.thumbHeight
        style.thumbClipContent = DSSliderThumbTokens.thumbClipContent
        style.thumbVerticalAlignment = DSSliderThumbTokens.thumbVerticalAlignment
        style.thumbHorizontalAlignment = DSSliderThumbTokens.thumbHorizontalAlignment
        style.thumbVerticalArrangement = DSSliderThumbTokens.thumbVerticalArrangement
        if (state == ODSActions.HOVERED) {
            style.thumbBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.shadesSecondaryAccentShadesSecondaryAccentDominant))
            style.thumbBorder = DSSliderThumbTokens.thumbBorderStateHovered
        }
        if (state == ODSActions.PRESSED) {
            style.thumbBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.shadesSecondaryAccentShadesSecondaryAccentRecessive))
            style.thumbBorder = DSSliderThumbTokens.thumbBorderStatePressed
        }
        style.innerThumbWidth = style.thumbWidth?.minus(style.thumbBorder?.times(2) ?: 0.dp)
            ?: 0.dp // Not exported by plugin.
        style.innerThumbHeight = style.thumbHeight?.minus(style.thumbBorder?.times(2) ?: 0.dp)
            ?: 0.dp // Not exported by plugin.
        return style
    }
}
