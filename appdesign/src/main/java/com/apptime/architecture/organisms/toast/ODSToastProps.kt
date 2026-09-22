package com.telekom.odsystem.organisms.toast

import com.telekom.odsystem.atoms.link.ODSLinkProps

enum class ODSToastMode {
    SUCCESS,
    INFORMATIVE,
}

/**
 * Properties describing an ODS toast notification.
 *
 * @property showCloseButton Displays a close icon when true.
 * @property text Main message text of the toast.
 * @property title Optional title above the text.
 * @property link1Props First optional link configuration.
 * @property link2Props Second optional link configuration.
 * @property mode Visual mode of the toast, can be success or informative.
 */
data class ODSToastProps(
    var showCloseButton: Boolean = true,
    var text: String? = null,
    var title: String? = null,
    var mode: ODSToastMode = ODSToastMode.SUCCESS,
    var link1Props: ODSLinkProps? = null,
    var link2Props: ODSLinkProps? = null
)
