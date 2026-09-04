package com.app.screentime.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.switch.ODSSwitch
import com.telekom.odsystem.atoms.switch.ODSSwitchProps
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun BiometricSecurityCard(
    isEnabled: Boolean,
    scheme: ODSTheme,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = 12.dp),
        border = ODSBorder(
            width = 1.dp,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        ),
        padding = ODSPadding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        ODSRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSRow(
                verticalAlignment = Alignment.CenterVertically,
                gap = 12.dp,
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                ODSIcon(
                    modifier = Modifier,
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ion_finger_print_outline
                    ),
                    tint = scheme.functionalDestructiveStandard.getColor(),
                    height = 24.dp,
                    width = 24.dp
                )
                ODSText(
                    text = "Biometric Lock",
                    style = ODSTextStyles.bodySRegular,
                    color = scheme.basicText
                )
            }

            ODSSwitch(
                modifier = Modifier.wrapContentSize(),
                scheme = scheme,
                props = ODSSwitchProps(selected = isEnabled),
                onCheckedChange = onToggle
            )
        }
    }
}
