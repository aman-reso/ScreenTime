package com.telekom.odsystem.atoms.progressbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSProgressBarTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("All")
class ODSProgressBarStyle {
    var gap: Dp? = null
    var minWidth: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var dataGap: Dp? = null
    var dataClipContent: Boolean? = null
    var dataVerticalAlignment: Alignment.Vertical? = null
    var dataHorizontalAlignment: Alignment.Horizontal? = null
    var dataHorizontalArrangement: Arrangement.Horizontal? = null
    var mainDataGap: Dp? = null
    var mainDataVerticalAlignment: Alignment.Vertical? = null
    var mainDataHorizontalAlignment: Alignment.Horizontal? = null
    var mainDataVerticalArrangement: Arrangement.Vertical? = null
    var mainDataTextGap: Dp? = null
    var mainDataTextVerticalAlignment: Alignment.Vertical? = null
    var mainDataTextHorizontalAlignment: Alignment.Horizontal? = null
    var mainDataTextHorizontalArrangement: Arrangement.Horizontal? = null
    var dataProgressTrackContainerVerticalAlignment: Alignment.Vertical? = null
    var dataProgressTrackContainerHorizontalAlignment: Alignment.Horizontal? = null
    var dataProgressTrackContainerVerticalArrangement: Arrangement.Vertical? = null
    var extraDataGap: Dp? = null
    var extraDataWidth: Dp? = null
    var extraDataVerticalAlignment: Alignment.Vertical? = null
    var extraDataHorizontalAlignment: Alignment.Horizontal? = null
    var extraDataVerticalArrangement: Arrangement.Vertical? = null
    var extraDataTextVerticalAlignment: Alignment.Vertical? = null
    var extraDataTextHorizontalAlignment: Alignment.Horizontal? = null
    var extraDataTextHorizontalArrangement: Arrangement.Horizontal? = null
    var extraDataProgressTrackContainerHeight: Dp? = null
    var extraDataProgressTrackContainerVerticalAlignment: Alignment.Vertical? = null
    var extraDataProgressTrackContainerHorizontalAlignment: Alignment.Horizontal? = null
    var extraDataProgressTrackContainerVerticalArrangement: Arrangement.Vertical? = null
    var dataWarningColor: HexColor? = null
    var dataWarningWidth: Dp? = null
    var dataWarningHeight: Dp? = null
    var extraDataWarningColor: HexColor? = null
    var extraDataWarningWidth: Dp? = null
    var extraDataWarningHeight: Dp? = null
    var dataSuccessColor: HexColor? = null
    var dataSuccessWidth: Dp? = null
    var dataSuccessHeight: Dp? = null
    var extraDataSuccessColor: HexColor? = null
    var extraDataSuccessWidth: Dp? = null
    var extraDataSuccessHeight: Dp? = null
    var labelTextTextStyle: ODSTextStyle? = null
    var labelTextColor: HexColor? = null
    var labelTextTextAlign: TextAlign? = null
    var labelTextTextOverflow: TextOverflow? = null
    var counterTextTextStyle: ODSTextStyle? = null
    var counterTextColor: HexColor? = null
    var counterTextTextAlign: TextAlign? = null
    var counterTextTextOverflow: TextOverflow? = null
    var extraDataTextTextStyle: ODSTextStyle? = null
    var extraDataTextColor: HexColor? = null
    var extraDataTextTextAlign: TextAlign? = null
    var extraDataTextTextOverflow: TextOverflow? = null
    var helperTextTextStyle: ODSTextStyle? = null
    var helperTextColor: HexColor? = null
    var helperTextTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSProgressBarProps
    ): ODSProgressBarStyle {
        var style = ODSProgressBarStyle()
        style.gap = DSProgressBarTokens.gap
        style.minWidth = DSProgressBarTokens.minWidth
        style.verticalAlignment = DSProgressBarTokens.verticalAlignment
        style.horizontalAlignment = DSProgressBarTokens.horizontalAlignment
        style.verticalArrangement = DSProgressBarTokens.verticalArrangement
        style.dataGap = DSProgressBarTokens.dataGap
        style.dataClipContent = DSProgressBarTokens.dataClipContent
        style.dataVerticalAlignment = DSProgressBarTokens.dataVerticalAlignment
        style.dataHorizontalAlignment = DSProgressBarTokens.dataHorizontalAlignment
        style.dataHorizontalArrangement = DSProgressBarTokens.dataHorizontalArrangement
        style.mainDataGap = DSProgressBarTokens.mainDataGap
        style.mainDataVerticalAlignment = DSProgressBarTokens.mainDataVerticalAlignment
        style.mainDataHorizontalAlignment = DSProgressBarTokens.mainDataHorizontalAlignment
        style.mainDataVerticalArrangement = DSProgressBarTokens.mainDataVerticalArrangement
        style.mainDataTextGap = DSProgressBarTokens.mainDataTextGap
        style.mainDataTextVerticalAlignment = DSProgressBarTokens.mainDataTextVerticalAlignment
        style.mainDataTextHorizontalAlignment = DSProgressBarTokens.mainDataTextHorizontalAlignment
        style.mainDataTextHorizontalArrangement =
            DSProgressBarTokens.mainDataTextHorizontalArrangement
        style.dataProgressTrackContainerVerticalAlignment =
            DSProgressBarTokens.dataProgressTrackContainerVerticalAlignment
        style.dataProgressTrackContainerHorizontalAlignment =
            DSProgressBarTokens.dataProgressTrackContainerHorizontalAlignment
        style.dataProgressTrackContainerVerticalArrangement =
            DSProgressBarTokens.dataProgressTrackContainerVerticalArrangement
        style.extraDataGap = DSProgressBarTokens.extraDataGap
        style.extraDataWidth = DSProgressBarTokens.extraDataWidth
        style.extraDataVerticalAlignment = DSProgressBarTokens.extraDataVerticalAlignment
        style.extraDataHorizontalAlignment = DSProgressBarTokens.extraDataHorizontalAlignment
        style.extraDataVerticalArrangement = DSProgressBarTokens.extraDataVerticalArrangement
        style.extraDataTextVerticalAlignment = DSProgressBarTokens.extraDataTextVerticalAlignment
        style.extraDataTextHorizontalAlignment =
            DSProgressBarTokens.extraDataTextHorizontalAlignment
        style.extraDataTextHorizontalArrangement =
            DSProgressBarTokens.extraDataTextHorizontalArrangement
        style.extraDataProgressTrackContainerVerticalAlignment =
            DSProgressBarTokens.extraDataProgressTrackContainerVerticalAlignment
        style.extraDataProgressTrackContainerHorizontalAlignment =
            DSProgressBarTokens.extraDataProgressTrackContainerHorizontalAlignment
        style.extraDataProgressTrackContainerVerticalArrangement =
            DSProgressBarTokens.extraDataProgressTrackContainerVerticalArrangement
        if (props.size == ODSProgressBarSize.SMALL) {
            style.extraDataProgressTrackContainerHeight =
                DSProgressBarTokens.extraDataProgressTrackContainerHeightSizeSmall
        }
        if (props.size == ODSProgressBarSize.LARGE) {
            style.extraDataProgressTrackContainerHeight =
                DSProgressBarTokens.extraDataProgressTrackContainerHeightSizeLarge
        }
        if (props.size == ODSProgressBarSize.MEDIUM) {
            style.extraDataProgressTrackContainerHeight =
                DSProgressBarTokens.extraDataProgressTrackContainerHeightSizeMedium
        }
        if (props.mode == ODSProgressBarMode.ERROR && !props.disabled) {
            style.dataWarningColor = scheme.functionalDestructiveStandard
            style.dataWarningWidth = DSProgressBarTokens.dataWarningWidthStatusError
            style.dataWarningHeight = DSProgressBarTokens.dataWarningHeightStatusError
        }
        if (props.mode == ODSProgressBarMode.ERROR && !props.disabled) {
            style.extraDataWarningColor = scheme.functionalDestructiveStandard
            style.extraDataWarningWidth = DSProgressBarTokens.extraDataWarningWidthStatusError
            style.extraDataWarningHeight = DSProgressBarTokens.extraDataWarningHeightStatusError
        }
        if (props.mode == ODSProgressBarMode.SUCCESS && !props.disabled) {
            style.dataSuccessColor = scheme.functionalSuccessStandard
            style.dataSuccessWidth = DSProgressBarTokens.dataSuccessWidthStatusSuccess
            style.dataSuccessHeight = DSProgressBarTokens.dataSuccessHeightStatusSuccess
        }
        if (props.mode == ODSProgressBarMode.SUCCESS && !props.disabled) {
            style.extraDataSuccessColor = scheme.functionalSuccessStandard
            style.extraDataSuccessWidth = DSProgressBarTokens.extraDataSuccessWidthStatusSuccess
            style.extraDataSuccessHeight = DSProgressBarTokens.extraDataSuccessHeightStatusSuccess
        }
        style.labelTextTextStyle = DSProgressBarTokens.labelTextTextStyle
        style.labelTextTextAlign = DSProgressBarTokens.labelTextTextAlign
        style.labelTextTextOverflow = DSProgressBarTokens.labelTextTextOverflow
        if (!props.disabled) {
            style.labelTextColor = scheme.basicText
        }
        if (props.mode == ODSProgressBarMode.STANDARD && props.disabled) {
            style.labelTextColor = scheme.interactionStatesDisabledTextDisabled
        }
        if (props.mode == ODSProgressBarMode.STANDARD) {
            style.counterTextTextStyle = DSProgressBarTokens.counterTextTextStyleStatusStandard
            style.counterTextTextAlign = DSProgressBarTokens.counterTextTextAlignStatusStandard
            style.counterTextTextOverflow =
                DSProgressBarTokens.counterTextTextOverflowStatusStandard
        }
        if (props.mode == ODSProgressBarMode.STANDARD && !props.disabled) {
            style.counterTextColor = scheme.basicTextRecessive
        }
        if (props.mode == ODSProgressBarMode.STANDARD && props.disabled) {
            style.counterTextColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        if (props.mode == ODSProgressBarMode.STANDARD) {
            style.extraDataTextTextStyle = DSProgressBarTokens.extraDataTextTextStyleStatusStandard
            style.extraDataTextTextAlign = DSProgressBarTokens.extraDataTextTextAlignStatusStandard
            style.extraDataTextTextOverflow =
                DSProgressBarTokens.extraDataTextTextOverflowStatusStandard
        }
        if (props.mode == ODSProgressBarMode.STANDARD && !props.disabled) {
            style.extraDataTextColor = scheme.basicTextRecessive
        }
        if (props.mode == ODSProgressBarMode.STANDARD && props.disabled) {
            style.extraDataTextColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        style.helperTextTextStyle = DSProgressBarTokens.helperTextTextStyle
        style.helperTextTextAlign = DSProgressBarTokens.helperTextTextAlign
        if (!props.disabled) {
            style.helperTextColor = scheme.basicTextRecessive
        }
        if (props.mode == ODSProgressBarMode.STANDARD && props.disabled) {
            style.helperTextColor = scheme.interactionStatesDisabledTextRecessiveDisabled
        }
        return style
    }
}
