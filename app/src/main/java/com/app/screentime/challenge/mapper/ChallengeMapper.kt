package com.app.screentime.challenge.mapper

import com.app.screentime.challenge.model.ChallengeDisplayType
import com.app.screentime.challenge.model.GroupedChallenges
import com.app.screentime.network.model.Challenge
import javax.inject.Inject

/**
 * Mapper for challenge data
 */
class ChallengeMapper @Inject constructor() {

    /**
     * Group challenges by display type
     * Unknown types are grouped as OTHER
     */
    fun groupChallengesByType(challenges: List<Challenge>): GroupedChallenges {
        val challengesByType = challenges.groupBy { challenge ->
            ChallengeDisplayType.fromString(challenge.displayType)
        }

        return GroupedChallenges(
            featuredChallenge = challengesByType[ChallengeDisplayType.FEATURE]?.firstOrNull(),
            trendingChallenges = challengesByType[ChallengeDisplayType.TRENDING] ?: emptyList(),
            specialEvents = challengesByType[ChallengeDisplayType.SPECIAL] ?: emptyList(),
            quickJoinChallenges = challengesByType[ChallengeDisplayType.QUICK_JOIN] ?: emptyList(),
            otherChallenges = challengesByType[ChallengeDisplayType.OTHER] ?: emptyList()
        )
    }
}

