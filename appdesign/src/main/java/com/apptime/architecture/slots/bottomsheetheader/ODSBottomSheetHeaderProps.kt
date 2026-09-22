package com.telekom.odsystem.slots.bottomsheetheader

/**
 * Defines the size of the bottom sheet header.
 *
 * `LARGE` - Renders a larger header, typically with more prominent text and potentially more spacing.
 * `SMALL` - Renders a smaller, more compact header.
 */
enum class ODSBottomSheetHeaderSize {
    LARGE,
    SMALL,
}

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-04 (v1.32.3) - uid: 5c52583b
 * Figma link: https://figma.com/design/RTdgj2EBwu8TwoaWWVEovL/ODS_OneID_Production_Library?node-id=17924-44
 */

/**
 * Properties for configuring the ODS Bottom Sheet Header.
 *
 * This class defines the various attributes that can be customized for the header
 * of an ODS Bottom Sheet component.
 *
 * @property largeHeading The main heading text displayed when the header size is `LARGE`.
 * @property showBackArrow If `true`, a back arrow icon will be displayed, typically used for navigation. Defaults to `false`.
 * @property size The size of the bottom sheet header. See [ODSBottomSheetHeaderSize] for available options. Defaults to [ODSBottomSheetHeaderSize.LARGE].
 * @property smallHeading The main heading text displayed when the header size is `SMALL`.
 * @property subtitle Optional subtitle text displayed below the heading.
 */
data class ODSBottomSheetHeaderProps(
    var largeHeading: String? = null,
    var showBackArrow: Boolean = false,
    var size: ODSBottomSheetHeaderSize = ODSBottomSheetHeaderSize.LARGE,
    var smallHeading: String? = null,
    var subtitle: String? = null,
)
