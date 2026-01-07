package com.app.screentime.challenge.usecase

import com.app.screentime.challenge.component.util.formatDate
import com.app.screentime.challenge.model.ChallengeDetailUiProps
import com.app.screentime.challenge.model.ChallengeReward
import com.app.screentime.network.model.ChallengeDetails
import com.app.screentime.network.model.ChallengeRankingsResponse
import com.app.screentime.utils.DateUtils
import javax.inject.Inject

/**
 * Use case for challenge detail operations
 * Contains all business logic for processing challenge detail data
 */
class ChallengeDetailUseCase @Inject constructor() {

    /**
     * Get Challenge Detail UI Props
     * Processes ChallengeDetails and ChallengeRankingsResponse into UI-ready props
     */
    fun getChallengeDetailUiProps(
        challengeDetails: ChallengeDetails,
        challengeRankings: ChallengeRankingsResponse?,
        isJoining: Boolean
    ): ChallengeDetailUiProps {
        // Determine hasJoined from API response (userRank in rankings indicates user has joined)
        // Check both hasJoined flag and userRank presence
        val hasJoined = challengeDetails.hasJoined || challengeRankings?.userRank != null

        // Check if challenge is completed
        val isCompleted = DateUtils.isAfter(challengeDetails.endTime)

        // Format dates
        val startDateFormatted = formatDate(challengeDetails.startTime)
        val endDateFormatted = formatDate(challengeDetails.endTime)
        val dateRange = "$startDateFormatted - $endDateFormatted"

        // Format duration as string (Days, Hours, or Minutes)
        val duration = DateUtils.formatChallengeDuration(
            challengeDetails.startTime,
            challengeDetails.endTime
        )

        // Get tags
        val tags = listOfNotNull(challengeDetails.tag)

        // Get prize display value
        val displayPrize = challengeDetails.prize ?: "₹15,000"

        // Get participant count
        val participantCount = challengeRankings?.totalParticipants
            ?: challengeDetails.participantCount

        // Get leaderboard data
        val topRankings = challengeRankings?.rankings?.take(10) ?: emptyList()
        val userRank = challengeRankings?.userRank
        val showLeaderboard = challengeRankings != null && challengeRankings.rankings.isNotEmpty()

        val showJoinButton = !hasJoined && !isCompleted

        // Get available rewards (for now, using dummy data - can be replaced with API data later)
        val availableRewards = listOf(
            ChallengeReward(
                id = "1",
                title = "Premium Subscription",
                description = "Get 1 month free premium subscription",
                coin = "500 coins",
                imageUrl = null,
                tagUrl = null
            ),
            ChallengeReward(
                id = "2",
                title = "Cash Reward",
                description = "Win ₹500 cash prize",
                coin = "1000 coins",
                imageUrl = null,
                tagUrl = null
            ),
            ChallengeReward(
                id = "3",
                title = "Gift Card",
                description = "Amazon gift card worth ₹1000",
                coin = "750 coins",
                imageUrl = null,
                tagUrl = null
            )
        )

        return ChallengeDetailUiProps(
            id = challengeDetails.id,
            title = challengeDetails.title,
            description = challengeDetails.description,
            reward = challengeDetails.reward,
            thumbnail = challengeDetails.thumbnail,
            scheme = challengeDetails.getTheme(),
            tags = tags,
            duration = duration,
            dateRange = dateRange,
            startDateFormatted = startDateFormatted,
            endDateFormatted = endDateFormatted,
            prize = challengeDetails.prize,
            displayPrize = displayPrize,
            participantCount = participantCount,
            hasJoined = hasJoined,
            isCompleted = isCompleted,
            topRankings = topRankings,
            userRank = userRank,
            showLeaderboard = showLeaderboard,
            rules = challengeDetails.rules,
            sponsor = challengeDetails.sponsor,
            availableRewards = availableRewards,
            showJoinButton = showJoinButton
        )
    }
}

