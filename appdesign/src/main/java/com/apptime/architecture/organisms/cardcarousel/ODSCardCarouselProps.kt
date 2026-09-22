package com.telekom.odsystem.organisms.cardcarousel

/**
 * Properties configuring a card carousel.
 *
 * @property carouselSize Number of items visible at once.
 * @property loop Whether the carousel loops continuously.
 * @property autoScroll Enables automatic scrolling when true.
 * @property autoScrollDuration Delay between auto-scroll steps in milliseconds.
 * @property initialPage The starting page index when the carousel is first displayed.
 * @property fill If true, content slot container will expand to fill the height of the parent container.
 */
data class ODSCardCarouselProps(
    var carouselSize: Int = 3,
    var loop: Boolean = true,
    var autoScroll: Boolean = true,
    var autoScrollDuration: Long = 1000,
    var initialPage: Int = 0,
    var fill: Boolean = false,
)
