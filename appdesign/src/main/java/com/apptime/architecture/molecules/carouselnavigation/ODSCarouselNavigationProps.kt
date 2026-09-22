package com.telekom.odsystem.molecules.carouselnavigation

import android.content.Context
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel

/**
 * Properties for the left navigation button in a carousel.
 *
 * @property disabled Disables the button when true.
 */
data class ODSCarouselNavigationNavigationLeftButtonProps(
    var disabled: Boolean = false,
)

internal fun ODSCarouselNavigationNavigationLeftButtonProps.toODSButtonProps(context: Context): ODSButtonProps {
    return ODSButtonProps(
        buttonIcon = ODSIconModel(
            drawableRes = R.drawable.left_condensed_type_standard,
            contentDescription = context.getString(R.string.semantic_previous)
        ),
        buttonType = ODSButtonButtonType.ICON_ONLY,
        size = ODSButtonSize.SMALL,
        variant = ODSButtonVariant.GHOST,
        disabled = this.disabled
    )
}

/**
 * Properties for the right navigation button in a carousel.
 *
 * @property disabled Disables the button when true.
 */
data class ODSCarouselNavigationNavigationRightButtonProps(
    var disabled: Boolean = false,
)

internal fun ODSCarouselNavigationNavigationRightButtonProps.toODSButtonProps(context: Context): ODSButtonProps {
    return ODSButtonProps(
        buttonIcon = ODSIconModel(
            drawableRes = R.drawable.right_condensed_type_standard,
            contentDescription = context.getString(R.string.semantic_next)
        ),
        buttonType = ODSButtonButtonType.ICON_ONLY,
        size = ODSButtonSize.SMALL,
        variant = ODSButtonVariant.GHOST,
        disabled = this.disabled
    )
}

/**
 * Properties describing the ODS carousel navigation component.
 *
 * @property dots Number of dots displayed in the indicator.
 * @property selectedIndex Index of the currently active item.
 * @property navigationLeftButtonProps Configuration of the left navigation button.
 * @property navigationRightButtonProps Configuration of the right navigation button.
 */
data class ODSCarouselNavigationProps(
    var dots: Int = 5,
    var selectedIndex: Int = 0,
    var navigationLeftButtonProps: ODSCarouselNavigationNavigationLeftButtonProps? = null,
    var navigationRightButtonProps: ODSCarouselNavigationNavigationRightButtonProps? = null
)
