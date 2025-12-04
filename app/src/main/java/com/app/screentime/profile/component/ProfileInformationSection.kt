package com.app.screentime.profile.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.screentime.R
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandard
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardProps
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardVariant

import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Security
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import com.telekom.odsystem.slots.cardcontentbasic.ODSCardContentBasic

@Composable
fun ProfileInformationSection(
    onTOTPClick: () -> Unit = {},
    scheme: ODSTheme = neutralScheme
) {

    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        gap = DSVariables.spacingComponent0
    ) {
        ODSListRowStandard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onTOTPClick),
            scheme = scheme,
            props = ODSListRowStandardProps(
                variant = ODSListRowStandardVariant.ICON,
                labelText = stringResource(R.string.one_time_password),
                descriptionText = "View and manage your two-factor authentication code",
                descriptionTitle = "Security",
                icon = ODSIconModel(imageVector = Icons.Default.Security),
                showDescriptionTitle = true
            )
        )
        ODSDivider(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSDividerProps(
                variant = ODSDividerVariant.HORIZONTAL
            )
        )
    }
}

