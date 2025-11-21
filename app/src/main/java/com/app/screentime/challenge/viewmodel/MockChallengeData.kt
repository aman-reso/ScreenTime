package com.app.screentime.challenge.viewmodel

import com.app.screentime.network.model.Challenge
import com.app.screentime.network.model.ChallengeDetails
import com.app.screentime.network.model.ChallengeRanking
import com.app.screentime.network.model.ChallengeRankingsResponse
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object MockChallengeData {
    
    private val formatter = DateTimeFormatter.ISO_INSTANT
    
    private fun getFutureDate(daysFromNow: Long): String {
        return Instant.now()
            .plusSeconds(daysFromNow * 24 * 60 * 60)
            .atZone(ZoneId.systemDefault())
            .format(formatter)
    }
    
    private fun getPastDate(daysAgo: Long): String {
        return Instant.now()
            .minusSeconds(daysAgo * 24 * 60 * 60)
            .atZone(ZoneId.systemDefault())
            .format(formatter)
    }

    fun getMockChallenges(): List<Challenge> {
        return listOf(
            Challenge(
                id = 1,
                title = "Digital Detox Challenge",
                description = "Reduce your daily screen time by 30% over the next 2 weeks. Track your progress and compete with others to minimize phone usage!",
                reward = "Digital Wellness Badge + Premium Features (1 month)",
                startTime = getPastDate(5),
                endTime = getFutureDate(9),
                thumbnail = "https://images.unsplash.com/photo-1611162617474-5b21e879e113?w=800&h=400&fit=crop",
                hasJoined = false
            ),
            Challenge(
                id = 2,
                title = "Focus Mode Marathon",
                description = "Complete 20 focused work sessions in 7 days. Each session must be at least 25 minutes of uninterrupted focus time.",
                reward = "Focus Master Badge + 500 Points",
                startTime = getPastDate(2),
                endTime = getFutureDate(5),
                thumbnail = "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=800&h=400&fit=crop",
                hasJoined = true
            ),
            Challenge(
                id = 3,
                title = "Early Bird Challenge",
                description = "Wake up before 6 AM and limit phone usage in the first hour. Build a healthy morning routine!",
                reward = "Early Bird Trophy + Morning Routine Guide",
                startTime = getPastDate(1),
                endTime = getFutureDate(13),
                thumbnail = "https://images.unsplash.com/photo-1495616811223-4d98c6e9c869?w=800&h=400&fit=crop",
                hasJoined = false
            ),
            Challenge(
                id = 4,
                title = "Social Media Detox",
                description = "Reduce social media usage by 50% this week. Track your time and discover what you can accomplish!",
                reward = "Social Freedom Badge + Productivity Tips",
                startTime = getPastDate(3),
                endTime = getFutureDate(4),
                thumbnail = "https://images.unsplash.com/photo-1521791136064-7986c2920216?w=800&h=400&fit=crop",
                hasJoined = false
            ),
            Challenge(
                id = 5,
                title = "Weekend Warrior",
                description = "Keep your weekend screen time under 3 hours per day. Enjoy real-world activities and connections!",
                reward = "Weekend Warrior Medal + Activity Ideas",
                startTime = getPastDate(0),
                endTime = getFutureDate(2),
                thumbnail = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=400&fit=crop",
                hasJoined = true
            )
        )
    }

    fun getMockChallengeDetails(challengeId: Int): ChallengeDetails? {
        val challenge = getMockChallenges().find { it.id == challengeId } ?: return null
        
        return ChallengeDetails(
            id = challenge.id,
            title = challenge.title,
            description = challenge.description,
            reward = challenge.reward,
            startTime = challenge.startTime,
            endTime = challenge.endTime,
            thumbnail = challenge.thumbnail,
            challengeType = "LESS_SCREENTIME",
            isActive = true,
            participantCount = when (challengeId) {
                1 -> 125
                2 -> 89
                3 -> 156
                4 -> 67
                5 -> 203
                else -> 100
            },
            createdAt = getPastDate(30)
        )
    }

    fun getMockChallengeRankings(challengeId: Int): ChallengeRankingsResponse? {
        val challenge = getMockChallenges().find { it.id == challengeId } ?: return null
        val participantCount = when (challengeId) {
            1 -> 125
            2 -> 89
            3 -> 156
            4 -> 67
            5 -> 203
            else -> 100
        }

        val rankings = listOf(
            ChallengeRanking(rank = 1, userId = "alex_tech", totalDuration = 7200000, appCount = 5), // 2 hours
            ChallengeRanking(rank = 2, userId = "sarah_focus", totalDuration = 10800000, appCount = 8), // 3 hours
            ChallengeRanking(rank = 3, userId = "mike_digital", totalDuration = 12600000, appCount = 6), // 3.5 hours
            ChallengeRanking(rank = 4, userId = "emma_wellness", totalDuration = 14400000, appCount = 7), // 4 hours
            ChallengeRanking(rank = 5, userId = "james_pro", totalDuration = 16200000, appCount = 9), // 4.5 hours
            ChallengeRanking(rank = 6, userId = "lisa_mindful", totalDuration = 18000000, appCount = 5), // 5 hours
            ChallengeRanking(rank = 7, userId = "david_zen", totalDuration = 19800000, appCount = 6), // 5.5 hours
            ChallengeRanking(rank = 8, userId = "olivia_balance", totalDuration = 21600000, appCount = 8), // 6 hours
            ChallengeRanking(rank = 9, userId = "noah_clear", totalDuration = 23400000, appCount = 7), // 6.5 hours
            ChallengeRanking(rank = 10, userId = "sophia_peace", totalDuration = 25200000, appCount = 9) // 7 hours
        )

        // Add current user rank (simulate user is ranked 5th)
        val userRank = ChallengeRanking(
            rank = 5,
            userId = "current_user",
            totalDuration = 16200000, // 4.5 hours
            appCount = 9
        )

        return ChallengeRankingsResponse(
            challengeId = challengeId,
            challengeTitle = challenge.title,
            challengeType = "LESS_SCREENTIME",
            rankings = rankings,
            userRank = userRank,
            totalParticipants = participantCount
        )
    }
}

