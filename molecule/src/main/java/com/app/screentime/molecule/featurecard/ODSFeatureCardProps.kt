package com.app.screentime.molecule.featurecard

import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.icon.ODSIconModel

/**
 * Properties used to configure a feature card component.
 *
 * @property title The main title text (e.g., "Thailand")
 * @property subtitle The label text displayed above title (default: "eSIM")
 * @property iconImage The icon/image model displayed on the left
 * @property showArrow Whether to show the navigation arrow icon (default: true)
 * @property arrowIcon Optional custom arrow icon (default: right arrow)
 */
data class ODSFeatureCardProps(
    var title: String,
    var subtitle: String? = null,
    var iconImage: ODSIconModel? = null,
    var showArrow: Boolean = true,
    var arrowIcon: ODSIconModel? = null
)
