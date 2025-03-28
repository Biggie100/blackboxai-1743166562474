package com.advancedquiz.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.advancedquiz.auth.WalletConnectManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val repository: LeaderboardRepository,
    private val walletConnectManager: WalletConnectManager
) : ViewModel() {

    val leaderboardState: StateFlow<List<LeaderboardEntry>> = repository.leaderboardEntries
        .map { entries ->
            entries.mapIndexed { index, entry ->
                // Highlight current user's entry
                if (entry.username.contains(walletConnectManager.session?.walletAddress?.take(6) ?: "")) {
                    entry.copy(isCurrentUser = true)
                } else {
                    entry
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun updateScore(points: Int, level: Int) {
        viewModelScope.launch {
            val username = walletConnectManager.session?.walletAddress?.let {
                "${it.take(6)}...${it.takeLast(4)}"
            } ?: "Anonymous"
            
            repository.updateLeaderboard(
                LeaderboardEntry(
                    rank = 0, // Will be calculated by repository
                    username = username,
                    points = points,
                    level = level,
                    isCurrentUser = true
                )
            )
        }
    }
}