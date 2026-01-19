package com.telekom.odsystem.atoms.thumbnail

import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.thumbnail.ODSThumbnailType.ICON
import com.telekom.odsystem.atoms.thumbnail.ODSThumbnailType.IMAGE

/**
 * Specifies the type of visual to be displayed by the ODSThumbnail component.
 *
 * Use [IMAGE] to display an image defined by an [ODSImageModel].
 * Use [ICON] to display an icon defined by an [ODSIconModel].
 */
enum class ODSThumbnailType {
    IMAGE,
    ICON,
}

/**
 * Code generated with ODS RADD Code Generator
 * 2025-08-27 (v1.32.3) - uid: 1dc2b9ea
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=7904-10948
 */

/**
 * Properties for the ODSThumbnail component.
 *
 * @param icon The icon model to display when type is [ODSThumbnailType.ICON].
 * @param type The type of visual to display, either an image or an icon.
 * @param image The image model to display when type is [ODSThumbnailType.IMAGE].
 */
data class ODSThumbnailProps(
    var icon: ODSIconModel? = null,
    var type: ODSThumbnailType = IMAGE,
    var image: ODSImageModel? = null, // Not exported by the plugin
)
