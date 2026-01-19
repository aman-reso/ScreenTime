package com.app.screentime.config.data

/**
 * Feature flags enum
 * All feature flags for screens and features in the app
 */
enum class Feature(val key: String) {
    // Feature-based flags
    CHALLENGES("challenges"),
    REWARDS("rewards"),
    WALLPAPER("wallpaper"),
    LEADERBOARD("leaderboard"),
    FOCUS_MODE("focus_mode"),
    APP_LOCK("app_lock"),
    
    // Screen-based flags
    LANDING_SCREEN("landing_screen"),
    PROFILE_SCREEN("profile_screen"),
    STATISTICS_SCREEN("statistics_screen"),
    CHALLENGE_LIST_SCREEN("challenge_list_screen"),
    CHALLENGE_DETAIL_SCREEN("challenge_detail_screen"),
    LEADERBOARD_SCREEN("leaderboard_screen"),
    REWARD_SCREEN("reward_screen"),
    COIN_HISTORY_SCREEN("coin_history_screen"),
    REWARD_TRANSACTION_SCREEN("reward_transaction_screen"),
    CONTROL_CENTER_SCREEN("control_center_screen"),
    RECORD_DETAIL_SCREEN("record_detail_screen"),
    SINGLE_APP_USAGE_DETAIL_SCREEN("single_app_usage_detail_screen"),
    LOCATION_MANAGEMENT_SCREEN("location_management_screen"),
    CAPTURED_NOTIFICATIONS_SCREEN("captured_notifications_screen"),
    FILE_MANAGER_SCREEN("file_manager_screen"),
    APP_LOCK_SCREEN("app_lock_screen"),
    WALLPAPER_SCREEN("wallpaper_screen"),
    PERMISSION_SCREEN("permission_screen"),
    CONSENT_SCREEN("consent_screen"),
    SEARCH_SCREEN("search_screen")
}