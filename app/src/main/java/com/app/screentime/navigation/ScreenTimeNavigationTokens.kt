package com.app.screentime.navigation

/**
 * Design tokens and constants for ScreenTime Navigation.
 *
 * @property routeToIndexMap Maps screen routes to their navigation indices.
 * @property bottomNavigationRoutes List of routes that should show bottom navigation.
 */
data class ScreenTimeNavigationTokens(
    val routeToIndexMap: Map<Screen, Int>,
    val bottomNavigationRoutes: List<Screen>
)

/**
 * Default navigation tokens for ScreenTime app.
 */
val defaultScreenTimeNavigationTokens = ScreenTimeNavigationTokens(
    routeToIndexMap = mapOf(
        Screen.Landing to 0,
        Screen.Statistics to 1,
        Screen.Challenges to 2,
        Screen.Profile to 3
    ),
    bottomNavigationRoutes = listOf(
        Screen.Landing,
        Screen.Statistics,
        Screen.Challenges,
        Screen.Profile
    )
)


