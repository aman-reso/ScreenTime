package com.app.screentime.navigation


sealed class Screen {
    object Landing : Screen()
    object Profile : Screen()
    object Search : Screen()
    data class RecordDetail(val params: RecordDetailParams? = null) : Screen() {
    }

    object Statistics : Screen()
    data class SingleAppUsageDetail(val params: SingleAppUsageDetailParams? = null) : Screen() {
    }

    object Permission : Screen()
    object FocusMode : Screen()
    object AppBlocking : Screen()
    object Leaderboard : Screen()
    object BlockedLinks : Screen()
    object Challenges : Screen()
    data class ChallengeDetail(val params: ChallengeDetailParams? = null) : Screen() {
    }

    object Reward : Screen()
    object CoinHistory : Screen()
    data class RewardTransaction(val transactionId: Int? = null) : Screen()
}
