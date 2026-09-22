package com.app.screentime.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.zonaLightODSTheme
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Preview(showBackground = true)
@Composable
fun ScreenTimeBottomNavigation(
    scheme: ODSTheme = zonaLightODSTheme, selectedIndex: Int = 0, onTabSelected: (Int) -> Unit = {}
) {
    ODSBox(
        modifier = Modifier
            .wrapContentSize()
            .navigationBarsPadding(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        padding = ODSPadding(
            horizontal = DSVariables.spacingComponent7,
            vertical = DSVariables.spacingComponent3
        ),
        contentAlignment = Alignment.Center,
        cornerRadius = ODSCorners(all = DSVariables.radiusFull),
        border = ODSBorder(
            width = 1.dp,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        )
    ) {
        ODSRow(
            modifier = Modifier.wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            gap = DSVariables.spacingComponent4
        ) {
            bottomNavTabs.forEachIndexed { index, tab ->
                val isSelected = selectedIndex == index
                val tint = if (isSelected) {
                    scheme.basicAccent.getColor()
                } else {
                    scheme.basicTextRecessive.getColor()
                }

                ODSBox(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onTabSelected(index) }),
                    background = if (isSelected) {
                        listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
                    } else {
                        emptyList()
                    },
                    contentAlignment = Alignment.Center
                ) {
                    if (tab.imageVector != null) {
                        ODSIcon(
                            iconModel = ODSIconModel(imageVector = tab.imageVector),
                            tint = tint,
                            modifier = Modifier.size(22.dp)
                        )
                    } else if (tab.iconRes != null) {
                        ODSIcon(
                            iconModel = ODSIconModel(drawableRes = tab.iconRes),
                            tint = tint,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}