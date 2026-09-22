package com.telekom.odsystem.organisms.pageheader

import com.telekom.odsystem.molecules.tabs.ODSTabsProps

/**
 * Defines the types of page headers available.
 *
 * This enum distinguishes between main top-level page headers
 * and headers for sub-pages or sections within an application.
 */
enum class ODSPageHeaderType {
    MAIN_PAGE_HEADER_STANDARD,
    MAIN_PAGE_HEADER_SLIM,
    SUB_PAGE_HEADER,
}

/**
 * Properties used to configure the appearance and behavior of an ODS page header.
 *
 * @property showLogo Determines whether the logo is displayed in the page header. Default is `true`.
 * @property showTopSection Determines whether the top section is displayed. Default is `true`.
 * @property type The type of the page header, which can affect its layout and styling. Default is [ODSPageHeaderType.MAIN_PAGE_HEADER_STANDARD].
 * @property tabsProps Optional properties for configuring tabs within the page header, allowing for navigation between different sections or views. Default is `null`, meaning no tabs are displayed.
 */
data class ODSPageHeaderProps(
    var showLogo: Boolean = true,
    var showTopSection: Boolean = true,
    var type: ODSPageHeaderType = ODSPageHeaderType.MAIN_PAGE_HEADER_STANDARD,
    var tabsProps: ODSTabsProps? = null
)
