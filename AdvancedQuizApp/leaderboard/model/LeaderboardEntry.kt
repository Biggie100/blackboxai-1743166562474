package com.advancedquiz.leaderboard.model

data class LeaderboardEntry(
    val rank: Int,
    val username: String,
    val points: Int,
    val level: Int,
    val isCurrentUser: Boolean = false
)