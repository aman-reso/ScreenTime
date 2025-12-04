package com.telekom.odsystem.organisms.slider

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSSliderTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSSliderStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var sliderContainerGap: Dp? = null
    var sliderContainerVerticalAlignment: Alignment.Vertical? = null
    var sliderContainerHorizontalAlignment: Alignment.Horizontal? = null
    var sliderContainerVerticalArrangement: Arrangement.Vertical? = null
    var trackLabelsGap: Dp? = null
    var trackLabelsVerticalAlignment: Alignment.Vertical? = null
    var trackLabelsHorizontalAlignment: Alignment.Horizontal? = null
    var trackLabelsVerticalArrangement: Arrangement.Vertical? = null
    var trackContainerBackgroundColor: List<ODSColorModel>? = null
    var trackContainerPadding: ODSPadding? = null
    var trackContainerBorderRadius: ODSCorners? = null
    var trackContainerHeight: Dp? = null
    var trackContainerVerticalAlignment: Alignment.Vertical? = null
    var trackContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var indicatorBackgroundColor: List<ODSColorModel>? = null
    var indicatorBorderRadius: ODSCorners? = null
    var indicatorWidth: Dp? = null
    var indicatorHeight: Dp? = null
    var indicatorClipContent: Boolean? = null
    var indicator1BackgroundColor: List<ODSColorModel>? = null
    var indicator1BorderRadius: ODSCorners? = null
    var indicator1Width: Dp? = null
    var indicator1Height: Dp? = null
    var indicator1ClipContent: Boolean? = null
    var progressBackgroundColor: List<ODSColorModel>? = null
    var progressBorderRadius: ODSCorners? = null
    var progressWidth: Dp? = null
    var progressClipContent: Boolean? = null
    var progressVerticalAlignment: Alignment.Vertical? = null
    var progressHorizontalAlignment: Alignment.Horizontal? = null
    var progressHorizontalArrangement: Arrangement.Horizontal? = null
    var trackLabelContainerVerticalAlignment: Alignment.Vertical? = null
    var trackLabelContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var minLabelTextStyle: ODSTextStyle? = null
    var minLabelColor: HexColor? = null
    var minLabelTextAlign: TextAlign? = null
    var maxLabelTextStyle: ODSTextStyle? = null
    var maxLabelColor: HexColor? = null
    var maxLabelTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSSliderProps
    ): ODSSliderStyle {
        var style = ODSSliderStyle()
        style.gap = DSSliderTokens.gap
        style.verticalAlignment = DSSliderTokens.verticalAlignment
        style.horizontalAlignment = DSSliderTokens.horizontalAlignment
        style.verticalArrangement = DSSliderTokens.verticalArrangement
        style.sliderContainerGap = DSSliderTokens.sliderContainerGap
        style.sliderContainerVerticalAlignment = DSSliderTokens.sliderContainerVerticalAlignment
        style.sliderContainerHorizontalAlignment = DSSliderTokens.sliderContainerHorizontalAlignment
        style.sliderContainerVerticalArrangement = DSSliderTokens.sliderContainerVerticalArrangement
        style.trackLabelsGap = DSSliderTokens.trackLabelsGap
        style.trackLabelsVerticalAlignment = DSSliderTokens.trackLabelsVerticalAlignment
        style.trackLabelsHorizontalAlignment = DSSliderTokens.trackLabelsHorizontalAlignment
        style.trackLabelsVerticalArrangement = DSSliderTokens.trackLabelsVerticalArrangement
        style.trackContainerBackgroundColor =
            listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        style.trackContainerPadding = DSSliderTokens.trackContainerPadding
        style.trackContainerBorderRadius = DSSliderTokens.trackContainerBorderRadius
        style.trackContainerHeight = DSSliderTokens.trackContainerHeight
        style.trackContainerVerticalAlignment = DSSliderTokens.trackContainerVerticalAlignment
        style.trackContainerHorizontalArrangement =
            DSSliderTokens.trackContainerHorizontalArrangement
        style.indicatorBackgroundColor =
            listOf(ODSColorModel(hexColor = scheme.shadesSecondaryAccentShadesSecondaryAccentExtraDominant))
        style.indicatorBorderRadius = DSSliderTokens.indicatorBorderRadius
        style.indicatorWidth = DSSliderTokens.indicatorWidth
        style.indicatorHeight = DSSliderTokens.indicatorHeight
        style.indicatorClipContent = DSSliderTokens.indicatorClipContent
        style.indicator1BackgroundColor =
            listOf(ODSColorModel(hexColor = scheme.shadesSecondaryAccentShadesSecondaryAccentExtraDominant))
        style.indicator1BorderRadius = DSSliderTokens.indicator1BorderRadius
        style.indicator1Width = DSSliderTokens.indicator1Width
        style.indicator1Height = DSSliderTokens.indicator1Height
        style.indicator1ClipContent = DSSliderTokens.indicator1ClipContent
        style.progressBackgroundColor =
            listOf(ODSColorModel(hexColor = scheme.shadesSecondaryAccentShadesSecondaryAccentExtraDominant))
        style.progressBorderRadius = DSSliderTokens.progressBorderRadius
        style.progressClipContent = DSSliderTokens.progressClipContent
        style.progressVerticalAlignment = DSSliderTokens.progressVerticalAlignment
        style.progressHorizontalAlignment = DSSliderTokens.progressHorizontalAlignment
        style.progressHorizontalArrangement = DSSliderTokens.progressHorizontalArrangement
//        if (props.range == ODSSliderRange.MAX) {
//            style.progressWidth = DSSliderTokens.progressWidthRangeMax
//        }
//        if (!props.twoThumbs && props.range == ODSSliderRange.SELECTED) {
//            style.progressWidth = DSSliderTokens.progressWidthRangeSelected
//        }
//        if (props.twoThumbs && props.range == ODSSliderRange.SELECTED) {
//            style.progressWidth = DSSliderTokens.progressWidthTwoThumbsRangeSelected
//        }
        style.trackLabelContainerVerticalAlignment =
            DSSliderTokens.trackLabelContainerVerticalAlignment
        style.trackLabelContainerHorizontalArrangement =
            DSSliderTokens.trackLabelContainerHorizontalArrangement
        style.minLabelTextStyle = DSSliderTokens.minLabelTextStyle
        style.minLabelColor = scheme.basicTextRecessive
        style.minLabelTextAlign = DSSliderTokens.minLabelTextAlign
        style.maxLabelTextStyle = DSSliderTokens.maxLabelTextStyle
        style.maxLabelColor = scheme.basicTextRecessive
        style.maxLabelTextAlign = DSSliderTokens.maxLabelTextAlign
        return style
    }
}
