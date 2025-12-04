package com.telekom.odsystem.organisms.cardchoice

import com.telekom.odsystem.atoms.checkboxicon.ODSCheckboxIconProps
import com.telekom.odsystem.atoms.checkboxicon.ODSCheckboxIconSelected
import com.telekom.odsystem.atoms.checkboxicon.ODSCheckboxIconSize
import com.telekom.odsystem.atoms.radioicon.ODSRadioIconProps
import com.telekom.odsystem.atoms.radioicon.ODSRadioIconSize
import com.telekom.odsystem.atoms.switchicon.ODSSwitchIconProps
import com.telekom.odsystem.atoms.switchicon.ODSSwitchIconSize
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.organisms.cardchoice.ODSCardChoiceSelectorAlignment.MIDDLE
import com.telekom.odsystem.organisms.cardchoice.ODSCardChoiceSelectorAlignment.TOP
import com.telekom.odsystem.organisms.cardchoice.ODSCardChoiceSelectorPosition.LEFT
import com.telekom.odsystem.organisms.cardchoice.ODSCardChoiceSelectorPosition.RIGHT
import com.telekom.odsystem.organisms.cardchoice.ODSCardChoiceType.CHECKBOX_CHOICE
import com.telekom.odsystem.organisms.cardchoice.ODSCardChoiceType.RADIO_CHOICE
import com.telekom.odsystem.organisms.cardchoice.ODSCardChoiceType.SWITCH_CHOICE

/**
 * Vertical alignment of the selector in the card choice.
 *
 * @property [MIDDLE] - Vertically aligns the selector to the center of the card.
 * @property [TOP] - Vertically aligns the selector to the top of the card.
 */
enum class ODSCardChoiceSelectorAlignment {
    MIDDLE,
    TOP,
}

/**
 * Represents the position of the selector in the card choice component.
 *
 * @property LEFT The selector is positioned on the left side of the card.
 * @property RIGHT The selector is positioned on the right side of the card.
 */
enum class ODSCardChoiceSelectorPosition {
    LEFT,
    RIGHT,
}

/**
 * Defines the type of selector to be used in the card choice component.
 *
 * @property [CHECKBOX_CHOICE] Uses a checkbox as the selector.
 * @property [RADIO_CHOICE] Uses a radio button as the selector.
 * @property [SWITCH_CHOICE] Uses a switch as the selector.
 */
enum class ODSCardChoiceType {
    CHECKBOX_CHOICE,
    RADIO_CHOICE,
    SWITCH_CHOICE,
}

/**
 * Properties for the radio icon used in the ODS Card Choice component.
 *
 * This data class defines the customizable properties of the radio icon
 * when the card choice type is set to [ODSCardChoiceType.RADIO_CHOICE].
 *
 * @property size The size of the radio icon. Defaults to [ODSRadioIconSize.LARGE].
 */
data class ODSCardChoiceRadioIconProps(
    var size: ODSRadioIconSize = ODSRadioIconSize.LARGE,
)

/**
 * Properties specific to the switch icon within an ODS Card Choice component.
 *
 * This data class allows customization of the switch icon's appearance.
 *
 * @property size The size of the switch icon. Defaults to [ODSSwitchIconSize.SMALL].
 */
data class ODSCardChoiceSwitchIconProps(
    var size: ODSSwitchIconSize = ODSSwitchIconSize.SMALL,
)

/**
 * Properties for the checkbox icon used in the ODS Card Choice component.
 *
 * This data class defines the customizable properties of the checkbox icon
 * when the card choice type is set to [ODSCardChoiceType.CHECKBOX_CHOICE].
 *
 * @param size The size of the checkbox icon. Defaults to [ODSCheckboxIconSize.LARGE].
 */
data class ODSCardChoiceCheckboxIconProps(
    var size: ODSCheckboxIconSize = ODSCheckboxIconSize.LARGE,
)

internal fun ODSCardChoiceRadioIconProps.toODSRadioIconProps(
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

internal fun ODSCardChoiceSwitchIconProps.toODSSwitchIconProps(
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

internal fun ODSCardChoiceCheckboxIconProps.toODSCheckboxIconProps(
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
 * 2025-09-10 (v1.33.1) - uid: 22378211
 * Figma link: https://figma.com/design/hre4oCiCoYfStczE6GmCaA/Untitled?node-id=84-11470
 */

/**
 * Represents the properties of an ODS Card Choice component.
 *
 * This component allows users to select one or more options from a set.
 *
 * @property disabled Whether the card choice is disabled. Default is `false`.
 * @property filled Whether the card choice is filled. Default is `true`.
 * @property readOnly Whether the card choice is read-only. Default is `false`.
 * @property selected Whether the card choice is selected. Default is `false`.
 * @property selectorAlignment The vertical alignment of the selector. Default is [ODSCardChoiceSelectorAlignment.TOP].
 * @property selectorPosition The horizontal position of the selector. Default is [ODSCardChoiceSelectorPosition.LEFT].
 * @property subtle Whether the card choice has a subtle appearance. Default is `false`.
 * @property type The type of selector to use (e.g., radio button, checkbox, switch). Default is [ODSCardChoiceType.RADIO_CHOICE].
 * @property radioIconProps Properties for the radio icon, if the type is [ODSCardChoiceType.RADIO_CHOICE].
 * @property switchIconProps Properties for the switch icon, if the type is [ODSCardChoiceType.SWITCH_CHOICE].
 * @property checkboxIconProps Properties for the checkbox icon, if the type is [ODSCardChoiceType.CHECKBOX_CHOICE].
 */
data class ODSCardChoiceProps(
    var disabled: Boolean = false,
    var filled: Boolean = true,
    var readOnly: Boolean = false,
    var selected: Boolean = false,
    var selectorAlignment: ODSCardChoiceSelectorAlignment = ODSCardChoiceSelectorAlignment.TOP,
    var selectorPosition: ODSCardChoiceSelectorPosition = ODSCardChoiceSelectorPosition.LEFT,
    var subtle: Boolean = false,
    var type: ODSCardChoiceType = ODSCardChoiceType.RADIO_CHOICE,
    var radioIconProps: ODSCardChoiceRadioIconProps = ODSCardChoiceRadioIconProps(), // Not exported by the plugin
    var switchIconProps: ODSCardChoiceSwitchIconProps = ODSCardChoiceSwitchIconProps(), // Not exported by the plugin
    var checkboxIconProps: ODSCardChoiceCheckboxIconProps = ODSCardChoiceCheckboxIconProps(), // Not exported by the plugin
)
