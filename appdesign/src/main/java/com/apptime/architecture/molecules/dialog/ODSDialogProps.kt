package com.telekom.odsystem.molecules.dialog

/**
 * Properties configuring a standard ODS dialog.
 *
 * @property showCloseButton Displays a dismiss button in the header when true.
 * @property showScrollbar Adds a scrollbar to the content area.
 * @property title Optional title shown at the top.
 * @property bodyText Body text displayed in the dialog. If `text` is set, it will be used as the body text.
 */
data class ODSDialogProps(
    var showCloseButton: Boolean = true,
    var showScrollbar: Boolean = true,
    var bodyText: String? = null,
    var title: String? = null,
)
