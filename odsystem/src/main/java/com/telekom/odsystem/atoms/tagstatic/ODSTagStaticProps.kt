package com.telekom.odsystem.atoms.tagstatic

import com.telekom.odsystem.atoms.icon.ODSIconModel

/**
 * Defines the visual types or categories for an [ODSTagStaticProps].
 *
 * This enum is used to control the appearance of the static tag,
 * allowing for different visual distinctions based on its purpose or context
 * (e.g., informational, warning, promotional).
 */
enum class ODSTagStaticType {
    /** Basic or default appearance for the tag. */
    BASIC,
    /** A more subtle or subdued appearance. */
    SUBTLE,
    /** A stronger, more prominent appearance. */
    STRONG,
    /** An appearance indicating a warning or caution. */
    WARNING,
    /** An appearance used for promotional content or highlights. */
    PROMOTION,
    /** An appearance indicating savings or a discount. */
    SAVINGS,
    SUCCESS,
    ERROR,
}

/**
 * Represents the properties for configuring an ODS (presumably "OD System") Static Tag component.
 *
 * This data class allows customization of the tag's state (disabled),
 * content (label, icon), and visual type. Static tags are typically used for displaying
 * non-interactive information or categorizations.
 *
 * @property disabled Determines if the tag appears in a disabled state (e.g., grayed out).
 *                    Note that static tags are generally non-interactive regardless of this property.
 *                    Defaults to `false`.
 * @property icon The [ODSIconModel] to be displayed on the tag, typically on the leading side.
 *                Can be `null` if no icon is needed.
 * @property label The text content to be displayed on the tag. Can be `null` if the tag is icon-only
 *                 or if no text is desired.
 * @property type The [ODSTagStaticType] that defines the visual style of the tag.
 *                Defaults to [ODSTagStaticType.BASIC].
 */
data class ODSTagStaticProps(
    var disabled: Boolean = false,
    var icon: ODSIconModel? = null,
    var label: String? = null,
    var type: ODSTagStaticType = ODSTagStaticType.BASIC,
)
