package com.app.screentime.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen : NavKey {

    // ── Auth ──────────────────────────────────────────────────────────────────
    @Serializable
    object Login : Screen()

    @Serializable
    object Register : Screen()

    @Serializable
    object ModelOnboarding : Screen() // Extra onboarding for model role

    // ── Main tabs (bottom nav) ────────────────────────────────────────────────
    @Serializable
    object Discover : Screen() // Browse / discover models

    @Serializable
    object ChatList : Screen() // List of conversations

    @Serializable
    object Wallet : Screen() // Wallet balance & history

    @Serializable
    object Profile : Screen() // User's own profile

    // ── Detail screens ────────────────────────────────────────────────────────
    @Serializable
    data class ModelProfile(val modelId: String, val modelName: String = "Model") : Screen()

    @Serializable
    data class Chat(val modelId: String, val modelName: String) : Screen()

    @Serializable
    data class VoiceCall(val modelId: String, val modelName: String) : Screen()

    @Serializable
    data class VideoCall(val modelId: String, val modelName: String, val ratePerMin: Double = 15.0, val avatarUrl: String = "") : Screen()

    @Serializable
    object TopUp : Screen() // Top-up / recharge wallet

    @Serializable
    object Settings : Screen()

    // ── Live Streaming ────────────────────────────────────────────────────────
    @Serializable
    object LiveList : Screen()

    @Serializable
    data class LiveViewer(
        val streamId: String,
        val hostId: String,
        val hostName: String,
        val hostAvatar: String
    ) : Screen()

    @Serializable
    data class LiveHost(
        val streamTitle: String = "My Live Show"
    ) : Screen()

    @Serializable
    object SocialSpaceDemo : Screen() // Showcase demo screen
}
