package com.telekom.odsystem.slots.pagetitlesubtitle

/**
 * Defines the type of the page title and subtitle.
 *
 * Used to specify whether the title/subtitle are styled as a main or sub-page title.
 */
enum class ODSPageTitleSubtitleType {
    MAIN_PAGE_TITLE,
    SUB_PAGE_TITLE,
}

/**
 * Properties for the ODSPageTitleSubtitle component.
 *
 * @property showPageSubtitle Whether to show the page subtitle. Defaults to `true`.
 * @property subtitleText The subtitle text.
 * @property titleText The title text.
 * @property type The visual type of the page title and subtitle. Defaults to [ODSPageTitleSubtitleType.MAIN_PAGE_TITLE].
 * @property truncation Whether text should be truncated if it overflows. Defaults to `false`.
 */
data class ODSPageTitleSubtitleProps(
    var showPageSubtitle: Boolean = true,
    var subtitleText: String? = null,
    var titleText: String? = null,
    var type: ODSPageTitleSubtitleType = ODSPageTitleSubtitleType.MAIN_PAGE_TITLE,
    var truncation: Boolean = false,
)
