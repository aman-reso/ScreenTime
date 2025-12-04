package com.app.screentime.challenge.component.detail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandard
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardProps
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardVariant
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Participants section.
 */
@Composable
fun ParticipantsSection(
    participantCount: Int, scheme: ODSTheme = neutralScheme
) {
    ODSCardBasic(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = ODSPadding(DSVariables.spacingComponent4),
        contentSlot = {
            ODSListRowStandard(
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme,
                props = ODSListRowStandardProps(
                    variant = ODSListRowStandardVariant.ICON,
                    label = "Participants",
                    descriptionTitle = participantCount.toString(),
                    icon = ODSIconModel(
                        drawableRes = R.drawable.happy_person_type_bold_size_standard,
                        tint = scheme.basicAccent,
                        contentDescription = "Participants"
                    )
                )
            )
//        ODSListRowStandard(modifier = Modifier.fillMaxWidth(), scheme = scheme)
//        ODSRow(modifier = Modifier.wrapContentWidth()) {
//            val sampleInitials = listOf("SJ", "MC", "JW", "EW", "JP")
//            val avatarsToShow = sampleInitials.take(3)
//            val overlapOffset = (-12).dp
//            val avatarSize = 40
//
//            avatarsToShow.forEachIndexed { index, initials ->
//                Box(
//                    modifier = Modifier
//                        .size(avatarSize.dp)
//                        .offset(x = (index * overlapOffset).value.dp)
//                        .zIndex((avatarsToShow.size - index).toFloat())
//                        .clip(CircleShape)
//                        .border(
//                            width = 1.dp,
//                            color = scheme.basicBackgroundCard.getColor(),
//                            shape = CircleShape
//                        )
//                ) {
//                    ODSAvatar(
//                        modifier = Modifier.size(avatarSize.dp),
//                        scheme = scheme,
//                        props = ODSAvatarProps(
//                            variant = ODSAvatarVariant.INITIALS,
//                            initials = initials,
//                            size = ODSAvatarSize.SMALL,
//                            showBadge = false
//                        )
//                    )
//                }
//            }
//        }
        })
}

