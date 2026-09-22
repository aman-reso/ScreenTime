package com.telekom.odsystem.atoms

import androidx.compose.ui.unit.Dp

sealed class ODSHeightType {
    object Fill : ODSHeightType()
    object Wrap : ODSHeightType()
    data class Fixed(val height: Dp) : ODSHeightType()
}
