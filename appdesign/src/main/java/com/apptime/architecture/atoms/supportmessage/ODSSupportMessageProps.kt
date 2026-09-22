package com.telekom.odsystem.atoms.supportmessage

/**
 * Defines the type of the ODS support message.
 */
enum class ODSSupportMessageMode {
    INFORMATIVE,
    ERROR,
    SUCCESS,
}

/**
 * Properties used to configure the appearance and behavior of an ODS support message.
 *
 * @property disabled Indicates whether the support message is disabled and non-visible.
 * @property helperText The text content of the support message, used as a helper text.
 * @property mode The mode of the support message, which can be either informative, error, or success.
 */
data class ODSSupportMessageProps(
    var disabled: Boolean = false,
    var helperText: String? = null,
    var mode: ODSSupportMessageMode = ODSSupportMessageMode.INFORMATIVE,
)
