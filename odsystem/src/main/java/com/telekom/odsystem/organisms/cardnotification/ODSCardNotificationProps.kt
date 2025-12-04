package com.telekom.odsystem.organisms.cardnotification

/**
 * Properties describing an individual card notification.
 *
 * @property showCloseButton Whether a dismiss button is visible.
 * @property text Main text of the notification.
 * @property title Optional title text for the notification.
 */
data class ODSCardNotificationProps(
    var showCloseButton: Boolean = true,
    var text: String? = null,
    var title: String? = null,
)
