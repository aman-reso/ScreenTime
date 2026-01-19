package com.telekom.odsystem.organisms.chatbubble

import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.molecules.chatbubbleleadingelement.ODSChatBubbleLeadingElementProps
import com.telekom.odsystem.organisms.chatbubble.ODSChatBubbleVariant.INCOMING
import com.telekom.odsystem.organisms.chatbubble.ODSChatBubbleVariant.OUTGOING

/**
 * Defines the visual style of the chat bubble.
 *
 * It can be either [INCOMING] for messages received from other users,
 * or [OUTGOING] for messages sent by the current user.
 * This affects the alignment and color scheme of the bubble.
 */
enum class ODSChatBubbleVariant {
    INCOMING,
    OUTGOING,
}

/**
 * Properties for a button within an ODS Chat Bubble.
 *
 * @property label The text label displayed on the button. Defaults to null, meaning no label.
 * @property leftIcon Whether an icon should be displayed to the left of the label. Defaults to false.
 * @property buttonIcon The [ODSIconModel] to be used as the button's icon. Defaults to null, meaning no icon.
 */
data class ODSChatBubbleButtonProps(
    var label: String? = null,
    var leftIcon: Boolean = false,
    var buttonIcon: ODSIconModel? = null,
)

internal fun ODSChatBubbleButtonProps.toODSButtonProps(): ODSButtonProps {
    return ODSButtonProps(
        label = this.label,
        leftIcon = this.leftIcon,
        buttonIcon = this.buttonIcon,
        size = ODSButtonSize.SMALL,
        variant = ODSButtonVariant.GHOST
    )
}

/**
 * Properties for the ODS Chat Bubble component.
 *
 * @property firstMessage True if this is the first message from the sender, affects bubble shape.
 * @property helperText Optional text below the main content.
 * @property showActions Toggles visibility of action buttons.
 * @property showBackground Toggles visibility of the bubble background.
 * @property showError Toggles error state indication.
 * @property showFooter Toggles visibility of the footer (timestamps, status).
 * @property showHelperText Toggles visibility of the helper text.
 * @property variant Style of the bubble (incoming/outgoing).
 * @property chatBubbleLeadingElementProps Optional properties for a leading element (e.g., avatar).
 * @property buttonProps Optional properties for action buttons.
 */
data class ODSChatBubbleProps(
    var firstMessage: Boolean = true,
    var helperText: String? = null,
    var showActions: Boolean = true,
    var showBackground: Boolean = true,
    var showError: Boolean = false,
    var showFooter: Boolean = true,
    var showHelperText: Boolean = true,
    var variant: ODSChatBubbleVariant = INCOMING,
    var chatBubbleLeadingElementProps: ODSChatBubbleLeadingElementProps? = null,
    var buttonProps: ODSChatBubbleButtonProps? = null
)
