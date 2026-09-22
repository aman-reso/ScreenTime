package com.telekom.odsystem.organisms.cardnotificationstack

import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.organisms.cardnotification.ODSCardNotificationProps

enum class ODSCardNotificationStackLinkAlignment {
    CENTERED,
    RIGHT_SIDE,
    LEFT_SIDE,
}

/**
 * Properties for the "view all" button in the notification stack.
 *
 * @property buttonLabel The label for the "view all" button.
 */
data class ODSCardNotificationStackViewAllButtonProps(
    var buttonLabel: String? = null,
)

internal fun ODSCardNotificationStackViewAllButtonProps.toODSButtonProps(): ODSButtonProps {
    return ODSButtonProps(
        label = this.buttonLabel,
        buttonType = ODSButtonButtonType.STANDARD,
        rightIcon = true,
        size = ODSButtonSize.SMALL,
        variant = ODSButtonVariant.GHOST,
        buttonIcon = ODSIconModel(drawableRes = R.drawable.navigation_right_type_standard),
    )
}

/**
 * Properties configuring a stack of notification cards.
 *
 * @property show2ndCard Whether to show the second card in the stack.
 * @property show3rdCard Whether to show the third card in the stack.
 * @property linkAlignment Alignment of the "view all" button.
 * @property notificationCardProps Properties for the main notification card.
 * @property viewAllButtonProps Properties for the "view all" button.
 */
data class ODSCardNotificationStackProps(
    var show2ndCard: Boolean = false,
    var show3rdCard: Boolean = false,
    var linkAlignment: ODSCardNotificationStackLinkAlignment = ODSCardNotificationStackLinkAlignment.CENTERED,
    var notificationCardProps: ODSCardNotificationProps? = null,
    var viewAllButtonProps: ODSCardNotificationStackViewAllButtonProps? = null
)
