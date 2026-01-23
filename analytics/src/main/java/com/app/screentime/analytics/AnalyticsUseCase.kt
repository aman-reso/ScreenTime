package com.app.screentime.analytics

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case for analytics tracking
 * Provides a clean interface for tracking events and screen views
 * All events automatically include common parameters: app_version, country, username
 */
@Singleton
class AnalyticsUseCase @Inject constructor(
    private val analyticsService: AnalyticsService
) {
    /**
     * Track a screen view
     */
    fun trackScreenView(screenName: String) {
        analyticsService.trackScreenView(screenName)
    }

    /**
     * Track a custom event
     */
    fun trackEvent(eventName: String, parameters: Map<String, String> = emptyMap()) {
        analyticsService.trackEvent(eventName, parameters)
    }

    // Convenience methods for common events
    fun trackAppOpen() {
        trackEvent(AnalyticsConstants.EVENT_APP_OPEN)
    }

    fun trackUsagePermissionScreen() {
        trackScreenView(AnalyticsConstants.SCREEN_USAGE_PERMISSION)
        trackEvent(AnalyticsConstants.EVENT_USAGE_PERMISSION_SCREEN)
    }

    fun trackHomeScreen() {
        trackScreenView(AnalyticsConstants.SCREEN_HOME)
        trackEvent(AnalyticsConstants.EVENT_HOME_SCREEN)
    }

    fun trackBatteryHealth() {
        trackScreenView(AnalyticsConstants.SCREEN_BATTERY_HEALTH)
        trackEvent(AnalyticsConstants.EVENT_BATTERY_HEALTH)
    }

    fun trackLeaderboardClick() {
        trackEvent(AnalyticsConstants.EVENT_LEADERBOARD_CLICK)
    }

    fun trackLeaderboardView() {
        trackScreenView(AnalyticsConstants.SCREEN_LEADERBOARD)
        trackEvent(AnalyticsConstants.EVENT_LEADERBOARD_VIEW)
    }

    fun trackSearchClick() {
        trackEvent(AnalyticsConstants.EVENT_SEARCH_CLICK)
    }

    fun trackSearchUsernameScreen() {
        trackScreenView(AnalyticsConstants.SCREEN_SEARCH)
        trackEvent(AnalyticsConstants.EVENT_SEARCH_USERNAME_SCREEN)
    }

    fun trackTOTPVerify() {
        trackEvent(AnalyticsConstants.EVENT_TOTP_VERIFY)
    }

    fun trackStatisticsScreen() {
        trackScreenView(AnalyticsConstants.SCREEN_STATISTICS)
        trackEvent(AnalyticsConstants.EVENT_STATISTICS_SCREEN)
    }

    fun trackChallengeListScreen() {
        trackScreenView(AnalyticsConstants.SCREEN_CHALLENGE_LIST)
        trackEvent(AnalyticsConstants.EVENT_CHALLENGE_LIST_SCREEN)
    }

    fun trackChallengeDetailScreen() {
        trackScreenView(AnalyticsConstants.SCREEN_CHALLENGE_DETAIL)
        trackEvent(AnalyticsConstants.EVENT_CHALLENGE_DETAIL_SCREEN)
    }

    fun trackProfileScreen() {
        trackScreenView(AnalyticsConstants.SCREEN_PROFILE)
        trackEvent(AnalyticsConstants.EVENT_PROFILE_SCREEN)
    }

    fun trackLanguageChangeClick() {
        trackEvent(AnalyticsConstants.EVENT_LANGUAGE_CHANGE_CLICK)
    }

    fun trackThemeChange(theme: String? = null) {
        trackEvent(
            AnalyticsConstants.EVENT_THEME_CHANGE,
            theme?.let { mapOf("theme" to it) } ?: emptyMap()
        )
    }

    fun trackControlCenter() {
        trackScreenView(AnalyticsConstants.SCREEN_CONTROL_CENTER)
        trackEvent(AnalyticsConstants.EVENT_CONTROL_CENTER)
    }

    fun trackLocationScreen() {
        trackScreenView(AnalyticsConstants.SCREEN_LOCATION)
        trackEvent(AnalyticsConstants.EVENT_LOCATION_SCREEN)
    }

    fun trackRewardClick() {
        trackEvent(AnalyticsConstants.EVENT_REWARD_CLICK)
    }

    fun trackNotificationRecoverClick() {
        trackEvent(AnalyticsConstants.EVENT_NOTIFICATION_RECOVER_CLICK)
    }

    fun trackNotificationRecoverScreen() {
        trackScreenView(AnalyticsConstants.SCREEN_NOTIFICATION_RECOVER)
        trackEvent(AnalyticsConstants.EVENT_NOTIFICATION_RECOVER_SCREEN)
    }

    fun trackShareApp() {
        trackEvent(AnalyticsConstants.EVENT_SHARE_APP)
    }

    fun trackFloatingButtonRewardClick() {
        trackEvent(AnalyticsConstants.EVENT_FLOATING_BUTTON_REWARD_CLICK)
    }

    fun trackEditUsernameClick() {
        trackEvent(AnalyticsConstants.EVENT_EDIT_USERNAME_CLICK)
    }

    fun trackEditUsernameSuccess() {
        trackEvent(AnalyticsConstants.EVENT_EDIT_USERNAME_SUCCESS)
    }

    fun trackEditUsernameFailure() {
        trackEvent(AnalyticsConstants.EVENT_EDIT_USERNAME_FAILURE)
    }

    fun trackSetWallpaperClick() {
        trackEvent(AnalyticsConstants.EVENT_SET_WALLPAPER_CLICK)
    }

    fun trackSetWidget() {
        trackEvent(AnalyticsConstants.EVENT_SET_WIDGET)
    }
}







