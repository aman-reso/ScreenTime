package com.telekom.odsystem.organisms.cardchoiceimage

import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.checkboxicon.ODSCheckboxIconProps
import com.telekom.odsystem.atoms.checkboxicon.ODSCheckboxIconSelected
import com.telekom.odsystem.atoms.checkboxicon.ODSCheckboxIconSize
import com.telekom.odsystem.atoms.radioicon.ODSRadioIconProps
import com.telekom.odsystem.atoms.radioicon.ODSRadioIconSize
import com.telekom.odsystem.atoms.switchicon.ODSSwitchIconProps
import com.telekom.odsystem.atoms.switchicon.ODSSwitchIconSize
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSAspectRatio
import com.telekom.odsystem.organisms.cardchoiceimage.ODSCardChoiceImageType.CHECKBOX_CHOICE
import com.telekom.odsystem.organisms.cardchoiceimage.ODSCardChoiceImageType.RADIO_CHOICE
import com.telekom.odsystem.organisms.cardchoiceimage.ODSCardChoiceImageType.SWITCH_CHOICE

/**
 * Defines the type of icon to be displayed within the ODS Card Choice Image component.
 *
 * This enum class specifies the different types of interactive elements that can be
 * used in conjunction with the card choice image, allowing for various selection behaviors.
 *
 * @property CHECKBOX_CHOICE Represents a checkbox icon.
 * @property RADIO_CHOICE Represents a radio button icon.
 * @property SWITCH_CHOICE Represents a switch icon.
 */
enum class ODSCardChoiceImageType {
    CHECKBOX_CHOICE,
    RADIO_CHOICE,
    SWITCH_CHOICE,
}

/**
 * Properties for the radio icon used in the ODS Card Choice Image component.
 *
 * This data class defines the customizable properties of the radio icon
 * when the card choice type is set to [ODSCardChoiceImageType.RADIO_CHOICE].
 *
 * @property size The size of the radio icon. Defaults to [ODSRadioIconSize.LARGE].
 */
data class ODSCardChoiceImageRadioIconProps(
    var size: ODSRadioIconSize = ODSRadioIconSize.LARGE,
)

/**
 * Properties specific to the switch icon within an ODS Card Choice Image component.
 *
 * This data class allows customization of the switch icon's appearance.
 *
 * @property size The size of the switch icon. Defaults to [ODSSwitchIconSize.SMALL].
 */
data class ODSCardChoiceImageSwitchIconProps(
    var size: ODSSwitchIconSize = ODSSwitchIconSize.SMALL,
)

/**
 * Properties for the checkbox icon used in the ODS Card Choice Image component.
 *
 * This data class defines the customizable properties of the checkbox icon
 * when the card choice type is set to [ODSCardChoiceImageType.CHECKBOX_CHOICE].
 *
 * @param size The size of the checkbox icon. Defaults to [ODSCheckboxIconSize.LARGE].
 */
data class ODSCardChoiceImageCheckboxIconProps(
    var size: ODSCheckboxIconSize = ODSCheckboxIconSize.LARGE,
)

internal fun ODSCardChoiceImageRadioIconProps.toODSRadioIconProps(
    disabled: Boolean,
    selected: Boolean,
    state: ODSActions,
): ODSRadioIconProps {
    return ODSRadioIconProps(
        disabled = disabled,
        selected = selected,
        size = this.size,
        state = state,
    )
}

internal fun ODSCardChoiceImageSwitchIconProps.toODSSwitchIconProps(
    disabled: Boolean,
    selected: Boolean,
    state: ODSActions
): ODSSwitchIconProps {
    return ODSSwitchIconProps(
        disabled = disabled,
        selected = selected,
        size = this.size,
        state = state,
    )
}

internal fun ODSCardChoiceImageCheckboxIconProps.toODSCheckboxIconProps(
    disabled: Boolean,
    selected: Boolean,
    state: ODSActions
): ODSCheckboxIconProps {
    return ODSCheckboxIconProps(
        disabled = disabled,
        selected =
            when (selected) {
                true -> ODSCheckboxIconSelected.SELECTED
                false -> ODSCheckboxIconSelected.UNSELECTED
            },
        size = this.size,
        state = state,
    )
}

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-11 (v1.33.1) - uid: 4794709a
 * Figma link: https://figma.com/design/hre4oCiCoYfStczE6GmCaA/Untitled?node-id=86-10038
 */

/**
 * Represents the properties for an ODS Card Choice Image component.
 *
 * This data class defines various customizable attributes of the card choice image,
 * allowing control over its appearance, state, and behavior.
 *
 * @property disabled Indicates whether the card choice is disabled. Defaults to `false`.
 * @property filled Indicates whether the card should have a filled background. Defaults to `true`.
 * @property readOnly Indicates whether the card choice is read-only. Defaults to `false`.
 * @property selected Indicates whether the card choice is selected. Defaults to `false`.
 * @property subtle Indicates whether the card should have a subtle appearance. Defaults to `false`.
 * @property type The type of interactive element to display (e.g., radio, checkbox, switch). Defaults to [ODSCardChoiceImageType.RADIO_CHOICE].
 * @property image The image model to be displayed on the card. Defaults to `null`.
 * @property imageAspectRatio The aspect ratio of the image. Defaults to [ODSAspectRatio.VALUE_4_3].
 * @property radioIconProps Properties for the radio icon, applicable when `type` is [ODSCardChoiceImageType.RADIO_CHOICE].
 * @property switchIconProps Properties for the switch icon, applicable when `type` is [ODSCardChoiceImageType.SWITCH_CHOICE].
 * @property checkboxIconProps Properties for the checkbox icon, applicable when `type` is [ODSCardChoiceImageType.CHECKBOX_CHOICE].
 */
data class ODSCardChoiceImageProps(
    var disabled: Boolean = false,
    var filled: Boolean = true,
    var readOnly: Boolean = false,
    var selected: Boolean = false,
    var subtle: Boolean = false,
    var type: ODSCardChoiceImageType = RADIO_CHOICE,
    var image: ODSImageModel? = null, // Not exported by the plugin
    var imageAspectRatio: ODSAspectRatio = ODSAspectRatio.VALUE_4_3, // Not exported by the plugin
    var radioIconProps: ODSCardChoiceImageRadioIconProps = ODSCardChoiceImageRadioIconProps(), // Not exported by the plugin
    var switchIconProps: ODSCardChoiceImageSwitchIconProps = ODSCardChoiceImageSwitchIconProps(), // Not exported by the plugin
    var checkboxIconProps: ODSCardChoiceImageCheckboxIconProps = ODSCardChoiceImageCheckboxIconProps(), // Not exported by the plugin
)
