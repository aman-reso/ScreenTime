package com.app.screentime.ntoificationstack

import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import java.util.UUID


/**
 * Code generated with ODS RADD Code Generator
 * 2025-11-04 (v1.34.1) - uid: 5bee5665
 * Figma link: https://figma.com/design/3MbZ8LOrBNBjTZX9J3t8Lu/OneApp ODS Library?node-id=8940-4365
 */

data class OAServiceNotificationSingleProps(
    val titleLabel: String? = null,
    val actionText: String? = null,
    var iconDrawable: Int = -1,//-1 is default
)


/**
 * Code generated with ODS RADD Code Generator Plugin
 * 2025-05-24 (v1.31.1) - uid: 3e4cb405
 * Figma link: https://figma.com/design/3MbZ8LOrBNBjTZX9J3t8Lu/OneApp ODS Library?node-id=657-1741
 */

data class ODSCardNotificationModel(
    val notificationProps: OAServiceNotificationSingleProps? = null,
    val targetUrl: String? = null,
    val id: String = UUID.randomUUID().toString(),
)