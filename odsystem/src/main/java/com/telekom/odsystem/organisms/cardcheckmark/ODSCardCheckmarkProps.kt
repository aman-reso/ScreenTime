package com.telekom.odsystem.organisms.cardcheckmark

import com.telekom.odsystem.organisms.cardcheckmark.ODSCardCheckmarkSelectorAlignment.MIDDLE
import com.telekom.odsystem.organisms.cardcheckmark.ODSCardCheckmarkSelectorAlignment.TOP

/**
 * Defines the vertical alignment of the checkmark selector within the card.
 *
 * @property TOP Aligns the checkmark selector to the top of the card.
 * @property MIDDLE Aligns the checkmark selector to the middle of the card.
 */
enum class ODSCardCheckmarkSelectorAlignment {
    TOP,
    MIDDLE,
}

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-09 (v1.33.1) - uid: 5ac49cde
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=8756-22767
 */

/**
 * Represents the properties of an ODS Card Checkmark component.
 *
 * @property disabled Indicates whether the card checkmark is disabled. Default is `false`.
 * @property filled Indicates whether the card checkmark is filled. Default is `true`.
 * @property readOnly Indicates whether the card checkmark is read-only. Default is `false`.
 * @property selected Indicates whether the card checkmark is selected. Default is `false`.
 * @property selectorAlignment Specifies the alignment of the selector within the card checkmark.
 *                             Default is [ODSCardCheckmarkSelectorAlignment.TOP].
 * @property subtle Indicates whether the card checkmark has a subtle appearance. Default is `false`.
 */
data class ODSCardCheckmarkProps(
    var disabled: Boolean = false,
    var filled: Boolean = true,
    var readOnly: Boolean = false,
    var selected: Boolean = false,
    var selectorAlignment: ODSCardCheckmarkSelectorAlignment = ODSCardCheckmarkSelectorAlignment.TOP,
    var subtle: Boolean = false,
)
