package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSOffset
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSFilterChipTokens(
    val zStackMinHeight: Dp,
    val zStackMinWidth: Dp,
    val zStackContentAlignmentExpanded: Alignment,
    val minHeight: Dp,
    val minWidth: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val contentAlignmentExpanded: Alignment,
    val filterChipGap: Dp,
    val filterChipPadding: ODSPadding,
    val filterChipCornerRadius: ODSCorners,
    val filterChipBorder: Dp,
    val filterChipMinHeight: Dp,
    val filterChipMinWidth: Dp,
    val filterChipVerticalAlignment: Alignment.Vertical,
    val filterChipHorizontalAlignment: Alignment.Horizontal,
    val filterChipHorizontalArrangement: Arrangement.Horizontal,
    val filterStyle: ODSTextStyle,
    val filterTextAlign: TextAlign,
    val filterOverflow: TextOverflow,
    val filterOverflowExpandedDisabled: TextOverflow,
    val filterOverflowExpanded: TextOverflow,
    val filterOverflowExpandedStatePressed: TextOverflow,
    val collapseDownWidth: Dp,
    val collapseDownHeight: Dp,
    val collapseUpWidthExpanded: Dp,
    val collapseUpHeightExpanded: Dp,
    val odsChipFilterListContainerAbsoluteContentAlignmentExpanded: Alignment,
    val odsChipFilterListContainerAbsoluteOffsetExpanded: ODSOffset,
    var dropdownBorderRadius: ODSCorners? = null, // Not exported from the plugin
    var dropdownPadding: ODSPadding? = null, // Not exported from the plugin
    var dropdownOffset: DpOffset? = null, // Not exported from the plugin
    var dropdownBorderWidth: Dp? = null, // Not exported from the plugin
)

val defaultODSFilterChipTokens = ODSFilterChipTokens(
    zStackMinHeight = DSVariables.sizingMinimumTappableArea,
    zStackMinWidth = DSVariables.sizingComponent15,
    zStackContentAlignmentExpanded = Alignment.CenterStart,
    minHeight = DSVariables.sizingMinimumTappableArea,
    minWidth = DSVariables.sizingComponent15,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Center,
    contentAlignmentExpanded = Alignment.CenterStart,
    filterChipGap = DSVariables.spacingComponent3,
    filterChipPadding = ODSPadding(
        left = DSVariables.spacingComponent6,
        right = DSVariables.spacingComponent5
    ),
    filterChipCornerRadius = ODSCorners(all = DSVariables.radiusSmall),
    filterChipBorder = DSVariables.strokes1,
    filterChipMinHeight = DSVariables.sizingComponent13,
    filterChipMinWidth = DSVariables.sizingComponent15,
    filterChipVerticalAlignment = Alignment.CenterVertically,
    filterChipHorizontalAlignment = Alignment.CenterHorizontally,
    filterChipHorizontalArrangement = Arrangement.Center,
    filterStyle = DSTextStyles.bodyMBold,
    filterTextAlign = TextAlign.Left,
    filterOverflow = TextOverflow.Ellipsis,
    filterOverflowExpandedDisabled = TextOverflow.Ellipsis,
    filterOverflowExpanded = TextOverflow.Ellipsis,
    filterOverflowExpandedStatePressed = TextOverflow.Ellipsis,
    collapseDownWidth = DSVariables.sizingComponent10,
    collapseDownHeight = DSVariables.sizingComponent10,
    collapseUpWidthExpanded = DSVariables.sizingComponent10,
    collapseUpHeightExpanded = DSVariables.sizingComponent10,
    odsChipFilterListContainerAbsoluteContentAlignmentExpanded = Alignment.TopStart,
    odsChipFilterListContainerAbsoluteOffsetExpanded = ODSOffset(x = 0.dp, y = 52.dp),
    dropdownBorderRadius = ODSCorners(all = DSVariables.radiusMedium),
    dropdownPadding = ODSPadding(
        top = 4.dp,
        bottom = 4.dp,
        left = 12.dp,
        right = 12.dp
    ),
    dropdownOffset = DpOffset(DSVariables.spacingComponent0, DSVariables.spacingComponent2),
    dropdownBorderWidth = 1.dp
)

var DSFilterChipTokens: ODSFilterChipTokens = defaultODSFilterChipTokens
