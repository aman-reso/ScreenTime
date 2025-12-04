package com.app.screentime.navigation

/**
 * Design tokens and constants for ScreenTime Navigation.
 *
 * @property routeToIndexMap Maps screen routes to their navigation indices.
 * @property bottomNavigationRoutes List of routes that should show bottom navigation.
 */
data class ScreenTimeNavigationTokens(
    val routeToIndexMap: Map<String, Int>,
    val bottomNavigationRoutes: List<String>
)

/**
 * Default navigation tokens for ScreenTime app.
 */
val defaultScreenTimeNavigationTokens = ScreenTimeNavigationTokens(
    routeToIndexMap = mapOf(
        Screen.Landing.route to 0,
        Screen.Statistics.route to 1,
        Screen.Challenges.route to 2,
        Screen.Profile.route to 3
    ),
    bottomNavigationRoutes = listOf(
        Screen.Landing.route,
        Screen.Statistics.route,
        Screen.Profile.route,
        Screen.Challenges.route
    )
)


