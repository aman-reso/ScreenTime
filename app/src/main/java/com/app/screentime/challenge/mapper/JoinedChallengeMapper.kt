package com.app.screentime.challenge.mapper

import com.app.screentime.network.model.Challenge
import com.app.screentime.network.model.UserChallenge
import javax.inject.Inject

/**
 * Mapper for joined challenge data
 * Converts UserChallenge to Challenge for display purposes
 */
class JoinedChallengeMapper @Inject constructor() {

    /**
     * Convert UserChallenge to Challenge for display
     */
    fun toChallenge(userChallenge: UserChallenge): Challenge {
        return Challenge(
            id = userChallenge.id,
            title = userChallenge.title,
            description = userChallenge.description,
            reward = userChallenge.reward,
            prize = null,
            rules = null,
            displayType = null,
            tags = null,
            sponsor = null,
            startTime = userChallenge.startTime,
            endTime = userChallenge.endTime,
            thumbnail = userChallenge.thumbnail,
            packageNames = null,
            participantCount = 0,
            hasJoined = true,
            tag = null,
            scheme = null
        )
    }

    /**
     * Convert list of UserChallenge to list of Challenge
     */
    fun toChallengeList(userChallenges: List<UserChallenge>): List<Challenge> {
        return userChallenges.map { toChallenge(it) }
    }
}

