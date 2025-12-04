package com.telekom.odsystem.charts.compose.common

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.telekom.odsystem.charts.core.common.Defaults.TEXT_COMPONENT_TEXT_SIZE

internal fun TextUnit.pixelSize() =
    when (type) {
        TextUnitType.Sp -> value
        TextUnitType.Em -> value
        else -> TEXT_COMPONENT_TEXT_SIZE
    }
