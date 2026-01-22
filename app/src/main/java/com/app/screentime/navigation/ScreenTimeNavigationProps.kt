package com.app.screentime.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Wallpaper
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import com.app.screentime.config.R
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
    var navigationItems: List<ODSBottomNavigationItemProps> = emptyList()
)

/**
 * Default navigation items for ScreenTime app (phone - shows Statistics).
 */

val defaultNavigationMobileNotIndiaItems = listOf(
    ODSBottomNavigationItemProps(
        textRes = R.string.nav_home,
        icon = ODSIconModel(imageVector = Icons.Outlined.Home),
        iconActive = ODSIconModel(imageVector = Icons.Default.Home)
    ), ODSBottomNavigationItemProps(
        textRes = R.string.nav_statistics,
        icon = ODSIconModel(imageVector = Icons.Outlined.BarChart),
        iconActive = ODSIconModel(imageVector = Icons.Default.Analytics)
    ), ODSBottomNavigationItemProps(
        textRes = R.string.nav_wallpaper,
        icon = ODSIconModel(imageVector = Icons.Outlined.Wallpaper),
        iconActive = ODSIconModel(imageVector = Icons.Default.Wallpaper)
    ), ODSBottomNavigationItemProps(
        textRes = R.string.nav_profile,
        icon = ODSIconModel(imageVector = Icons.Outlined.Person),
        iconActive = ODSIconModel(imageVector = Icons.Default.Person)
    )
)

val tabletNavigationNotIndiaItems = listOf(
    ODSBottomNavigationItemProps(
        textRes = R.string.nav_home,
        icon = ODSIconModel(imageVector = Icons.Outlined.Home),
        iconActive = ODSIconModel(imageVector = Icons.Default.Home)
    ), ODSBottomNavigationItemProps(
        textRes = R.string.nav_leaderboard,
        icon = ODSIconModel(imageVector = Icons.Outlined.EmojiEvents),
        iconActive = ODSIconModel(imageVector = Icons.Default.EmojiEvents)
    ), ODSBottomNavigationItemProps(
        textRes = R.string.nav_wallpaper,
        icon = ODSIconModel(imageVector = Icons.Outlined.Wallpaper),
        iconActive = ODSIconModel(imageVector = Icons.Default.Wallpaper)
    ), ODSBottomNavigationItemProps(
        textRes = R.string.nav_profile,
        icon = ODSIconModel(imageVector = Icons.Outlined.Person),
        iconActive = ODSIconModel(imageVector = Icons.Default.Person)
    )
)


val tabletNavigationIndiaItems = listOf(
    ODSBottomNavigationItemProps(
        textRes = R.string.nav_home,
        icon = ODSIconModel(imageVector = Icons.Outlined.Home),
        iconActive = ODSIconModel(imageVector = Icons.Default.Home)
    ), ODSBottomNavigationItemProps(
        textRes = R.string.nav_challenges,
        icon = ODSIconModel(imageVector = Icons.Outlined.Flag),
        iconActive = ODSIconModel(imageVector = Icons.Default.Flag)
    ), ODSBottomNavigationItemProps(
        textRes = R.string.nav_leaderboard,
        icon = ODSIconModel(imageVector = Icons.Outlined.EmojiEvents),
        iconActive = ODSIconModel(imageVector = Icons.Default.EmojiEvents)
    ), ODSBottomNavigationItemProps(
        textRes = R.string.nav_profile,
        icon = ODSIconModel(imageVector = Icons.Outlined.Person),
        iconActive = ODSIconModel(imageVector = Icons.Default.Person)
    )
)

val MobileNavigationIndiaItems = listOf(
    ODSBottomNavigationItemProps(
        textRes = R.string.nav_home,
        icon = ODSIconModel(imageVector = Icons.Outlined.Home),
        iconActive = ODSIconModel(imageVector = Icons.Default.Home)
    ), ODSBottomNavigationItemProps(
        textRes = R.string.nav_statistics,
        icon = ODSIconModel(imageVector = Icons.Outlined.BarChart),
        iconActive = ODSIconModel(imageVector = Icons.Default.Analytics)
    ), ODSBottomNavigationItemProps(
        textRes = R.string.nav_challenges,
        icon = ODSIconModel(imageVector = Icons.Outlined.Flag),
        iconActive = ODSIconModel(imageVector = Icons.Default.Flag)
    ), ODSBottomNavigationItemProps(
        textRes = R.string.nav_profile,
        icon = ODSIconModel(imageVector = Icons.Outlined.Person),
        iconActive = ODSIconModel(imageVector = Icons.Default.Person)
    )
)

/**
 * Get navigation items based on device type (tablet or phone).
 */
@Composable
fun getNavigationItems(isIndia: Boolean): List<ODSBottomNavigationItemProps> {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isExpandedScreen = windowSizeClass.isWidthAtLeastBreakpoint(840)
    return if (isExpandedScreen) {
        if (isIndia) {
            tabletNavigationIndiaItems
        } else {
            tabletNavigationNotIndiaItems
        }
    } else {
        if (isIndia) {
            MobileNavigationIndiaItems
        } else {
            defaultNavigationMobileNotIndiaItems
        }
    }
}

/**
 * Get navigation tokens based on device type (tablet or phone).
 */
@Composable
fun getNavigationTokens(isIndia: Boolean): ScreenTimeNavigationTokens {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isExpandedScreen = windowSizeClass.isWidthAtLeastBreakpoint(840)
    return if (isExpandedScreen) {
        if (isIndia) {
            tabletIndiaScreenTimeNavigationToken
        } else {
            tabletNotIndiaScreenTimeNavigationToken
        }
    } else {
        if (isIndia) {
            mobileIndiaScreenTimeNavigationToken
        } else {
            mobileNotIndiaScreenTimeNavigationToken
        }
    }
}

