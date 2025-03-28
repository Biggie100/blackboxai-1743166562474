package com.advancedquiz.quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ProgressTracker : ViewModel() {
    var weeklyProgress by mutableStateOf<List<DailyProgress>>(emptyList())
        private set
    
    var achievements by mutableStateOf<List<Achievement>>(emptyList())
        private set

    init {
        loadProgress()
    }

    private fun loadProgress() {
        viewModelScope.launch {
            // Simulate loading progress data
            weeklyProgress = listOf(
                DailyProgress(day = "Mon", correctAnswers = 8, totalQuestions = 10),
                DailyProgress(day = "Tue", correctAnswers = 6, totalQuestions = 10),
                DailyProgress(day = "Wed", correctAnswers = 9, totalQuestions = 10),
                DailyProgress(day = "Thu", correctAnswers = 7, totalQuestions = 10),
                DailyProgress(day = "Fri", correctAnswers = 10, totalQuestions = 10),
                DailyProgress(day = "Sat", correctAnswers = 5, totalQuestions = 10),
                DailyProgress(day = "Sun", correctAnswers = 8, totalQuestions = 10)
            )

            achievements = listOf(
                Achievement(
                    title = "Fast Learner", 
                    description = "Completed 5 quizzes in one day",
                    unlocked = true,
                    tier = AchievementTier.BRONZE
                ),
                Achievement(
                    title = "AR Explorer", 
                    description = "Completed 3 AR quizzes",
                    unlocked = false,
                    tier = AchievementTier.SILVER
                ),
                Achievement(
                    title = "Perfect Score", 
                    description = "Scored 100% on any quiz",
                    unlocked = true,
                    tier = AchievementTier.GOLD
                ),
                Achievement(
                    title = "Web3 Pioneer",
                    description = "Earned an NFT reward",
                    unlocked = false,
                    tier = AchievementTier.PLATINUM
                ),
                Achievement(
                    title = "Consistent Scholar",
                    description = "Played 7 days in a row",
                    unlocked = false,
                    tier = AchievementTier.SILVER
                ),
                Achievement(
                    title = "Speed Demon",
                    description = "Answered 10 questions in under 1 minute",
                    unlocked = false,
                    tier = AchievementTier.GOLD
                )
            )
        }
    }

    fun updateProgress(correct: Int, total: Int) {
        viewModelScope.launch {
            // In a real app, this would update backend/database
            weeklyProgress = weeklyProgress.mapIndexed { index, progress ->
                if (index == weeklyProgress.size - 1) {
                    progress.copy(
                        correctAnswers = progress.correctAnswers + correct,
                        totalQuestions = progress.totalQuestions + total
                    )
                } else {
                    progress
                }
            }
        }
    }
}

data class DailyProgress(
    val day: String,
    val correctAnswers: Int,
    val totalQuestions: Int
) {
    val progressPercent: Float
        get() = correctAnswers.toFloat() / totalQuestions.toFloat()
}

enum class AchievementTier {
    BRONZE, SILVER, GOLD, PLATINUM
}

data class Achievement(
    val title: String,
    val description: String,
    val unlocked: Boolean,
    val tier: AchievementTier = AchievementTier.BRONZE,
    val points: Int = when(tier) {
        AchievementTier.BRONZE -> 10
        AchievementTier.SILVER -> 30
        AchievementTier.GOLD -> 50
        AchievementTier.PLATINUM -> 100
    }
)
