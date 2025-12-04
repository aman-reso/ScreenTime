package com.app.screentime.consent.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.switch.ODSSwitch
import com.telekom.odsystem.atoms.switch.ODSSwitchProps

import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Preview(showBackground = true)
@Composable
fun ConsentingSection(
    title: String = "Title",
    description: String = "Description",
    checked: Boolean = false,
    isMandatory: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
    scheme: ODSTheme = neutralScheme
) {
    ODSRow(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ODSColumn(modifier = Modifier.weight(1f)) {
            ODSText(
                text = title,
                style = DSTextStyles.bodyMRegular,
                color = scheme.basicText
            )
            ODSText(
                text = description,
                style = DSTextStyles.bodyMBold,
                color = scheme.basicTextRecessive
            )
        }

        ODSSwitch(
            scheme = scheme,
            props = ODSSwitchProps(
                selected = if (isMandatory) true else checked,
                disabled = isMandatory,
                readOnly = isMandatory
            ),
            onCheckedChange = onCheckedChange
        )
    }
}
