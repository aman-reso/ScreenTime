package com.app.screentime.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen : NavKey {
    @Serializable
    object Landing : Screen()

    @Serializable
    object Profile : Screen()

    @Serializable
    object Search : Screen() // Removed - Search feature disabled
    data class RecordDetail(val params: RecordDetailParams? = null) : Screen()

    @Serializable
    object Statistics : Screen()

    @Serializable
    data class SingleAppUsageDetail(val params: SingleAppUsageDetailParams? = null) : Screen()

    @Serializable
    object Permission : Screen()

    @Serializable
    object FocusMode : Screen()

    // object AppBlocking : Screen() // Removed - App Blocking feature disabled
    @Serializable
    object AppLock : Screen()

    @Serializable
    object Leaderboard : Screen()

    // object BlockedLinks : Screen() // Removed - VPN/BlockedLinks feature disabled
    @Serializable
    object Challenges : Screen() // Removed - Challenge feature disabled

    @Serializable
    data class ChallengeDetail(val params: ChallengeDetailParams? = null) :
        Screen() { // Removed - Challenge feature disabled
    }

    @Serializable
    object Reward : Screen() // Removed - Reward feature disabled

    @Serializable
    object CoinHistory : Screen() // Removed - Reward feature disabled

    @Serializable
    data class RewardTransaction(val transactionId: Int? = null) :
        Screen() // Removed - Reward feature disabled

    @Serializable
    object Wallpaper : Screen() // Removed - Wallpaper feature disabled

    @Serializable
    data class FullScreenWallpaper(val params: FullScreenWallpaperParams? = null) : Screen()

    @Serializable
    object CapturedNotifications : Screen()

    @Serializable
    object ControlCenter : Screen()

    @Serializable
    object ManageLocation : Screen()

    @Serializable
    object FileManager : Screen()

    @Serializable
    object WallPaperSearch : Screen()

    @Serializable
    object Customisation : Screen()
}
