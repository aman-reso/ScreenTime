package com.telekom.odsystem.organisms.searchview

import com.telekom.odsystem.molecules.searchbar.ODSSearchBarProps

/**
 * Data class representing the properties for configuring an ODS Search View component.
 *
 * An ODS Search View provides a search interface with optional back navigation and a search bar.
 *
 * @property showBackButton Indicates whether to display a back button in the search view. Defaults to `true`.
 * @property searchBarProps Properties for configuring the search bar within the search view, allowing customization of its appearance and behavior. Defaults to `null`, indicating no specific configuration.
 */
data class ODSSearchViewProps(
    var showBackButton: Boolean = true,
    var searchBarProps: ODSSearchBarProps? = null,
)
