package com.app.screentime.navigation

sealed class Screen(val route: String) {
    object Landing : Screen("landing")
    object Profile : Screen("profile")
    object Search : Screen("search")
    object RecordDetail : Screen("record_detail/{username}") {
        fun createRoute(username: String) = "record_detail/$username"
    }

    object AppDetails : Screen("app_details/{packageName}") {
        fun createRoute(packageName: String) = "app_details/$packageName"
    }

    object Statistics : Screen("statistics")
    object SingleAppUsageDetail : Screen("app_usage_detail/{packageName}") {
        fun createRoute(packageName: String) = "app_usage_detail/$packageName"
    }

    object Permission : Screen("permission")
    object FocusMode : Screen("focus_mode")
    object AppBlocking : Screen("app_blocking")
    object Leaderboard : Screen("leaderboard")
    object BlockedLinks : Screen("blocked_links")
}
