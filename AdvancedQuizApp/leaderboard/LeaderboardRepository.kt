package com.advancedquiz.leaderboard

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class LeaderboardRepository {
    private val _leaderboardEntries = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val leaderboardEntries: Flow<List<LeaderboardEntry>> = _leaderboardEntries

    init {
        // Simulate fetching leaderboard data
        fetchLeaderboard()
    }

    private fun fetchLeaderboard() {
        // In a real app, this would fetch from a backend service
        _leaderboardEntries.value = listOf(
            LeaderboardEntry(
                rank = 1,
                username = "Player1",
                points = 1250,
                level = 12
            ),
            LeaderboardEntry(
                rank = 2,
                username = "QuizMaster",
                points = 1100,
                level = 10
            ),
            LeaderboardEntry(
                rank = 3,
                username = "ARChampion",
                points = 950,
                level = 9
            ),
            LeaderboardEntry(
                rank = 4,
                username = "Player4",
                points = 800,
                level = 7
            ),
            LeaderboardEntry(
                rank = 5,
                username = "Web3Explorer",
                points = 750,
                level = 6
            )
        )
    }

    fun updateLeaderboard(newEntry: LeaderboardEntry) {
        // Logic to update leaderboard with new entry
        _leaderboardEntries.value = _leaderboardEntries.value + newEntry
    }
}