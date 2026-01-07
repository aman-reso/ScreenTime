package com.app.screentime.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
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
 * Default navigation items for ScreenTime app (phone - shows Statistics).
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

/**
 * Navigation items for tablet devices (shows Leaderboard instead of Statistics).
 */
val tabletNavigationItems = listOf(
    ODSBottomNavigationItemProps(
        text = "Home",
        icon = ODSIconModel(imageVector = Icons.Default.Home),
        iconActive = ODSIconModel(imageVector = Icons.Default.Home)
    ),
    ODSBottomNavigationItemProps(
        text = "Leaderboard",
        icon = ODSIconModel(imageVector = Icons.Default.EmojiEvents),
        iconActive = ODSIconModel(imageVector = Icons.Default.EmojiEvents)
    ),
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

/**
 * Get navigation items based on device type (tablet or phone).
 */
@Composable
fun getNavigationItems(): List<ODSBottomNavigationItemProps> {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isExpandedScreen = windowSizeClass.isWidthAtLeastBreakpoint(840)
    return if (isExpandedScreen) {
        tabletNavigationItems
    } else {
        defaultNavigationItems
    }
}

/**
 * Get navigation tokens based on device type (tablet or phone).
 */
@Composable
fun getNavigationTokens(): ScreenTimeNavigationTokens {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isExpandedScreen = windowSizeClass.isWidthAtLeastBreakpoint(840)
    return if (isExpandedScreen) {
        tabletScreenTimeNavigationTokens
    } else {
        defaultScreenTimeNavigationTokens
    }
}

