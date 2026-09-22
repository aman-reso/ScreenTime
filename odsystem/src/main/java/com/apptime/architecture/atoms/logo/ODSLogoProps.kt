package com.telekom.odsystem.atoms.logo

/**
 * Defines the visual variants for an [ODSLogoProps].
 *
 * This enum is used to select different versions or styles of a logo.
 */
enum class ODSLogoType {
    PRIMARY,
    SECONDARY,
}

/**
 * Represents the properties for configuring an ODS (presumably "OD System") Logo component.
 *
 * This data class allows customization of the logo's visual variant.
 * @property type The [ODSLogoType] that defines which version or style of the logo
 */
data class ODSLogoProps(
    var type: ODSLogoType = ODSLogoType.PRIMARY,
)
