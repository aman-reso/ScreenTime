package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSListItemTokens(
    val gap: Dp,
    val padding: ODSPadding,
    val minHeight: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val markerWidthPrefixBulletpoint: Dp,
    val markerWidthPrefixOutlineBullet: Dp,
    val markerWidthPrefixIcon: Dp,
    val markerHeightPrefixBulletpoint: Dp,
    val markerHeightPrefixOutlineBullet: Dp,
    val markerHeightPrefixIcon: Dp,
    val markerVerticalAlignment: Alignment.Vertical,
    val markerHorizontalAlignmentPrefixBulletpoint: Alignment.Horizontal,
    val markerHorizontalAlignmentPrefixOutlineBullet: Alignment.Horizontal,
    val markerHorizontalAlignmentPrefixIcon: Alignment.Horizontal,
    val markerHorizontalAlignmentPrefixNumber: Alignment.Horizontal,
    val markerHorizontalArrangementPrefixBulletpoint: Arrangement.Horizontal,
    val markerHorizontalArrangementPrefixOutlineBullet: Arrangement.Horizontal,
    val markerHorizontalArrangementPrefixIcon: Arrangement.Horizontal,
    val markerHorizontalArrangementPrefixNumber: Arrangement.Horizontal,
    val markerMinWidthPrefixNumber: Dp,
    val innerCircleBorderRadiusPrefixBulletpoint: ODSCorners,
    val innerCircleBorderRadiusPrefixOutlineBullet: ODSCorners,
    val innerCircleWidthPrefixBulletpoint: Dp,
    val innerCircleWidthPrefixOutlineBullet: Dp,
    val innerCircleHeightPrefixBulletpoint: Dp,
    val innerCircleHeightPrefixOutlineBullet: Dp,
    val innerCircleBorderPrefixOutlineBullet: Dp,
    val marker2TextStylePrefixNumber: ODSTextStyle,
    val marker2TextAlignPrefixNumber: TextAlign,
    val iconWidthPrefixIcon: Dp,
    val iconHeightPrefixIcon: Dp,
    val labelTextStyle: ODSTextStyle,
    val labelTextStyleLink: ODSTextStyle,
    val labelTextAlign: TextAlign,
    val labelTextDecorationLink: TextDecoration,
    val underlineThickness: Dp // Not exported from the plugin
)

val defaultODSListItemTokens = ODSListItemTokens(
    gap = DSVariables.spacingComponent3,
    padding = ODSPadding(
        top = DSVariables.spacingComponent1,
        bottom = DSVariables.spacingComponent1
    ),
    minHeight = 24.dp,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start,
    markerWidthPrefixBulletpoint = DSVariables.sizingComponent8,
    markerWidthPrefixOutlineBullet = DSVariables.sizingComponent8,
    markerWidthPrefixIcon = DSVariables.sizingComponent8,
    markerHeightPrefixBulletpoint = DSVariables.sizingComponent8,
    markerHeightPrefixOutlineBullet = DSVariables.sizingComponent8,
    markerHeightPrefixIcon = DSVariables.sizingComponent8,
    markerVerticalAlignment = Alignment.CenterVertically,
    markerHorizontalAlignmentPrefixBulletpoint = Alignment.CenterHorizontally,
    markerHorizontalAlignmentPrefixOutlineBullet = Alignment.CenterHorizontally,
    markerHorizontalAlignmentPrefixIcon = Alignment.CenterHorizontally,
    markerHorizontalAlignmentPrefixNumber = Alignment.End,
    markerHorizontalArrangementPrefixBulletpoint = Arrangement.Center,
    markerHorizontalArrangementPrefixOutlineBullet = Arrangement.Center,
    markerHorizontalArrangementPrefixIcon = Arrangement.Center,
    markerHorizontalArrangementPrefixNumber = Arrangement.End,
    markerMinWidthPrefixNumber = 20.dp,
    innerCircleBorderRadiusPrefixBulletpoint = ODSCorners(all = DSVariables.radiusFull),
    innerCircleBorderRadiusPrefixOutlineBullet = ODSCorners(all = DSVariables.radiusFull),
    innerCircleWidthPrefixBulletpoint = 6.dp,
    innerCircleWidthPrefixOutlineBullet = 6.dp,
    innerCircleHeightPrefixBulletpoint = 6.dp,
    innerCircleHeightPrefixOutlineBullet = 6.dp,
    innerCircleBorderPrefixOutlineBullet = DSVariables.strokes1,
    marker2TextStylePrefixNumber = DSTextStyles.bodyMRegular,
    marker2TextAlignPrefixNumber = TextAlign.Right,
    iconWidthPrefixIcon = DSVariables.sizingComponent7,
    iconHeightPrefixIcon = DSVariables.sizingComponent7,
    labelTextStyle = DSTextStyles.bodyMRegular,
    labelTextStyleLink = DSTextStyles.linkMRegular,
    labelTextAlign = TextAlign.Left,
    labelTextDecorationLink = TextDecoration.Underline,
    underlineThickness = 1.dp
)

var DSListItemTokens: ODSListItemTokens = defaultODSListItemTokens
