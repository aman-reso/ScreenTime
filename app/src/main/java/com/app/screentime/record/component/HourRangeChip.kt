package com.app.screentime.record.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel

import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.DSTextStyles

@Composable
fun HourRangeChip(
    hour: Int,
    scheme: com.telekom.odsystem.tokens.tokens.ODSTheme,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    val hourRange = remember(hour) {
        val startHour = "%02d:00".format(hour)
        val endHour = "%02d:00".format((hour + 1) % 24)
        "$startHour - $endHour"
    }

    ODSBox(
        modifier = Modifier.clickable(onClick = onClick),
        background = if (selected) {
            listOf(ODSColorModel(scheme.basicAccent))
        } else {
            listOf(ODSColorModel(scheme.basicBackgroundCard))
        },
        cornerRadius = ODSCorners(all = 8.dp),
        padding = ODSPadding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            if (selected) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        imageVector = Icons.Filled.Done,
                        tint = scheme.basicTextOnAccent,
                        contentDescription = "Selected"
                    ),
                    width = 18.dp,
                    height = 18.dp
                )
            }
            ODSText(
                text = hourRange,
                style = DSTextStyles.bodyMBold,
                color = if (selected) scheme.basicTextOnAccent else scheme.basicText
            )
        }
    }
}

