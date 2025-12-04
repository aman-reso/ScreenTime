package com.app.screentime.profile.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.avatar.ODSAvatar
import com.telekom.odsystem.atoms.avatar.ODSAvatarProps
import com.telekom.odsystem.atoms.avatar.ODSAvatarSize
import com.telekom.odsystem.atoms.avatar.ODSAvatarVariant
import com.telekom.odsystem.extensions.onClick
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun UserProfileCard(
    modifier: Modifier = Modifier,
    username: String? = null,
    userId: String? = null,
    onUsernameClick: (() -> Unit) = {},
    scheme: ODSTheme = neutralScheme
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        val displayName = username ?: userId ?: "User"
        val initials = displayName.split(" ")
            .take(2)
            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
            .joinToString("")
            .take(2)
            .ifEmpty { "U" }

        ODSAvatar(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            scheme = scheme,
            props = ODSAvatarProps(
                variant = if (initials.isNotEmpty()) ODSAvatarVariant.INITIALS else ODSAvatarVariant.ICON,
                initials = initials.ifEmpty { null },
                icon = if (initials.isEmpty()) ODSIconModel(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Avatar",
                    tint = scheme.basicTextRecessive
                ) else null,
                size = ODSAvatarSize.LARGE,
                showBadge = false
            )
        )
        ODSRow(
            modifier = Modifier
                .wrapContentWidth()
                .onClick {
                    onUsernameClick.invoke()
                },
            gap = DSVariables.spacingComponent3,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(DSVariables.spacingComponent6))
            ODSText(
                text = username ?: userId ?: "User",
                style = DSTextStyles.bodyMBold,
                textAlign = TextAlign.Center,
                color = scheme.basicText
            )
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.edit_type_standard,
                    contentDescription = "Edit Username",
                    tint = scheme.basicText
                ), width = DSVariables.spacingComponent6, height = DSVariables.spacingComponent6
            )
        }
        ODSText(
            text = "Joined On Nov 10, 2024",
            style = DSTextStyles.bodySRegular,
            modifier = Modifier.wrapContentWidth(),
            textAlign = TextAlign.Center,
            color = scheme.basicTextRecessive
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}