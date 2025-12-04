package com.telekom.odsystem.atoms.typinganimation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding

data class ODSTypingAnimationTokens(
    val gap: Dp,
    val padding: ODSPadding,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val dotCornerRadius: ODSCorners,
    val dotWidth: Dp,
    val dotHeight: Dp,
    val dotClipContent: Boolean,
    val dot2CornerRadius: ODSCorners,
    val dot2Width: Dp,
    val dot2Height: Dp,
    val dot2ClipContent: Boolean,
    val dot3CornerRadius: ODSCorners,
    val dot3Width: Dp,
    val dot3Height: Dp,
    val dot3ClipContent: Boolean
)

val defaultODSTypingAnimationTokens = ODSTypingAnimationTokens(
    gap = DSVariables.spacingComponent1,
    padding = ODSPadding(all = DSVariables.spacingComponent5),
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    horizontalArrangement = Arrangement.Center,
    dotCornerRadius = ODSCorners(all = DSVariables.radiusFull),
    dotWidth = DSVariables.sizingComponent3,
    dotHeight = DSVariables.sizingComponent3,
    dotClipContent = true,
    dot2CornerRadius = ODSCorners(all = DSVariables.radiusFull),
    dot2Width = DSVariables.sizingComponent3,
    dot2Height = DSVariables.sizingComponent3,
    dot2ClipContent = true,
    dot3CornerRadius = ODSCorners(all = DSVariables.radiusFull),
    dot3Width = DSVariables.sizingComponent3,
    dot3Height = DSVariables.sizingComponent3,
    dot3ClipContent = true
)

var DSTypingAnimationTokens: ODSTypingAnimationTokens = defaultODSTypingAnimationTokens
