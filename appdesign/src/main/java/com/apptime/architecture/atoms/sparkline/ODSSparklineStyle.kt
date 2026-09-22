package com.telekom.odsystem.atoms.sparkline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSSparklineStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
//    var backgroundColor: List<ODSColorModel>? = null
    var borderRadius: ODSCorners? = null
    var minWidth: Dp? = null // Not used in Mobile
    var width: Dp? = null
    var height: Dp? = null
    var clipContent: Boolean? = null
    var verticalArrangement: Arrangement.Vertical? = null // Not used in Mobile

    //    var bar1BackgroundColor: List<ODSColorModel>? = null
//    var bar1BorderRadius: ODSCorners? = null
//    var bar1Width: Dp? = null
//    var bar1Height: Dp? = null
//    var bar1ClipContent: Boolean? = null
//    var bar1VerticalAlignment: Alignment.Vertical? = null
//    var bar1HorizontalAlignment: Alignment.Horizontal? = null
//    var bar1HorizontalArrangement: Arrangement.Horizontal? = null
//    var bar1Padding: ODSPadding? = null
//    var bar1VerticalArrangement: Arrangement.Vertical? = null
    var dotIndicatorBackgroundColor: List<ODSColorModel>? = null
    var dotIndicatorBorderRadius: ODSCorners? = null
    var dotIndicatorWidth: Dp? = null
    var dotIndicatorHeight: Dp? = null
    var dotIndicatorClipContent: Boolean? = null

    //    var bar2BackgroundColor: List<ODSColorModel>? = null
//    var bar2BorderRadius: ODSCorners? = null
//    var bar2Width: Dp? = null
//    var bar2Height: Dp? = null
//    var bar2ClipContent: Boolean? = null
//    var bar3BackgroundColor: List<ODSColorModel>? = null
//    var bar3BorderRadius: ODSCorners? = null
//    var bar3Width: Dp? = null
//    var bar3Height: Dp? = null
//    var bar3ClipContent: Boolean? = null
//    var bar4BackgroundColor: List<ODSColorModel>? = null
//    var bar4BorderRadius: ODSCorners? = null
//    var bar4Width: Dp? = null
//    var bar4Height: Dp? = null
//    var bar4ClipContent: Boolean? = null
//    var bar5BackgroundColor: List<ODSColorModel>? = null
//    var bar5BorderRadius: ODSCorners? = null
//    var bar5Width: Dp? = null
//    var bar5Height: Dp? = null
//    var bar5ClipContent: Boolean? = null
//    var bar6BackgroundColor: List<ODSColorModel>? = null
//    var bar6BorderRadius: ODSCorners? = null
//    var bar6Width: Dp? = null
//    var bar6Height: Dp? = null
//    var bar6ClipContent: Boolean? = null
//    var bar7BackgroundColor: List<ODSColorModel>? = null
//    var bar7BorderRadius: ODSCorners? = null
//    var bar7Width: Dp? = null
//    var bar7Height: Dp? = null
//    var bar7ClipContent: Boolean? = null
//    var bar8BackgroundColor: List<ODSColorModel>? = null
//    var bar8BorderRadius: ODSCorners? = null
//    var bar8Width: Dp? = null
//    var bar8Height: Dp? = null
//    var bar8ClipContent: Boolean? = null
//    var bar8VerticalAlignment: Alignment.Vertical? = null
//    var bar8HorizontalAlignment: Alignment.Horizontal? = null
//    var bar8HorizontalArrangement: Arrangement.Horizontal? = null
//    var bar8Padding: ODSPadding? = null
//    var bar8VerticalArrangement: Arrangement.Vertical? = null
    var progressIndicatorBackgroundColor: List<ODSColorModel>? = null

    //    var progressIndicatorWidth: Dp? = null
    var progressIndicatorHeight: Dp? = null
//    var dotIndicator1BackgroundColor: List<ODSColorModel>? = null
//    var dotIndicator1BorderRadius: ODSCorners? = null
//    var dotIndicator1Width: Dp? = null
//    var dotIndicator1Height: Dp? = null
//    var dotIndicator1ClipContent: Boolean? = null

    // Custom Additions
    var barClipContent: Boolean? = null
    var barPadding: ODSPadding? = null
    var barWidth: Dp? = null
    var barHeight: List<Dp>? = null
    var dotHorizontalPadding: ODSPadding? = null
    var dotHorizontalArrangement: Arrangement.Horizontal? = null
    var dataProgressTrackBackgroundColor: List<ODSColorModel>? = null

    fun getStyle(
        scheme: ODSTheme,
        props: ODSSparklineProps
    ): ODSSparklineStyle {
        var style = ODSSparklineStyle()
        style.horizontalAlignment = DSSparklineTokens.horizontalAlignment
        if (props.type == ODSSparklineType.BARS) {
            style.gap = DSSparklineTokens.gapTypeBars
            style.verticalAlignment = DSSparklineTokens.verticalAlignmentTypeBars
            style.horizontalArrangement = DSSparklineTokens.horizontalArrangementTypeBars
        }
        if (props.type == ODSSparklineType.PROGRESS_BAR) {
            style.verticalAlignment = DSSparklineTokens.verticalAlignmentTypeProgressBar
//            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.shadesNeutralShades200))
            style.borderRadius = DSSparklineTokens.borderRadiusTypeProgressBar
            style.height = DSSparklineTokens.heightTypeProgressBar
            style.clipContent = DSSparklineTokens.clipContentTypeProgressBar
            style.verticalArrangement = DSSparklineTokens.verticalArrangementTypeProgressBar
        }
        if (props.type == ODSSparklineType.PROGRESS_BAR && props.size == ODSSparklineSize.SMALL) {
            style.minWidth = DSSparklineTokens.minWidthTypeProgressBarSizeSmall
            style.width = DSSparklineTokens.widthTypeProgressBarSizeSmall
        }
        if (props.type == ODSSparklineType.PROGRESS_BAR && props.size == ODSSparklineSize.LARGE) {
            style.minWidth = DSSparklineTokens.minWidthTypeProgressBarSizeLarge
            style.width = DSSparklineTokens.widthTypeProgressBarSizeLarge
        }
//        if (props.type == ODSSparklineType.BARS) {
//            style.bar1BackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
//            style.bar1BorderRadius = DSSparklineTokens.bar1BorderRadiusTypeBars
//            style.bar1ClipContent = DSSparklineTokens.bar1ClipContentTypeBars
//            style.bar1VerticalAlignment = DSSparklineTokens.bar1VerticalAlignmentTypeBars
//        }
//        if (props.type == ODSSparklineType.BARS && props.percentage == ODSSparklinePercentage .0) {
//            style.bar1BackgroundColor =
//                listOf(ODSColorModel(hexColor = scheme.shadesNeutralShades200))
//        }
//        if (props.type == ODSSparklineType.BARS && props.size == ODSSparklineSize.SMALL) {
//            style.bar1Width = DSSparklineTokens.bar1WidthTypeBarsSizeSmall
//            style.bar1Height = DSSparklineTokens.bar1HeightTypeBarsSizeSmall
//            style.bar1HorizontalAlignment =
//                DSSparklineTokens.bar1HorizontalAlignmentTypeBarsSizeSmall
//            style.bar1HorizontalArrangement =
//                DSSparklineTokens.bar1HorizontalArrangementTypeBarsSizeSmall
//        }
//        if (props.type == ODSSparklineType.BARS && props.size == ODSSparklineSize.LARGE) {
//            style.bar1Width = DSSparklineTokens.bar1WidthTypeBarsSizeLarge
//            style.bar1Height = DSSparklineTokens.bar1HeightTypeBarsSizeLarge
//            style.bar1HorizontalAlignment =
//                DSSparklineTokens.bar1HorizontalAlignmentTypeBarsSizeLarge
//            style.bar1Padding = DSSparklineTokens.bar1PaddingTypeBarsSizeLarge
//            style.bar1VerticalArrangement =
//                DSSparklineTokens.bar1VerticalArrangementTypeBarsSizeLarge
//        }
        style.dotIndicatorBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
        style.dotIndicatorBorderRadius = DSSparklineTokens.dotIndicatorBorderRadius
        style.dotIndicatorWidth = DSSparklineTokens.dotIndicatorWidth
        style.dotIndicatorHeight = DSSparklineTokens.dotIndicatorHeight
        style.dotIndicatorClipContent = DSSparklineTokens.dotIndicatorClipContent
//        if (props.type == ODSSparklineType.BARS) {
//            style.bar2BackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
//            style.bar2BorderRadius = DSSparklineTokens.bar2BorderRadiusTypeBars
//            style.bar2ClipContent = DSSparklineTokens.bar2ClipContentTypeBars
//        }
//        if (props.type == ODSSparklineType.BARS && props.percentage == ODSSparklinePercentage .0) {
//            style.bar2BackgroundColor =
//                listOf(ODSColorModel(hexColor = scheme.shadesNeutralShades200))
//        }
//        if (props.type == ODSSparklineType.BARS && props.size == ODSSparklineSize.SMALL) {
//            style.bar2Width = DSSparklineTokens.bar2WidthTypeBarsSizeSmall
//            style.bar2Height = DSSparklineTokens.bar2HeightTypeBarsSizeSmall
//        }
//        if (props.type == ODSSparklineType.BARS && props.size == ODSSparklineSize.LARGE) {
//            style.bar2Width = DSSparklineTokens.bar2WidthTypeBarsSizeLarge
//            style.bar2Height = DSSparklineTokens.bar2HeightTypeBarsSizeLarge
//        }
//        if (props.type == ODSSparklineType.BARS) {
//            style.bar3BackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
//            style.bar3BorderRadius = DSSparklineTokens.bar3BorderRadiusTypeBars
//            style.bar3ClipContent = DSSparklineTokens.bar3ClipContentTypeBars
//        }
//        if (props.type == ODSSparklineType.BARS && props.percentage == ODSSparklinePercentage .0) {
//            style.bar3BackgroundColor =
//                listOf(ODSColorModel(hexColor = scheme.shadesNeutralShades200))
//        }
//        if (props.type == ODSSparklineType.BARS && props.percentage == ODSSparklinePercentage .25) {
//            style.bar3BackgroundColor =
//                listOf(ODSColorModel(hexColor = scheme.shadesNeutralShades200))
//        }
//        if (props.type == ODSSparklineType.BARS && props.size == ODSSparklineSize.SMALL) {
//            style.bar3Width = DSSparklineTokens.bar3WidthTypeBarsSizeSmall
//            style.bar3Height = DSSparklineTokens.bar3HeightTypeBarsSizeSmall
//        }
//        if (props.type == ODSSparklineType.BARS && props.size == ODSSparklineSize.LARGE) {
//            style.bar3Width = DSSparklineTokens.bar3WidthTypeBarsSizeLarge
//            style.bar3Height = DSSparklineTokens.bar3HeightTypeBarsSizeLarge
//        }
//        if (props.type == ODSSparklineType.BARS) {
//            style.bar4BackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
//            style.bar4BorderRadius = DSSparklineTokens.bar4BorderRadiusTypeBars
//            style.bar4ClipContent = DSSparklineTokens.bar4ClipContentTypeBars
//        }
//        if (props.type == ODSSparklineType.BARS && props.percentage == ODSSparklinePercentage .0) {
//            style.bar4BackgroundColor =
//                listOf(ODSColorModel(hexColor = scheme.shadesNeutralShades200))
//        }
//        if (props.type == ODSSparklineType.BARS && props.percentage == ODSSparklinePercentage .25) {
//            style.bar4BackgroundColor =
//                listOf(ODSColorModel(hexColor = scheme.shadesNeutralShades200))
//        }
//        if (props.type == ODSSparklineType.BARS && props.size == ODSSparklineSize.SMALL) {
//            style.bar4Width = DSSparklineTokens.bar4WidthTypeBarsSizeSmall
//            style.bar4Height = DSSparklineTokens.bar4HeightTypeBarsSizeSmall
//        }
//        if (props.type == ODSSparklineType.BARS && props.size == ODSSparklineSize.LARGE) {
//            style.bar4Width = DSSparklineTokens.bar4WidthTypeBarsSizeLarge
//            style.bar4Height = DSSparklineTokens.bar4HeightTypeBarsSizeLarge
//        }
//        if (props.type == ODSSparklineType.BARS) {
//            style.bar5BackgroundColor =
//                listOf(ODSColorModel(hexColor = scheme.shadesNeutralShades200))
//            style.bar5BorderRadius = DSSparklineTokens.bar5BorderRadiusTypeBars
//            style.bar5ClipContent = DSSparklineTokens.bar5ClipContentTypeBars
//        }
//        if (props.type == ODSSparklineType.BARS && props.percentage == ODSSparklinePercentage .75) {
//            style.bar5BackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
//        }
//        if (props.type == ODSSparklineType.BARS && props.percentage == ODSSparklinePercentage .100) {
//            style.bar5BackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
//        }
//        if (props.type == ODSSparklineType.BARS && props.size == ODSSparklineSize.SMALL) {
//            style.bar5Width = DSSparklineTokens.bar5WidthTypeBarsSizeSmall
//            style.bar5Height = DSSparklineTokens.bar5HeightTypeBarsSizeSmall
//        }
//        if (props.type == ODSSparklineType.BARS && props.size == ODSSparklineSize.LARGE) {
//            style.bar5Width = DSSparklineTokens.bar5WidthTypeBarsSizeLarge
//            style.bar5Height = DSSparklineTokens.bar5HeightTypeBarsSizeLarge
//        }
//        if (props.type == ODSSparklineType.BARS) {
//            style.bar6BackgroundColor =
//                listOf(ODSColorModel(hexColor = scheme.shadesNeutralShades200))
//            style.bar6BorderRadius = DSSparklineTokens.bar6BorderRadiusTypeBars
//            style.bar6ClipContent = DSSparklineTokens.bar6ClipContentTypeBars
//        }
//        if (props.type == ODSSparklineType.BARS && props.percentage == ODSSparklinePercentage .75) {
//            style.bar6BackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
//        }
//        if (props.type == ODSSparklineType.BARS && props.percentage == ODSSparklinePercentage .100) {
//            style.bar6BackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
//        }
//        if (props.type == ODSSparklineType.BARS && props.size == ODSSparklineSize.SMALL) {
//            style.bar6Width = DSSparklineTokens.bar6WidthTypeBarsSizeSmall
//            style.bar6Height = DSSparklineTokens.bar6HeightTypeBarsSizeSmall
//        }
//        if (props.type == ODSSparklineType.BARS && props.size == ODSSparklineSize.LARGE) {
//            style.bar6Width = DSSparklineTokens.bar6WidthTypeBarsSizeLarge
//            style.bar6Height = DSSparklineTokens.bar6HeightTypeBarsSizeLarge
//        }
//        if (props.type == ODSSparklineType.BARS) {
//            style.bar7BackgroundColor =
//                listOf(ODSColorModel(hexColor = scheme.shadesNeutralShades200))
//            style.bar7BorderRadius = DSSparklineTokens.bar7BorderRadiusTypeBars
//            style.bar7ClipContent = DSSparklineTokens.bar7ClipContentTypeBars
//        }
//        if (props.type == ODSSparklineType.BARS && props.percentage == ODSSparklinePercentage .100) {
//            style.bar7BackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
//        }
//        if (props.type == ODSSparklineType.BARS && props.size == ODSSparklineSize.SMALL) {
//            style.bar7Width = DSSparklineTokens.bar7WidthTypeBarsSizeSmall
//            style.bar7Height = DSSparklineTokens.bar7HeightTypeBarsSizeSmall
//        }
//        if (props.type == ODSSparklineType.BARS && props.size == ODSSparklineSize.LARGE) {
//            style.bar7Width = DSSparklineTokens.bar7WidthTypeBarsSizeLarge
//            style.bar7Height = DSSparklineTokens.bar7HeightTypeBarsSizeLarge
//        }
//        if (props.type == ODSSparklineType.BARS) {
//            style.bar8BackgroundColor =
//                listOf(ODSColorModel(hexColor = scheme.shadesNeutralShades200))
//            style.bar8BorderRadius = DSSparklineTokens.bar8BorderRadiusTypeBars
//            style.bar8ClipContent = DSSparklineTokens.bar8ClipContentTypeBars
//            style.bar8VerticalAlignment = DSSparklineTokens.bar8VerticalAlignmentTypeBars
//        }
//        if (props.type == ODSSparklineType.BARS && props.percentage == ODSSparklinePercentage .100) {
//            style.bar8BackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
//        }
//        if (props.type == ODSSparklineType.BARS && props.size == ODSSparklineSize.SMALL) {
//            style.bar8Width = DSSparklineTokens.bar8WidthTypeBarsSizeSmall
//            style.bar8Height = DSSparklineTokens.bar8HeightTypeBarsSizeSmall
//            style.bar8HorizontalAlignment =
//                DSSparklineTokens.bar8HorizontalAlignmentTypeBarsSizeSmall
//            style.bar8HorizontalArrangement =
//                DSSparklineTokens.bar8HorizontalArrangementTypeBarsSizeSmall
//        }
//        if (props.type == ODSSparklineType.BARS && props.size == ODSSparklineSize.LARGE) {
//            style.bar8Width = DSSparklineTokens.bar8WidthTypeBarsSizeLarge
//            style.bar8Height = DSSparklineTokens.bar8HeightTypeBarsSizeLarge
//            style.bar8HorizontalAlignment =
//                DSSparklineTokens.bar8HorizontalAlignmentTypeBarsSizeLarge
//            style.bar8Padding = DSSparklineTokens.bar8PaddingTypeBarsSizeLarge
//            style.bar8VerticalArrangement =
//                DSSparklineTokens.bar8VerticalArrangementTypeBarsSizeLarge
//        }

        if (props.type == ODSSparklineType.PROGRESS_BAR) {
            style.progressIndicatorBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.basicText))
            style.progressIndicatorHeight = DSSparklineTokens.progressIndicatorHeightTypeProgressBar
        }
//        if (props.type == ODSSparklineType.PROGRESS_BAR && props.percentage == ODSSparklinePercentage .0 && props.size == ODSSparklineSize.SMALL) {
//            style.progressIndicatorWidth =
//                DSSparklineTokens.progressIndicatorWidthTypeProgressBarPercentage0SizeSmall
//        }
//        if (props.type == ODSSparklineType.PROGRESS_BAR && props.percentage == ODSSparklinePercentage .25 && props.size == ODSSparklineSize.SMALL) {
//            style.progressIndicatorWidth =
//                DSSparklineTokens.progressIndicatorWidthTypeProgressBarPercentage25SizeSmall
//        }
//        if (props.type == ODSSparklineType.PROGRESS_BAR && props.percentage == ODSSparklinePercentage .50 && props.size == ODSSparklineSize.SMALL) {
//            style.progressIndicatorWidth =
//                DSSparklineTokens.progressIndicatorWidthTypeProgressBarPercentage50SizeSmall
//        }
//        if (props.type == ODSSparklineType.PROGRESS_BAR && props.percentage == ODSSparklinePercentage .75 && props.size == ODSSparklineSize.SMALL) {
//            style.progressIndicatorWidth =
//                DSSparklineTokens.progressIndicatorWidthTypeProgressBarPercentage75SizeSmall
//        }
//        if (props.type == ODSSparklineType.PROGRESS_BAR && props.percentage == ODSSparklinePercentage .100 && props.size == ODSSparklineSize.SMALL) {
//            style.progressIndicatorWidth =
//                DSSparklineTokens.progressIndicatorWidthTypeProgressBarPercentage100SizeSmall
//        }
//        if (props.type == ODSSparklineType.PROGRESS_BAR && props.percentage == ODSSparklinePercentage .0 && props.size == ODSSparklineSize.LARGE) {
//            style.progressIndicatorWidth =
//                DSSparklineTokens.progressIndicatorWidthTypeProgressBarPercentage0SizeLarge
//        }
//        if (props.type == ODSSparklineType.PROGRESS_BAR && props.percentage == ODSSparklinePercentage .25 && props.size == ODSSparklineSize.LARGE) {
//            style.progressIndicatorWidth =
//                DSSparklineTokens.progressIndicatorWidthTypeProgressBarPercentage25SizeLarge
//        }
//        if (props.type == ODSSparklineType.PROGRESS_BAR && props.percentage == ODSSparklinePercentage .50 && props.size == ODSSparklineSize.LARGE) {
//            style.progressIndicatorWidth =
//                DSSparklineTokens.progressIndicatorWidthTypeProgressBarPercentage50SizeLarge
//        }
//        if (props.type == ODSSparklineType.PROGRESS_BAR && props.percentage == ODSSparklinePercentage .75 && props.size == ODSSparklineSize.LARGE) {
//            style.progressIndicatorWidth =
//                DSSparklineTokens.progressIndicatorWidthTypeProgressBarPercentage75SizeLarge
//        }
//        if (props.type == ODSSparklineType.PROGRESS_BAR && props.percentage == ODSSparklinePercentage .100 && props.size == ODSSparklineSize.LARGE) {
//            style.progressIndicatorWidth =
//                DSSparklineTokens.progressIndicatorWidthTypeProgressBarPercentage100SizeLarge
//        }
//        if (props.type == ODSSparklineType.PROGRESS_BAR) {
//            style.dotIndicator1BackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
//            style.dotIndicator1BorderRadius =
//                DSSparklineTokens.dotIndicator1BorderRadiusTypeProgressBar
//            style.dotIndicator1Width = DSSparklineTokens.dotIndicator1WidthTypeProgressBar
//            style.dotIndicator1Height = DSSparklineTokens.dotIndicator1HeightTypeProgressBar
//            style.dotIndicator1ClipContent =
//                DSSparklineTokens.dotIndicator1ClipContentTypeProgressBar
//        }
        // Custom addition
        style.dataProgressTrackBackgroundColor =
            listOf(ODSColorModel(hexColor = scheme.shadesNeutralShades200))
        if (props.type == ODSSparklineType.BARS) {
            style.barClipContent = DSSparklineTokens.bar1ClipContentTypeBars
            style.borderRadius = DSSparklineTokens.bar1BorderRadiusTypeBars
            style.progressIndicatorBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.basicText))

            when (props.size) {
                ODSSparklineSize.SMALL -> {
                    style.barWidth = DSSparklineTokens.barWidthSmall
                    style.barHeight = listOf(
                        DSSparklineTokens.bar1HeightTypeBarsSizeSmall,
                        DSSparklineTokens.bar2HeightTypeBarsSizeSmall,
                        DSSparklineTokens.bar3HeightTypeBarsSizeSmall,
                        DSSparklineTokens.bar4HeightTypeBarsSizeSmall,
                        DSSparklineTokens.bar5HeightTypeBarsSizeSmall,
                        DSSparklineTokens.bar6HeightTypeBarsSizeSmall,
                        DSSparklineTokens.bar7HeightTypeBarsSizeSmall,
                        DSSparklineTokens.bar8HeightTypeBarsSizeSmall
                    )
                }

                ODSSparklineSize.LARGE -> {
                    style.barPadding = DSSparklineTokens.bar1PaddingTypeBarsSizeLarge
                    style.barWidth = DSSparklineTokens.barWidthLarge
                    style.barHeight = listOf(
                        DSSparklineTokens.bar1HeightTypeBarsSizeLarge,
                        DSSparklineTokens.bar2HeightTypeBarsSizeLarge,
                        DSSparklineTokens.bar3HeightTypeBarsSizeLarge,
                        DSSparklineTokens.bar4HeightTypeBarsSizeLarge,
                        DSSparklineTokens.bar5HeightTypeBarsSizeLarge,
                        DSSparklineTokens.bar6HeightTypeBarsSizeLarge,
                        DSSparklineTokens.bar7HeightTypeBarsSizeLarge,
                        DSSparklineTokens.bar8HeightTypeBarsSizeLarge
                    )
                }
            }
        }
        style.dotHorizontalPadding = DSSparklineTokens.dotHorizontalPadding
        style.dotHorizontalArrangement = DSSparklineTokens.dotHorizontalArrangement
        return style
    }
}
