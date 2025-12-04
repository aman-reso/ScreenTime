package com.telekom.odsystem.molecules.accordion

enum class ODSAccordionSize {
    /** Large accordion used in regular layouts. */
    LARGE,

    /** Compact accordion for dense layouts. */
    SMALL,
}

/**
 * Properties used to configure an ODS accordion.
 *
 * @property disabled Prevents user interaction when true.
 * @property expanded Indicates if the accordion is open.
 * @property headerText Text displayed in the header area.
 * @property size Controls the overall component size.
 */
data class ODSAccordionProps(
    var disabled: Boolean = false,
    var expanded: Boolean = false,
    var headerText: String? = null,
    var size: ODSAccordionSize = ODSAccordionSize.LARGE,
)
