package com.app.screentime.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Security
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ProfileMenuCard(
    scheme: ODSTheme,
    onLanguageClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onTermsClick: () -> Unit,
    onHelpClick: () -> Unit,
    onLogoutClick: () -> Unit,
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
        padding = ODSPadding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        ODSColumn(modifier = Modifier.fillMaxWidth()) {
            ProfileMenuItem(
                icon = Icons.Outlined.Language,
                label = "Language & Region",
                scheme = scheme,
                onClick = onLanguageClick
            )
            ODSDivider(scheme = scheme, props = ODSDividerProps(variant = ODSDividerVariant.HORIZONTAL))
            ProfileMenuItem(
                icon = Icons.Outlined.Security,
                label = "Privacy & Security",
                scheme = scheme,
                onClick = onPrivacyClick
            )
            ODSDivider(scheme = scheme, props = ODSDividerProps(variant = ODSDividerVariant.HORIZONTAL))
            ProfileMenuItem(
                icon = Icons.Outlined.Description,
                label = "Terms of Service",
                scheme = scheme,
                onClick = onTermsClick
            )
            ODSDivider(scheme = scheme, props = ODSDividerProps(variant = ODSDividerVariant.HORIZONTAL))
            ProfileMenuItem(
                icon = Icons.AutoMirrored.Outlined.HelpOutline,
                label = "Help & Support",
                scheme = scheme,
                onClick = onHelpClick
            )
            ODSDivider(scheme = scheme, props = ODSDividerProps(variant = ODSDividerVariant.HORIZONTAL))
            ProfileMenuItem(
                icon = Icons.Outlined.Logout,
                label = "Log Out",
                scheme = scheme,
                isDestructive = true,
                onClick = onLogoutClick
            )
        }
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    label: String,
    scheme: ODSTheme,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    ODSRow(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ODSRow(
            verticalAlignment = Alignment.CenterVertically,
            gap = 12.dp
        ) {
            ODSIcon(
                iconModel = ODSIconModel(imageVector = icon),
                tint = if (isDestructive) scheme.functionalDestructiveStandard.getColor() else scheme.basicText.getColor()
            )
            ODSText(
                text = label,
                style = ODSTextStyles.bodySRegular,
                color = if (isDestructive) scheme.functionalDestructiveStandard else scheme.basicText
            )
        }

        ODSIcon(
            iconModel = ODSIconModel(drawableRes = R.drawable.arrow_right),
            tint = scheme.basicTextRecessive.getColor(),
            modifier = Modifier.size(16.dp)
        )
    }
}
