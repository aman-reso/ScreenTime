package com.app.screentime.feature.discover

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ModelProfileBottomBar(
    scheme: ODSTheme,
    onStartChat: () -> Unit,
    onStartVoiceCall: () -> Unit,
    onStartVideoCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        border = ODSBorder(
            width = 1.dp,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        ),
        cornerRadius = ODSCorners(all = 36.dp),
        padding = ODSPadding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Chat Action Button (Round)
            ODSBox(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onStartChat),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCardSubtle)),
                border = ODSBorder(
                    width = 1.dp,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                ),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = R.drawable.message),
                    tint = scheme.basicText.getColor(),
                    modifier = Modifier.size(22.dp)
                )
            }

            // 2. Video Call Hero Button (Prominent Accent Round)
            ODSBox(
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onStartVideoCall),
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = R.drawable.video),
                    tint = HexColor(0xFFFFFFFF).getColor(),
                    modifier = Modifier.size(26.dp)
                )
            }

            // 3. Voice Call Action Button (Round)
            ODSBox(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onStartVoiceCall),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCardSubtle)),
                border = ODSBorder(
                    width = 1.dp,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                ),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = R.drawable.call),
                    tint = scheme.basicText.getColor(),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
