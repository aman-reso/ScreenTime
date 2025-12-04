package com.telekom.odsystem.molecules.chatbubbleleadingelement

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.avatar.ODSAvatar
import com.telekom.odsystem.atoms.badgeicon.ODSBadgeIcon
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSChatBubbleLeadingElement is a composable function that displays a leading element for a chat bubble.
 * It can display either an avatar or a badge icon based on the provided props.
 *
 * @param modifier The modifier to be applied to the component.
 * @param scheme The ODSTheme to be used for styling the component. Defaults to neutralScheme.
 * @param props The ODSChatBubbleLeadingElementProps to configure the leading element. Defaults to an empty ODSChatBubbleLeadingElementProps.
 */
@Composable
fun ODSChatBubbleLeadingElement(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSChatBubbleLeadingElementProps = ODSChatBubbleLeadingElementProps()
) {
    val style = ODSChatBubbleLeadingElementStyle().getStyle(scheme = scheme)

    ODSRow(
        modifier = modifier,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        horizontalArrangement = style.horizontalArrangement
    ) {
        if (props.type == ODSChatBubbleLeadingElementType.ODS_AVATAR) {
            props.avatarProps?.let { avatarProps ->
                ODSAvatar(scheme = scheme, props = avatarProps)
            }
        }
        if (props.type == ODSChatBubbleLeadingElementType.ODS_BADGE_ICON) {
            props.badgeIconProps?.let { badgeIconProps ->
                ODSBadgeIcon(scheme = scheme, props = badgeIconProps)
            }
        }
    }
}
