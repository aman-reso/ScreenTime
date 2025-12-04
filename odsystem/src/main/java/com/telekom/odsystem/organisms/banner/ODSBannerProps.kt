package com.telekom.odsystem.organisms.banner

import com.telekom.odsystem.atoms.link.ODSLinkProps

/**
 * Defines the type of the ODS banner.
 */
enum class ODSBannerMode {
    ERROR,
    SUCCESS,
    INFORMATIVE,
    WARNING,
}

/**
 * Properties used to configure the appearance and behavior of an ODS banner.
 *
 * @property showCloseButton Indicates whether the banner should display a close button.
 * @property text The main text content displayed in the banner.
 * @property title The title of the banner, providing a brief summary of its purpose.
 * @property link1Props The properties for the first link displayed in the banner (if applicable).
 * @property link2Props The properties for the second link displayed in the banner (if applicable).
 * @property mode The mode of the banner, which can be either error, success, informative, or warning.
 */
data class ODSBannerProps(
    var showCloseButton: Boolean = true,
    var text: String? = null,
    var title: String? = null,
    var mode: ODSBannerMode = ODSBannerMode.SUCCESS,
    var link1Props: ODSLinkProps? = null,
    var link2Props: ODSLinkProps? = null
)
