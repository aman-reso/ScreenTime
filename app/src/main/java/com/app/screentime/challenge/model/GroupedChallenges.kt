package com.app.screentime.challenge.model

import com.app.screentime.network.model.Challenge

/**
 * Data class representing challenges grouped by display type
 */
data class GroupedChallenges(
    val featuredChallenge: Challenge?,
    val trendingChallenges: List<Challenge>,
    val specialEvents: List<Challenge>,
    val quickJoinChallenges: List<Challenge>,
    val otherChallenges: List<Challenge>
)

