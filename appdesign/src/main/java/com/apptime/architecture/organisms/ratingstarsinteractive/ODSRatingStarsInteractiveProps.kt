package com.telekom.odsystem.organisms.ratingstarsinteractive

/**
 * Properties configuring an interactive rating stars component.
 *
 * @property disabled Disables user interaction.
 * @property helperText Optional helper text below the stars.
 * @property ratingLabel Accessible label describing the rating.
 * @property readOnly Prevents the user from changing the rating.
 * @property stars Initial number of selected stars.
 */
data class ODSRatingStarsInteractiveProps(
    var disabled: Boolean = false,
    var helperText: String? = null,
    var ratingLabel: String? = null,
    var readOnly: Boolean = false,
    var stars: Int? = 1
)
