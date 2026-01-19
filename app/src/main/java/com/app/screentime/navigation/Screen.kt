package com.app.screentime.navigation


sealed class Screen {
    object Landing : Screen()
    object Profile : Screen()

    object Search : Screen() // Removed - Search feature disabled
    data class RecordDetail(val params: RecordDetailParams? = null) : Screen()

    object Statistics : Screen()
    data class SingleAppUsageDetail(val params: SingleAppUsageDetailParams? = null) : Screen()

    object Permission : Screen()
    object FocusMode : Screen()

    // object AppBlocking : Screen() // Removed - App Blocking feature disabled
    object AppLock : Screen()
    object Leaderboard : Screen()

    // object BlockedLinks : Screen() // Removed - VPN/BlockedLinks feature disabled
    object Challenges : Screen() // Removed - Challenge feature disabled
    data class ChallengeDetail(val params: ChallengeDetailParams? = null) :
        Screen() { // Removed - Challenge feature disabled
    }

    object Reward : Screen() // Removed - Reward feature disabled
    object CoinHistory : Screen() // Removed - Reward feature disabled
    data class RewardTransaction(val transactionId: Int? = null) :
        Screen() // Removed - Reward feature disabled

    object Wallpaper : Screen() // Removed - Wallpaper feature disabled
    data class FullScreenWallpaper(val params: FullScreenWallpaperParams? = null) : Screen()
    object CapturedNotifications : Screen()
    object ControlCenter : Screen()
    object ManageLocation : Screen()
    object FileManager : Screen()
}
