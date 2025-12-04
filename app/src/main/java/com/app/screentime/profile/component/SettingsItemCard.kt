package com.app.screentime.profile.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.screentime.profile.model.ProfileSettingsUi
import com.app.screentime.profile.utils.SettingsIconMapper
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandard
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardProps
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardVariant

import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.extensions.onClick
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun SettingsItemCard(
    data: ProfileSettingsUi,
    scheme: ODSTheme = neutralScheme,
    onClick: () -> Unit = {},
) {
    val text = stringResource(data.text)
    ODSRow(
        modifier = Modifier
            .fillMaxWidth()
            .onClick {
                onClick.invoke()
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        ODSListRowStandard(
            modifier = Modifier
                .weight(0.9f),
            scheme = scheme,
            props = ODSListRowStandardProps(
                variant = ODSListRowStandardVariant.ICON,
                labelText = text,
                icon = ODSIconModel(imageVector = SettingsIconMapper.getIcon(data.key, text))
            ),
        )
        ODSIcon(
            modifier = Modifier.weight(0.1f),
            iconModel = ODSIconModel(
                tint = scheme.basicText,
                drawableRes = R.drawable.right_condensed_type_standard,
                contentDescription = text
            )
        )
    }
}