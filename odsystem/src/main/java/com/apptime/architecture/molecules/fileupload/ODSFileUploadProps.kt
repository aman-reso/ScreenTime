package com.telekom.odsystem.molecules.fileupload

import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.progressbar.ODSProgressBarProps
import com.telekom.odsystem.atoms.thumbnail.ODSThumbnailProps
import com.telekom.odsystem.molecules.fileupload.ODSFileUploadType.EXTENDED
import com.telekom.odsystem.molecules.fileupload.ODSFileUploadType.SIMPLE

/**
 * Represents the type of file upload component.
 *
 * * [EXTENDED] - A file upload component with additional features like a visual preview, progress bar, and action button.
 * * [SIMPLE] - A basic file upload component with minimal UI elements.
 */
enum class ODSFileUploadType {
    EXTENDED,
    SIMPLE,
}

/**
 * Code generated with ODS RADD Code Generator
 * 2025-08-29 (v1.32.3) - uid: 1dc2ba06
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=7904-10955
 */

/**
 * Represents the properties for an ODS File Upload component.
 *
 * @property label The label text displayed for the file upload. Defaults to null.
 * @property subtle A boolean indicating if the file upload should have a subtle appearance. Defaults to true.
 * @property type The type of the file upload, determining its visual style and behavior. Defaults to [ODSFileUploadType.EXTENDED].
 * @property actionButtonProps Optional properties for an action button associated with the file upload. Defaults to null.
 * @property thumbnailProps Optional properties for a visual element (e.g., an icon) displayed within the file upload. Defaults to null.
 * @property progressBarProps Optional properties for a progress bar, typically used to show upload progress. Defaults to null.
 * @property loadingSpinnerProps Optional properties for a loading spinner, indicating an ongoing process. Defaults to null.
 */
data class ODSFileUploadProps(
    var label: String? = null,
    var subtle: Boolean = true,
    var type: ODSFileUploadType = EXTENDED,
    var actionButtonProps: ODSButtonProps? = null,
    var thumbnailProps: ODSThumbnailProps? = null,
    var progressBarProps: ODSProgressBarProps? = null,
    var loadingSpinnerProps: ODSLoadingSpinnerProps? = null
)
