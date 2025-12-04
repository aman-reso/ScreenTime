package com.telekom.odsystem.molecules.bottomsheet

import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.foundations.ODSAspectRatio

/**
 * Properties configuring the ODS bottom sheet.
 *
 * @property showHandle Displays a drag handle when true.
 * @property image Optional image shown at the top of the sheet.
 * @property imageAspectRatio Aspect ratio for the header image.
 */
data class ODSBottomSheetProps(
    var showHandle: Boolean = true,
    var image: ODSImageModel? = null,
    var imageAspectRatio: ODSAspectRatio = ODSAspectRatio.VALUE_4_3,
)
