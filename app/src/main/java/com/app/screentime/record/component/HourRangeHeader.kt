package com.app.screentime.record.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSText

import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.DSTextStyles

@Composable
fun HourRangeHeader(hour: Int, scheme: com.telekom.odsystem.tokens.tokens.ODSTheme) {
    val hourRange = remember(hour) {
        val startHour = String.format("%02d:00", hour)
        val endHour = String.format("%02d:00", (hour + 1) % 24)
        "$startHour-$endHour"
    }

    ODSText(
        text = hourRange,
        style = DSTextStyles.bodyMRegular,
        color = scheme.basicText,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

