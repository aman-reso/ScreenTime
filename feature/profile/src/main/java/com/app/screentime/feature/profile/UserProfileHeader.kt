package com.app.screentime.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

import com.app.screentime.core.model.UserRole
import com.telekom.odsystem.atoms.tagstatic.ODSTagStatic
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticProps
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticType

@Composable
fun UserProfileHeader(
    displayName: String,
    bio: String,
    role: UserRole = UserRole.USER,
    scheme: ODSTheme,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier.padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        gap = 10.dp
    ) {
        // Avatar with Coral Pink Border & 100% Badge
        Box(
            modifier = Modifier.size(88.dp),
            contentAlignment = Alignment.Center
        ) {
            ODSBox(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                border = ODSBorder(
                    width = 2.5.dp,
                    colorList = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
                ),
                contentAlignment = Alignment.Center
            ) {
                val initial = if (displayName.isNotBlank()) displayName.take(1).uppercase() else "U"
                ODSText(
                    text = initial,
                    style = ODSTextStyles.titleL,
                    color = scheme.functionalDestructiveStandard
                )
            }

            // 100% Completion Badge
            ODSBox(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 6.dp),
                background = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard)),
                cornerRadius = ODSCorners(all = 8.dp),
                padding = ODSPadding(horizontal = 8.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                ODSText(
                    text = "100%",
                    style = ODSTextStyles.microcopyBold,
                    color = HexColor(0xFFFFFFFF)
                )
            }
        }

        // Name, User Role Tag, and Subtitle
        ODSColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            gap = 4.dp
        ) {
            ODSRow(
                verticalAlignment = Alignment.CenterVertically,
                gap = 6.dp
            ) {
                ODSText(
                    text = if (displayName.isNotBlank()) displayName else "User",
                    style = ODSTextStyles.titleS,
                    color = scheme.basicText
                )
                ODSText(
                    text = "✦",
                    style = ODSTextStyles.bodySBold,
                    color = scheme.functionalDestructiveStandard
                )
            }

            // ODS Tag for showing user type (User / Model)
            val isModel = role == UserRole.MODEL
            ODSTagStatic(
                scheme = scheme,
                props = ODSTagStaticProps(
                    label = if (isModel) "✦ Creator Mode" else "👤 User Mode",
                    type = if (isModel) ODSTagStaticType.PROMOTION else ODSTagStaticType.BASIC
                )
            )

            ODSText(
                text = if (bio.isNotBlank()) bio else "Member · Evermore Edition",
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicTextRecessive
            )
        }

        // Edit Profile Button
        ODSButton(
            scheme = scheme,
            props = ODSButtonProps(
                label = "Edit Profile",
                buttonIcon = ODSIconModel(imageVector = Icons.Outlined.Edit),
                variant = ODSButtonVariant.SECONDARY,
                size = ODSButtonSize.SMALL
            ),
            onClick = onEditClick
        )
    }
}
