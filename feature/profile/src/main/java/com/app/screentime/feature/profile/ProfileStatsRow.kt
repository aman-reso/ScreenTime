package com.app.screentime.feature.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ProfileStatsRow(
    callsMade: String = "12",
    talkTime: String = "48m",
    favoritesCount: Int = 0,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        gap = 10.dp
    ) {
        StatItem(
            value = callsMade,
            label = "Calls Made",
            scheme = scheme,
            modifier = Modifier.weight(1f)
        )
        StatItem(
            value = talkTime,
            label = "Talk Time",
            scheme = scheme,
            modifier = Modifier.weight(1f)
        )
        StatItem(
            value = "$favoritesCount",
            label = "Favorites",
            scheme = scheme,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier,
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = 12.dp),
        border = ODSBorder(
            width = 1.dp,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        ),
        padding = ODSPadding(vertical = 12.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        ODSColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            gap = 2.dp
        ) {
            ODSText(
                text = value,
                style = ODSTextStyles.bodyMBold,
                color = scheme.basicText
            )
            ODSText(
                text = label,
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicTextRecessive
            )
        }
    }
}
