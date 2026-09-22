package com.telekom.odsystem.organisms.ratingstarsstatic

/**
 * Properties describing a static (non-interactive) rating display.
 *
 * @property helperText Optional helper text below the stars.
 * @property label Accessibility label for the rating.
 * @property stars Amount of filled stars to show.
 */
data class ODSRatingStarsStaticProps(
    var helperText: String? = null,
    var label: String? = null,
    var stars: Int = 0,
)
