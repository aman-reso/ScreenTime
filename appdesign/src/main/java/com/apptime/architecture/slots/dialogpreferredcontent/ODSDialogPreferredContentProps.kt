package com.telekom.odsystem.slots.dialogpreferredcontent

import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.textarea.ODSTextAreaProps

enum class ODSDialogPreferredContentVariant {
    GENERAL_WITH_IMAGE,
    TEXT_AREA_INPUT,
}

/**
 * Properties describing the main content area of a dialog.
 *
 * @property content Main textual content of the dialog.
 * @property heading Optional heading shown at the top.
 * @property subtitle Additional subtitle text.
 * @property imageModel Optional image model shown for GENERAL_WITH_IMAGE.
 * @property textAreaProps Text area state for TEXT_AREA_INPUT.
 * @property variant Visual variant of the preferred content, either GENERAL_WITH_IMAGE or TEXT_AREA_INPUT.
 */
data class ODSDialogPreferredContentProps(
    var content: String? = null,
    var heading: String? = null,
    var subtitle: String? = null,
    var variant: ODSDialogPreferredContentVariant = ODSDialogPreferredContentVariant.GENERAL_WITH_IMAGE,
    var imageModel: ODSImageModel? = null,
    var textAreaProps: ODSTextAreaProps? = null
)
