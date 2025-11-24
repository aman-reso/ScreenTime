package com.app.screentime.challenge.viewmodel

import com.app.screentime.network.model.Challenge
import com.app.screentime.network.model.ChallengeDetails
import com.app.screentime.network.model.ChallengeRanking
import com.app.screentime.network.model.ChallengeRankingsResponse
import com.app.screentime.utils.DateUtils

object MockChallengeData {
    
    private fun getFutureDate(daysFromNow: Long): String {
        return DateUtils.futureDate(daysFromNow.toInt())
    }
    
    private fun getPastDate(daysAgo: Long): String {
        return DateUtils.pastDate(daysAgo.toInt())
    }

    fun getMockChallenges(): List<Challenge> {
        return listOf(
            // Featured Challenge
            Challenge(
                id = 1,
                title = "Morning Yoga Flow",
                description = "Start your day with mindfulness and energy. 7 days of guided sessions.",
                reward = "500 pts",
                prize = "<div><strong>Rank 1:</strong> 500 points</div><div><strong>Rank 2:</strong> 300 points</div><div><strong>Rank 3:</strong> 100 points</div>",
                rules = "<div><strong>Rules:</strong></div><ul><li>Complete daily yoga sessions</li><li>Track your progress</li><li>Stay consistent for 7 days</li></ul>",
                tag = "Wellness",
                sponsor = "AppTime",
                startTime = getPastDate(1),
                endTime = getFutureDate(7),
                thumbnail = "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800&h=400&fit=crop",
                hasJoined = false
            ),
            // Trending Challenges
            Challenge(
                id = 2,
                title = "Code Streak",
                description = "Commit code daily.",
                reward = "300 pts",
                prize = "<div><strong>Rank 1:</strong> 300 points</div>",
                rules = "<div><strong>Rules:</strong></div><ul><li>Code every day</li><li>Track your commits</li></ul>",
                tag = "Coding",
                sponsor = "AppTime",
                startTime = getPastDate(0),
                endTime = getFutureDate(7),
                thumbnail = "https://images.unsplash.com/photo-1461749280684-dccba630e2f6?w=800&h=400&fit=crop",
                hasJoined = false
            ),
            Challenge(
                id = 3,
                title = "Book Worm",
                description = "Read 30 mins/d",
                reward = "150 pts",
                prize = "<div><strong>Rank 1:</strong> 150 points</div>",
                rules = "<div><strong>Rules:</strong></div><ul><li>Read 30 minutes daily</li><li>Track reading time</li></ul>",
                tag = "Mindfulness",
                sponsor = "AppTime",
                startTime = getPastDate(0),
                endTime = getFutureDate(14),
                thumbnail = "https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=800&h=400&fit=crop",
                hasJoined = false
            ),
            // Special Events
            Challenge(
                id = 4,
                title = "Forest Bathing Walk",
                description = "Reconnect with nature this weekend.",
                reward = "1000 pts",
                prize = "<div><strong>Rank 1:</strong> 1000 points</div>",
                rules = "<div><strong>Rules:</strong></div><ul><li>Take a nature walk</li><li>Limit phone usage</li></ul>",
                tag = "Wellness",
                sponsor = "AppTime",
                startTime = getPastDate(0),
                endTime = getFutureDate(2),
                thumbnail = "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=800&h=400&fit=crop",
                hasJoined = false
            ),
            // Quick Join Challenges
            Challenge(
                id = 5,
                title = "Hydration Hero",
                description = "Daily • 8 Glasses",
                reward = "50 pts",
                prize = "<div><strong>Rank 1:</strong> 50 points</div>",
                rules = "<div><strong>Rules:</strong></div><ul><li>Drink 8 glasses of water daily</li></ul>",
                tag = "Fitness",
                sponsor = "AppTime",
                startTime = getPastDate(0),
                endTime = getFutureDate(30),
                thumbnail = "https://images.unsplash.com/photo-1523362628745-0c100150b504?w=800&h=400&fit=crop",
                hasJoined = false
            ),
            Challenge(
                id = 6,
                title = "Sleep Well",
                description = "Daily • 8 Hours",
                reward = "100 pts",
                prize = "<div><strong>Rank 1:</strong> 100 points</div>",
                rules = "<div><strong>Rules:</strong></div><ul><li>Get 8 hours of sleep daily</li></ul>",
                tag = "Wellness",
                sponsor = "AppTime",
                startTime = getPastDate(0),
                endTime = getFutureDate(30),
                thumbnail = "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=800&h=400&fit=crop",
                hasJoined = false
            ),
            // Additional challenges
            Challenge(
                id = 7,
                title = "Digital Detox Challenge",
                description = "Reduce your daily screen time by 30% over the next 2 weeks.",
                reward = "Digital Wellness Badge + Premium Features (1 month)",
                prize = "<div><strong>Rank 1:</strong> 100 points</div><div><strong>Rank 2:</strong> 50 points</div><div><strong>Rank 3:</strong> 10 points</div>",
                rules = "<div><strong>Rules:</strong></div><ul><li>Track your screen time daily</li><li>Reduce usage by at least 30% from baseline</li></ul>",
                tag = "Wellness",
                sponsor = "AppTime",
                startTime = getPastDate(5),
                endTime = getFutureDate(9),
                thumbnail = "https://images.unsplash.com/photo-1611162617474-5b21e879e113?w=800&h=400&fit=crop",
                hasJoined = false
            ),
            Challenge(
                id = 8,
                title = "Focus Mode Marathon",
                description = "Complete 20 focused work sessions in 7 days.",
                reward = "Focus Master Badge + 500 Points",
                startTime = getPastDate(2),
                endTime = getFutureDate(5),
                thumbnail = "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=800&h=400&fit=crop",
                hasJoined = true
            )
        )
    }
    
    fun getFeaturedChallenge(): Challenge? {
        return getMockChallenges().firstOrNull { it.id == 1 }
    }
    
    fun getTrendingChallenges(): List<Challenge> {
        return getMockChallenges().filter { it.id in listOf(2, 3) }
    }
    
    fun getSpecialEvents(): List<Challenge> {
        return getMockChallenges().filter { it.id == 4 }
    }
    
    fun getQuickJoinChallenges(): List<Challenge> {
        return getMockChallenges().filter { it.id in listOf(5, 6) }
    }

    fun getMockChallengeDetails(challengeId: Int): ChallengeDetails? {
        val challenge = getMockChallenges().find { it.id == challengeId } ?: return null
        
        return ChallengeDetails(
            id = challenge.id,
            title = challenge.title,
            description = challenge.description,
            reward = challenge.reward,
            prize = challenge.prize,
            rules = challenge.rules,
            tag = challenge.tag,
            sponsor = challenge.sponsor,
            startTime = challenge.startTime,
            endTime = challenge.endTime,
            thumbnail = challenge.thumbnail,
            challengeType = "LESS_SCREENTIME",
            isActive = true,
            participantCount = when (challengeId) {
                1 -> 1243 // Morning Yoga Flow - matches the design
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
            1 -> 1243 // Morning Yoga Flow - matches the design
            2 -> 89
            3 -> 156
            4 -> 67
            5 -> 203
            else -> 100
        }

        val rankings = listOf(
            ChallengeRanking(rank = 1, userId = "Sarah Jenkins", totalDuration = 7200000, appCount = 5), // 2 hours
            ChallengeRanking(rank = 2, userId = "Mike Chen", totalDuration = 10800000, appCount = 8), // 3 hours
            ChallengeRanking(rank = 3, userId = "Jessica Wu", totalDuration = 12600000, appCount = 6), // 3.5 hours
            ChallengeRanking(rank = 4, userId = "emma_wellness", totalDuration = 14400000, appCount = 7), // 4 hours
            ChallengeRanking(rank = 5, userId = "james_pro", totalDuration = 16200000, appCount = 9), // 4.5 hours
            ChallengeRanking(rank = 6, userId = "lisa_mindful", totalDuration = 18000000, appCount = 5), // 5 hours
            ChallengeRanking(rank = 7, userId = "david_zen", totalDuration = 19800000, appCount = 6), // 5.5 hours
            ChallengeRanking(rank = 8, userId = "olivia_balance", totalDuration = 21600000, appCount = 8), // 6 hours
            ChallengeRanking(rank = 9, userId = "noah_clear", totalDuration = 23400000, appCount = 7), // 6.5 hours
            ChallengeRanking(rank = 10, userId = "sophia_peace", totalDuration = 25200000, appCount = 9) // 7 hours
        )

        // Add current user rank (simulate user is ranked 42nd for Morning Yoga Flow)
        val userRank = ChallengeRanking(
            rank = if (challengeId == 1) 42 else 5,
            userId = "Aman Kumar",
            totalDuration = if (challengeId == 1) 12600000 else 16200000, // 3.5 hours for challenge 1
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

