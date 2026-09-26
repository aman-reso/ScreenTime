package com.app.screentime.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Profile Header Component for "My Account" Screen (Figma node-id 10-1148).
 *
 * Rules:
 * 1. 100% ODS components.
 * 2. Colors picked from `scheme: ODSTheme`.
 * 3. Max text size 16sp (bodyMBold).
 * 4. Padding and margins from `ODSVariables`.
 */
@Composable
fun AccountProfileHeader(
    userName: String = "User",
    email: String? = null,
    subtitle: String = "",
    avatarUrl: String? = null,
    scheme: ODSTheme = zonaODSTheme,
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ODSVariables.spacingLayout1)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onProfileClick
            ),
        gap = ODSVariables.spacingComponent5,
        horizontalAlignment = Alignment.Start,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        if (!avatarUrl.isNullOrBlank()) {
            ODSImage(
                imageModel = ODSImageModel(
                    url = avatarUrl,
                    contentDescription = "avatar"
                ),
                width = 72.dp,
                height = 72.dp,
                cornerRadius = ODSCorners(all = 36.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            ODSBox(
                modifier = Modifier.size(72.dp),
                cornerRadius = ODSCorners(all = 36.dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_user,
                        contentDescription = "avatar"
                    ),
                    tint = scheme.basicTextRecessive.getColor(),
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        // Name, Email, PRO Badge, and Subscription Details
        ODSColumn(
            modifier = Modifier.weight(1f),
            gap = ODSVariables.spacingComponent1,
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            ODSRow(
                gap = ODSVariables.spacingComponent3,
                horizontalAlignment = Alignment.Start,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                ODSText(
                    text = userName,
                    style = ODSTextStyles.bodyMBold, // 16sp max text size
                    color = scheme.basicText
                )
            }

            if (!email.isNullOrBlank()) {
                ODSText(
                    text = email,
                    style = ODSTextStyles.bodySRegular, // 14sp
                    color = scheme.basicTextRecessive
                )
            }

            if (subtitle.isNotBlank()) {
                ODSText(
                    text = subtitle,
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.basicAccentSecondary
                )
            }
        }
    }
}
