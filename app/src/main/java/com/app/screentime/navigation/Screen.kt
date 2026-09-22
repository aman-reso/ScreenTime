package com.app.screentime.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen : NavKey {

    // ── Dating Flow Screens ────────────────────────────────────────────────────
    @Serializable
    object Home : Screen() // Tab 1: Home Feed (HomeFeedScreen)

    @Serializable
    object DiscoverMap : Screen() // Tab 2: Location-based Discovery (DiscoverMapScreen)

    @Serializable
    object DatingPreferences : Screen() // Screen 1: "Tell us your kind of fun" onboarding / preferences

    @Serializable
    object Likes : Screen() // Liked profiles & favorites

    @Serializable
    object Discover : Screen() // Match discovery card swiping / Legacy alias

    @Serializable
    object ChatList : Screen() // Tab 3: Active chats & matches list

    @Serializable
    object TermsOfService : Screen()

    @Serializable
    data class ProfileDetail(val userId: String, val userName: String) : Screen()

    @Serializable
    object Transactions : Screen()

    @Serializable
    object AddFunds : Screen()

    @Serializable
    object EditProfile : Screen()

    @Serializable
    object Notifications : Screen()

    // ── Communication & Call Screens ──────────────────────────────────────────
    @Serializable
    data class Chat(val modelId: String, val modelName: String) : Screen()

    @Serializable
    data class VoiceCall(
        val modelId: String,
        val modelName: String,
        val avatarUrl: String = ""
    ) : Screen()

    @Serializable
    data class VideoCall(
        val modelId: String,
        val modelName: String,
        val ratePerMin: Double = 15.0,
        val avatarUrl: String = ""
    ) : Screen()

    // ── Connect & Creator Legacy Support (Kept for safe backward compatibility) ─
    @Serializable
    object Explore : Screen()

    @Serializable
    object VoiceCreateWizard : Screen()

    @Serializable
    object Sites : Screen()

    @Serializable
    object Templates : Screen()

    @Serializable
    object VoiceStudio : Screen()

    @Serializable
    object AISiteStudio : Screen()

    @Serializable
    object Monetization : Screen()

    @Serializable
    data class SiteDetail(val siteId: String) : Screen()


    @Serializable
    object Wallet : Screen()

    @Serializable
    object Profile : Screen()

    @Serializable
    object Account : Screen()
}
