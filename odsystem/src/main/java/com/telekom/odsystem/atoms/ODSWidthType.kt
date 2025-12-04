package com.telekom.odsystem.atoms

import androidx.compose.ui.unit.Dp

sealed class ODSWidthType {
    object Fill : ODSWidthType()
    object Wrap : ODSWidthType()
    data class Fixed(val width: Dp) : ODSWidthType()
}
