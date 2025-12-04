package com.telekom.odsystem.organisms.inlinenotification

import com.telekom.odsystem.atoms.link.ODSLinkProps

enum class ODSInlineNotificationMode {
    ERROR,
    SUCCESS,
    INFORMATIVE,
    WARNING,
}

/**
 * Properties describing an inline notification component.
 *
 * @property showCloseButton Displays a close button when true.
 * @property text Main message text of the notification.
 * @property title Optional title displayed above the text.
 * @property link1Props First optional link configuration.
 * @property link2Props Second optional link configuration.
 * @property mode Visual mode of the notification, can be error, success, informative, or warning.
 */
data class ODSInlineNotificationProps(
    var showCloseButton: Boolean = true,
    var text: String? = null,
    var title: String? = null,
    var mode: ODSInlineNotificationMode = ODSInlineNotificationMode.SUCCESS,
    var link1Props: ODSLinkProps? = null,
    var link2Props: ODSLinkProps? = null
)
