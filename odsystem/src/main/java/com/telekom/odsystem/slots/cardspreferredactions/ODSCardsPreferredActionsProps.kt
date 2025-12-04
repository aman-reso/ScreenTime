package com.telekom.odsystem.slots.cardspreferredactions

import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import java.util.UUID

/** Types of preferred action layouts for card components. */
enum class ODSCardsPreferredActionsType {
    /** Only a single action button is shown. */
    SINGLE_ACTION,

    /** Primary and secondary action buttons are displayed. */
    DOUBLE_ACTION,

    /** Compact button that reveals more options. */
    MORE_ACTIONS,

    /** Expanded list of multiple actions. */
    MORE_ACTIONS_EXPANDED,
}

/**
 * Properties describing preferred actions displayed on a card.
 *
 * @property showFirstAction Controls visibility of the first action.
 * @property type Layout type for the actions row.
 * @property mainActionProps Properties for the main action button.
 * @property secondaryActionProps Properties for the secondary action button.
 * @property utilityButtonProps Configuration for an optional utility button.
 * @property actionButtonProps Single additional action button properties.
 * @property actionButtonPropsList Collection of optional action buttons.
 */
data class ODSCardsPreferredActionsProps(
    var showFirstAction: Boolean = true,
    var type: ODSCardsPreferredActionsType = ODSCardsPreferredActionsType.SINGLE_ACTION,
    var mainActionProps: ODSButtonProps? = null,
    var secondaryActionProps: ODSButtonProps? = null,
    var utilityButtonProps: ODSCardsPreferredActionsUtilityButtonProps? = null,
    var actionButtonProps: ODSButtonProps? = null,
    val actionButtonPropsList: List<ODSActionButtonModel>? = null // Not exported from plugin
)

/**
 * Properties for the utility button used within preferred actions.
 *
 * @property variant Visual variant of the button.
 * @property size Size of the button.
 * @property disabled Disables the button when true.
 */
data class ODSCardsPreferredActionsUtilityButtonProps(
    var variant: ODSButtonVariant = ODSButtonVariant.PRIMARY,
    var size: ODSButtonSize = ODSButtonSize.LARGE,
    var disabled: Boolean = false,
)

internal fun ODSCardsPreferredActionsUtilityButtonProps.toODSButtonProps(
    buttonIcon: ODSIconModel? = null,
): ODSButtonProps {
    return ODSButtonProps(
        variant = this.variant,
        size = this.size,
        disabled = this.disabled,
        buttonType = ODSButtonButtonType.ICON_ONLY, // Not exported from plugin
        buttonIcon = buttonIcon // Not exported from plugin
    )
}

// Not exported from plugin
/** Model representing an additional action button. */
data class ODSActionButtonModel(
    val id: String = UUID.randomUUID().toString(),
    var buttonProps: ODSButtonProps? = null
)
