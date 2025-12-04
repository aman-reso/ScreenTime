package com.telekom.odsystem.slots.dialogbottomsheetpreferredactions

import com.telekom.odsystem.atoms.button.ODSButtonProps

enum class ODSDialogBottomSheetPreferredActionsVariant {
    STACKED,
    SIDE_BY_SIDE,
    SIDE_BY_SIDE_FILL,
}

/**
 * Properties describing preferred actions of a dialog bottom sheet.
 *
 * @property variant Layout variant for arranging the actions.
 * @property tertiaryActionProps Optional tertiary action button properties.
 * @property secondaryActionProps Optional secondary action button properties.
 * @property mainActionProps Properties for the main action button.
 */
data class ODSDialogBottomSheetPreferredActionsProps(
    var variant: ODSDialogBottomSheetPreferredActionsVariant = ODSDialogBottomSheetPreferredActionsVariant.SIDE_BY_SIDE,
    var tertiaryActionProps: ODSButtonProps? = null,
    var secondaryActionProps: ODSButtonProps? = null,
    var mainActionProps: ODSButtonProps? = null
)
