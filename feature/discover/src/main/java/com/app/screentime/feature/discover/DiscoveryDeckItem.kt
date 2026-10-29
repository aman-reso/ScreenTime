package com.app.screentime.feature.discover

import com.app.screentime.core.model.ModelProfile
import com.google.android.gms.ads.nativead.NativeAd

/**
 * Represents an item in the Dating Discovery Swipe Stack.
 * Can either be a standard user profile or a Tinder-style Native Video Ad Card.
 */
sealed interface DiscoveryDeckItem {
    val id: String

    data class ProfileCard(val profile: ModelProfile) : DiscoveryDeckItem {
        override val id: String get() = profile.id
    }

    data class AdCard(
        val nativeAd: NativeAd,
        val adInstanceId: String = "deck_ad_${System.currentTimeMillis()}_${(1000..9999).random()}"
    ) : DiscoveryDeckItem {
        override val id: String get() = adInstanceId
    }
}
