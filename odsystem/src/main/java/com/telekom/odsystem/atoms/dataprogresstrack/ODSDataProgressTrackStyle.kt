package com.telekom.odsystem.atoms.dataprogresstrack

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSDataProgressTrackTokens
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSDataProgressTrackStyle {
    var backgroundColor: List<ODSColorModel>? = null
    var padding: ODSPadding? = null
    var borderRadius: ODSCorners? = null
    var height: Dp? = null
    var clipContent: Boolean? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var indicatorEndBackgroundColor: List<ODSColorModel>? = null
    var indicatorEndBorderRadius: ODSCorners? = null
    var indicatorEndWidth: Dp? = null
    var indicatorEndHeight: Dp? = null
    var indicatorEndClipContent: Boolean? = null
    var indicatorStartBackgroundColor: List<ODSColorModel>? = null
    var indicatorStartBorderRadius: ODSCorners? = null
    var indicatorStartWidth: Dp? = null
    var indicatorStartHeight: Dp? = null
    var indicatorStartClipContent: Boolean? = null
    var progressBackgroundColor: List<ODSColorModel>? = null
    var progressWidth: Dp? = null // Not used in mobile
    var progressHeight: Dp? = null

    // Custom Addition
    var dotHorizontalPadding: ODSPadding? = null
    var dotHorizontalArrangement: Arrangement.Horizontal? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSDataProgressTrackProps
    ): ODSDataProgressTrackStyle {
        var style = ODSDataProgressTrackStyle()
        style.borderRadius = DSDataProgressTrackTokens.borderRadius
        style.clipContent = DSDataProgressTrackTokens.clipContent
        style.verticalAlignment = DSDataProgressTrackTokens.verticalAlignment
        style.horizontalArrangement = DSDataProgressTrackTokens.horizontalArrangement
        if (props.size == ODSDataProgressTrackSize.LARGE) {
            style.padding = DSDataProgressTrackTokens.paddingSizeLarge
            style.height = DSDataProgressTrackTokens.heightSizeLarge
        }
        if (props.size == ODSDataProgressTrackSize.SMALL) {
            style.padding = DSDataProgressTrackTokens.paddingSizeSmall
            style.height = DSDataProgressTrackTokens.heightSizeSmall
        }
        if (props.size == ODSDataProgressTrackSize.MEDIUM) {
            style.padding = DSDataProgressTrackTokens.paddingSizeMedium
            style.height = DSDataProgressTrackTokens.heightSizeMedium
        }
        if (props.mode == ODSDataProgressTrackMode.SUCCESS) {
            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
            // Custom Addition
            style.progressBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.functionalSuccessStandard))
            style.indicatorEndBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.functionalSuccessStandard))
            style.indicatorStartBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.functionalSuccessStandard))
        }
        if (props.mode == ODSDataProgressTrackMode.STANDARD) {
            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        }
//        if (props.type == ODSDataProgressTrackMode.ERROR && props.progress == ODSDataProgressTrackProgress.0) {
//            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveSubtle))
//        }
//        if (props.type == ODSDataProgressTrackMode.ERROR && props.progress == ODSDataProgressTrackProgress.25) {
//            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveSubtle))
//        }
//        if (props.type == ODSDataProgressTrackMode.ERROR && props.progress == ODSDataProgressTrackProgress.50) {
//            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveSubtle))
//        }
//        if (props.type == ODSDataProgressTrackMode.ERROR && props.progress == ODSDataProgressTrackProgress.75) {
//            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveSubtle))
//        }
//        if (props.type == ODSDataProgressTrackMode.DISABLED && props.progress == ODSDataProgressTrackProgress.0) {
//            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundSubtleDisabled))
//        }
        style.indicatorEndBorderRadius = DSDataProgressTrackTokens.indicatorEndBorderRadius
        style.indicatorEndWidth = DSDataProgressTrackTokens.indicatorEndWidth
        style.indicatorEndHeight = DSDataProgressTrackTokens.indicatorEndHeight
        style.indicatorEndClipContent = DSDataProgressTrackTokens.indicatorEndClipContent
        if (props.mode == ODSDataProgressTrackMode.STANDARD) {
            style.indicatorEndBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
        }
//        if (props.type == ODSDataProgressTrackMode.ERROR && props.progress == ODSDataProgressTrackProgress.0) {
//            style.indicatorEndBackgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
//        }
//        if (props.type == ODSDataProgressTrackMode.ERROR && props.progress == ODSDataProgressTrackProgress.25) {
//            style.indicatorEndBackgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
//        }
//        if (props.type == ODSDataProgressTrackMode.ERROR && props.progress == ODSDataProgressTrackProgress.50) {
//            style.indicatorEndBackgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
//        }
//        if (props.type == ODSDataProgressTrackMode.ERROR && props.progress == ODSDataProgressTrackProgress.75) {
//            style.indicatorEndBackgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
//        }
//        if (props.type == ODSDataProgressTrackMode.SUCCESS && props.progress == ODSDataProgressTrackProgress.100) {
//            style.indicatorEndBackgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalSuccessStandard))
//        }
//        if (props.type == ODSDataProgressTrackMode.DISABLED && props.progress == ODSDataProgressTrackProgress.0) {
//            style.indicatorEndBackgroundColor = listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledTextDisabled))
//        }
        style.indicatorStartBorderRadius = DSDataProgressTrackTokens.indicatorStartBorderRadius
        style.indicatorStartWidth = DSDataProgressTrackTokens.indicatorStartWidth
        style.indicatorStartHeight = DSDataProgressTrackTokens.indicatorStartHeight
        style.indicatorStartClipContent = DSDataProgressTrackTokens.indicatorStartClipContent
        if (props.mode == ODSDataProgressTrackMode.STANDARD) {
            style.indicatorStartBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
        }
//        if (props.type == ODSDataProgressTrackMode.ERROR && props.progress == ODSDataProgressTrackProgress.0) {
//            style.indicatorStartBackgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
//        }
//        if (props.type == ODSDataProgressTrackMode.ERROR && props.progress == ODSDataProgressTrackProgress.25) {
//            style.indicatorStartBackgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
//        }
//        if (props.type == ODSDataProgressTrackMode.ERROR && props.progress == ODSDataProgressTrackProgress.50) {
//            style.indicatorStartBackgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
//        }
//        if (props.type == ODSDataProgressTrackMode.ERROR && props.progress == ODSDataProgressTrackProgress.75) {
//            style.indicatorStartBackgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
//        }
//        if (props.type == ODSDataProgressTrackMode.SUCCESS && props.progress == ODSDataProgressTrackProgress.100) {
//            style.indicatorStartBackgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalSuccessStandard))
//        }
//        if (props.type == ODSDataProgressTrackMode.DISABLED && props.progress == ODSDataProgressTrackProgress.0) {
//            style.indicatorStartBackgroundColor = listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledTextDisabled))
//        }
        if (props.size == ODSDataProgressTrackSize.LARGE) {
            style.progressHeight = DSDataProgressTrackTokens.progressHeightSizeLarge
        }
        if (props.size == ODSDataProgressTrackSize.SMALL) {
            style.progressHeight = DSDataProgressTrackTokens.progressHeightSizeSmall
        }
        if (props.size == ODSDataProgressTrackSize.MEDIUM) {
            style.progressHeight = DSDataProgressTrackTokens.progressHeightSizeMedium
        }
        if (props.mode == ODSDataProgressTrackMode.STANDARD) {
            style.progressBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicText))
        }
//        if (props.type == ODSDataProgressTrackMode.ERROR && props.progress == ODSDataProgressTrackProgress.0) {
//            style.progressBackgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
//            style.progressWidth = DSDataProgressTrackTokens.progressWidthTypeErrorProgress0
//        }
//        if (props.type == ODSDataProgressTrackMode.ERROR && props.progress == ODSDataProgressTrackProgress.25) {
//            style.progressBackgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
//            style.progressWidth = DSDataProgressTrackTokens.progressWidthTypeErrorProgress25
//        }
//        if (props.type == ODSDataProgressTrackMode.ERROR && props.progress == ODSDataProgressTrackProgress.50) {
//            style.progressBackgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
//        }
//        if (props.type == ODSDataProgressTrackMode.ERROR && props.progress == ODSDataProgressTrackProgress.75) {
//            style.progressBackgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
//            style.progressWidth = DSDataProgressTrackTokens.progressWidthTypeErrorProgress75
//        }
//        if (props.type == ODSDataProgressTrackMode.SUCCESS && props.progress == ODSDataProgressTrackProgress.100) {
//            style.progressBackgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalSuccessStandard))
//            style.progressWidth = DSDataProgressTrackTokens.progressWidthTypeSuccessProgress100
//        }
//        if (props.type == ODSDataProgressTrackMode.DISABLED && props.progress == ODSDataProgressTrackProgress.0) {
//            style.progressBackgroundColor = listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundSubtleDisabled))
//            style.progressWidth = DSDataProgressTrackTokens.progressWidthTypeDisabledProgress0
//        }
//        if (props.type == ODSDataProgressTrackMode.STANDARD && props.progress == ODSDataProgressTrackProgress.25) {
//            style.progressWidth = DSDataProgressTrackTokens.progressWidthTypeStandardProgress25
//        }
//        if (props.type == ODSDataProgressTrackMode.STANDARD && props.progress == ODSDataProgressTrackProgress.50) {
//            style.progressWidth = DSDataProgressTrackTokens.progressWidthTypeStandardProgress50
//        }
//        if (props.type == ODSDataProgressTrackMode.STANDARD && props.progress == ODSDataProgressTrackProgress.75) {
//            style.progressWidth = DSDataProgressTrackTokens.progressWidthTypeStandardProgress75
//        }
//        if (props.type == ODSDataProgressTrackMode.STANDARD && props.progress == ODSDataProgressTrackProgress.100) {
//            style.progressWidth = DSDataProgressTrackTokens.progressWidthTypeStandardProgress100
//        }
//        if (props.type == ODSDataProgressTrackMode.STANDARD && props.progress == ODSDataProgressTrackProgress.0 && props.size == ODSDataProgressTrackSize.LARGE) {
//            style.progressWidth = DSDataProgressTrackTokens.progressWidthTypeStandardProgress0SizeLarge
//        }
//        if (props.type == ODSDataProgressTrackMode.ERROR && props.progress == ODSDataProgressTrackProgress.50 && props.size == ODSDataProgressTrackSize.LARGE) {
//            style.progressWidth = DSDataProgressTrackTokens.progressWidthTypeErrorProgress50SizeLarge
//        }
//        if (props.type == ODSDataProgressTrackMode.ERROR && props.progress == ODSDataProgressTrackProgress.50 && props.size == ODSDataProgressTrackSize.SMALL) {
//            style.progressWidth = DSDataProgressTrackTokens.progressWidthTypeErrorProgress50SizeSmall
//        }
//        if (props.type == ODSDataProgressTrackMode.STANDARD && props.progress == ODSDataProgressTrackProgress.0 && props.size == ODSDataProgressTrackSize.MEDIUM) {
//            style.progressWidth = DSDataProgressTrackTokens.progressWidthTypeStandardProgress0SizeMedium
//        }
//        if (props.type == ODSDataProgressTrackMode.STANDARD && props.progress == ODSDataProgressTrackProgress.0 && props.size == ODSDataProgressTrackSize.SMALL) {
//            style.progressWidth = DSDataProgressTrackTokens.progressWidthTypeStandardProgress0SizeSmall
//        }
//        if (props.type == ODSDataProgressTrackMode.ERROR && props.progress == ODSDataProgressTrackProgress.50 && props.size == ODSDataProgressTrackSize.MEDIUM) {
//            style.progressWidth = DSDataProgressTrackTokens.progressWidthTypeErrorProgress50SizeMedium
//        }

        // Custom Addition
        if (props.mode == ODSDataProgressTrackMode.ERROR) {
            style.backgroundColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
            style.progressBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
            style.indicatorEndBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
            style.indicatorStartBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
            style.backgroundColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveSubtle))
        }

        if (props.mode == ODSDataProgressTrackMode.DISABLED) {
            style.backgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledStrokeSubtleDisabled))
            style.progressBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundSubtleDisabled))
            style.indicatorEndBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledTextDisabled))
            style.indicatorStartBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledTextDisabled))
        }

        if (props.size == ODSDataProgressTrackSize.LARGE) {
            style.dotHorizontalPadding = DSDataProgressTrackTokens.dotHorizontalPaddingLarge
            style.dotHorizontalArrangement = DSDataProgressTrackTokens.dotHorizontalArrangementLarge
        }

        if (props.size == ODSDataProgressTrackSize.SMALL) {
            style.dotHorizontalPadding = DSDataProgressTrackTokens.dotHorizontalPaddingSmall
            style.dotHorizontalArrangement = DSDataProgressTrackTokens.dotHorizontalArrangementSmall
        }

        if (props.size == ODSDataProgressTrackSize.MEDIUM) {
            style.dotHorizontalPadding = DSDataProgressTrackTokens.dotHorizontalPaddingMedium
            style.dotHorizontalArrangement =
                DSDataProgressTrackTokens.dotHorizontalArrangementMedium
        }
        return style
    }
}
