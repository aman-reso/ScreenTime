package com.telekom.odsystem.organisms.cardquickaction

import com.telekom.odsystem.atoms.icon.ODSIconModel

/**
 * Specifies the size of the quick action button within an `ODSCardQuickAction`.
 * The size affects the overall dimensions of the button, including its icon and text.
 */
enum class ODSCardQuickActionSize {
    SMALL,
    MEDIUM,
}

/**
 * Properties for the ODSCardQuickAction component.
 *
 * @property disabled If `true`, the action is disabled. Defaults to `false`.
 * @property filled If `true`, the action has a filled background. Defaults to `true`.
 * @property size The size of the action, [ODSCardQuickActionSize]. Defaults to [ODSCardQuickActionSize.MEDIUM].
 * @property subtle If `true`, the action uses a more subtle color scheme. Defaults to `false`.
 */
data class ODSCardQuickActionProps(
    var disabled: Boolean = false,
    var filled: Boolean = true,
    var size: ODSCardQuickActionSize = ODSCardQuickActionSize.MEDIUM,
    var subtle: Boolean = false,
    var iconModel: ODSIconModel? = null
)
