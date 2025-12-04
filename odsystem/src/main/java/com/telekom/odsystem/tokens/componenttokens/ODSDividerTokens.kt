package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSPadding

/**
 * Created by dmarinopoulos on 3/4/24
 */

data class ODSDividerTokens(
    var verticalAlignmentTypeVertical: Alignment.Vertical,
    var verticalAlignmentTypeHorizontal: Alignment.Vertical,
    var horizontalAlignmentTypeVertical: Alignment.Horizontal,
    var horizontalAlignmentTypeHorizontal: Alignment.Horizontal,
    var horizontalArrangementTypeVertical: Arrangement.Horizontal,
    var paddingTypeVerticalInset: ODSPadding,
    var paddingTypeVerticalSpacing: ODSPadding,
    var paddingTypeVerticalInsetSpacing: ODSPadding,
    var paddingTypeHorizontalInset: ODSPadding,
    var paddingTypeHorizontalSpacing: ODSPadding,
    var paddingTypeHorizontalInsetSpacing: ODSPadding,
    var verticalArrangementTypeHorizontal: Arrangement.Vertical,
    var thickness: Dp, // Not exported from the plugin
)

var defaultODSDividerTokens = ODSDividerTokens(
    verticalAlignmentTypeVertical = Alignment.CenterVertically,
    verticalAlignmentTypeHorizontal = Alignment.Top,
    horizontalAlignmentTypeVertical = Alignment.CenterHorizontally,
    horizontalAlignmentTypeHorizontal = Alignment.Start,
    horizontalArrangementTypeVertical = Arrangement.Center,
    paddingTypeVerticalInset = ODSPadding(
        top = DSVariables.spacingComponent5,
        bottom = DSVariables.spacingComponent5
    ),
    paddingTypeVerticalSpacing = ODSPadding(
        left = DSVariables.spacingComponent9,
        right = DSVariables.spacingComponent9
    ),
    paddingTypeVerticalInsetSpacing = ODSPadding(
        top = DSVariables.spacingComponent5,
        bottom = DSVariables.spacingComponent5,
        left = DSVariables.spacingComponent9,
        right = DSVariables.spacingComponent9
    ),
    paddingTypeHorizontalInset = ODSPadding(
        left = DSVariables.spacingComponent5,
        right = DSVariables.spacingComponent5
    ),
    paddingTypeHorizontalSpacing = ODSPadding(
        top = DSVariables.spacingComponent9,
        bottom = DSVariables.spacingComponent9
    ),
    paddingTypeHorizontalInsetSpacing = ODSPadding(
        top = DSVariables.spacingComponent9,
        bottom = DSVariables.spacingComponent9,
        left = DSVariables.spacingComponent5,
        right = DSVariables.spacingComponent5
    ),
    verticalArrangementTypeHorizontal = Arrangement.Top,
    thickness = 1.dp
)

var DSDividerTokens: ODSDividerTokens = defaultODSDividerTokens
