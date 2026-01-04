package com.app.screentime.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.organisms.bottomnavigation.ODSBottomNavigationItemProps

/**
 * Properties used to configure the appearance and behavior of ScreenTime Navigation.
 *
 * @property showBottomNavigation Indicates whether the bottom navigation bar should be displayed.
 * @property navigationItems The list of navigation items to display in the bottom navigation bar.
 */
data class ScreenTimeNavigationProps(
    var showBottomNavigation: Boolean = true,
    var navigationItems: List<ODSBottomNavigationItemProps> = defaultNavigationItems
)

/**
 * Default navigation items for ScreenTime app.
 */
val defaultNavigationItems = listOf(
    ODSBottomNavigationItemProps(
        text = "Home",
        icon = ODSIconModel(imageVector = Icons.Default.Home),
        iconActive = ODSIconModel(imageVector = Icons.Default.Home)
    ),
    ODSBottomNavigationItemProps(
        text = "Statistics",
        icon = ODSIconModel(imageVector = Icons.Default.BarChart),
        iconActive = ODSIconModel(imageVector = Icons.Default.Analytics)
    ),
//    ODSBottomNavigationItemProps(
//        text = "Focus",
//        icon = ODSIconModel(imageVector = Icons.Default.Timer),
//        iconActive = ODSIconModel(imageVector = Icons.Default.Timer)
//    ),
    // Challenge feature disabled
    ODSBottomNavigationItemProps(
        text = "Challenges",
        icon = ODSIconModel(imageVector = Icons.Default.Flag),
        iconActive = ODSIconModel(imageVector = Icons.Default.Flag)
    ),
    ODSBottomNavigationItemProps(
        text = "Profile",
        icon = ODSIconModel(imageVector = Icons.Default.Person),
        iconActive = ODSIconModel(imageVector = Icons.Default.Person)
    )
)

