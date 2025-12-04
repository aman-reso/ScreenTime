package com.telekom.odsystem.molecules.chatbubbleleadingelement

import com.telekom.odsystem.atoms.avatar.ODSAvatarProps
import com.telekom.odsystem.atoms.badgeicon.ODSBadgeIconProps
import com.telekom.odsystem.molecules.chatbubbleleadingelement.ODSChatBubbleLeadingElementType.ODS_AVATAR
import com.telekom.odsystem.molecules.chatbubbleleadingelement.ODSChatBubbleLeadingElementType.ODS_BADGE_ICON

/**
 * The type of the leading element in the ODSChatBubble.
 *
 * @property ODS_AVATAR an avatar.
 * @property ODS_BADGE_ICON a badge with an icon.
 */
enum class ODSChatBubbleLeadingElementType {
    ODS_AVATAR,
    ODS_BADGE_ICON,
}

/**
 * Properties for ODSChatBubbleLeadingElement.
 *
 * @property type Leading element type (ODS_AVATAR or ODS_BADGE_ICON). Default: ODS_BADGE_ICON.
 * @property avatarProps Avatar properties (if type is ODS_AVATAR).
 * @property badgeIconProps Badge icon properties (if type is ODS_BADGE_ICON).
 */
data class ODSChatBubbleLeadingElementProps(
    var type: ODSChatBubbleLeadingElementType = ODSChatBubbleLeadingElementType.ODS_BADGE_ICON,
    var avatarProps: ODSAvatarProps? = null,
    var badgeIconProps: ODSBadgeIconProps? = null
)
